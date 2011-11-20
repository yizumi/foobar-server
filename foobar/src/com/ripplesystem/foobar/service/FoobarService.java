package com.ripplesystem.foobar.service;

/**
 * [A note on persistence]
 * Persistence is such a bitch.
 * The trick is to not keep any persistence logic in FoobarService.
 * Keep all persistence logic in FoobarDataService.
 */

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import com.google.inject.Inject;
import com.ripplesystem.foobar.command.*;
import com.ripplesystem.foobar.model.*;

public class FoobarService
{
	public static final String GET_SHOP_IMAGE_URL = "/foobar/GetShopImage?shopKey=%d";
	
	private static final Logger log = Logger.getLogger(FoobarService.class.getName());
	private static final int MAX_TOKEN_INDEX = (21*22*23*24*25);
	private static final String URBAN_AIRSHIP_ADDR = "https://go.urbanairship.com/api/push/";
	private static final String URBAN_AIRSHIP_APP_ID = "_IG7YdfMRc2d9xLSscajHg";
	private static final String URBAN_AIRSHIP_MASTER_KEY = "xE38gi4USd6yQE91DczL2Q";
	
	private static final String APN_ADD_POINTS = "APN_ADD_POINTS";
	private static final String APN_TRAN_CANCEL_ADD = "APN_TRAN_CANCEL_ADD";
	private static final String APN_TRAN_CANCEL_REDEEM = "APN_TRAN_CANCEL_REDEEM";

	public static String convIndexToToken(Long index)
	{
		if (index == null)
			throw new NullPointerException("index");
		
		if (index > MAX_TOKEN_INDEX )
			throw new RuntimeException("Cannot create any more tokens!!");
		
		// Okay, get the 
		String chrs = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
		List<Character> master = new ArrayList<Character>();
		for (int i = 0; i < chrs.length(); i++)
			master.add(chrs.charAt(i));
		
		int intValue = index.intValue();

		int i1 = (intValue / (21*22*23*24));
		index = index % (21*22*23*24);
		int i2 = (intValue / (21*22*23));
		index = index % (21*22*23);
		int i3 = (intValue / (21*22));
		index = index % (21*22);
		int i4 = (intValue / 21);
		index = index % 21;
		int i5 = intValue;
		
		// Take one out, from the remaining, choose next
		char c1 = master.remove(i1);
		char c2 = master.remove(i2);
		char c3 = master.remove(i3);
		char c4 = master.remove(i4);
		char c5 = master.remove(i5);
		return new StringBuilder().append(c1).append(c2).append(c3).append(c4).append(c5).toString();		
	}
	
	public static Long convTokenToIndex(String token)
	{
		if (token == null)
			throw new NullPointerException("token");
		
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
		
		return Integer.valueOf(index).longValue();
	}
	
	public static void sendNotificationToUser(UserInfo user, String locKey, Object... args)
	{
		for (DeviceInfo device : user.getDevices())
		{
			if (device.getDeviceToken() == null)
			{
				log.log(Level.INFO, String.format("Not sending APN to DeviceInfo(%s): No device token registered", device.getKey()));
				continue;
			}
			
			if (!sendNotificationToDevice(device.getDeviceToken(), locKey, args))
			{
				log.log(Level.WARNING, String.format("APN Send Failed: %s to %s", locKey, device.getDeviceToken()));
			}
		}
	}

