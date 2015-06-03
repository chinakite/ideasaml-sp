/**
 * 
 */
package com.ideamoment.saml.sp.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import com.ideamoment.saml.SamlRequest;

/**
 * @author Chinakite
 *
 */
public class IdeaSamlShiroFilter extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        if (isLoginRequest(request, response)) {
            return true;
        } else {
            saveRequest(request);
            responseAuthnRequest(request, response);
            return false;
        }
    }
    
    protected void responseAuthnRequest(ServletRequest request, ServletResponse response) 
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        
        String issuerURL   = "http://localhost:9080/ideasaml-sp";
        String provideName = "ideasaml-sp";
        String acsURL      = "http://localhost:9080/ideasaml-sp/sso";
        
        SamlRequest samlReq = new SamlRequest(issuerURL, provideName, acsURL);
        String authnReq = samlReq.generateAuthnRequest();
        
        ((HttpServletRequest)request).getRequestDispatcher(
                "/WEB-INF/page/authnReq.jsp?authnReq=" + authnReq + "&relayState=" + req.getRequestURI()).forward(request,response);
    }

    
}
