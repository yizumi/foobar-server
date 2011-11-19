package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.ajax.JSON;
import org.mortbay.util.ajax.JSONObjectConvertor;

import com.ripplesystem.foobar.command.*;
import com.ripplesystem.foobar.model.*;
import com.ripplesystem.foobar.service.FoobarService;

public abstract class FBHttpServletBase extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FBHttpServletBase.class.getName());
	protected FoobarService fbs;
	
	static
	{
		// Here, add the classes that will be serialized to JSON.
		JSON.registerConvertor(FBAddPoints.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBAddPoints.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBCreateShop.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBCreateShop.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBCancelTransaction.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBCancelTransaction.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetRedeemToken.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetRedeemToken.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetShopListForDevice.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetShopListForDevice.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetTokenForDevice.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetTokenForDevice.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetShop.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetShop.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBLoginShop.class, new JSONObjectConvertor(false));		
		JSON.registerConvertor(FBLoginShop.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBQueryTransactions.class, new JSONObjectConvertor(false));		
		JSON.registerConvertor(FBQueryTransactions.Response.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBRedeemPoints.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBRedeemPoints.Response.class, new JSONObjectConvertor(false));		
		JSON.registerConvertor(FBUpdateShop.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBUpdateShop.Response.class, new JSONObjectConvertor(false));		
		JSON.registerConvertor(Date.class, new FBDateConvertor());
		JSON.registerConvertor(ShopInfo.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(TransactionInfo.class, new JSONObjectConvertor(false));
	}

	public FBHttpServletBase(FoobarService fbs)
	{
		this.fbs = fbs;
	}
	
	@Override
	public void doGet(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		doPost(httpReq, httpRes);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		String contentType = httpReq.getHeader("Content-Type");
		if (contentType != null)
		{
			if (contentType.toLowerCase().contains("multipart/form-data") ||
				contentType.toLowerCase().contains("multipart/mixed") )
			{
				httpReq = new MultipartRequest(httpReq);
			}
		}

		log.info(String.format("Requested %s", httpReq.toString()));
	
		// Save and respond
		FBCommand cmd = buildCommand(httpReq);
		execAndRespond(cmd, httpRes);
	}
	
	/**
	 * Builds a FBCommand from the given request 
	 * @param req
	 * @return
	 */
	protected abstract FBCommand buildCommand(HttpServletRequest req);
	
	private void execAndRespond(FBCommand cmd, HttpServletResponse httpRes) throws IOException
	{
		// Execute and return the result
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBCommandResponse cmdres = fbs.exec(cmd);
		// Convert the response into JSON string
		String json = JSON.getDefault().toJSON(cmdres);
		
		// Write the result
		httpRes.setStatus(200);
		httpRes.setCharacterEncoding("utf-8");
		httpRes.setContentType("application/json");
		httpRes.setContentLength(json.getBytes("utf-8").length);
		httpRes.getWriter().print(json);
		log.info(String.format("===> Response %s", json));

	}
}
