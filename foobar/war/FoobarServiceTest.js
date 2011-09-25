
var FoobarService = Class.create({
	exec : function(cmd) {
		var resp = null;
		Debug.printLine("<div style='background-color:Gray; color:#0000FF;margin:5px;'>out: " + Object.toJSON(cmd) + "</div>");
		new Ajax.Request("foobar/"+cmd.command, {
			asynchronous : false, // make this a synchronous call
			parameters : cmd,
			evalJS : true,
			onSuccess : function(ajaxres) {
				resp = ajaxres.responseJSON;
				Debug.printLine("<div style='background-color:Gray; color:#0000FF;margin:5px;'>in: " + ajaxres.responseText + "</div>");
			},
			onFailure : function(ajaxres) {
				Debug.printLine("<div style='background-color:Gray; color:#660000;margin:5px'>in: " + ajaxres.responseText + "</div>");
			}			
		});
		return resp;
	}
});

function testFBServiceInSequence()
{
	var fbs = new FoobarService();
	
	// Remove the shop if it was not cleaned last time
	cleanShopBeforeCreate(fbs);
	
	// This is the beginning of everything
	var deviceId = "7968d30409c82898a293e4da37d689d5df3de471f55678f558a226374a3dab93";
	var deviceId2 = "7968d30409c82898a293e4da37d689d5df3de471f55678f558a226374a3dab94";
	
	// Let's register the device
	var resToken = testGetTokenForDevice(fbs, deviceId);
	// Let's reigster another device
	var resToken2 = testGetTokenForDevice(fbs, deviceId2);
	// Test if two devices tokens are different from each other
	assertFalse(resToken.token == resToken2.token);
	// Let's register the shop
	var resShop = testCreateShop(fbs);
	// Update the shop
	var resUpdateShop = testUpdateShop(fbs, resShop.shopKey);
	// Get the shop
	var getShopInfoRes = testGetShopInfo(fbs, resShop.shopKey);
	// Let's give points to this guy
	var res2 = testAddPoints(fbs, resToken.token, resShop.shopKey);
	// Let's get list of shops
	var resShopList = testGetShopListForDevice(fbs, deviceId);
	// Let's get redeem token
	var resRedeemToken = testGetRedeemToken(fbs, deviceId, resUpdateShop.shopKey);
	// Let's subtract points from this dude.
	var resRedeemPoints = testRedeemPoints(fbs, resUpdateShop.shopKey, resRedeemToken.redeemToken);
	// Let's delete shop
	var resDeleteShop = testRemoveShop(fbs, getShopInfoRes.shop);
	// Let's get list of shops... this time it would be... empty!
	var resShopList2 = testGetShopListForDevice2(fbs, deviceId);
}

function testGetTokenForDevice(fbs, deviceId)
{
	var cmd = {
			"command" : "GetTokenForDevice",
			"deviceId" : deviceId
	};
		
	var res1 = fbs.exec(cmd);
	assertNotNull(res1.token);
	
	// Run this for the second time to see if the device and token can be brought from persistence.
	var res2 = fbs.exec(cmd);
	assertEquals(res1.token, res2.token);
	return res2;
}

function cleanShopBeforeCreate(fbs)
{
	var cmd = {
		command : "DeleteShop",
		email : "izumi@apcandsons.com",
		password : "12345678"
	};
	
	var res = fbs.exec(cmd);
	try
	{
		assertFalse(res.success);
	}
	catch(e)
	{
		console.info("The undeleted test shop was deleted");
		return res;
	}
	
	cmd.email = "izumi@apcandsons.net";
	cmd.password = "87654321";
	res = fbs.exec(cmd);
	try
	{
		assertFalse(res.success);
	}
	catch(e)
	{
		console.info("The undeleted test shop was deleted");
		return res;
	}
	return res;
}

function testCreateShop(fbs)
{
	var cmd = {
		command : "CreateShop",
		name: "まんじまけろ〜に",
		address: "東京都八王子西八王子１−２−３",
		tel: "03-1234-1234",
		url: "http://www.manjimakeroni.com",
		imageUrl: "http://www.manjimakeroni.com/pic.png",
		email: "izumi@apcandsons.com",
		password: "12345678",
		preferredLang: "en-US"
	};
	
	var res = fbs.exec(cmd);
	assertEquals(true, res.success);
	return res;
}

