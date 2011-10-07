package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBRedeemPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class RedeemPointsServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(RedeemPointsServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public RedeemPointsServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Log
		log.info("Requested GetTokenForDevice");
		
		// Build the command
		FBRedeemPoints cmd = new FBRedeemPoints();
		cmd.setPoints(Long.parseLong(httpReq.getParameter("points")));
		cmd.setRedeemToken(httpReq.getParameter("redeemToken"));
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		
		// Exec command
		execAndRespond(cmd, httpRes);
	}
}
