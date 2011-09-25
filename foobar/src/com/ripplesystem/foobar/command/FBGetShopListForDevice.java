package com.ripplesystem.foobar.command;

import java.util.HashMap;
import java.util.List;

import com.ripplesystem.foobar.model.ShopInfo;

public class FBGetShopListForDevice extends FBCommand
{	
	private String deviceId;
	
	public FBGetShopListForDevice()
	{
		super(FBCommandType.GET_SHOP_LIST_FOR_DEVICE);
	}
	
	public String getDeviceId() { return deviceId; }
	public void setDeviceId(String value) { deviceId = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_DEVICE_NOT_FOUND = 1;
		
		private List<ShopInfo> shops;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public List<ShopInfo> getShops() { return shops; }
		public void setShops(List<ShopInfo> value) { shops = value; }
	}
}