function testUpdateShop(fbs, shopKey)
{
	var cmd = {
		command : "UpdateShop",
		shopKey : shopKey,
		name : "まんじまけろーに",
		address : "東京都八王子西八王子２−３−４",
		tel : "080-1234-5678",
		url : "http://www.manjimakeroni.net",
		imageUrl : "http://www.manjimakeroni.net/pic.jpg",
		email : "izumi@apcandsons.net",
		password : "87654321",
		preferredLang : "ja-JP"
	};
	
	var res = fbs.exec(cmd);
	return res;
}

function testGetShopInfo(fbs, shopKey)
{
	var cmd = {
			command : "GetShopInfo",
			shopKey : shopKey
	};
	var res = fbs.exec(cmd);
	assertEquals(true, res.success);
	var shop = res.shop;
	assertEquals("まんじまけろーに", shop.name);
	assertEquals("東京都八王子西八王子２−３−４", shop.address);
	assertEquals("080-1234-5678", shop.tel);
	assertEquals("http://www.manjimakeroni.net", shop.url);
	assertEquals("http://www.manjimakeroni.net/pic.jpg", shop.imageUrl);
	assertEquals("izumi@apcandsons.net", shop.email);
	assertEquals("87654321", shop.password);
	assertEquals("ja-JP", shop.preferredLang)
	return res;
}

function testAddPoints(fbs, userToken, shopKey)
{
	var cmd = {
		command : "AddPoints",
		userToken : userToken,
		shopKey : shopKey,
		points : 100
	};
	
	{
		var res = fbs.exec(cmd);	
		assertEquals(true, res.success);
		assertEquals(100, res.currentPoints);
	}

	// Add points one more time
	{
		cmd.points = 50;
		var res = fbs.exec(cmd);
		assertEquals(true, res.success);
		assertEquals(150, res.currentPoints);
		return res;
	}
}

function testGetShopListForDevice(fbs, deviceId)
{
	var cmd = {
			command : "GetShopListForDevice",
			deviceId : deviceId
	};
	
	var res = fbs.exec(cmd);
	assertEquals(1, res.shops.length);
	var shop = res.shops[0];
	assertEquals("まんじまけろーに", shop.name);
	assertEquals(150, shop.points);
	return res;
}

function testGetRedeemToken(fbs, deviceId, shopKey)
{
	var cmd = {
		command : "GetRedeemToken",
		deviceId: deviceId,
		shopKey: shopKey
	};
	
	var res = fbs.exec(cmd);
	assertTrue(res.success);
	assertNotNull(res.redeemToken);
	assertNotNull(res.expiration);
	var exptime = Date.parse(res.expiration);
	assertTrue(exptime - new Date().getTime() >= 2.5 * 1000 * 60 * 60);
	assertTrue(exptime - new Date().getTime() <= 3.5 * 1000 * 60 * 60);
	return res;
}

function testRedeemPoints(fbs, shopKey, redeemToken)
{
	var cmd = {
			command : "RedeemPoints",
			redeemToken: redeemToken,
			shopKey: shopKey,
			points: 200
	};
		
	var res = fbs.exec(cmd);
	assertFalse(res.success);
	assertEquals(150, res.remainingPoints);
	
	cmd.points = 100;
	var res2 = fbs.exec(cmd);
	assertTrue(res2.success);
	assertEquals(50, res2.remainingPoints);
	return res2;
}

function testRemoveShop(fbs, shop)
{
	var cmd = {
			command: "DeleteShop",
			email: shop.email,
			password: shop.password
	};
		
	var res = fbs.exec(cmd);
	assertTrue(res.success);
	return res;
}

function testGetShopListForDevice2(fbs, deviceId)
{
	var cmd = {
			command: "GetShopListForDevice",
			deviceId: deviceId
	};
	
	var res = fbs.exec(cmd);
	assertEquals(0, res.shops.length);
	return res;
}