package com.ripplesystem.foobar.command;

public class FBRedeemPoints extends FBCommand
{
	private long shopKey;
	private String redeemToken;
	private long points;
	
	public FBRedeemPoints()
	{
		super(FBCommandType.REDEEM_POINTS);
	}
	
	public long getShopKey() { return shopKey; }
	public void setShopKey(long value) { shopKey = value; }
	
	public String getRedeemToken() { return redeemToken; }
	public void setRedeemToken(String value) { redeemToken = value; }
	
	public long getPoints() { return points; }
	public void setPoints(long value) { points = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_SHOP_NOT_FOUND = 1;
		public static final int FAILCODE_TOKEN_NOT_FOUND = 2;
		public static final int FAILCODE_INSUFFICIENT_POINTS = 3;
		public static final int FAILCODE_TOKEN_EXPIRED = 4; // For user's security!
		
		private long remainingPoints;		
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public long getRemainingPoints() { return remainingPoints; }
		public void setRemainingPoints(long value) { remainingPoints = value; }
	}
}
