package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCreateShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class CreateShopServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(CreateShopServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public CreateShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest rawReq, HttpServletResponse httpRes) throws IOException
	{
		MultipartRequestWrapper httpReq = new MultipartRequestWrapper(rawReq);
		
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
	    // Create command using request parameters
		FBCreateShop cmd = new FBCreateShop();
		cmd.setAddress(httpReq.getParameter("address"));
		cmd.setEmail(httpReq.getParameter("email"));
		cmd.setName(httpReq.getParameter("name"));
		cmd.setPassword(httpReq.getParameter("password"));
		cmd.setPreferredLang(httpReq.getParameter("preferredLang"));
		cmd.setTel(httpReq.getParameter("tel"));
		cmd.setUrl(httpReq.getParameter("url"));
		cmd.setImage(httpReq.getFile("image"));
		
		// Save and respond
		execAndRespond(cmd, httpRes);
	}
}
