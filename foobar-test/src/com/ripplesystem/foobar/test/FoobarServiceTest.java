package com.ripplesystem.foobar.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ripplesystem.foobar.command.*;
import com.ripplesystem.foobar.model.ShopInfo;
import com.ripplesystem.foobar.service.FoobarService;

public class FoobarServiceTest
{
	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp()
	{
		helper.setUp();
	}
	
	@After
	public void tearDown()
	{
		helper.tearDown();
	}
	
	@Test
	public void testFBServiceInSequence()
	{
		Injector injector = Guice.createInjector(new TestModule());
		FoobarService fbs = injector.getInstance(FoobarService.class);
		
		// This is the beginning of everything
		String deviceId = "7968d30409c82898a293e4da37d689d5df3de471f55678f558a226374a3dab93";
		String deviceId2 = "7968d30409c82898a293e4da37d689d5df3de471f55678f558a226374a3dab94";
		
		// Let's register the device
		FBGetTokenForDevice.Response resToken = testGetTokenForDevice(fbs, deviceId);
		// Let's reigster another device
		FBGetTokenForDevice.Response resToken2 = testGetTokenForDevice(fbs, deviceId2);
		// Test if two devices tokens are different from each other
		assertFalse(resToken.getToken().equals(resToken2.getToken()));
		// Let's register the shop
		FBCreateShop.Response resShop = testCreateShop(fbs);
		// Update the shop
		FBUpdateShop.Response resUpdateShop = testUpdateShop(fbs, resShop.getShopKey());
		// Get the shop
		FBGetShopInfo.Response getShopInfoRes = testGetShopInfo(fbs, resShop.getShopKey());
		// Login as the shop
		FBLoginShop.Response resShopLogin = testLoginShop(fbs, getShopInfoRes.getShop());
		// Let's give points to this guy
		FBAddPoints.Response res2 = testAddPoints(fbs, resToken.getToken(), resShop.getShopKey());
		// Let's get list of shops
		FBGetShopListForDevice.Response resShopList = testGetShopListForDevice(fbs, deviceId);
		// Let's get redeem token
		FBGetRedeemToken.Response resRedeemToken = testGetRedeemToken(fbs, deviceId, resUpdateShop.getShopKey());
		// Let's subtract points from this dude.
		FBRedeemPoints.Response resRedeemPoints = testRedeemPoints(fbs, resUpdateShop.getShopKey(), resRedeemToken.getRedeemToken());
		// Let's remove the shop
		FBDeleteShop.Response resDeleteShop = testRemoveShop(fbs, getShopInfoRes.getShop());
		// Let's get list of shops... this time it would be... empty!
		FBGetShopListForDevice.Response resShopList2 = testGetShopListForDevice2(fbs, deviceId);
	}
	
	/**
	 * Hello world
	 */
	private FBGetTokenForDevice.Response testGetTokenForDevice(FoobarService fbs, String deviceId)
	{
		FBGetTokenForDevice cmd = new FBGetTokenForDevice();
		cmd.setDeviceId(deviceId);
		cmd.setDeviceToken(null);
		
		FBGetTokenForDevice.Response res1 = (FBGetTokenForDevice.Response)fbs.exec(cmd);
		assertNotNull(res1.getToken());
		
		// Run this for the second time to see if the device and token can be brought from persistence.
		cmd.setDeviceToken("41268108");
		FBGetTokenForDevice.Response res2 = (FBGetTokenForDevice.Response)fbs.exec(cmd);
		assertEquals(res1.getToken(), res2.getToken());
		return res2;
	}
	
