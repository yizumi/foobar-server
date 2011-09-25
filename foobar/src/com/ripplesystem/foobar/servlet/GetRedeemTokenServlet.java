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
import com.ripplesystem.foobar.command.FBGetRedeemToken;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetRedeemTokenServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(GetRedeemTokenServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	private FoobarService fbs;

	@Inject
	public GetRedeemTokenServlet(FoobarService fbs)
	{
		this.fbs = fbs;
		JSON.registerConvertor(FBGetRedeemToken.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetRedeemToken.Response.class, new JSONObjectConvertor(false));
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
				
		// Execute and return the result
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBGetRedeemToken.Response cmdres = (FBGetRedeemToken.Response)fbs.exec(cmd);
		
		// Write the result
		httpRes.setStatus(200);
		httpRes.setCharacterEncoding("utf-8");
		httpRes.setContentType("application/json");
		String json = JSON.getDefault().toJSON(cmdres);
		httpRes.getWriter().print(json);
		log.info(String.format("===> Response %s", json));
	}
}
