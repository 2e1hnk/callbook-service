package cafe.deadbeef.callbook;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import QRZClient2.QRZCallsignNotFoundException;
import QRZClient2.QRZClient2;
import QRZClient2.QRZLookupResponse;
import cafe.deadbeef.callbook.web.dto.CallbookEntry;

@Service
public class CallbookEntryService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CallbookEntryRepository callbookEntryRepository;
	
	private QRZClient2 qrzClient;
	
	public CallbookEntryService(@Value("${qrz.username}") String qrz_username, @Value("${qrz.password}") String qrz_password) {
		qrzClient = new QRZClient2(qrz_username, qrz_password);
	}
	
	public void addNewContact (CallbookEntry callbookEntry) {
		callbookEntryRepository.save(callbookEntry);
		logger.info("Callbook entry saved");
	}
	
	public Iterable<CallbookEntry> getAllCallbookEntries() {
		Iterable<CallbookEntry> list = callbookEntryRepository.findAll();
		logger.info("" + ((Collection<?>) list).size() + " entries in callbook");
		return list;
	}
	
	public Optional<CallbookEntry> getCallbookEntryByCallsign(String callsign) {
		logger.info("Trying to find " + callsign + " ... " + callbookEntryRepository.findById(callsign).isPresent());
		if ( !callbookEntryRepository.findById(callsign).isPresent() ) {
			logger.info("Callsign " + callsign + " not found in callbook. Trying QRZ lookup");
			try {
				QRZLookupResponse qrzLookupResponse = qrzClient.lookupCallsign(callsign);
				CallbookEntry callbookEntry = new CallbookEntry(qrzLookupResponse);
				callbookEntryRepository.save(callbookEntry);
			} catch ( QRZCallsignNotFoundException e ) {
				// Callsign not found, nothing to do
				logger.error("QRZ Lookup for " + callsign + " returned no results.");
			}
		}
		return callbookEntryRepository.findById(callsign);
	}
}
