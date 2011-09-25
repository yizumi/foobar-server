package com.ripplesystem.foobar.command;

public abstract class FBCommandResponse
{
	protected  boolean isSuccessFlag;
	protected int failCode;
	
	public FBCommandResponse(boolean isSuccess)
	{
		this.isSuccessFlag = isSuccess;
	}

	public boolean isSuccess() { return isSuccessFlag; }
	public void setSuccess(boolean value) { isSuccessFlag = value; }
	
	public int getFailCode() { return failCode; }
	public void setFailCode(int value) { failCode = value; }
}
