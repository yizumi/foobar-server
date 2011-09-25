package com.ripplesystem.foobar.service;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF
{
    private static final PersistenceManagerFactory pmfInstance =
    		JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static PersistenceManagerFactory get()
    {
        return pmfInstance;
    }
}