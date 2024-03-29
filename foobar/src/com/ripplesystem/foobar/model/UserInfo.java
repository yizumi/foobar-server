package com.ripplesystem.foobar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.ripplesystem.foobar.service.FoobarService;

/**
 * A datamodel that represents a user.
 * 
 * @author izumi
 *
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION, detachable="true")
public class UserInfo
{
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long key;
	@Persistent
	private Long tokenId;
	@Persistent
	private String name;
	@Persistent
	private String email;
	@Persistent
	private boolean isEmailVerifiedFlag;
	@Persistent
	private String password;
	@Persistent
	private Date lastLogin;
	@Persistent
	private Date firstLogin;
	@Persistent(mappedBy="userInfo", defaultFetchGroup="true")
	@Element(dependent = "true")
	private List<PositionInfo> positions = new ArrayList<PositionInfo>();
	@Persistent(mappedBy="userInfo", defaultFetchGroup="true")
	@Element(dependent = "true")
	private List<DeviceInfo> devices = new ArrayList<DeviceInfo>();
	
	public Long getKey() { return key; }
	
	public Long getTokenId() { return tokenId; }
	public void setTokenId(Long value) { tokenId = value; }
	
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	public String getEmail() { return email; }
	public void setEmail(String value) { email = value; }
	
	public boolean isEmailVerified() { return isEmailVerifiedFlag; }
	public void setEmailVerified(boolean value) { isEmailVerifiedFlag = value; }
	
	public String getPassword() { return password; }
	public void setPassword(String value) { password = value; }
	
	public Date getLastLogin() { return lastLogin; }
	public void setLastLogin(Date value) { lastLogin = value; }
	
	public Date getFirstLogin() { return firstLogin; }
	public void setFirstLogin(Date value) { firstLogin = value; }
	
	public List<DeviceInfo> getDevices() { return devices; }
	public void setDevices(List<DeviceInfo> value) { devices = value; }
	
	public List<PositionInfo> getPositions() { return positions; }
	public void setPositions(List<PositionInfo> value) { positions = value; }
	
	@Override
	public String toString()
	{
		return String.format("[%s] %s (%s)", 
			tokenId != null ? FoobarService.convIndexToToken(tokenId) : "",
			name,
			email);
	}
}
