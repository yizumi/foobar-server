package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCancelTransaction;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class CancelTransactionServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;

	@Inject
	public CancelTransactionServlet(FoobarService fbs)
	{
		super(fbs);
	}

	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		FBCancelTransaction cmd = new FBCancelTransaction();
		cmd.setTransactionKey(Long.parseLong(req.getParameter("transactionKey")));
		return cmd;
	}
}
