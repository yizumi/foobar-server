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
import com.ripplesystem.foobar.command.FBAddPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class AddPointsServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(AddPointsServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	private FoobarService fbs;

	@Inject
	public AddPointsServlet(FoobarService fbs)
	{
		this.fbs = fbs;
		JSON.registerConvertor(FBAddPoints.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBAddPoints.Response.class, new JSONObjectConvertor(false));
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
				
		// Execute and return the result
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBAddPoints.Response cmdres = (FBAddPoints.Response)fbs.exec(cmd);
		
		// Write the result
		httpRes.setStatus(200);
		httpRes.setCharacterEncoding("utf-8");
		httpRes.setContentType("application/json");
		String json = JSON.getDefault().toJSON(cmdres);
		httpRes.getWriter().print(json);
		log.info(String.format("===> Response %s", json));
	}
}
