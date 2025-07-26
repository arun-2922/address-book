package com.uc.address_book.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteAddressBookEntryResponse {
    private long deleted;
}
