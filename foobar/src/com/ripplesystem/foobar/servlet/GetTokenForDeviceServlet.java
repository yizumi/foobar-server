package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBGetTokenForDevice;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetTokenForDeviceServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(GetTokenForDeviceServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Inject
	public GetTokenForDeviceServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Log the request
		log.info("Requested GetTokenForDevice");
		
		// Translate Http Request to a command
		FBGetTokenForDevice cmd = new FBGetTokenForDevice();
		cmd.setDeviceId(httpReq.getParameter("deviceId"));
		cmd.setDeviceToken(httpReq.getParameter("deviceToken"));
		
		// run and respond
		execAndRespond(cmd, httpRes);
	}
}
