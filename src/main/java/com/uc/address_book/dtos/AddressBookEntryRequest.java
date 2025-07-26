package com.uc.address_book.dtos;

import lombok.Data;

@Data
public class AddressBookEntryRequest {
    private String id;
    private String name;
    private String phone;
    private String email;
}
