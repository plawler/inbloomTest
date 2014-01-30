package com.springapp.mvc.controller;

import com.google.common.collect.Lists;
import com.springapp.mvc.command.RealmSelection;
import com.springapp.mvc.dto.Realm;
import com.springapp.mvc.dto.Service;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 1/29/14
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RealmController {

    private static final Logger log = LoggerFactory.getLogger(RealmController.class);
    private static final String realmsUrl = "http://localhost:8082/oauth_sample/realms"; // get this from properties
    private static final String idpByRealmUrl = "http://localhost:8082/oauth_sample/dataservices/"; // get this from properties

    @RequestMapping(value = "/realms", method = RequestMethod.GET)
    public String getRealms(ModelMap model) {
        model.put("realms", getRealms());
        model.put("realmSelection", new RealmSelection());
        return "realms";
    }

    @RequestMapping(value = "/realms", method = RequestMethod.POST)
    public String selectRealm(@ModelAttribute RealmSelection selection, HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        log.info("Realm selected is: " + selection.getRealmIdentifier());
        // todo: implement dynamic idp endppint lookup and redirect to authenticating address
        RestTemplate rt = new RestTemplate();
        String result = rt.getForObject(idpByRealmUrl + selection.getRealmIdentifier(), String.class);
        request.getSession().setAttribute("apiUrl", getService(result).getUrl());
        return "redirect:/";
    }

    private List<Realm> getRealms() {
        RestTemplate rt = new RestTemplate();
        String result = rt.getForObject(realmsUrl, String.class);
        try {
            return buildRealmMap(result);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private List<Realm> buildRealmMap(String json) throws IOException {
        List<Realm> realms = Lists.newArrayList();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode results = mapper.readTree(json);

        Iterator<JsonNode> iterator = results.getElements();
        while (iterator.hasNext()) {
            realms.add((Realm)mapper.readValue(iterator.next(), new TypeReference<Realm>(){}));
        }

        return realms;
    }

    private Service getService(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<Service>(){});
    }
}
