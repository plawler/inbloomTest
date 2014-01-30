package com.springapp.mvc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 1/28/14
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class GatewayFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(GatewayFilter.class);
    private static final String REALM_PRESENTATION = "REALM_PRESENTATION";
    private static final String REALM_SELECTION = "REALM_SELECTION";
    private static final String OPERATOR_CONNECTION = "OPERATOR_CONNECTION";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Gateway filter - realm checks");

        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute(REALM_PRESENTATION) == null) {
                session.setAttribute(REALM_PRESENTATION, true);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/realms");
                dispatcher.forward(request, response);
            } else if (session.getAttribute(REALM_SELECTION) == null) {
                session.setAttribute(REALM_SELECTION, true);
                RequestDispatcher dispatcher = request.getRequestDispatcher(request.getServletPath());
                dispatcher.forward(request, response);
            } else if (session.getAttribute(OPERATOR_CONNECTION) == null){
                session.setAttribute(OPERATOR_CONNECTION, true);
                String apiUrl = (String) request.getSession().getAttribute("apiUrl");
                log.info("Retrieving the apiUrl: " + apiUrl);
                request.setAttribute("apiUrl", apiUrl);
                session.removeAttribute("apiUrl");
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            throw new IllegalStateException("HttpSession is null. Close the browser and log back in."); // todo: redirect to generic app launch page
        }
    }

}
