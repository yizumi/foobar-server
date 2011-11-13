package com.ripplesystem.foobar.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ripplesystem.foobar.command.FBCommand;
import com.ripplesystem.foobar.command.FBGetShop;
import com.ripplesystem.foobar.service.FoobarService;

@Singleton
public class GetShopImageServlet extends FBHttpServletBase
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GetShopImageServlet.class.getName());
	
	@Inject
	public GetShopImageServlet(FoobarService fbs)
	{
		super(fbs);
	}
	
	@Override
	public void doGet(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		doPost(httpReq, httpRes);
	}
	
	@Override
	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException
	{
		FBGetShop cmd = new FBGetShop();
		cmd.setShopKey(Long.parseLong(httpReq.getParameter("shopKey")));
		String filter = httpReq.getParameter("filter");
		
		FBGetShop.Response res = (FBGetShop.Response)fbs.exec(cmd);
		if (res == null || !res.isSuccess())
		{
			log.info("Redirecing to empty image");
			httpRes.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			httpRes.setHeader("Location", "/image/NoImage.png");
			return;
		}
		Blob imageBlob = res.getShop().getImage();
		httpRes.setContentType("image/jpeg");
		
		if (filter != null)
		{
			if ("100x100".equals(filter))
			{
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				Image oldImage = ImagesServiceFactory.makeImage(imageBlob.getBytes());
				Transform resize = ImagesServiceFactory.makeResize(100,100);
				Image newImage = imagesService.applyTransform(resize, oldImage);
				httpRes.getOutputStream().write(newImage.getImageData());
				return;
			}
		}

		httpRes.getOutputStream().write(imageBlob.getBytes());
	}

	@Override
	protected FBCommand buildCommand(HttpServletRequest req)
	{
		// Should not be called.
		return null;
	}
}
