package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBGetShopListForDevice;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetShopListForDeviceServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;

	@Inject
	public GetShopListForDeviceServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Create command using request parameters
		FBGetShopListForDevice cmd = new FBGetShopListForDevice();
		cmd.setDeviceId(req.getParameter("deviceId"));
		return cmd;
	}
}
