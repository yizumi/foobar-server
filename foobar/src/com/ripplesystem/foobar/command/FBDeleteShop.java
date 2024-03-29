package com.ripplesystem.foobar.command;

public class FBDeleteShop extends FBCommand
{
	private String email;
	private String password;
	
	public FBDeleteShop()
	{
		super(FBCommandType.DELETE_SHOP);
	}
	
	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_STORE_NOT_FOUND = 1;
		public static final int FAILCODE_PASSWORD_NOT_MATCH = 2;
		public static final int FAILCODE_CONTRACT_MISMATCH = 3;
		
		public Response(boolean success)
		{
			super(success);
		}
	}
}
