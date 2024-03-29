package com.ripplesystem.foobar.impl;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ripplesystem.foobar.IUserService;

/**
 * This is an implementation if IUserService based on Google App Engine's user service.
 * We made this into an interface based call to ensure local testability.
 * @author izumi
 *
 */
public class GaeInteropUserService implements IUserService
{
	// Just a base
	private UserService _base;

	/**
	 * Default constructor
	 */
	public GaeInteropUserService()
	{
		_base = UserServiceFactory.getUserService();
	}
	
	/**
	 * Gets the current user
	 */
	@Override
	public User getCurrentUser() {
		// 
		return _base.getCurrentUser();
	}

	/**
	 * Gets the login URL.
	 */
	@Override
	public String createLoginURL(String requestUri) {
		// 
		return _base.createLoginURL(requestUri);
	}
	
}