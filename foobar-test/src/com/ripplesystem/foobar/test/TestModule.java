package com.ripplesystem.foobar.test;

import javax.jdo.PersistenceManager;

import com.google.inject.AbstractModule;
import com.ripplesystem.foobar.service.PMF;

public class TestModule extends AbstractModule {
	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(PersistenceManager.class).toInstance(PMF.get().getPersistenceManager());
		
	}
}
