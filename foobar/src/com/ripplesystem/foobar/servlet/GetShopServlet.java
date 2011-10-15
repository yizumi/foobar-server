package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBGetShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetShopServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;

	@Inject
	public GetShopServlet(FoobarService fbs)
	{
		super(fbs);
	}

	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Create command using request parameters
		FBGetShop cmd = new FBGetShop();
		cmd.setShopKey(Long.parseLong(req.getParameter("shopKey")));
		return cmd;
	}
}
