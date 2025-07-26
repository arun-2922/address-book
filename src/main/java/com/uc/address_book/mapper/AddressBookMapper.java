package com.uc.address_book.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uc.address_book.dtos.AddressBookEntryRequest;
import com.uc.address_book.dtos.AddressBookEntryResponse;
import com.uc.address_book.entity.AddressBook;

@Mapper(componentModel = "spring")
public interface AddressBookMapper {
    @Mapping(target = "id", ignore = true)
    AddressBook requestToEntity(AddressBookEntryRequest request);

    AddressBookEntryResponse toDto(AddressBook addressBook);
    AddressBook responsetoEntity(AddressBookEntryResponse response);
}
