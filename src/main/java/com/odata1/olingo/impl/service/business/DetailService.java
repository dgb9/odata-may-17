package com.odata1.olingo.impl.service.business;

import com.odata1.olingo.impl.jpa.data.DetailData;
import com.odata1.olingo.impl.tools.EdmUtil;
import com.odata1.olingo.impl.tools.ODataFilterProcessor;
import com.odata1.olingo.impl.tools.ParametersHolder;
import com.odata1.olingo.impl.tools.SqlFilterAndParams;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class DetailService {
    @PersistenceContext
    private EntityManager em;

    public EntityCollection fetchCollectionData(UriInfo uriInfo) throws ODataApplicationException {
        DetailFieldMapper fieldMapper = new DetailFieldMapper();

        String sqlStart = "select distinct a from DetailData a";

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
        TypedQuery<DetailData> query = em.createQuery(sql, DetailData.class);

        // now push all the parameters
        params.getParameters().forEach((parameter) -> {
            String name = parameter.getName();
            Object value = parameter.getValue();

            query.setParameter(name, value);
        });

        int maximumFetch = top + skip;
        query.setMaxResults(maximumFetch);

        List<DetailData> list = null;
        list = query.getResultList();

        // the list has maximum maximumFetch size; if list size is bigger than skip, we only consider
        // the values from skip all the way to the end of the list
        List<DetailData> resultList = list;
        if (skip < list.size()) {
            resultList = list.subList(skip, list.size());
        }

        EntityCollection res = new EntityCollection();
        for (DetailData detail : resultList) {
            res.getEntities().add(DetailUtil.convert(detail));
        }

        return res;
    }

    public List<DetailData> getDetailDataByCrtIds(List<String> crtIds) {
        String sql = "select detail from DetailData detail where detail.crtId in (:ids)";
        TypedQuery<DetailData> query = em.createQuery(sql, DetailData.class);
        query.setParameter("ids", crtIds);

        return query.getResultList();
    }
}
