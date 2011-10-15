package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBRedeemPoints;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class RedeemPointsServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public RedeemPointsServlet(FoobarService fbs)
	{
		super(fbs);
	}

	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Build the command
		FBRedeemPoints cmd = new FBRedeemPoints();
		cmd.setPoints(Long.parseLong(req.getParameter("points")));
		cmd.setRedeemToken(req.getParameter("redeemToken"));
		cmd.setShopKey(Long.parseLong(req.getParameter("shopKey")));
		return cmd;
	}
}
