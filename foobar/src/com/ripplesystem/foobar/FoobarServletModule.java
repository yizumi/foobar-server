package com.ripplesystem.foobar;

import javax.jdo.PersistenceManager;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.ripplesystem.foobar.impl.GaeInteropUserService;
import com.ripplesystem.foobar.impl.StdoutLogger;
import com.ripplesystem.foobar.service.FoobarDataService;
import com.ripplesystem.foobar.service.FoobarService;
import com.ripplesystem.foobar.service.PMF;
import com.ripplesystem.foobar.servlet.*;

/**
 * This class contains the registration of classes
 * and url-to-servlet mapping.
 * @author izumi
 */
public class FoobarServletModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		// Configure interface to concrete class mapping
		bind(ILogger.class).to(StdoutLogger.class).in(Scopes.SINGLETON);
		bind(IUserService.class).to(GaeInteropUserService.class).in(Scopes.SINGLETON);
		bind(PersistenceManager.class).toInstance(PMF.get().getPersistenceManager());
		bind(FoobarDataService.class).in(Scopes.SINGLETON);
		bind(FoobarService.class).in(Scopes.SINGLETON);
				
		// Configure url-to-servlet mapping 
		serve("/Test").with(TestServlet.class);
		serve("/foobar/GetTokenForDevice").with(GetTokenForDeviceServlet.class);
		serve("/foobar/CreateShop").with(CreateShopServlet.class);
        serve("/foobar/UpdateShop").with(UpdateShopServlet.class);
		serve("/foobar/GetShop").with(GetShopServlet.class);
		serve("/foobar/AddPoints").with(AddPointsServlet.class);
		serve("/foobar/GetShopListForDevice").with(GetShopListForDeviceServlet.class);
		serve("/foobar/GetRedeemToken").with(GetRedeemTokenServlet.class);
		serve("/foobar/RedeemPoints").with(RedeemPointsServlet.class);
		serve("/foobar/DeleteShop").with(DeleteShopServlet.class);
		serve("/foobar/LoginShop").with(LoginShopServlet.class);
		serve("/foobar/GetShopImage").with(GetShopImageServlet.class);
		serve("/foobar/QueryTransactions").with(QueryTransactionsServlet.class);
		serve("/foobar/CancelTransaction").with(CancelTransactionServlet.class);
	}
}
