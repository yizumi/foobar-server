package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBGetRedeemToken;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetRedeemTokenServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public GetRedeemTokenServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public FBGetRedeemToken buildCommand(HttpServletRequest httpReq)
	{
		// Create command using request parameters
		FBGetRedeemToken cmd = new FBGetRedeemToken();
		cmd.setDeviceId(httpReq.getParameter("deviceId"));
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		
		// Hit and run
		return cmd;
	}
}
