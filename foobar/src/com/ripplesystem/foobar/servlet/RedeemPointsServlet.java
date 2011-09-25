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
import com.ripplesystem.foobar.command.FBRedeemPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class RedeemPointsServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(RedeemPointsServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	private FoobarService fbs;

	@Inject
	public RedeemPointsServlet(FoobarService fbs)
	{
		this.fbs = fbs;
		JSON.registerConvertor(FBRedeemPoints.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBRedeemPoints.Response.class, new JSONObjectConvertor(false));		
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
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBRedeemPoints.Response res = (FBRedeemPoints.Response)fbs.exec(cmd);
		
		// Return the result
		httpRes.setStatus(200);
		httpRes.setCharacterEncoding("utf-8");
		httpRes.setContentType("application/json");
		String json = JSON.getDefault().toJSON(res);
		httpRes.getWriter().print(json);
		log.info(String.format("===> Response %s", json));
	}
}