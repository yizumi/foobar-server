package com.ripplesystem.foobar.command;

public class FBCancelTransaction extends FBCommand
{
	private Long transactionKey;
	
	public FBCancelTransaction()
	{
		super(FBCommandType.CANCEL_TRANSACTION);
	}
	
	public Long getTransactionKey() { return transactionKey; }
	public void setTransactionKey(Long value) { transactionKey = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_INVALID_KEY = 1;
		public static final int FAILCODE_ALREADY_CANCELLED = 2;
		public static final int FAILCODE_USER_NOT_FOUND = 3;
		public static final int FAILCODE_POSITION_NOT_FOUND = 4;

		private long transactionKey;
		private long userKey;
		private long shopKey;
		private long balance;
		private String userToken;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public long getTransactionKey() { return transactionKey; }
		public void setTransactionKey(long value) { transactionKey = value; }
		
		public long getUserKey() { return userKey; }
		public void setUserKey(long value) { userKey = value; }
		
		public long getShopKey() { return shopKey; }
		public void setShopKey(long value) { shopKey = value; }
		
		public long getRemainingPoints() { return balance; }
		public void setBalance(long value) { balance = value; }
		
		public String getUserToken() { return userToken; }
		public void setUserToken(String value) { userToken = value; }
	}
}
