package com.ripplesystem.foobar.command;

import com.ripplesystem.foobar.model.ShopInfo;

public class FBGetShopInfo extends FBCommand
{
	private long shopKey;
	
	public FBGetShopInfo()
	{
		super(FBCommandType.GET_SHOP_INFO);
	}
	
	public long getShopKey() { return shopKey; }
	public void setShopKey(long value) { shopKey = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_SHOP_NOT_FOUND = 1;
		
		private ShopInfo shop;
		
		public Response(boolean success)
		{
			super(success);
		}
		
		public ShopInfo getShop() { return shop; }
		public void setShop(ShopInfo value) { shop = value; }
	}
}
