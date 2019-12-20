package cafe.deadbeef.callbook.web.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cafe.deadbeef.callbook.CallbookEntryService;
import cafe.deadbeef.callbook.web.dto.CallbookEntry;

@Controller
public class CallbookController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CallbookEntryService callbookEntryService;

	public CallbookController() {
		super();
	}
	
    // API - read
    @PreAuthorize("#oauth2.hasScope('callbook') and #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/{callsign}")
    @ResponseBody
    public ResponseEntity<CallbookEntry> findById(@PathVariable String callsign) {
    	logger.info("Serving lookup response for " + callsign);
    	final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Optional<CallbookEntry> callbookEntry = callbookEntryService.getCallbookEntryByCallsign(callsign);
        
        if ( callbookEntry.isPresent() ) {
        	return new ResponseEntity<CallbookEntry>(callbookEntry.get(), httpHeaders, HttpStatus.OK);
        } else {
        	return new ResponseEntity<CallbookEntry>(HttpStatus.NOT_FOUND);
        }
    }

    // write API not yet implemented
}
