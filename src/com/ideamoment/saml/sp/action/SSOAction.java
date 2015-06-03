/**
 * 
 */
package com.ideamoment.saml.sp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ideamoment.saml.MockUser;
import com.ideamoment.saml.SamlDecoder;
import com.ideamoment.saml.SamlLogoutRequest;
import com.ideamoment.saml.SamlResponse;
import com.ideamoment.saml.shiro.token.IdeaSamlToken;
import com.ideamoment.saml.util.ShiroSecurityUtils;



/**
 * @author Chinakite
 *
 */
@Controller
public class SSOAction {
    @RequestMapping(value="/sso", method=RequestMethod.POST)
    public void validateSamlResponse(HttpServletRequest request, HttpServletResponse response) {
        String samlResponseStr = request.getParameter("samlResponse");
        String relayState = request.getParameter("relayState");
        
        MockUser user = new MockUser();
        
        SamlDecoder decoder = new SamlDecoder(samlResponseStr);
        samlResponseStr = decoder.decode();
        
        SamlResponse samlResponse = new SamlResponse(user, samlResponseStr);
        samlResponse.readResponseFromStr();
        
        user = (MockUser)samlResponse.getUser();
        
        String sessionId = samlResponse.getSessionId();
        
        IdeaSamlToken token = new IdeaSamlToken(user, false);
        Subject subject = ShiroSecurityUtils.getSubject(sessionId);
        subject.login(token);
        
        try {
            response.sendRedirect(relayState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        
        String issuerURL   = "http://localhost:9080/ideasaml-sp";
        
        Subject subject = SecurityUtils.getSubject();
        
        String nameID = (String)subject.getPrincipal();
        
        SamlLogoutRequest samlLogoutReq = new SamlLogoutRequest(issuerURL, nameID);
        String logoutReq = samlLogoutReq.generateLogoutRequest();
        
        Map model = new HashMap();
        model.put("logoutReq", logoutReq);
        
        return new ModelAndView("/WEB-INF/page/logoutReq.jsp", model);
    }
    
    @RequestMapping(value="/slo", method=RequestMethod.POST)
    public void slo(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        
        try {
            response.sendRedirect("http://localhost:9080/ideasaml-sp/test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value="/test", method=RequestMethod.GET)
    public ModelAndView testPage(HttpServletRequest request, HttpServletResponse response) {
        
        Subject subject = SecurityUtils.getSubject();
        String userId = (String)subject.getPrincipal();
        
        Map model = new HashMap();
        model.put("userId", userId);
        
        return new ModelAndView("/WEB-INF/page/testPage.jsp", model);
    }
    
    @RequestMapping(value="/testAnother", method=RequestMethod.GET)
    public ModelAndView testAnotherPage(HttpServletRequest request, HttpServletResponse response) {
        
        Subject subject = SecurityUtils.getSubject();
        String userId = (String)subject.getPrincipal();
        
        Map model = new HashMap();
        model.put("userId", userId);
        
        return new ModelAndView("/WEB-INF/page/testPage.jsp", model);
        
    }
}
