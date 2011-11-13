package com.ripplesystem.foobar.command;

import com.ripplesystem.foobar.model.ShopInfo;

public final class FBLoginShop extends FBCommand
{
	private String email;
	private String password;
	
	public FBLoginShop()
	{
		super(FBCommandType.LOGIN_SHOP);
	}

	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public class Response extends FBCommandResponse
	{
		private long shopKey;
		private ShopInfo shop;
		
		public static final int FAILCODE_SHOP_NOT_FOUND = 1;
		public static final int FAILCODE_PASSWORD_MISMATCH = 2;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public long getShopKey() { return shopKey; }
		public void setShopKey(long value) { shopKey = value; }
		
		public ShopInfo getShop() { return shop; }
		public void setShop(ShopInfo value) { shop = value; }
	}
}
