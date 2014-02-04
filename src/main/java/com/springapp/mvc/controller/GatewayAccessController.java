package com.springapp.mvc.controller;

import com.google.common.collect.Lists;
import com.springapp.mvc.command.RealmSelection;
import com.springapp.mvc.dto.Realm;
import com.springapp.mvc.dto.Service;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
public class GatewayAccessController {

    private static final Logger log = LoggerFactory.getLogger(GatewayAccessController.class);

    @Value("${realmsEndpoint}")
    private String realmsUrl;

    @Value("${servicesEndpoint}")
    private String servicesUrl;

    @RequestMapping(value = "/realms", method = RequestMethod.GET)
    public String getRealms(ModelMap model) {
        model.put("realms", getRealms());
        model.put("realmSelection", new RealmSelection());
        return "realms";
    }

    @RequestMapping(value = "/realms", method = RequestMethod.POST)
    public String selectRealm(@ModelAttribute RealmSelection selection, HttpServletRequest request)
            throws ServletException, IOException {
        log.info("Realm selected is: " + selection.getRealmIdentifier());
        RestTemplate rt = new RestTemplate();
        String result = rt.getForObject(servicesUrl + selection.getRealmIdentifier(), String.class);
        request.getSession().setAttribute("service", getService(result));
        return "redirect:/";
    }

    private List<Realm> getRealms() {
        RestTemplate rt = new RestTemplate();
        String result = rt.getForObject(realmsUrl, String.class);
        try {
            return buildRealmMap(result);
        } catch (IOException e) {
            e.printStackTrace();
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
