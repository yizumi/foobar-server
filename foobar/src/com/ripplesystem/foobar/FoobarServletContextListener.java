package com.ripplesystem.foobar;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * This is what's called upon webapp initialization.
 * Initialize and return the injection module which would contain the injection mapping
 * for resolving servlets.
 */
public class FoobarServletContextListener extends GuiceServletContextListener
{
	@Override
	protected Injector getInjector()
	{
		// Create a injection container for web servlets
		// and register the injector so that it can be picked up by
		// the web.xml
		ServletModule sv = new FoobarServletModule();
		return Guice.createInjector(sv);
	}

}
