package com.uc.address_book.entity;

import java.util.UUID;

import lombok.Data;

@Data
public class AddressBook {
    private String id;
    private String name;
    private String phone;
    private String email;

    public AddressBook(){
        this.id = UUID.randomUUID().toString();
    }
}
