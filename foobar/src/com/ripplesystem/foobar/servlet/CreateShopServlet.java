package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCreateShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class CreateShopServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public CreateShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	protected FBCreateShop buildCommand(HttpServletRequest req)
	{
	    // Create command using request parameters
		FBCreateShop cmd = new FBCreateShop();
		cmd.setAddress(req.getParameter("address"));
		cmd.setEmail(req.getParameter("email"));
		cmd.setName(req.getParameter("name"));
		cmd.setPassword(req.getParameter("password"));
		cmd.setPreferredLang(req.getParameter("preferredLang"));
		cmd.setTel(req.getParameter("tel"));
		cmd.setUrl(req.getParameter("url"));
		if (req instanceof MultipartRequest)
			cmd.setImage(((MultipartRequest)req).getFile("image"));
		
		return cmd;
	}
}
