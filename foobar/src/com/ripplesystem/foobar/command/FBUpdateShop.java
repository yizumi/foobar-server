package com.ripplesystem.foobar.command;

import com.google.appengine.api.datastore.Blob;
import com.ripplesystem.foobar.model.ShopInfo;

public class FBUpdateShop extends FBCommand
{
	private long shopKey;
	private String name;
	private String address;
	private String tel;
	private String url;
	private Blob image;
	private String email;
	private String password;
	private String preferredLang;
	
	public FBUpdateShop()
	{
		super(FBCommandType.UPDATE_SHOP);
	}

	public long getShopKey() { return shopKey; }
	public void setShopKey(long value) { shopKey = value; }
	
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	public String getAddress() { return address; }
	public void setAddress(String value) { address = value; }
	
	public String getTel() { return tel; }
	public void setTel(String value) { tel = value; }
	
	public String getUrl() { return url; }
	public void setUrl(String value) { url = value; }
	
	public Blob getImage() { return image; }
	public void setImage(Blob value) { image= value; }
	
	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public String getPreferredLang() { return preferredLang; }
	public void setPreferredLang(String value) { preferredLang = value; }

	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_SHOP_NOT_EXIST = 1;
		public static final int FAILCODE_EMAIL_EXISTS_ALREADY = 2;
		public static final int FAILCODE_MISSING_REQUIRED_FIELD = 3;
		
		private long shopKey;
		private ShopInfo shop;
		
		public Response(boolean success)
		{
			super(success);
		}
		
		public long getShopKey() { return shopKey; }
		public void setShopKey(long value) { shopKey = value; }
		
		public ShopInfo getShop() { return shop; }
		public void setShop(ShopInfo value) { shop = value; }
	}
	

}
