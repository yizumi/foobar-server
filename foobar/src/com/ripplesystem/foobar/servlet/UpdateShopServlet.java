package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBUpdateShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class UpdateShopServlet extends FBHttpServletBase
{
	private static final Logger log = Logger.getLogger(UpdateShopServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Inject
	public UpdateShopServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doPost(HttpServletRequest rawReq, HttpServletResponse httpRes) throws IOException
	{
		MultipartRequestWrapper httpReq = new MultipartRequestWrapper(rawReq);
		
		// Logging
		log.info(String.format("Requested %s", this.getClass().getName()));
		
		// Create command using request parameters
		FBUpdateShop cmd = new FBUpdateShop();
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		cmd.setAddress(httpReq.getParameter("address"));
		cmd.setEmail(httpReq.getParameter("email"));
		cmd.setName(httpReq.getParameter("name"));
		cmd.setPassword(httpReq.getParameter("password"));
		cmd.setPreferredLang(httpReq.getParameter("preferredLang"));
		cmd.setTel(httpReq.getParameter("tel"));
		cmd.setUrl(httpReq.getParameter("url"));
		cmd.setImage(httpReq.getFile("image"));
		
		execAndRespond(cmd, httpRes);				
	}
}
