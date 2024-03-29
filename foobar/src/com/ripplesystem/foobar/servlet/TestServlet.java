package com.ripplesystem.foobar.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.ILogger;
import com.ripplesystem.foobar.IUserService;

@Singleton
public class TestServlet extends HttpServlet
{
	private ILogger _logger;
	private IUserService _userSvc;

	@Inject
	public TestServlet(
			ILogger logger,
			IUserService userSvc)
	{
		_logger = logger;
		_userSvc = userSvc;
	}

	private static final long serialVersionUID = 1L;

	public void doGet( HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		res.setContentType("text/plain");
		res.getWriter().write("Hello, world!");
		_logger.Info("Got request from %s", req.getRemoteAddr());
		if (_userSvc.getCurrentUser() == null)
		{
			res.getWriter().println("Hi, you're not logged in, but that's all good.");
		}
	}
}
