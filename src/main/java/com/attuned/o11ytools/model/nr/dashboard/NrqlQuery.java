package com.attuned.o11ytools.model.nr.dashboard;

import java.util.Arrays;

public class NrqlQuery {

    private String[] accountIds;
    private String query;
    
    public NrqlQuery() {
    	
    }
    
	public NrqlQuery(String[] accountIds, String query) {
		super();
		this.accountIds = accountIds;
		this.query = query;
	}

	public String[] getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(String[] accountIds) {
		this.accountIds = accountIds;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "NrqlQuery [accountIds=" + Arrays.toString(accountIds) + ", query=" + query + "]";
	}
	
	

}
