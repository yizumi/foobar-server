package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBUpdateShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class UpdateShopServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(UpdateShopServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public UpdateShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBUpdateShop cmd = new FBUpdateShop();
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		cmd.setAddress(httpReq.getParameter("address"));
		cmd.setEmail(httpReq.getParameter("email"));
		cmd.setImageUrl(httpReq.getParameter("imageUrl"));
		cmd.setName(httpReq.getParameter("name"));
		cmd.setPassword(httpReq.getParameter("password"));
		cmd.setPreferredLang(httpReq.getParameter("preferredLang"));
		cmd.setTel(httpReq.getParameter("tel"));
		cmd.setUrl(httpReq.getParameter("url"));
		
		execAndRespond(cmd, httpRes);				
	}
}
