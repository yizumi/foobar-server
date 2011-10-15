package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBAddPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class AddPointsServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public AddPointsServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public FBAddPoints buildCommand(HttpServletRequest httpReq)
	{
		// Create command using request parameters
		FBAddPoints cmd = new FBAddPoints();
		cmd.setPoints(Long.parseLong(httpReq.getParameter("points")));
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		cmd.setUserToken(httpReq.getParameter("userToken"));
		
		// Hit and run
		return cmd;
	}
}