	public static boolean sendNotificationToDevice(String deviceToken, String locKey, Object... args)
	{
		try
		{
			// Prepare contents
			deviceToken = deviceToken.replaceAll(" ", "");
			StringBuilder alert = new StringBuilder();
			alert.append("{\"loc-key\":\"").append(locKey).append("\",\"loc-args\":[");
			for (int i = 0; i < args.length; i++)
			{
				if (i > 0)
					alert.append(",");
				alert.append(String.format("\"%s\"",args[i].toString()));
			}
			alert.append("],\"action-loc-key\":\"APN_OK\"}");
			
			String content = String.format("{\"device_tokens\":[\"%s\"],\"aps\":{\"alert\":%s,\"sound\":\"default\"}}", 
					deviceToken, alert);
			int length = content.getBytes("UTF-8").length;
			
			// Prepare connection
			URL url = new URL(URBAN_AIRSHIP_ADDR);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			
			// Prepare authorization string
			String authString = URBAN_AIRSHIP_APP_ID + ":" + URBAN_AIRSHIP_MASTER_KEY;
			String authStringBase64 = Base64.encodeBase64String(authString.getBytes());
			authStringBase64 = authStringBase64.trim();
			
			// Set headers
			con.setRequestProperty("Content-Type","application/json; charset=utf-8");
			con.setRequestProperty("Authorization", "Basic " + authStringBase64);
			con.setRequestProperty("Content-Length", Integer.toString(length));
			
			// Set contents
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "utf-8");
			writer.write(content);
			
			// Close
			writer.close();
			
			// Check response
			int responseCode = con.getResponseCode();
			if (responseCode == 200)
				return true;
			log.log(Level.WARNING, String.format("Push Notification Not Sent: Received ResponseCode(%d)", responseCode));
			return false;
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Push Notification Not Sent", e);
			return false;
		}
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
					return execGetShopInfo((FBGetShop)cmd);
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
				case QUERY_TRANSACTIONS:
					return execQueryTransactions((FBQueryTransactions)cmd);
				case CANCEL_TRANSACTION:
					return execCancelTransaction((FBCancelTransaction)cmd);
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
			userInfo.setTokenId(sis.issueNextUserTokenId());
			userInfo.setFirstLogin(new Date());
			userInfo.setLastLogin(new Date());
			userInfo.setName(String.format("User %s", userInfo.getTokenId())); // TokenId becomes the temporary name.
			
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
		res.setToken(convIndexToToken(userInfo.getTokenId()));
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
		shop.setImage(cmd.getImage());
		shop.setName(cmd.getName());
		shop.setPassword(cmd.getPassword());
		shop.setPreferredLang(cmd.getPreferredLang());
		shop.setTel(cmd.getTel());
		shop.setUrl(cmd.getUrl());
		
		sis.save(shop);
		
		FBCreateShop.Response res = cmd.new Response(true);
		shop.setImageUrl(String.format(GET_SHOP_IMAGE_URL, shop.getKey()));
		res.setShopKey(shop.getKey());
		res.setShop(shop);
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
		shop.setImage(cmd.getImage());
		shop.setName(cmd.getName());
		shop.setPassword(cmd.getPassword());
		shop.setPreferredLang(cmd.getPreferredLang());
		shop.setTel(cmd.getTel());
		shop.setUrl(cmd.getUrl());
		sis.save(shop);
		
