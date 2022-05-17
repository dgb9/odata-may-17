package com.odata1.olingo.controller;

import com.odata1.olingo.impl.service.DemoComplexCollectionProcessor;
import com.odata1.olingo.impl.service.business.Service;
import com.odata1.olingo.impl.service.provider.DemoEdmProvider;
import com.odata1.olingo.impl.service.DemoEntityCollectionProcessor;
import com.odata1.olingo.impl.service.DemoEntityProcessor;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class ODataController {

    private static final Logger LOG = LoggerFactory.getLogger(ODataController.class);

    private Service service;

    @RequestMapping(path = "/odata/**", method = {RequestMethod.GET, RequestMethod.POST})
    public void odataController(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // create odata handler and configure it with EdmProvider and Processor
            OData odata = OData.newInstance();
            ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(), new ArrayList<EdmxReference>());

            ODataHttpHandler handler = odata.createHandler(edm);
            handler.register(new DemoEntityCollectionProcessor(service));
            handler.register(new DemoEntityProcessor(service));
            handler.register(new DemoComplexCollectionProcessor(service));

            req.setAttribute("requestMapping", "/odata");

            // let the handler do the work
            handler.process(req, resp);
        }
        catch (Exception e) {
            LOG.error("odata general servlet error", e);
        }
    }

    @Autowired
    public void setService(Service service) {
        this.service = service;
    }
}
