package main.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import main.SystemContextHolder;
import main.customExceptions.CustomGeneralException;
import main.dbmodels.DbContact;
import main.dbmodels.DbUser;
import main.jparepositories.ContactRepository;
import main.restmodels.BaseResponse;
import main.restmodels.ContactFilterRequest;
import main.restmodels.FetchAllContactsRequest;
import main.restmodels.NewContactSaveRequest;
import main.utils.GenericUtils;
import main.utils.GsonUtils;

@RestController
@RequestMapping({ "/contact" })
public class ContactController {

	@Autowired
	private ContactRepository contactRepo;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	protected ResponseEntity<?> saveContact(@RequestBody NewContactSaveRequest contactSaveRequest) {
		DbContact dbContact = GsonUtils.convert(contactSaveRequest, DbContact.class);
		DbUser dbUser = SystemContextHolder.getLoggedInUser();
		dbContact.setUserId(dbUser.getId());
		contactRepo.save(dbContact);
		return ResponseEntity.ok(BaseResponse.success());
	}

	@RequestMapping(value = "/fetch-all", method = RequestMethod.POST)
	protected ResponseEntity<?> getAllContacts(@RequestBody FetchAllContactsRequest request) {
		DbUser dbUser = SystemContextHolder.getLoggedInUser();
		Integer page = request.getPage();
		Page<DbContact> contacts = contactRepo.findByUserId(dbUser.getId(), new PageRequest(page, 10));
		return ResponseEntity.ok(contacts);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	protected ResponseEntity<?> filterAndFetchContact(@RequestBody ContactFilterRequest filter) {
		DbUser dbUser = SystemContextHolder.getLoggedInUser();
		List<DbContact> contacts = ObjectUtils.firstNonNull(contactRepo.findByUserId(dbUser.getId()),
				Collections.emptyList());
		List<DbContact> filteredContacts = GenericUtils.filter(contacts, filter);
		return ResponseEntity.ok(filteredContacts);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	protected ResponseEntity<?> updateContact(@RequestBody DbContact updateRequest) throws CustomGeneralException {
		updateRequest.setUserId(null);
		DbContact contact = contactRepo.findById(updateRequest.getId());
		if (contact.getUserId() != SystemContextHolder.getLoggedInUser().getId()) {
			throw new CustomGeneralException("Could not update");
		}
		DbContact updatedContact = GenericUtils.update(contact, updateRequest);
		contactRepo.save(updatedContact);
		return ResponseEntity.ok(BaseResponse.success());
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	protected ResponseEntity<?> deleteContact(@RequestBody ContactFilterRequest filter) {
		DbUser dbUser = SystemContextHolder.getLoggedInUser();
		List<DbContact> contacts = contactRepo.findByUserId(dbUser.getId());
		List<DbContact> filteredContacts = GenericUtils.filter(contacts, filter);
		filteredContacts.forEach(contact -> contactRepo.delete(contact));
		return ResponseEntity.ok(BaseResponse.success());
	}

	@RequestMapping(value = "/dummy-contacts", method = RequestMethod.GET)
	protected void saveDummyContacts() {
		List<String> dummyNames = Arrays.asList("Tim", "Bob", "Duke", "Harry", "Michelle", "Aster", "Jane");
		for (int i = 0; i < 100; i++) {
			DbContact dbContact = DbContact.builder()
					.name(dummyNames.get(((int) (Math.random() * 10)) % dummyNames.size()))
					.userId(SystemContextHolder.getLoggedInUser().getId()).description("Dummy description").build();
			dbContact.setEmail(dbContact.getName() + i + "@scm.com");
			StringBuilder phone = new StringBuilder("");
			while(phone.length() != 10) {
				phone.append((int)(Math.random()*10));
			}
			dbContact.setPhone(phone.toString());
			contactRepo.save(dbContact);
		}
	}
}
