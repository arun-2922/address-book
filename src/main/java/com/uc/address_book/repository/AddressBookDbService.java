package com.uc.address_book.repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.uc.address_book.dtos.AddressBookEntryRequest;
import com.uc.address_book.dtos.AddressBookEntryResponse;
import com.uc.address_book.entity.AddressBook;
import com.uc.address_book.mapper.AddressBookMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressBookDbService {

    private final AddressBookMapper addressBookMapper;

    private ConcurrentHashMap<String, AddressBook> addressBookRecords = new ConcurrentHashMap<>(100000);

    public AddressBookEntryResponse save(AddressBookEntryRequest addressBookEntry) {
        log.info("saving entry");
        AddressBook addressBook = addressBookMapper.requestToEntity(addressBookEntry);
        //addressBook.setId(UUID.randomUUID().toString());
        addressBookRecords.put(addressBook.getId(),addressBook);

        return addressBookMapper.toDto(addressBook);
    }

    public AddressBookEntryResponse getAddressBookEntryById(String id) {
        return addressBookMapper.toDto(addressBookRecords.get(id));
    }

    public AddressBookEntryResponse update(AddressBookEntryRequest addressBookEntry) {
        String entryId = addressBookEntry.getId();
        log.info("updating entry with id: {}", entryId);

        AddressBookEntryResponse currentAddressBookEntryResponse = getAddressBookEntryById(entryId);

        if(addressBookEntry.getEmail()!=null){
            currentAddressBookEntryResponse.setEmail(addressBookEntry.getEmail());
        }
        if(addressBookEntry.getName()!=null){
            currentAddressBookEntryResponse.setName(addressBookEntry.getName());
        }
        if(addressBookEntry.getPhone()!=null){
            currentAddressBookEntryResponse.setPhone(addressBookEntry.getPhone());
        }

        addressBookRecords.put(entryId, addressBookMapper.responsetoEntity(currentAddressBookEntryResponse));

        return currentAddressBookEntryResponse;
    }

    public AddressBook deleteEntryById(String entryToDeleteId) {
        log.info("deleting entry with id:{}", entryToDeleteId);
        return addressBookRecords.remove(entryToDeleteId);
    }

    
}
