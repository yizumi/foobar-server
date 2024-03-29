package com.ripplesystem.foobar.model;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class DeviceInfo
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus",key="gae.encoded-pk",value="true")
	private String encodedKey;
	@Persistent
	private UserInfo userInfo;
	@Persistent
	private String deviceId;
	// This is for apple push notification
	@Persistent
	private String deviceToken;
	
	public String getKey() { return encodedKey; }
	
	public UserInfo getUserInfo() { return userInfo; }
	public void setUserInfo(UserInfo value) { userInfo = value; }
	
	public String getDevieId() { return deviceId; }
	public void setDeviceId(String value) { deviceId = value; }
	
	public String getDeviceToken() { return deviceToken; }
	public void setDeviceToken(String value) { deviceToken = value; }
}
