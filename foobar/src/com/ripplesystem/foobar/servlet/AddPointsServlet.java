package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBAddPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class AddPointsServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(AddPointsServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public AddPointsServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBAddPoints cmd = new FBAddPoints();
		cmd.setPoints(Long.parseLong(httpReq.getParameter("points")));
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		cmd.setUserToken(httpReq.getParameter("userToken"));
		
		// Hit and run
		execAndRespond(cmd, httpRes);
	}
}
