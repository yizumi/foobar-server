package com.ripplesystem.foobar.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ripplesystem.foobar.service.FoobarService;

public class FoobarServiceAPNTest
{
	@Test
	public void testAPNOnline()
	{
		String deviceToken = "7968D30409C82898A293E4DA37D689D5DF3DE471F55678F558A226374A3DAB93";
		boolean success = FoobarService.sendNotificationToDevice(deviceToken, "APN_ADD_POINTS", "フーバーカフェ", "100");
		assertTrue(success);
	}
}
