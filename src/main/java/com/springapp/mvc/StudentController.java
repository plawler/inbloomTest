package com.springapp.mvc;

import org.inbloom.model.student.StudentFactory;
import org.slc.sli.api.client.RESTClient;
import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.client.impl.BasicClient;
import org.slc.sli.api.client.util.Query;
import org.slc.sli.api.client.util.URLBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/5/13
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class StudentController {

    private static final String RESOURCE = ResourceNames.STUDENTS;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String findAllRest(HttpServletRequest request, ModelMap model) {
        String result = queryResource(request, RESOURCE, null);
        model.put("students", StudentFactory.createStudents(result));
        return "students";
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public String find(HttpServletRequest request, ModelMap model, @RequestParam("id") String id) {
        String result = getEntity(request, RESOURCE, id);
        model.put("student", StudentFactory.createStudent(result));
        return "student";
    }

    private String getEntity(HttpServletRequest request, String resource, String entityId) {
        RESTClient client = ((BasicClient) request.getSession().getAttribute("client")).getRESTClient();
        URLBuilder urlBuilder = URLBuilder.create(client.getBaseURL()).entityType(resource);
        if (entityId != null) {
            urlBuilder.id(entityId);
        }
        String result = executeRequest(urlBuilder, client);
        return result;
    }

    private String queryResource(HttpServletRequest request, String resource, Query query) {
        RESTClient client = ((BasicClient) request.getSession().getAttribute("client")).getRESTClient();
        URLBuilder urlBuilder = URLBuilder.create(client.getBaseURL()).entityType(resource);
        if (query != null) {
            urlBuilder.query(query);
        }
        String result = executeRequest(urlBuilder, client);
        return result;
    }

    private String executeRequest(URLBuilder urlBuilder, RESTClient client) {
        String result = null;
        try {
            URL url = urlBuilder.build();
            Response response = client.getRequest(url);
            result = response.readEntity(String.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

}
