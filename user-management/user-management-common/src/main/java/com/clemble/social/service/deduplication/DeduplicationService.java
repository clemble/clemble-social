package com.clemble.social.service.deduplication;


public interface DeduplicationService {
    
    public void checkForDuplicates(String userID, String providerID);

    public void checkForDuplicates(String userID, String providerID, String contactID);

}
