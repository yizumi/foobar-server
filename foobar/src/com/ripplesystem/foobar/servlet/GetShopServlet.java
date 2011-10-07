package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBGetShopInfo;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetShopServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(GetShopServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Inject
	public GetShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBGetShopInfo cmd = new FBGetShopInfo();
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));

		// Hit and run
		execAndRespond(cmd, httpRes);
	}
}
