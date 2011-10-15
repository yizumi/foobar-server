package com.ripplesystem.foobar.command;

/**
 * Enum of different kinds of commands supported by FooBar.
 * @author yizumi
 */
public enum FBCommandType
{
	// Called when FooBar starts for the first time. Provides a token.
	GET_TOKEN_FOR_DEVICE,
	// Called when user wants to register a new shop. Provides a shopKey.
	CREATE_SHOP,
	// Called when user wants to modify shop information.  Keyed by shopKey.
	UPDATE_SHOP,
	// Called when user wants to get shopInformation.
	GET_SHOP_INFO,
	// Called when user wants to add points.
	ADD_POINTS,
	// Called when user Refreshes the shop list.
	GET_SHOP_LIST_FOR_DEVICE,
	// Called when user wants Redemption token for using points
	GET_REDEEM_TOKEN,
	// Called when shop wants to redeem points
	REDEEM_POINTS,
	// Called when shop needs the list of 
	GET_USER_LIST,
	// Called when shop wants to find out about specific user
	GET_USER_DETAIL,
	// Called when shop wants to remove itself
	DELETE_SHOP,
	// Called when user wants to delete himself
	DELETE_USER,
	// Called when user wants to login as an existing shop. Provides a shopKey.
	LOGIN_SHOP,
	// Called when user wants to login.
	LOGIN_USER,	
	// Called when shop or user wants to get transaction info
	QUERY_TRANSACTION_INFOS
}
