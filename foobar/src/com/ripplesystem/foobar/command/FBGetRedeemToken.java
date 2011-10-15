package com.ripplesystem.foobar.command;

import java.util.Date;

public class FBGetRedeemToken extends FBCommand
{
	private String deviceId;
	private long shopKey;
	
	public FBGetRedeemToken()
	{
		super(FBCommandType.GET_REDEEM_TOKEN);
	}
	
	public String getDeviceId() { return deviceId; }
	public void setDeviceId(String value) { deviceId = value; }
	
	public long getShopKey() { return shopKey; }
	public void setShopKey(long value) { shopKey = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_NO_DEVICE_FOUND = 1;
		public static final int FAILCODE_NO_SHOP_FOUND = 2;
		public static final int FAILCODE_NO_POINTS_TO_REDEEM = 3;
		public static final int FAILCODE_TOKEN_ASSIGN_FAILED = 4;
		
		private String redeemToken;
		private Date expiration;
		
		public Response(boolean success)
		{
			super(success);
		}
		
		public String getRedeemToken() { return redeemToken; }
		public void setRedeemToken(String value) { redeemToken = value; }
		
		public Date getExpiration() { return expiration; }
		public void setExpiration(Date value) { expiration = value; }
	}
}
