package cafe.deadbeef.callbook.web.controller;

import java.security.Principal;
import java.util.Map;

import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/users/extra")
    @ResponseBody
    public Map<String, Object> getExtraInfo(Authentication auth, Principal principal) {
    	
    	logger.info("User details requested by " + auth.getName());
    	logger.info("User details " + auth.getDetails().toString());
    	
    	
        OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        
        logger.info("oauthDetails :" + oauthDetails.toString());
        logger.info("tokenType: " + oauthDetails.getTokenType());
        logger.info("tokenValue: " + oauthDetails.getTokenValue());
        logger.info("sessionId: " + oauthDetails.getSessionId());
        logger.info("decoded: " + oauthDetails.getDecodedDetails().toString());
        
    	/*
    	if (principal instanceof OAuth2Authentication) {
    		OAuth2Authentication authentication = (OAuth2Authentication) principal;
    		Object authDetails = authentication.getDetails();
    		if (authDetails instanceof OAuth2AuthenticationDetails) {
    			OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) authDetails;
    			
    	        logger.info("oauthDetails :" + oauthDetails.toString());
    	        logger.info("tokenType: " + oauthDetails.getTokenType());
    	        logger.info("tokenValue: " + oauthDetails.getTokenValue());
    	        logger.info("sessionId: " + oauthDetails.getSessionId());
    	        logger.info("decoded: " + oauthDetails.getDecodedDetails().toString());   

    		}
    	}
*/
        Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String key : details.keySet()) {
        	sb.append(String.format("\"%s\": \"%s\", ", key, details.get(key).toString()));
        }
        sb.append("]");
        logger.debug(sb.toString());
        
        return details;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld(Principal principal) {
        return "Hello " + principal.getName();
    }
}