	private FBCreateShop.Response testCreateShop(FoobarService fbs)
	{
		FBCreateShop cmd = new FBCreateShop();
		cmd.setName("まんじまけろ〜に");
		cmd.setAddress("東京都八王子西八王子１−２−３");
		cmd.setTel("03-1234-1234");
		cmd.setUrl("http://www.manjimakeroni.com");
		cmd.setImageUrl("http://www.manjimakeroni.com/pic.png");
		cmd.setEmail("izumi@apcandsons.com");
		cmd.setPassword("12345678");
		cmd.setPreferredLang("ja-JP");
		
		FBCreateShop.Response res = (FBCreateShop.Response)fbs.exec(cmd);
		assertEquals(true, res.isSuccess());
		return res;
	}
	
	private FBUpdateShop.Response testUpdateShop(FoobarService fbs, long shopKey)
	{
		FBUpdateShop cmd = new FBUpdateShop();
		cmd.setShopKey(shopKey);
		cmd.setName("まんじまけろーに");
		cmd.setAddress("東京都八王子西八王子２−３−４");
		cmd.setTel("080-1234-5678");
		cmd.setUrl("http://www.manjimakeroni.net");
		cmd.setImageUrl("http://www.manjimakeroni.net/pic.jpg");
		cmd.setEmail("izumi@apcandsons.net");
		cmd.setPassword("87654321");
		cmd.setPreferredLang("en-US");
		
		FBUpdateShop.Response res = (FBUpdateShop.Response)fbs.exec(cmd);
		return res;
	}
	
	private FBGetShopInfo.Response testGetShopInfo(FoobarService fbs, long shopKey)
	{
		FBGetShopInfo cmd = new FBGetShopInfo();
		cmd.setShopKey(shopKey);
		FBGetShopInfo.Response res = (FBGetShopInfo.Response)fbs.exec(cmd);
		ShopInfo shop = res.getShop();
		assertEquals("まんじまけろーに", shop.getName());
		assertEquals("東京都八王子西八王子２−３−４", shop.getAddress());
		assertEquals("080-1234-5678", shop.getTel());
		assertEquals("http://www.manjimakeroni.net", shop.getUrl());
		assertEquals("http://www.manjimakeroni.net/pic.jpg", shop.getImageUrl());
		assertEquals("izumi@apcandsons.net", shop.getEmail());
		assertEquals("87654321", shop.getPassword());
		assertEquals("en-US", shop.getPreferredLang());
		return res;
	}
	
	private FBLoginShop.Response testLoginShop(FoobarService fbs, ShopInfo shop)
	{
		// TODO Auto-generated method stub
		FBLoginShop cmd = new FBLoginShop();
		
		// Emulate wrong email address entry
		{
			cmd.setEmail(shop.getEmail() + "x");
			FBLoginShop.Response res = (FBLoginShop.Response)fbs.exec(cmd);
			assertFalse(res.isSuccess());
			assertEquals(FBLoginShop.Response.FAILCODE_SHOP_NOT_FOUND, res.getFailCode());
		}
		
		// Set the correct email this time
		cmd.setEmail(shop.getEmail());
		
		// Emulate a mistake by entering a wrong password
		{
			cmd.setPassword(shop.getPassword() + "x");
			FBLoginShop.Response res = (FBLoginShop.Response)fbs.exec(cmd);
			assertFalse(res.isSuccess());
			assertEquals(FBLoginShop.Response.FAILCODE_PASSWORD_MISMATCH, res.getFailCode());
		}
		
		// Get everything right this time.
		{
			cmd.setPassword(shop.getPassword());
			FBLoginShop.Response res = (FBLoginShop.Response)fbs.exec(cmd);
			assertTrue(res.isSuccess());
			assertEquals(shop.getKey().longValue(), res.getShopKey());
			return res;
		}
	}

