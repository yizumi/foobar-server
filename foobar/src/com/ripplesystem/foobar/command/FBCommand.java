package com.ripplesystem.foobar.command;

/**
 * @author yizumi
 */
public abstract class FBCommand
{
	private FBCommandType commandType;
	
	public FBCommand(FBCommandType commandType)
	{
		this.commandType = commandType;
	}
	
	public FBCommandType getCommandType()
	{
		return commandType;
	}
}
