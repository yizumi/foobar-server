package com.ripplesystem.foobar.servlet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBUpdateShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class UpdateShopServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public UpdateShopServlet(FoobarService fbs)
	{
		super(fbs);
	}

	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Create command using request parameters
		FBUpdateShop cmd = new FBUpdateShop();
		cmd.setShopKey(Long.parseLong(req.getParameter("shopKey")));
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
