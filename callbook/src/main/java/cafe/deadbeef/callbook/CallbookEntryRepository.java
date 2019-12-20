package cafe.deadbeef.callbook;

import org.springframework.data.repository.CrudRepository;

import cafe.deadbeef.callbook.web.dto.CallbookEntry;

public interface CallbookEntryRepository extends CrudRepository<CallbookEntry, String> {

}
