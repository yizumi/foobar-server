package com.ripplesystem.foobar.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;

public final class MultipartRequest implements HttpServletRequest
{
	private static final Logger log = Logger.getLogger(MultipartRequest.class.getName());
	private HttpServletRequest httpReq;
	private Map<String,String> params = new HashMap<String,String>();
	private Map<String,Blob> files = new HashMap<String,Blob>();
	
	public MultipartRequest(HttpServletRequest req) throws IOException
	{
		this.httpReq = req;
		initialize();
	}
	
	private void initialize() throws IOException
	{
		try
		{
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(httpReq);
			while (iter.hasNext())
			{
				FileItemStream item = iter.next();
				InputStream stream = item.openStream();
				String fieldName = item.getFieldName();
				
				if (item.isFormField())
				{
					
					byte[] buffer = new byte[1024];
					int length = stream.read(buffer);
					String fieldValue = new String(buffer, 0, length, "utf-8");
					log.info(String.format("Field: %s, Value: %s.", fieldName, fieldValue));
					params.put(fieldName, fieldValue);
				}
				else
				{
					Blob imageBlob = new Blob(IOUtils.toByteArray(stream));
					files.put(fieldName, imageBlob);
				}
			}
		}
		catch (FileUploadException e)
		{
			log.log(Level.SEVERE, "FileUploadException occured", e);
		}
	}
	
	/**
	 * Returns a Blob representing an uploaded file for the given fieldname.
	 */
	public Blob getFile(String field)
	{
		if (files.containsKey(field))
			return files.get(field);
		return null;
	}
	
	@Override
	public Object getAttribute(String arg0)
	{
		return httpReq.getAttribute(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames()
	{
		return httpReq.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding()
	{
		return httpReq.getCharacterEncoding();
	}

	@Override
	public int getContentLength()
	{
		return httpReq.getContentLength();
	}

	@Override
	public String getContentType()
	{
		return httpReq.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		return httpReq.getInputStream();
	}

	@Override
	public String getLocalAddr()
	{
		return httpReq.getLocalAddr();
	}

	@Override
	public String getLocalName()
	{
		return httpReq.getLocalName();
	}

	@Override
	public int getLocalPort()
	{
		return httpReq.getLocalPort();
	}

	@Override
	public Locale getLocale()
	{
		return httpReq.getLocale();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getLocales()
	{
		return httpReq.getLocales();
	}

	@Override
	public String getParameter(String field)
	{
		if (params.containsKey(field))
			return params.get(field);
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap()
	{
		return params;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getParameterNames()
	{
		final Iterator<String> iterator = params.keySet().iterator();
		return new Enumeration() {

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Object nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public String[] getParameterValues(String field)
	{
		if (params.containsKey(field))
			return new String[] { params.get(field) };
		return null;
	}

	@Override
	public String getProtocol()
	{
		return httpReq.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		return httpReq.getReader();
	}

	@Override
	public String getRealPath(String arg0)
	{
		return httpReq.getRealPath(arg0);
	}

	@Override
	public String getRemoteAddr()
	{
		return httpReq.getRemoteAddr();
	}

	@Override
	public String getRemoteHost()
	{
		return httpReq.getRemoteHost();
	}

	@Override
	public int getRemotePort()
	{
		return httpReq.getRemotePort();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		return httpReq.getRequestDispatcher(arg0);
	}

	@Override
	public String getScheme()
	{
		return httpReq.getScheme();
	}

	@Override
	public String getServerName()
	{
		return httpReq.getServerName();
	}

	@Override
	public int getServerPort()
	{
		return httpReq.getServerPort();
	}

	@Override
	public boolean isSecure()
	{
		return httpReq.isSecure();
	}

	@Override
	public void removeAttribute(String arg0)
	{
		httpReq.removeAttribute(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
	{
		httpReq.setAttribute(arg0, arg1);
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException
	{
		httpReq.setCharacterEncoding(arg0);
	}

	@Override
	public String getAuthType()
	{
		return httpReq.getAuthType();
	}

	@Override
	public String getContextPath()
	{
		return httpReq.getContextPath();
	}

	@Override
	public Cookie[] getCookies()
	{
		return httpReq.getCookies();
	}

	@Override
	public long getDateHeader(String arg0)
	{
		return httpReq.getDateHeader(arg0);
	}

	@Override
	public String getHeader(String arg0)
	{
		return httpReq.getHeader(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaderNames()
	{
		return httpReq.getHeaderNames();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaders(String arg0)
	{
		return httpReq.getHeaders(arg0);
	}

	@Override
	public int getIntHeader(String arg0)
	{
		return httpReq.getIntHeader(arg0);
	}

	@Override
	public String getMethod()
	{
		return httpReq.getMethod();
	}

	@Override
	public String getPathInfo()
	{
		return httpReq.getPathInfo();
	}

	@Override
	public String getPathTranslated()
	{
		return httpReq.getPathTranslated();
	}

	@Override
	public String getQueryString()
	{
		return httpReq.getQueryString();
	}

	@Override
	public String getRemoteUser()
	{
		return httpReq.getRemoteUser();
	}

	@Override
	public String getRequestURI()
	{
		return httpReq.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL()
	{
		return httpReq.getRequestURL();
	}

	@Override
	public String getRequestedSessionId()
	{
		return httpReq.getRequestedSessionId();
	}

	@Override
	public String getServletPath()
	{
		return httpReq.getServletPath();
	}

	@Override
	public HttpSession getSession()
	{
		return httpReq.getSession();
	}

	@Override
	public HttpSession getSession(boolean arg0)
	{
		return httpReq.getSession(arg0);
	}

	@Override
	public Principal getUserPrincipal()
	{
		return httpReq.getUserPrincipal();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie()
	{
		return httpReq.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL()
	{
		return httpReq.isRequestedSessionIdFromURL();
	}

	@Override
	public boolean isRequestedSessionIdFromUrl()
	{
		return httpReq.isRequestedSessionIdFromURL();
	}

	@Override
	public boolean isRequestedSessionIdValid()
	{
		return httpReq.isRequestedSessionIdValid();
	}

	@Override
	public boolean isUserInRole(String arg0)
	{
		return httpReq.isUserInRole(arg0);
	}
}