		FBUpdateShop.Response res = cmd.new Response(true);
		res.setShopKey(shop.getKey());
		shop.setImageUrl(String.format(GET_SHOP_IMAGE_URL, shop.getKey()));
		res.setShop(shop);
		return res;
	}

	/**
	 * Gets the ShopInfo by shopKey.
	 * @param cmd
	 * @return
	 */
	private FBGetShop.Response execGetShopInfo(FBGetShop cmd)
	{
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if (shop == null)
		{
			FBGetShop.Response res = cmd.new Response(false);
			res.setFailCode(FBGetShop.Response.FAILCODE_SHOP_NOT_FOUND);
			return res;
		}
		
		FBGetShop.Response res = cmd.new Response(true);
		shop.setImageUrl(String.format(GET_SHOP_IMAGE_URL, shop.getKey()));
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
		UserInfo user = sis.getUserInfoByTokenId(convTokenToIndex(cmd.getUserToken()));
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
			if (pos.getShopKey() != null && pos.getShopKey().equals(cmd.getShopKey()))
			{
				posInfo = pos;
				break;
			}
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
		
		FoobarService.sendNotificationToUser(user, APN_ADD_POINTS, shop.getName(), cmd.getPoints());
				
		// Add transaction history
		TransactionInfo tx = new TransactionInfo();
		tx.setAddOrRedeem(TransactionType.Add);
		tx.setPoints(cmd.getPoints());
		tx.setShopKey(shop.getKey());
		// tx.setShopMessage(null);
		tx.setShopName(shop.getName());
		tx.setTime(new Date());
		tx.setUserKey(user.getKey());
		tx.setUserName(user.getName());
		sis.save(tx);
		
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
				if (pos.getShopKey() != null && pos.getShopKey().equals(shopInfo.getKey()))
				{
					shopInfo.setPoints(pos.getBalance());
				}
			}
			shopInfo.setImageUrl(String.format(GET_SHOP_IMAGE_URL, shopInfo.getKey()));
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
			if (pos.getShopKey() != null && pos.getShopKey().equals(cmd.getShopKey()))
			{
				posInfo = pos;
				break;
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
		if (!sis.assignRedeemToken(posInfo))
		{
			FBGetRedeemToken.Response res = cmd.new Response(false);
			res.setFailCode(FBGetRedeemToken.Response.FAILCODE_TOKEN_ASSIGN_FAILED);
			return res;
		}
		
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
		// Find shop
		ShopInfo shop = sis.getShopInfoByKey(cmd.getShopKey());
		if (shop == null)
		{
			FBRedeemPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBRedeemPoints.Response.FAILCODE_SHOP_NOT_FOUND);
			return res;
		}
		log.info(String.format("ShopKey: %d",shop.getKey()));

		// Find Position info from redeem token 
		Long tokenIndex = convTokenToIndex(cmd.getRedeemToken());
		PositionInfo position = sis.getPositionInfoByShopKeyAndTokenIndex(cmd.getShopKey(), tokenIndex);
		if (position == null)
		{
			FBRedeemPoints.Response res = cmd.new Response(false);
			res.setFailCode(FBRedeemPoints.Response.FAILCODE_TOKEN_NOT_FOUND);
			return res;
		}
		
		// Find user from position
		Long userKey = position.getUserInfo().getKey();
		UserInfo user = sis.getUserInfoByKey(userKey);
		
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
		
		for (DeviceInfo device : user.getDevices())
		{
			if (device.getDeviceToken() != null)
			{
				// Requires internationalization here!
				String message = String.format("%sで%dPt利用しました", shop.getName(), position.getBalance());
				FoobarService.sendNotificationToDevice(device.getDeviceToken(), message);
			}
		}
		TransactionInfo tx = new TransactionInfo();
		tx.setAddOrRedeem(TransactionType.Redeem);
		tx.setPoints(cmd.getPoints());
		tx.setShopKey(shop.getKey());
		// tx.setShopMessage(null);
		tx.setShopName(shop.getName());
		tx.setTime(new Date());
		tx.setUserKey(user.getKey());
		tx.setUserName(user.getName());
		sis.save(tx);
		
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
		shop.setImageUrl(String.format(GET_SHOP_IMAGE_URL, shop.getKey()));
		res.setShop(shop);
		return res;
	}

	/**
	 * This runs the command to fetch transaction items from the foobar data service.
	 * @param cmd
	 * @return
	 */
	private FBQueryTransactions.Response execQueryTransactions(FBQueryTransactions cmd)
	{
		FBQueryTransactions.Response res = cmd.new Response(true);
		res.setCount(cmd.getCount());
		res.setPage(cmd.getPage());
		
		if (cmd.getShopKey() == null && cmd.getUserToken() == null)
		{
			res.setSuccess(false);
			res.setFailCode(FBQueryTransactions.Response.FAILCODE_MISSING_PARAMETERS);
			return res;		
		}
		
		// Check ShopKey or UserToken...
		UserInfo user = sis.getUserInfoByTokenId(convTokenToIndex(cmd.getUserToken()));
		if (user == null)
		{
			res.setSuccess(false);
			res.setFailCode(FBQueryTransactions.Response.FAILCODE_NO_SUCH_USER);
			return res;
		}
		
		// Get the shop keys
		long shopKey = cmd.getShopKey() != null ? cmd.getShopKey() : 0;
		List<TransactionInfo> txs = sis.getTransactionInfosByShopKeyOrUserKey(shopKey, user.getKey(), cmd.getCount(), cmd.getPage());
		
		// If there are more items than you asked for,
		if (txs.size() > cmd.getCount())
		{
			// Recreate a list
			List<TransactionInfo> modified = new ArrayList<TransactionInfo>();
			for (int i = 0; i < cmd.getCount(); i++)
			{
				modified.add(txs.get(i));
			}
			res.setTransactions(modified);
			// Indicate that there are more items available
			res.setHasMore(true);
		}
		else
		{
			res.setTransactions(txs);
		}
		
		return res;
	}

	/**
	 * This method cancels the transaction as cancelled.
	 * Erros out if the transaction cannot be found, or shop no longer exists.
	 */
	private FBCommandResponse execCancelTransaction(FBCancelTransaction cmd)
	{
		TransactionInfo tran = sis.getTransactionInfoByKey(cmd.getTransactionKey());
		if (tran == null)
		{
			FBCancelTransaction.Response res = cmd.new Response(false);
			res.setFailCode(FBCancelTransaction.Response.FAILCODE_INVALID_KEY);
			return res; 
		}
		
		if (tran.isCancelled())
		{
			FBCancelTransaction.Response res = cmd.new Response(false);
			res.setFailCode(FBCancelTransaction.Response.FAILCODE_ALREADY_CANCELLED);
			return res;
		}
		
		// get transaction object
		UserInfo user = sis.getUserInfoByKey(tran.getUserKey());
		if (user == null)
		{
			FBCancelTransaction.Response res = cmd.new Response(false);
			res.setFailCode(FBCancelTransaction.Response.FAILCODE_USER_NOT_FOUND);
			return res;
		}
		
		// Get the position
		PositionInfo position = null;
		for (PositionInfo pos : user.getPositions())
		{
			if (pos.getShopKey() != null && pos.getShopKey().equals(tran.getShopKey()))
			{
				position = pos;
				break;
			}
		}
		
		if (position == null)
		{
			FBCancelTransaction.Response res = cmd.new Response(false);
			res.setFailCode(FBCancelTransaction.Response.FAILCODE_POSITION_NOT_FOUND);
			return res;
		}
		
		// Mark this transaction as cancelled
		log.log(Level.INFO, String.format("TransactionInfo(%d) is going to be cancelled.", tran.getKey()));
		position.addOrRedeemPoints(((tran.getAddOrRedeem() == TransactionType.Add ? -1 : +1) * tran.getPoints()));
		tran.setCancelled(true);
		sis.save(user);
		sis.save(tran);
		log.log(Level.INFO, String.format("TransactionInfo(%d) has been cancelled.", tran.getKey()));
		
		// Do APN Push
		String messageType = (tran.getAddOrRedeem() == TransactionType.Add) ? APN_TRAN_CANCEL_ADD : APN_TRAN_CANCEL_REDEEM;
		FoobarService.sendNotificationToUser(user, messageType, tran.getShopName(), tran.getPoints());
		
		// Create the response
		FBCancelTransaction.Response res = cmd.new Response(true);
		res.setTransactionKey(tran.getKey());
		res.setShopKey(tran.getShopKey());
		res.setUserKey(tran.getUserKey());
		res.setBalance(position.getBalance());
		res.setUserToken(convIndexToToken(user.getTokenId()));
		
		return res;
	}

}
