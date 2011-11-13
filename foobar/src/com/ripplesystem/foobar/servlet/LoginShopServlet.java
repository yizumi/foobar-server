package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBLoginShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class LoginShopServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;

	@Inject
	public LoginShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Translate Http Request to a command
		FBLoginShop cmd = new FBLoginShop();
		cmd.setEmail(req.getParameter("email"));
		cmd.setPassword(req.getParameter("password"));
		return cmd;
	}
}
