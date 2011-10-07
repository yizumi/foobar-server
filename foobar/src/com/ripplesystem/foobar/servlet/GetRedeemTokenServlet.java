package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBGetRedeemToken;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetRedeemTokenServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(GetRedeemTokenServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public GetRedeemTokenServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBGetRedeemToken cmd = new FBGetRedeemToken();
		cmd.setDeviceId(httpReq.getParameter("deviceId"));
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		
		// Hit and run
		execAndRespond(cmd, httpRes);
	}
}
