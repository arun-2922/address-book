package com.uc.address_book.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uc.address_book.dtos.AddressBookEntryRequest;
import com.uc.address_book.dtos.AddressBookEntryResponse;
import com.uc.address_book.dtos.DeleteAddressBookEntryResponse;
import com.uc.address_book.dtos.SearchAddressBookRequest;
import com.uc.address_book.service.AddressBookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressBookController {

    private final AddressBookService addressBookService;

    @PostMapping("/create")
    public ResponseEntity<List<AddressBookEntryResponse>> createAddressBookEntry(
            @RequestBody List<AddressBookEntryRequest> createAddressBookEntryRequest) {
        log.info("received request to create address book entry");
        return ResponseEntity.ok().body(addressBookService.createAddressBookEntry(createAddressBookEntryRequest));
    }

    //Should be PatchMapping when some of the fields are getting updated
    @PutMapping("/update")
    public ResponseEntity<List<AddressBookEntryResponse>> updateAddressBookEntry(
            @RequestBody List<AddressBookEntryRequest> updateAddressBookEntryRequest) {
        log.info("received request to update address book entry");
        return ResponseEntity.ok().body(addressBookService.updateAddressBookEntry(updateAddressBookEntryRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteAddressBookEntryResponse> deleteAddressBookEntry(
            @RequestBody List<String> deleteAddressBookEntryRequest) {
        log.info("received request to delete address book entry");
        return ResponseEntity.ok().body(addressBookService.deleteAddressBookEntry(deleteAddressBookEntryRequest));
    }

    @PostMapping("/search")
    public ResponseEntity<List<AddressBookEntryResponse>> searchAddressBook(
            @RequestBody SearchAddressBookRequest searchAddressBookRequest) {
        log.info("received request to search address book entry");
        return ResponseEntity.ok().body(addressBookService.searchAddressBookRequest(searchAddressBookRequest));
    }

}
