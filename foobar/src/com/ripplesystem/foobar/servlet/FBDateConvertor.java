package com.ripplesystem.foobar.servlet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class FBDateConvertor implements Convertor
{
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZZZ");
	@SuppressWarnings("rawtypes")
	@Override
	public Object fromJSON(Map map)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toJSON(Object obj, Output buf)
	{
		if (obj instanceof Date)
		{
			buf.add(DATE_FORMATTER.format((Date)obj));
		}
		return;
	}

}
