package com.konloch.bythonplusplus;

import com.konloch.dynvarmap.DynVarMap;
import com.konloch.dynvarmap.serializer.DynVarSerializer;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class BPPConfig
{
	private final DynVarMap map = new DynVarMap();
	private final DynVarSerializer serializer = new DynVarSerializer("config.ini", map);
	
	public void load()
	{
		serializer.load();
	}
	
	public String getNewLine()
	{
		return map.getString("newline", "\r\n");
	}
	
	public String getPython()
	{
		return map.getString("python", "python");
	}
}
