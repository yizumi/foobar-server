package com.ripplesystem.foobar.command;

/**
 * This is a command to get a token for device. 
 * @author izumi
 *
 */
public class FBGetTokenForDevice extends FBCommand
{
	private String _deviceId;
	
	public FBGetTokenForDevice()
	{
		super(FBCommandType.GET_TOKEN_FOR_DEVICE);
	}
	
	public String getDeviceId() { return _deviceId; }
	public void setDeviceId(String value) { _deviceId = value; }
	
	public class Response extends FBCommandResponse
	{
		private String _token;
		
		public Response(boolean isSuccess)
		{
			super(isSuccess);
		}
		
		public String getToken() { return _token; }
		public void setToken(String value) { _token = value; }
	}
}