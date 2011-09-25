package com.ripplesystem.foobar.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;

/**
 * Object representation of a Shop.
 * 
 * 2011-09-19 1st draft
 * 2011-09-21 added preferredLang to choose the language to use for email communication
 * 
 *  @author yizumi
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class ShopInfo
{
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long key;
	@Persistent
	private String name;
	@Persistent
	private String address;
	@Persistent
	private String tel;
	@Persistent
	private String url;
	@Persistent
	private String imageUrl;
	@Persistent
	private String email;
	@Persistent
	private String preferredLang;
	@Persistent
	private String password;
	@Persistent
	private boolean isEmailVerified;
	@Persistent
	private int redeemTokenIndex;
	// Temporary place holder for user's points.
	private long points;
	
	public Long getKey() { return key; }
	
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	public String getAddress() { return address; }
	public void setAddress(String value) { address = value; }
	
	public String getTel() { return tel; }
	public void setTel(String value) { tel = value; }
	
	public String getUrl() { return url; }
	public void setUrl(String value) { url = value; }
	
	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String value) { imageUrl = value; }
	
	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public String getPreferredLang() { return preferredLang; }
	public void setPreferredLang(String value) { preferredLang = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public boolean isEmailVerified() { return isEmailVerified; }
	public void setEmailVerified(boolean value) { isEmailVerified = value; }

	public int nextRedeemTokenIndex() { return ++redeemTokenIndex; }
	
	public long getPoints() { return points; }
	public void setPoints(long value) { points = value; }
}
