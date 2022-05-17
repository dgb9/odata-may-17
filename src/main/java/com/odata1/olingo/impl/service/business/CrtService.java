package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.jpa.data.CrtData;
import com.odata1.olingo.impl.jpa.data.DetailData;
import com.odata1.olingo.impl.service.ODataConst;
import com.odata1.olingo.impl.service.provider.CrtConst;
import com.odata1.olingo.impl.tools.EdmUtil;
import com.odata1.olingo.impl.tools.ODataFilterProcessor;
import com.odata1.olingo.impl.tools.ParametersHolder;
import com.odata1.olingo.impl.tools.SqlFilterAndParams;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CrtService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DetailService detailService;

    public EntityCollection fetchCollectionData(UriInfo uriInfo) throws ODataApplicationException {
        CrtFieldMapper fieldMapper = new CrtFieldMapper();

        String sqlStart = "select distinct a from CrtData a";

        // process the order elements
        String sqlOrderBy = EdmUtil.processOrderBy("a.", uriInfo.getOrderByOption(), fieldMapper);

        // process the filter
        FilterOption filterOption = uriInfo.getFilterOption();
        SqlFilterAndParams filterAndParams = new ODataFilterProcessor().processFilterOption(filterOption, fieldMapper, true);

        String sqlFilter = filterAndParams.getSql();
        ParametersHolder params = filterAndParams.getHolder();

        int top = EdmUtil.getTop(uriInfo);
        int skip = EdmUtil.getSkip(uriInfo);

        String sql = String.join(" ", sqlStart, sqlFilter, sqlOrderBy);
        TypedQuery<CrtData> query = em.createQuery(sql, CrtData.class);

        // now push all the parameters
        params.getParameters().forEach((parameter) -> {
            String name = parameter.getName();
            Object value = parameter.getValue();

            query.setParameter(name, value);
        });

        int maximumFetch = top + skip;
        query.setMaxResults(maximumFetch);

        List<CrtData> list = null;
        list = query.getResultList();

        boolean isExpanded = uriInfo.getExpandOption() != null;
        // the list has maximum maximumFetch size; if list size is bigger than skip, we only consider
        // the values from skip all the way to the end of the list
        List<CrtData> resultList = list;
        if (skip < list.size()) {
            resultList = list.subList(skip, list.size());
        }

        Map<String, List<DetailData>> detailMap = loadDetailMap(resultList.stream().map(CrtData::getId).collect(Collectors.toList()));

        EntityCollection res = new EntityCollection();
        for (CrtData account : resultList) {
            Entity crtCurrent = CrtUtil.convert(account);

            if (isExpanded) {
                loadLink(crtCurrent, detailMap);
            }

            res.getEntities().add(crtCurrent);
        }

        return res;
    }

    private void loadLink(Entity crtCurrent, Map<String, List<DetailData>> detailMap) {
        String crtId = crtCurrent.getProperty(CrtConst.ID).getValue().toString();

        if (detailMap.containsKey(crtId) && detailMap.get(crtId).size() > 0) {
            // generate list of entities that is needed for link
            List<DetailData> listDetailData = detailMap.get(crtId);
            List<Entity> entities = listDetailData
                    .stream()
                    .map(DetailUtil::convert)
                    .collect(Collectors.toList());

            EntityCollection collection = new EntityCollection();
            collection.getEntities().addAll(entities);

            Link link = new Link();
            link.setTitle(ODataConst.NAV_DET);
            link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
            link.setRel(Constants.NS_ASSOCIATION_LINK_REL + ODataConst.NAV_DET);

            URI crtCurrentId = crtCurrent.getId();
            String href = crtCurrentId.toASCIIString();
            link.setHref(href);

            link.setInlineEntitySet(collection);

            crtCurrent.getNavigationLinks().add(link);
        }
    }

    private Map<String, List<DetailData>> loadDetailMap(List<String> crtIds) {
        Map<String, List<DetailData>> map = new HashMap<>();
        List<DetailData> list = detailService.getDetailDataByCrtIds(crtIds);

        list.forEach((detail) -> {
            String crtId = detail.getCrtId();

            List<DetailData> detailList = null;

            if (map.containsKey(crtId)) {
                detailList = map.get(crtId);
            } else {
                detailList = new ArrayList<>();
                map.put(crtId, detailList);
            }

            // adding the current element
            detailList.add(detail);
        });

        return map;
    }

}
