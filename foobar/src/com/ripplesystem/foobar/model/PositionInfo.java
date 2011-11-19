package com.ripplesystem.foobar.model;

import java.util.Date;

import javax.jdo.annotations.*;

/**
 * Represents the data model for point balance that user has with the shop.
 * @author yizumi
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class PositionInfo
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus",key="gae.encoded-pk",value="true")
	private String encodedKey;
	@Persistent
	private UserInfo userInfo;
	@Persistent
	private Long shopKey;
	@Persistent
	private Long redeemTokenIndex;
	@Persistent
	private long balance = 0;
	@Persistent
	private Date redeemTokenExpiration;
	
	public String getEncodedKey() { return encodedKey; }
	
	public UserInfo getUserInfo() { return userInfo; }
	public void setUserInfo(UserInfo value) { userInfo = value; }
	
	public Long getShopKey() { return shopKey; }
	public void setShopKey(Long value) { shopKey = value; }
	
	public Long getRedeemTokenIndex() { return redeemTokenIndex; }
	public void setRedeemTokenIndex(Long value) { redeemTokenIndex = value; }
	
	public long getBalance() { return balance; }
	public void setBalance(long value) { balance = value; }
	public long addOrRedeemPoints(long value) { return (balance += value); }

	public Date getRedeemTokenExpiration() { return redeemTokenExpiration; }
	public void setRedeemTokenExpiration(Date value) { redeemTokenExpiration = value; }
}
