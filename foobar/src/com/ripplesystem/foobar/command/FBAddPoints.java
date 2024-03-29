package com.ripplesystem.foobar.command;

public class FBAddPoints extends FBCommand
{
	private String userToken;
	private long shopKey;
	private long points;
	
	public FBAddPoints()
	{
		super(FBCommandType.ADD_POINTS);
	}
	
	public String getUserToken() { return userToken; }
	public void setUserToken(String value) { userToken = value; }
	
	public long getPoints() { return points; }
	public void setPoints(long value) { points = value; }

	public long getShopKey() { return shopKey; }
	public void setShopKey(long value) { shopKey = value; }	
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_USER_NOT_FOUND = 10;
		public static final int FAILCODE_SHOP_NOT_FOUND = 20;
		public static final int FAILCODE_REACHED_USER_MAXIMUM = 30;
		public static final int FAILCODE_REACHED_SHOP_MAXIMUM = 40;
		
		private long currentPoints;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public long getCurrentPoints() { return currentPoints; }
		public void setCurrentPoints(long value) { currentPoints = value; }
	}
}
