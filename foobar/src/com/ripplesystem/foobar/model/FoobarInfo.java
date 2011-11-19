package com.ripplesystem.foobar.model;

import javax.jdo.annotations.*;

/**
 * Manages application wide information. 
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class FoobarInfo
{
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long key;
	@Persistent
	private long lastUserId;
	
	/**
	 * Gets the next laserUserId.  
	 * The application should persist this object immediately after calling this method.
	 * @return
	 */
	public long nextLastUserId() { return ++lastUserId; }
}
