package com.ripplesystem.foobar.command;

/**
 * This is a command to get a token for device. 
 * @author izumi
 *
 */
public class FBGetTokenForDevice extends FBCommand
{
	private String deviceId;
	private String deviceToken; // May be null -- not required
	
	public FBGetTokenForDevice()
	{
		super(FBCommandType.GET_TOKEN_FOR_DEVICE);
	}
	
	public String getDeviceId() { return deviceId; }
	public void setDeviceId(String value) { deviceId = value; }
	
	public String getDeviceToken() { return deviceToken; }
	public void setDeviceToken(String value) { deviceToken = value; }
	
	public class Response extends FBCommandResponse
	{
		public static final int FAILCODE_MISSING_DEVICE_ID = 1;
		
		private String _token;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public String getToken() { return _token; }
		public void setToken(String value) { _token = value; }
	}
}
