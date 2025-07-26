package com.uc.address_book.repository;

import org.springframework.stereotype.Service;

import com.uc.address_book.dtos.AddressBookEntryResponse;
import com.uc.address_book.entity.AddressBookInfoTrie;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressBookTrieService {

    private AddressBookInfoTrie rootInfo = new AddressBookInfoTrie();

    public void insertEntryInfo(AddressBookEntryResponse createdEntry) {
        log.debug("inserting entry info in trie");
        if (createdEntry != null) {
            insertFieldValue(createdEntry.getName(), createdEntry.getId());
            insertFieldValue(createdEntry.getPhone(), createdEntry.getId());
            insertFieldValue(createdEntry.getEmail(), createdEntry.getId());
            insertFieldValue(createdEntry.getId(), createdEntry.getId());
        } 
    }

    private void insertFieldValue(String value, String entryId) {

        for (int a = 0; a < value.length(); a++) {
            AddressBookInfoTrie currNodeInfo = rootInfo;
            for (int b = a; b < value.length(); b++) {
                char ch = value.charAt(b);
                currNodeInfo = currNodeInfo.getChildTrieNode().computeIfAbsent(ch, c -> new AddressBookInfoTrie());
                currNodeInfo.getAddressIdsWithCurrPrefixInfo().add(entryId);
            }
        }

    }

    public AddressBookInfoTrie searchKeyword(String keyWordToSearch) {
        log.debug("searching keyword: {} in trie", keyWordToSearch);
        AddressBookInfoTrie currNodeInfo = rootInfo;
        for (int a = 0; a < keyWordToSearch.length(); a++) {
            char ch = keyWordToSearch.charAt(a);
            if (!currNodeInfo.getChildTrieNode().containsKey(ch)) {
                return null;
            } else {
                currNodeInfo = currNodeInfo.getChildTrieNode().get(ch);
            }
        }

        return currNodeInfo;
    }

    public void removeEntryInfo(AddressBookEntryResponse entry) {
        log.debug("removing entry from trie");
        if (entry != null) {
            removeFieldValue(entry.getName(), entry.getId());
            removeFieldValue(entry.getPhone(), entry.getId());
            removeFieldValue(entry.getEmail(), entry.getId());
            removeFieldValue(entry.getId(), entry.getId());
        }
    }

    private void removeFieldValue(String value, String entryId) {
        for (int a = 0; a < value.length(); a++) {
            AddressBookInfoTrie currNodeInfo = rootInfo;
            for(int b = a;b < value.length(); b++){
                char ch = value.charAt(b);
                currNodeInfo = currNodeInfo.getChildTrieNode().get(ch);
                if(currNodeInfo!=null){
                    currNodeInfo.getAddressIdsWithCurrPrefixInfo().remove(entryId);
                }
            }
        }

    }

}
