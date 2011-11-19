package com.ripplesystem.foobar.command;

import java.util.List;

import com.ripplesystem.foobar.model.TransactionInfo;

/**
 * Requeusts FoobarService to get Transaction history for a shop or user.
 * If both userToken and shopKey, this will return result for both the shop and the user.
 * @author izumi
 */
public final class FBQueryTransactions extends FBCommand
{
	public static final int MAX_COUNT = 25;
	
	private Long shopKey;
	private String userToken;
	private int count;
	private int page;
	
	public FBQueryTransactions()
	{
		super(FBCommandType.QUERY_TRANSACTIONS);
	}
	
	public Long getShopKey() { return shopKey; }
	public void setShopKey(Long value) { shopKey = value; }
	
	public String getUserToken() { return userToken; }
	public void setUserToken(String value) { userToken = value; }
	
	public int getCount() { return count; } 
	public void setCount(int value) { count = value; }
	
	public int getPage() { return page; }
	public void setPage(int value) { page = value; }
	
	public final class Response extends FBCommandResponse
	{
		public static final int FAILCODE_MISSING_PARAMETERS = 1;
		public static final int FAILCODE_NO_SUCH_USER = 2;
		public static final int FAILCODE_NO_SUCH_SHOP = 3;
		
		private int page;
		private int count;
		private boolean hasMoreFlag;
		private List<TransactionInfo> transactions;
		
		public Response(boolean success)
		{
			super(success);
		}
		
		public int getPage() { return page; }
		public void setPage(int value) { page = value; }
		
		public int getCount() { return count; }
		public void setCount(int value) { count = value; }

		public boolean isHasMore() { return hasMoreFlag; }
		public void setHasMore(boolean value) { hasMoreFlag = value; }
		
		public List<TransactionInfo> getTransactions() { return transactions; }
		public void setTransactions(List<TransactionInfo> value) { transactions = value; }
		
	}
}
