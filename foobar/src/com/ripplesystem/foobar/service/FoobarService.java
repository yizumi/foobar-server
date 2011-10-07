package com.ripplesystem.foobar.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.ripplesystem.foobar.command.*;
import com.ripplesystem.foobar.model.*;

public class FoobarService
{
	private static final Logger log = Logger.getLogger(FoobarService.class.getName());
	private static final int MAX_TOKEN_INDEX = (21*22*23*24*25);

	private static String convIndexToToken(int index)
	{
		if (index > MAX_TOKEN_INDEX )
			throw new RuntimeException("Cannot create any more tokens!!");
		
		// Okay, get the 
		String chrs = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
		List<Character> master = new ArrayList<Character>();
		for (int i = 0; i < chrs.length(); i++)
			master.add(chrs.charAt(i));

		int i1 = (index / (21*22*23*24));
		index = index % (21*22*23*24);
		int i2 = (index / (21*22*23));
		index = index % (21*22*23);
		int i3 = (index / (21*22));
		index = index % (21*22);
		int i4 = (index / 21);
		index = index % 21;
		int i5 = index;
		
		// Take one out, from the remaining, choose next
		char c1 = master.remove(i1);
		char c2 = master.remove(i2);
		char c3 = master.remove(i3);
		char c4 = master.remove(i4);
		char c5 = master.remove(i5);
		return new StringBuilder().append(c1).append(c2).append(c3).append(c4).append(c5).toString();		
	}
	
	private static int convTokenToIndex(String token)
	{
		String chrs = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
		List<Character> master = new ArrayList<Character>();
		for (int i = 0; i < chrs.length(); i++)
			master.add(chrs.charAt(i));

		int i1 = master.indexOf(token.charAt(0));
		master.remove(i1);
		int i2 = master.indexOf(token.charAt(1));
		master.remove(i2);
		int i3 = master.indexOf(token.charAt(2));
		master.remove(i3);
		int i4 = master.indexOf(token.charAt(3));
		master.remove(i4);
		int i5 = master.indexOf(token.charAt(4));
		
		int index = (
				(21 * 22 * 23 * 24 * i1) +
				(21 * 22 * 23 * i2) +
				(21 * 22 * i3) +
				(21 * i4) +
				(i5));
		
		return index;
	}

	private FoobarDataService sis;

	@Inject
	public FoobarService(FoobarDataService sis)
	{
		this.sis = sis;
	}
	
