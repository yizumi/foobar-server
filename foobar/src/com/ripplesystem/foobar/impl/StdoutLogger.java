package com.ripplesystem.foobar.impl;

import com.ripplesystem.foobar.ILogger;

/**
 * An implementation of ILogger that outputs to Standard Output.
 * @author izumi
 *
 */
public class StdoutLogger implements ILogger
{
	public void Info(String string, Object... args)
	{
		System.out.println(String.format(string, args));
	}
}
