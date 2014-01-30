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
    private static final String REALM_SELECTION = "REALM_SELECTION";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Gateway filter realm selection check");
        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute(REALM_SELECTION) == null) {
                session.setAttribute(REALM_SELECTION, true);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/realms");
                dispatcher.forward(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            throw new IllegalStateException("HttpSession is null. Bad.");
        }
    }

}