	/**
	 * Executes the given command returns a response.
	 * @param cmd
	 * @return
	 */
	public FBCommandResponse exec(FBCommand cmd)
	{
		try
		{
			switch (cmd.getCommandType())
			{
				case GET_TOKEN_FOR_DEVICE:
					return execGetTokenForDevice((FBGetTokenForDevice)cmd);
				case CREATE_SHOP:
					return execCreateShop((FBCreateShop)cmd);
				case UPDATE_SHOP:
					return execUpdateShop((FBUpdateShop)cmd);
				case GET_SHOP_INFO:
					return execGetShopInfo((FBGetShopInfo)cmd);
				case ADD_POINTS:
					return execAddPoints((FBAddPoints)cmd);
				case GET_SHOP_LIST_FOR_DEVICE:
					return execGetShopListForDevice((FBGetShopListForDevice)cmd);
				case GET_REDEEM_TOKEN:
					return execGetRedeemToken((FBGetRedeemToken)cmd);
				case REDEEM_POINTS:
					return execRedeemPoints((FBRedeemPoints)cmd);
				case DELETE_SHOP:
					return execDeleteShop((FBDeleteShop)cmd);
				case DELETE_USER:
					break;
				case GET_USER_LIST:
					break;
				case GET_USER_DETAIL:
					break;
				case LOGIN_SHOP:
					return execLoginShop((FBLoginShop)cmd);
				case LOGIN_USER:
					break;
			}
			return null;
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, String.format("Error while handling %s", cmd.getCommandType().toString()), e);
			return null;
		}
	}
	
	/**
	 * This method responds back with a token for the deviceId being sent.
	 * If the device Id is already registered, then it reuses the token that's been given.
	 * If the device Id is sent for the first time, it creates the User entity, registers the device,
	 * and response back with a new token.
	 * @param command
	 * @return
	 */
	private FBGetTokenForDevice.Response execGetTokenForDevice(FBGetTokenForDevice cmd) {
		// See if there is any ... device already registered.
		UserInfo userInfo = sis.getUserInfoByDeviceId(cmd.getDeviceId());
		if (userInfo == null)
		{
			userInfo = new UserInfo();
			userInfo.setFirstLogin(new Date());
			userInfo.setLastLogin(new Date());
			userInfo.setName(cmd.getDeviceId()); // DeviceId becomes the user's temporary name
			
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceId(cmd.getDeviceId());
			deviceInfo.setDeviceToken(cmd.getDeviceToken());
			
			// List<DeviceInfo> devices = new ArrayList<DeviceInfo>();
			// devices.add(deviceInfo);
			userInfo.getDevices().add(deviceInfo);
			
			sis.save(userInfo);
		}
		else
		{
			// Update deviceToken if different.
			// Don't overwrite the existing value if there is one already
			if (cmd.getDeviceToken() != null)
			{
				for (DeviceInfo d : userInfo.getDevices())
				{
					if (d.getDevieId().equals(cmd.getDeviceId()))
					{
						if (!cmd.getDeviceToken().equals(d.getDeviceToken()))
						{
							d.setDeviceToken(cmd.getDeviceToken());
							sis.save(userInfo);
						}
					}
				}
			}
		}
		
		FBGetTokenForDevice.Response res = cmd.new Response(true);
		res.setToken(convIndexToToken(userInfo.getKey().intValue()));
		// If not, create a new user based on device Id it receives.
		return res;
	}
	
	/**
	 * This method creates a new ShopInfo object, saves the information in persistence,
	 * and returns the shopKey in response.
	 * 
	 * By invoking this method, service will also send an email message with a verification URL. 
	 *
	 * @param cmd
	 * @return
	 */
	private FBCreateShop.Response execCreateShop(FBCreateShop cmd)
	{
		// Check that there is no shop registered by that email address.
		ShopInfo existingShop = sis.getShopInfoByEmail(cmd.getEmail());
		if (existingShop != null)
		{
			FBCreateShop.Response res = cmd.new Response(false);
			res.setFailCode(FBCreateShop.Response.FAILCODE_EMAIL_EXISTS_ALREADY);
			res.setShopKey(existingShop.getKey());
			return res;
		}
		
		// Check that the request has all the required information
		if (cmd.getAddress() == null ||
				cmd.getEmail() == null ||
				cmd.getName() == null ||
				cmd.getTel() == null ||
				cmd.getPassword() == null ||
				cmd.getPreferredLang() == null)
		{
			FBCreateShop.Response res = cmd.new Response(false);
			res.setFailCode(FBCreateShop.Response.FAILCODE_MISSING_REQUIRED_FIELD);
			return res;
		}
		
		// Create ShopInfo using the variables in the command
		ShopInfo shop = new ShopInfo();
		shop.setAddress(cmd.getAddress());
		shop.setEmail(cmd.getEmail());
		shop.setEmailVerified(false);
		shop.setImageUrl(cmd.getImageUrl());
		shop.setName(cmd.getName());
		shop.setPassword(cmd.getPassword());
		shop.setPreferredLang(cmd.getPreferredLang());
		shop.setTel(cmd.getTel());
		shop.setUrl(cmd.getUrl());
		
		sis.save(shop);
		
		FBCreateShop.Response res = cmd.new Response(true);
		res.setShopKey(shop.getKey());
		return res;
	}
	
	/**
	 * This method finds the shop, updates the information and returns the shopKey as a response.
	 * @param cmd
	 * @return
	 */
	private FBUpdateShop.Response execUpdateShop(FBUpdateShop cmd) {
		// Find the shop
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if (shop == null)
		{
			FBUpdateShop.Response res = cmd.new Response(false);
			res.setFailCode(FBUpdateShop.Response.FAILCODE_SHOP_NOT_EXIST);
			return res;
		}
		
		// Change the shop property
		shop.setAddress(cmd.getAddress());
		shop.setEmail(cmd.getEmail());
		shop.setEmailVerified(false);
		shop.setImageUrl(cmd.getImageUrl());
		shop.setName(cmd.getName());
		shop.setPassword(cmd.getPassword());
		shop.setPreferredLang(cmd.getPreferredLang());
		shop.setTel(cmd.getTel());
		shop.setUrl(cmd.getUrl());
		sis.save(shop);
		
		FBUpdateShop.Response res = cmd.new Response(true);
		res.setShopKey(shop.getKey());
		return res;
	}

	/**
	 * Gets the ShopInfo by shopKey.
	 * @param cmd
	 * @return
	 */
	private FBGetShopInfo.Response execGetShopInfo(FBGetShopInfo cmd)
	{
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if (shop == null)
		{
			FBGetShopInfo.Response res = cmd.new Response(false);
			res.setFailCode(FBGetShopInfo.Response.FAILCODE_SHOP_NOT_FOUND);
			return res;
		}
		
		FBGetShopInfo.Response res = cmd.new Response(true);
		res.setShop(shop);
		return res;
	}
	
	/**
	 * This method finds a user by token, sets the shop,
	 * @param command
	 * @return
	 */
	private FBAddPoints.Response execAddPoints(FBAddPoints cmd)
	{
		// Find shop
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if (shop == null)
		{
			FBAddPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBAddPoints.Response.FAILCODE_SHOP_NOT_FOUND);
			return res;
		}
		log.info(String.format("ShopKey: %d",shop.getKey()));
		

		// Find user
		int userKey = convTokenToIndex(cmd.getUserToken());
		UserInfo user = sis.getUserInfoByKey(userKey);
		if (user == null)
		{
			FBAddPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBAddPoints.Response.FAILCODE_USER_NOT_FOUND);
			return res;
		}
		log.info(String.format("Userkey: %d", user.getKey()));
		
		
		// Get or Create a new point
		PositionInfo posInfo = null;
		for (PositionInfo pos : user.getPositions())
		{
			if (pos.getShopKey() == cmd.getShopKey())
				posInfo = pos;
		}
		if (posInfo == null)
		{
			posInfo = new PositionInfo();
			posInfo.setUserInfo(user);
			posInfo.setShopKey(cmd.getShopKey());
			posInfo.setBalance(0);
			user.getPositions().add(posInfo);
			sis.save(user);
		}
		// Set points by adding to the current points
		sis.addOrRedeemPoints(posInfo, cmd.getPoints());
		FBAddPoints.Response res = cmd.new Response(true);
		res.setCurrentPoints(posInfo.getBalance());
		return res;
	}
	
	/**
	 * Gets UserInfo from the deviceId, gets the PositionInfo, finds the list of Shops.
	 * @param cmd
	 * @return
	 */
	private FBGetShopListForDevice.Response execGetShopListForDevice(FBGetShopListForDevice cmd) 
	{
		// Get UserInfo by deviceId
		UserInfo userInfo = sis.getUserInfoByDeviceId(cmd.getDeviceId());
		if (userInfo == null)
		{
			FBGetShopListForDevice.Response res = cmd.new Response(false);
			res.setFailCode(FBGetShopListForDevice.Response.FAILCODE_DEVICE_NOT_FOUND);
			return res;
		}
		log.info(String.format("UserKey: %d",userInfo.getKey()));
				
		// Get shop info using keys in shop infi
		if (userInfo.getPositions().size() == 0 )
		{
			FBGetShopListForDevice.Response res = cmd.new Response(true);
			return res;
		}
		
		// Build the list of ShopInfo keys
		List<Long> keys = new ArrayList<Long>();
		for (PositionInfo posInfo : userInfo.getPositions())
		{
			keys.add(posInfo.getShopKey());
			log.info(String.format( "Shop: %d, Points: %d, RedeemToken: %d, Balance: %d",
					posInfo.getShopKey(), posInfo.getBalance(), posInfo.getRedeemTokenIndex(), posInfo.getBalance()));
		}
		
		// Get ShopInfo list using keys.
		List<ShopInfo> shops = sis.getShopInfosByKeys(keys);
		
		// Build the ShopInfo-to-Points hashmap
		for (ShopInfo shopInfo : shops)
		{
			for (PositionInfo pos : userInfo.getPositions())
			{
				long posShopKey = pos.getShopKey().longValue();
				long shopKey = shopInfo.getKey().longValue();
				if (posShopKey == shopKey)
				{
					shopInfo.setPoints(pos.getBalance());
				}
			}
		}
		
		// Build and return the response
		FBGetShopListForDevice.Response res = cmd.new Response(true);
		res.setShops(shops);
		return res;
	}

	/**
	 * Gets UserInfo by deviceId, checks that shop exists, checks that user has points, and
	 * returns the redemption token and expiration for the token.
	 * @param cmd
	 * @return
	 */
	private FBGetRedeemToken.Response execGetRedeemToken(FBGetRedeemToken cmd)
	{
		// Does user really exist?
		UserInfo user = sis.getUserInfoByDeviceId(cmd.getDeviceId());
		if (user == null)
		{
			FBGetRedeemToken.Response res = cmd.new Response(false);
			res.setFailCode(FBGetRedeemToken.Response.FAILCODE_NO_DEVICE_FOUND);
			return res;
		}
		
		// Does he have position with the shop?
		PositionInfo posInfo = null;
		for (PositionInfo pos : user.getPositions())
		{
			if (pos.getShopKey() == cmd.getShopKey())
			{
				posInfo = pos;
			}
		}
		if (posInfo == null || !(posInfo.getBalance() > 0))
		{
			FBGetRedeemToken.Response res = cmd.new Response(false);
			res.setFailCode(FBGetRedeemToken.Response.FAILCODE_NO_POINTS_TO_REDEEM);
			return res;
		}
		
		// Does the shop really exist?
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if( shop == null)
		{
			FBGetRedeemToken.Response res = cmd.new Response(false);
			res.setFailCode(FBGetRedeemToken.Response.FAILCODE_NO_SHOP_FOUND);
			return res;
		}
		
		// Assign the token and expiration on the position info.
		sis.assignRedeemToken(posInfo);
		FBGetRedeemToken.Response res = cmd.new Response(true);
		res.setExpiration(posInfo.getRedeemTokenExpiration());
		res.setRedeemToken(convIndexToToken(posInfo.getRedeemTokenIndex()));
		sis.save(user);
		return res;
	}

	/**
	 * This guy gets the PositionInfo by redeem token and shopKey, then subtracts the points,
	 * and ... calls it done! 
	 * @param cmd
	 * @return
	 */
	private FBRedeemPoints.Response execRedeemPoints(FBRedeemPoints cmd)
	{
		// 
		int tokenIndex = convTokenToIndex(cmd.getRedeemToken());
		PositionInfo position = sis.getPositionInfoByShopKeyAndTokenIndex(cmd.getShopKey(), tokenIndex);
		if (position == null)
		{
			FBRedeemPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBRedeemPoints.Response.FAILCODE_TOKEN_NOT_FOUND);
			return res;
		}
		
		if (position.getRedeemTokenExpiration().getTime() <= new Date().getTime())
		{
			FBRedeemPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBRedeemPoints.Response.FAILCODE_TOKEN_EXPIRED);
			res.setRemainingPoints(position.getBalance());
			return res;
		}
		
		if (position.getBalance() < cmd.getPoints())
		{
			FBRedeemPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBRedeemPoints.Response.FAILCODE_INSUFFICIENT_POINTS);
			res.setRemainingPoints(position.getBalance());
			return res;
		}
		
		sis.addOrRedeemPoints(position, cmd.getPoints() * -1);
		FBRedeemPoints.Response res = cmd.new Response(true);
		res.setRemainingPoints(position.getBalance());
		return res;
	}

	/**
	 * This guy deletes the shop.
	 * @param cmd
	 * @return
	 */
	private FBDeleteShop.Response execDeleteShop(FBDeleteShop cmd)
	{
		ShopInfo shop = sis.getShopInfoByEmail(cmd.getEmail());
		if (shop == null)
		{
			FBDeleteShop.Response res = cmd.new Response(false);
			res.setFailCode(FBDeleteShop.Response.FAILCODE_STORE_NOT_FOUND);
			return res;
		}

		if (!shop.getPassword().equals(cmd.getPassword()))
		{
			FBDeleteShop.Response res = cmd.new Response(false);
			res.setFailCode(FBDeleteShop.Response.FAILCODE_PASSWORD_NOT_MATCH);
			return res;
		}
		
		sis.deleteShopByKey((long)shop.getKey());
		FBDeleteShop.Response res = cmd.new Response(true);
		return res;
	}

	private FBLoginShop.Response execLoginShop(FBLoginShop cmd)
	{
		ShopInfo shop = sis.getShopInfoByEmail(cmd.getEmail());
		if (shop == null)
		{
			FBLoginShop.Response res = cmd.new Response(false);
			res.setFailCode(FBLoginShop.Response.FAILCODE_SHOP_NOT_FOUND);
			return res;
		}
		
		if (!shop.getPassword().equals(cmd.getPassword()))
		{
			FBLoginShop.Response res = cmd.new Response(false);
			res.setFailCode(FBLoginShop.Response.FAILCODE_PASSWORD_MISMATCH);
			return res;
		}
		
		FBLoginShop.Response res = cmd.new Response(true);
		res.setShopKey(shop.getKey());
		return res;
	}

}
