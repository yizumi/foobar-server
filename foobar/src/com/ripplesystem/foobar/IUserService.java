package com.ripplesystem.foobar;

import com.google.appengine.api.users.User;

/**
 * Interface for user service.
 * Turned into an interface to maintain testability.
 * @author izumi
 *
 */
public interface IUserService {
	/**
	 * Gets the identity of the user currently logged into the system.
	 */
	User getCurrentUser();
	
	/**
	 * Gets the login URL for the requested URI.
	 */
	String createLoginURL(String requestUri);
}
