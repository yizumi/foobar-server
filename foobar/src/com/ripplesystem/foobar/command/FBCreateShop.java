package com.ripplesystem.foobar.command;

public class FBCreateShop extends FBCommand
{
	private String name;
	private String address;
	private String tel;
	private String url;
	private String imageUrl;
	private String email;
	private String password;
	private String preferredLang;
	
	public FBCreateShop()
	{
		super(FBCommandType.CREATE_SHOP);
	}

	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	public String getAddress() { return address; }
	public void setAddress(String value) { address = value; }
	
	public String getTel() { return tel; }
	public void setTel(String value) { tel = value; }
	
	public String getUrl() { return url; }
	public void setUrl(String value) { url = value; }
	
	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String value) { imageUrl = value; }
	
	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public String getPreferredLang() { return preferredLang; }
	public void setPreferredLang(String value) { preferredLang = value; }

	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_EMAIL_EXISTS_ALREADY = 1;
		public static final int FAILCODE_MISSING_REQUIRED_FIELD = 2;
		
		private long shopKey;
		
		public Response(boolean success)
		{
			super(success);
		}
		
		public long getShopKey() { return shopKey; }
		public void setShopKey(long value) { shopKey = value; }
	}
}
