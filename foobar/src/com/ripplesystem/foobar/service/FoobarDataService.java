package com.ripplesystem.foobar.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.inject.Inject;
import com.ripplesystem.foobar.model.DeviceInfo;
import com.ripplesystem.foobar.model.PositionInfo;
import com.ripplesystem.foobar.model.ShopInfo;
import com.ripplesystem.foobar.model.UserInfo;

public class FoobarDataService
{
	private static final Logger log = Logger.getLogger(FoobarDataService.class.getName());
	private PersistenceManager pm;

	@Inject
	public FoobarDataService(PersistenceManager pm)
	{
		this.pm = pm;
		pm.setDetachAllOnCommit(true);
		pm.getFetchPlan().setGroup(FetchGroup.ALL);
	}
 
	private void begin()
	{
		pm.currentTransaction().begin();
	}
	
	private void commit()
	{
		pm.currentTransaction().commit();
	}
	
	public void save(ShopInfo shop)
	{
		begin();
		try
		{
			log.info(String.format("Saving ShopInfo %s", shop.getName()));
			pm.makePersistent(shop);
		}
		finally
		{
			commit();
		}
	}
	
	public void save(UserInfo user)
	{
		begin();
		try
		{
			log.info( String.format("Saving UserInfo %s", user.getName()));
			pm.makePersistent(user);
		}
		finally
		{
			commit();
		}
	}

	/**
	 * 
	 */
	public ShopInfo getShopInfoByKey(Long key)
	{
		begin();
		try
		{
			ShopInfo shop = pm.getObjectById(ShopInfo.class,key);
			return pm.detachCopy(shop);
		}
		finally
		{
			commit();
		}		
	}
	
	/**
	 * finds a ShopInfo registered with the given email address.
	 * @param email
	 * @return
	 */
	public ShopInfo getShopInfoByEmail(String email) {
		javax.jdo.Query query = pm.newQuery(ShopInfo.class);
		query.setFilter("email == emailParam");
		query.declareParameters("String emailParam");
		
		try
		{
			List<ShopInfo> shops = (List<ShopInfo>)query.execute(email);
			if (shops.size() == 0)
				return null;
			ShopInfo shop = pm.detachCopy(shops.get(0));
			return shop;
		}
		finally
		{
			query.closeAll();
		}
	}

	/**
	 * Returns the UserInfo object by key
	 * @param key
	 * @return
	 */
	public UserInfo getUserInfoByKey(long key) {
		begin();
		try
		{
			UserInfo userInfo = pm.getObjectById(UserInfo.class, key);
			return pm.detachCopy(userInfo);
		}
		finally
		{
			commit();
		}
	}
		
	/**
	 * Gets a UserInfo for the given deviceId.
	 * If no UserInfo is registered for the given deviceId, it returns null.
	 */
	public UserInfo getUserInfoByDeviceId(String deviceId) {
		javax.jdo.Query query = pm.newQuery(DeviceInfo.class);
		query.setFilter("deviceId == deviceIdParam");
		query.declareParameters("String deviceIdParam");
		
		try
		{
			List<DeviceInfo> devices = (List<DeviceInfo>)query.execute(deviceId);
			if (devices.size() == 0)
			{
				return null;
			}
			UserInfo userInfo = devices.get(0).getUserInfo();
			return pm.detachCopy(userInfo);
		}
		finally
		{
			query.closeAll();
		}
	}

	/**
	 * Gets a UserInfo for the given deviceId.
	 * If no UserInfo is registered for the given deviceId, it returns null.
	 */
	public UserInfo getUserInfoByDeviceIdOld(String deviceId) {
		javax.jdo.Query query = pm.newQuery(UserInfo.class);
		query.setFilter("this.devices.contains(device) && device.deviceId == deviceIdParam");
		query.declareParameters("String deviceIdParam");
		
		try
		{
			List<UserInfo> users = (List<UserInfo>)query.execute(deviceId);
			if (users.size() == 0)
			{
				return null;
			}
			UserInfo userInfo = users.get(0);
			return pm.detachCopy(userInfo);
		}
		finally
		{
			query.closeAll();
		}
	}

	/**
	 * Returns multiple instances of ShopInfos for the given list of keys
	 * @param keys
	 * @return null if the keys list is null
	 */
	public List<ShopInfo> getShopInfosByKeys(List<Long> keys) {
		if (keys == null)
			return null;
		
		Query query = pm.newQuery(ShopInfo.class, ":p.contains(key)");
		try
		{
			List<ShopInfo> list = (List<ShopInfo>)query.execute(keys);
			return (List<ShopInfo>) pm.detachCopyAll(list);
		}
		finally
		{
			query.closeAll();
		}
	}

	/**
	 * Assigns a position
	 * @param posInfo
	 */
	public void assignRedeemToken(PositionInfo posInfo)
	{
		// Get the shop info...
		begin();
		try
		{
			ShopInfo shop = pm.getObjectById(ShopInfo.class, posInfo.getShopKey());
			Date expiration = new Date(new Date().getTime() + (3 * 60 * 60 * 1000));
			posInfo.setRedeemTokenIndex(shop.nextRedeemTokenIndex());
			posInfo.setRedeemTokenExpiration(expiration);
		}
		finally
		{
			commit();
		}
	}

	public PositionInfo getPositionInfoByShopKeyAndTokenIndex(long shopKey, int tokenIndex)
	{		
		Query query = pm.newQuery(PositionInfo.class);
		query.setFilter("shopKey == shopKeyParam && redeemTokenIndex == tokenIndexParam");
		query.declareParameters("long shopKeyParam, int tokenIndexParam");
		
		try
		{
			List<PositionInfo> positions = (List<PositionInfo>)query.execute(shopKey, tokenIndex);
			if (positions.size() == 0)
			{
				return null;
			}
			PositionInfo position = positions.get(0);
			return pm.detachCopy(position);
		}
		finally
		{
			query.closeAll();
		}
	}

	public void addOrRedeemPoints(PositionInfo position, long points) {
		begin();
		try
		{
			position.addOrRedeemPoints(points);
			pm.makePersistent(position);
		}
		finally
		{
			commit();
		}
	}

	public void deleteShopByKey(Long key)
	{
		begin();
		try
		{
			ShopInfo shop = pm.getObjectById(ShopInfo.class, key);
			pm.deletePersistent(shop);
		}
		finally
		{
			commit();
		}
		
		
	}
}