package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBGetTokenForDevice;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetTokenForDeviceServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;

	@Inject
	public GetTokenForDeviceServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Translate Http Request to a command
		FBGetTokenForDevice cmd = new FBGetTokenForDevice();
		cmd.setDeviceId(req.getParameter("deviceId"));
		cmd.setDeviceToken(req.getParameter("deviceToken"));
		return cmd;
	}
}
