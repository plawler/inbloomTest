package com.springapp.mvc.filter;

import com.google.common.collect.ImmutableMap;
import com.springapp.mvc.dto.Service;
import org.apache.http.client.utils.URIBuilder;
import org.slc.sli.api.client.RESTClient;
import org.slc.sli.api.client.SLIClient;
import org.slc.sli.api.client.impl.BasicClient;
import org.slc.sli.api.client.impl.BasicRESTClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/4/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private static final String CALL_BACK_PATH = "/testapp/";

    private String clientId;
    private String clientSecret;
    private URL apiUrl;
    private URL callbackUrl;
    private String afterCallbackRedirect;

    @Override
    public void destroy() {
        log.info("Destroy auth filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        SLIClient client = (SLIClient) httpRequest.getSession().getAttribute("client");

        log.info("URI:" + httpRequest.getRequestURI());

        if (client == null) {
            Service service = (Service) request.getAttribute("service");
            URL api = new URL(service.getUrl());

            RESTClient restClient = new BasicRESTClient(api, clientId, clientSecret, callbackUrl);
            client = new BasicClient(restClient);

            URL mangled = mangleUrl(client.getRESTClient().getLoginURL(), ImmutableMap.of("Realm", service.getUniqueRealmIdentifier()));

            ((HttpServletResponse) response).sendRedirect(mangled.toExternalForm());

            httpRequest.getSession().setAttribute("client", client);
        } else if (CALL_BACK_PATH.equals(httpRequest.getRequestURI())) {
            // Authentication was successful lets connect to the API
            String code = httpRequest.getParameter("code");
            Response apiResponse = null;
            try {
                apiResponse = client.getRESTClient().connect(code);
            } catch (MalformedURLException e) {
                log.error(String.format("Invalid/malformed URL when connecting: %s", e.toString()));
            } catch (URISyntaxException e) {
                log.error(String.format("Invalid/malformed URL when connecting: %s", e.toString()));
            }

            if ((apiResponse == null) || (apiResponse.getStatus() != 200)) {
                handleAPIResponseFailure(apiResponse, response);
            } else {
                ((HttpServletResponse) response).sendRedirect(afterCallbackRedirect);
            }
            log.info("callback");
        } else {
            // Assuming authenticated user, process request as necessary.
            log.info("chain");
            chain.doFilter(request, response);

            // Redirect to login on any errors
            // TODO - we should handle responses correctly here. If the session is invalidated,
            // we need to handle this properly. Same with the other HTTP response codes.
            // Possible solution:
            //      authenticate(request, response);
        }
    }

    private URL mangleUrl(URL url, ImmutableMap<String, String> params) {
        try {
            URIBuilder builder = new URIBuilder(url.toURI());
            for (String key : params.keySet()) {
                builder.addParameter(key, params.get(key));
            }
            URI newUrlEncoded = builder.build();
            return newUrlEncoded.toURL();
        } catch (URISyntaxException e) {
            log.error(String.format("Invalid/malformed URL when trying to mangle: %s", e.toString()));
        } catch (MalformedURLException e) {
            log.error(String.format("Invalid/malformed URL when trying to mangle: %s", e.toString()));
        }
        return null;
    }

    private void handleAPIResponseFailure(Response apiResponse, ServletResponse response) throws IOException {
        if (apiResponse == null) {
            ((HttpServletResponse) response).sendRedirect("404.html");
        } else {
            switch(apiResponse.getStatus()) {
                case 403:
                    ((HttpServletResponse) response).sendRedirect("403.html");
                    break;
                case 404:
                    ((HttpServletResponse) response).sendRedirect("404.html");
                    break;
                case 422:
                    ((HttpServletResponse) response).sendRedirect("422.html");
                    break;
                case 400:
                case 500:
                default:
                    ((HttpServletResponse) response).sendRedirect("500.html");
                    break;
            }
        }
    }

    @Override
    @SuppressWarnings({"PMD.AvoidThrowingRawExceptionTypes", "PMD.EmptyCatchBlock"})
    public void init(FilterConfig conf) throws ServletException {
        afterCallbackRedirect = conf.getInitParameter("afterCallbackRedirect");

        InputStream propStream = null;

        try {
            String externalProps = System.getProperty("sli.conf");
//            log.info("Loading properties from: {}", externalProps);
            if (externalProps != null) {
                try {
                    propStream = new FileInputStream(externalProps);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Unable to load properties file: " + externalProps, e);
                }
            } else {
                String env = System.getProperty("sli.env");
                if (env == null) {
                    throw new RuntimeException("sli.env system property is not set!");
                }
                propStream = this.getClass().getResourceAsStream("/config/" + env + ".properties");
                if (propStream == null) {
                    throw new RuntimeException("no properties file found for sli.env: " + env);
                }
            }

            Properties props = new Properties();
            try {
                props.load(propStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            clientId = props.getProperty("sli.sample.clientId");
            clientSecret = props.getProperty("sli.sample.clientSecret");
            String apiUrlString = props.getProperty("sli.sample.apiUrl");
            String callbackUrlString = props.getProperty("sli.sample.callbackUrl");
            if (clientId == null || clientSecret == null || apiUrlString == null || callbackUrlString == null) {
                throw new RuntimeException(
                        "Missing property.  All of the following properties must be available: clientId, clientSecret, apiUrl, callbackUrl");
            }
            try {
                apiUrl = new URL(apiUrlString);
                callbackUrl = new URL(callbackUrlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } finally {
            if (propStream != null) {
                try {
                    propStream.close();
                } catch (IOException e) {
                    //swallow exception
                }
            }
        }
    }


}
