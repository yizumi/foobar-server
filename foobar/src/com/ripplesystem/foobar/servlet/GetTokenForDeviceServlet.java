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
import com.ripplesystem.foobar.command.FBGetTokenForDevice;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetTokenForDeviceServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(GetTokenForDeviceServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	private FoobarService fbs;

	@Inject
	public GetTokenForDeviceServlet(FoobarService fbs)
	{
		this.fbs = fbs;
		JSON.registerConvertor(FBGetTokenForDevice.class, new JSONObjectConvertor(false));
		JSON.registerConvertor(FBGetTokenForDevice.Response.class, new JSONObjectConvertor(false));		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		log.info("Requested GetTokenForDevice");
		FBGetTokenForDevice cmd = new FBGetTokenForDevice();
		cmd.setDeviceId(req.getParameter("deviceId"));
		log.info(String.format("===> Command: %s", JSON.getDefault().toJSON(cmd)));
		FBGetTokenForDevice.Response response = (FBGetTokenForDevice.Response)fbs.exec(cmd);
		res.setStatus(200);
		res.setCharacterEncoding("utf-8");
		res.setContentType("application/json");
		String json = JSON.getDefault().toJSON(response);
		res.getWriter().print(json);
		log.info(String.format("===> Response %s", json));
	}
}