package com.uc.address_book.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressBookInfoTrie {
    private Map<Character, AddressBookInfoTrie> childTrieNode = new ConcurrentHashMap<>();
    private Set<String> addressIdsWithCurrPrefixInfo = new HashSet<>();
}
