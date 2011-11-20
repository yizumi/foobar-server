package com.ripplesystem.foobar.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleTest
{
	@Before
	public void setUp()
	{
		// helper.setUp();
		// injector = Guice.createInjector(new TestModule());
	}
	
	@After
	public void tearDown()
	{
		// helper.tearDown();
	}

	@Test
	public void testLong()
	{
		Long l1 = new Long(7);
		Long l2 = new Long(7);
		assertFalse(l1 == l2);
		assertTrue(l1.equals(l2));
	}
}
