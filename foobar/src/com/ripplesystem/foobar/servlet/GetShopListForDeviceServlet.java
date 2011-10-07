package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBGetShopListForDevice;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetShopListForDeviceServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(GetShopListForDeviceServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Inject
	public GetShopListForDeviceServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBGetShopListForDevice cmd = new FBGetShopListForDevice();
		cmd.setDeviceId(httpReq.getParameter("deviceId"));
		
		// Exec and respond
		execAndRespond(cmd, httpRes);
	}
}
