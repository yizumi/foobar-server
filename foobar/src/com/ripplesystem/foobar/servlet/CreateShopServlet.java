package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.ajax.JSON;
import org.mortbay.util.ajax.JSONObjectConvertor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCreateShop;
import com.ripplesystem.foobar.model.ShopInfo;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class CreateShopServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(CreateShopServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	private FoobarService fbs;

	@Inject
	public CreateShopServlet(FoobarService fbs)
	{
		this.fbs = fbs;
		JSON.registerConvertor(FBCreateShop.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBCreateShop.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(ShopInfo.class, new JSONObjectConvertor(false));
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBCreateShop cmd = new FBCreateShop();
		cmd.setAddress(httpReq.getParameter("address"));
		cmd.setEmail(httpReq.getParameter("email"));
		cmd.setImageUrl(httpReq.getParameter("imageUrl"));
		cmd.setName(httpReq.getParameter("name"));
		cmd.setPassword(httpReq.getParameter("password"));
		cmd.setPreferredLang(httpReq.getParameter("preferredLang"));
		cmd.setTel(httpReq.getParameter("tel"));
		cmd.setUrl(httpReq.getParameter("url"));
				
		// Execute and return the result
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBCreateShop.Response cmdres = (FBCreateShop.Response)fbs.exec(cmd);
		
		// Write the result
		httpRes.setStatus(200);
		httpRes.setCharacterEncoding("utf-8");
		httpRes.setContentType("application/json");
		String json = JSON.getDefault().toJSON(cmdres);
		httpRes.getWriter().print(json);
		log.info(String.format("===> Response %s", json));
	}
}
