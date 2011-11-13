package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBQueryTransactions;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class QueryTransactionsServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public QueryTransactionsServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		FBQueryTransactions cmd = new FBQueryTransactions();
		cmd.setCount(Integer.parseInt(req.getParameter("count")));
		cmd.setPage(Integer.parseInt(req.getParameter("page")));
		cmd.setShopKey(Long.parseLong(req.getParameter("shopKey")));
		cmd.setUserToken(req.getParameter("userToken"));
		return cmd;
	}
}