	private FBAddPoints.Response testAddPoints(FoobarService fbs, String userToken, long shopKey)
	{
		FBAddPoints cmd = new FBAddPoints();
		cmd.setUserToken(userToken);
		cmd.setShopKey(shopKey);
		cmd.setPoints(100);
		
		{
			FBAddPoints.Response res = (FBAddPoints.Response)fbs.exec(cmd);	
			assertEquals(true, res.isSuccess());
			assertEquals(100, res.getCurrentPoints());
		}

		// Add points
		{
			cmd.setPoints(50);
			cmd.setUserToken("ZYXWV"); // Some random token
			FBAddPoints.Response res = (FBAddPoints.Response)fbs.exec(cmd);
			assertFalse(res.isSuccess());
			assertEquals(FBAddPoints.Response.FAILCODE_USER_NOT_FOUND, res.getFailCode());
		}

		// Add points one more time
		{
			cmd.setUserToken(userToken);
			cmd.setPoints(50);
			FBAddPoints.Response res = (FBAddPoints.Response)fbs.exec(cmd);
			assertEquals(true, res.isSuccess());
			assertEquals(150, res.getCurrentPoints());
			return res;
		}		
	}
	
	private FBGetShopListForDevice.Response testGetShopListForDevice(FoobarService fbs, String deviceId)
	{
		FBGetShopListForDevice cmd = new FBGetShopListForDevice();
		cmd.setDeviceId(deviceId);
		
		FBGetShopListForDevice.Response res = (FBGetShopListForDevice.Response)fbs.exec(cmd);
		assertEquals(1, res.getShops().size());
		ShopInfo shop = (ShopInfo)res.getShops().get(0);
		assertEquals("まんじまけろーに", shop.getName());
		assertEquals((long)150, (long)res.getShops().get(0).getPoints());
		return res;
	}

	private FBGetRedeemToken.Response testGetRedeemToken(FoobarService fbs, String deviceId, long shopKey)
	{
		FBGetRedeemToken cmd = new FBGetRedeemToken();
		cmd.setDeviceId(deviceId);
		cmd.setShopKey(shopKey);
		
		FBGetRedeemToken.Response res = (FBGetRedeemToken.Response)fbs.exec(cmd);
		assertTrue(res.isSuccess());
		assertNotNull(res.getRedeemToken());
		assertNotNull(res.getExpiration());
		assertTrue(res.getExpiration().getTime() - new Date().getTime() >= 2.5 * 1000 * 60 * 60);
		assertTrue(res.getExpiration().getTime() - new Date().getTime() <= 3.5 * 1000 * 60 * 60);
		return res;
	}
	
	private FBRedeemPoints.Response testRedeemPoints(FoobarService fbs,	long shopKey, String redeemToken)
	{
		FBRedeemPoints cmd = new FBRedeemPoints();
		cmd.setRedeemToken(redeemToken);
		cmd.setShopKey(shopKey);
		cmd.setPoints(200); // Make this fail!
		
		FBRedeemPoints.Response res = (FBRedeemPoints.Response)fbs.exec(cmd);
		assertFalse(res.isSuccess());
		assertEquals(150, res.getRemainingPoints());
		
		cmd.setPoints(100);
		FBRedeemPoints.Response res2 = (FBRedeemPoints.Response)fbs.exec(cmd);
		assertTrue(res2.isSuccess());
		assertEquals(50, res2.getRemainingPoints());
		return res2;
	}
	
	private FBDeleteShop.Response testRemoveShop(FoobarService fbs, ShopInfo shop)
	{
		FBDeleteShop cmd = new FBDeleteShop();
		cmd.setEmail(shop.getEmail());
		cmd.setPassword(shop.getPassword());
		
		FBDeleteShop.Response res = (FBDeleteShop.Response)fbs.exec(cmd);
		assertTrue(res.isSuccess());
		return res;
	}

	private FBGetShopListForDevice.Response testGetShopListForDevice2(FoobarService fbs, String deviceId)
	{
		FBGetShopListForDevice cmd = new FBGetShopListForDevice();
		cmd.setDeviceId(deviceId);
		
		FBGetShopListForDevice.Response res = (FBGetShopListForDevice.Response)fbs.exec(cmd);
		assertEquals(0, res.getShops().size());
		return res;
	}

}