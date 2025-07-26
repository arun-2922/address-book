package com.uc.address_book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uc.address_book.dtos.AddressBookEntryRequest;
import com.uc.address_book.dtos.AddressBookEntryResponse;
import com.uc.address_book.dtos.DeleteAddressBookEntryResponse;
import com.uc.address_book.dtos.SearchAddressBookRequest;
import com.uc.address_book.entity.AddressBookInfoTrie;
import com.uc.address_book.repository.AddressBookDbService;
import com.uc.address_book.repository.AddressBookTrieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressBookService {

    private final AddressBookDbService addressBookDbService;
    private final AddressBookTrieService addressBookTrieService;

    public List<AddressBookEntryResponse> createAddressBookEntry(
            List<AddressBookEntryRequest> createAddressBookEntryRequest) {

        validateCreateAddressBookEntryRequest(createAddressBookEntryRequest);

        return createAddressBookEntryRequest.stream()
                .map(addressBookEntry -> {
                    AddressBookEntryResponse createdEntry = addressBookDbService.save(addressBookEntry);
                    addressBookTrieService.insertEntryInfo(createdEntry);
                    return createdEntry;
                })
                .collect(Collectors.toList());
    }

    private void validateCreateAddressBookEntryRequest(List<AddressBookEntryRequest> createAddressBookEntryRequest) {
        log.debug("validating create request data");
        createAddressBookEntryRequest.stream()
                .forEach(addressBookEntry -> {
                    if (addressBookEntry.getEmail() == null || addressBookEntry.getEmail().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid email field");
                    }
                    if (addressBookEntry.getName() == null || addressBookEntry.getName().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid name field");
                    }
                    if (addressBookEntry.getPhone() == null || addressBookEntry.getPhone().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid phone field");
                    }
                });
    }

    public List<AddressBookEntryResponse> searchAddressBookRequest(SearchAddressBookRequest searchAddressBookRequest) {
        String keyWordToSearch = searchAddressBookRequest.getQuery();
        if (keyWordToSearch == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid query");
        }
        AddressBookInfoTrie currNodeInfo = addressBookTrieService.searchKeyword(keyWordToSearch);

        if (currNodeInfo == null)
            return new ArrayList<>();

        return currNodeInfo.getAddressIdsWithCurrPrefixInfo().stream()
                .map(id -> {
                    AddressBookEntryResponse entryResponse = addressBookDbService.getAddressBookEntryById(id);
                    if (entryResponse == null) {
                        log.error("Unexpected error, no issue from client");
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error");
                    } else
                        return entryResponse;
                })
                .collect(Collectors.toList());
    }

    public List<AddressBookEntryResponse> updateAddressBookEntry(
            List<AddressBookEntryRequest> updateAddressBookEntryRequest) {
        validateUpdateAddressBookEntryRequest(updateAddressBookEntryRequest);

        List<AddressBookEntryResponse> response = new ArrayList<>();

        for (AddressBookEntryRequest addressBookEntry : updateAddressBookEntryRequest) {
            String entryId = addressBookEntry.getId();
            AddressBookEntryResponse originalEntry = addressBookDbService.getAddressBookEntryById(entryId);
            if (originalEntry == null) {
                log.error("entry to update with id: {} does not exist", entryId);
                continue;
            }
            AddressBookEntryResponse updatedEntry = addressBookDbService.update(addressBookEntry);
            addressBookTrieService.removeEntryInfo(originalEntry);
            addressBookTrieService.insertEntryInfo(updatedEntry);

            response.add(updatedEntry);
        }
        return response;
    }

    private void validateUpdateAddressBookEntryRequest(List<AddressBookEntryRequest> updateAddressBookEntryRequest) {
        log.debug("validating update request");
        updateAddressBookEntryRequest.stream()
                .forEach(addressBookEntry -> {
                    if (addressBookEntry.getEmail() != null && addressBookEntry.getEmail().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid email field");
                    }
                    if (addressBookEntry.getName() != null && addressBookEntry.getName().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid name field");
                    }
                    if (addressBookEntry.getPhone() != null && addressBookEntry.getPhone().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid phone field");
                    }
                    if (addressBookEntry.getId() == null || addressBookEntry.getId().isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid Id field");
                    }
                });

    }

    public DeleteAddressBookEntryResponse deleteAddressBookEntry(List<String> deleteAddressBookEntryRequest) {
        validateDeleteAddressBookEntryRequest(deleteAddressBookEntryRequest);

        long entriesDeleted = 0;

        for (String entryToDeleteId : deleteAddressBookEntryRequest) {
            AddressBookEntryResponse deletedEntry = addressBookDbService.getAddressBookEntryById(entryToDeleteId);
            if (deletedEntry == null) {
                log.error("entry not found with id: {} in our in-memory db", entryToDeleteId);
                continue;
            }
            addressBookDbService.deleteEntryById(entryToDeleteId);
            addressBookTrieService.removeEntryInfo(deletedEntry);
            entriesDeleted++;
        }

        return DeleteAddressBookEntryResponse.builder().deleted(entriesDeleted).build();
    }

    private void validateDeleteAddressBookEntryRequest(List<String> deleteAddressBookEntryRequest) {
        log.debug("validating delete request");
        deleteAddressBookEntryRequest.stream()
                .forEach(deleteId -> {
                    if (deleteId == null || deleteId.isBlank()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " invalid Id field");
                    }
                });
    }

}
