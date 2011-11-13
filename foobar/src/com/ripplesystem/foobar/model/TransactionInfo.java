package com.ripplesystem.foobar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class TransactionInfo
{
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long key;
	@Persistent
	private Long shopKey;
	@Persistent
	private Long userKey;
	@Persistent
	private List<String> searchKeys;
	@Persistent
	private Date time;
	@Persistent
	private TransactionType addOrRedeem;
	@Persistent
	private long points;
	@Persistent
	private String shopMessage;
	@Persistent
	private String shopName;
	@Persistent
	private String userName;
	
	public Long getKey() { return key; }
	
	public Long getShopKey() { return shopKey; }
	public void setShopKey(Long value) { shopKey = value; refreshSearchKey(); }
	
	public Long getUserKey() { return userKey; }
	public void setUserKey(Long value) { userKey = value; refreshSearchKey(); }
	
	public List<String> getSearchKeys() { return searchKeys; }
	public void setSearchKeys(List<String> value) { searchKeys = value; }
	
	public Date getTime() { return time; }
	public void setTime(Date value) { time = value; }
	
	public TransactionType getAddOrRedeem() { return addOrRedeem; }
	public void setAddOrRedeem(TransactionType value) { addOrRedeem = value; }
	
	public long getPoints() { return points; }
	public void setPoints(Long value) { points = value; }
	
	public String getShopMessage() { return shopMessage; }
	public void setShopMessage(String value) { shopMessage = value; }
	
	public String getShopName() { return shopName; }
	public void setShopName(String value) { shopName = value; }
	
	public String getUserName() { return userName; }
	public void setUserName(String value) { userName = value; }
	
	/**
	 * Internally generates the searchKey from shopKey and userKey.
	 */
	private void refreshSearchKey()
	{
		searchKeys = new ArrayList<String>();
		if (shopKey != null)
			searchKeys.add(String.format("shopKey:%d", shopKey));
		if (userKey != null)
			searchKeys.add(String.format("userKey:%d", userKey));
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %sed %d points to %s at %s", 
				shopName, 
				addOrRedeem.toString(), 
				points, 
				userName, 
				time != null ? time.toString() : "");
	}
}
