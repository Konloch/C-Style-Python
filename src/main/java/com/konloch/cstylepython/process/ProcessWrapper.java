package com.konloch.cstylepython.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konloch
 * @since 7/3/2024
 */
public class ProcessWrapper
{
	public final String cStylePythonCode;
	public final List<String> out = new ArrayList<>();
	public final List<String> err = new ArrayList<>();
	
	public ProcessWrapper(String cStylePythonCode)
	{
		this.cStylePythonCode = cStylePythonCode;
	}
	
	public void copyFromInputStream(List<String> list, InputStream is)
	{
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
		{
			String line;
			while ((line = reader.readLine()) != null)
				list.add(line);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printDebug()
	{
		//output sys out
		for(String out : out)
			System.out.println(out);
		
		//output sys err
		for(String err : err)
			System.err.println(err);
	}
	
	public void printScript()
	{
		System.out.println("C-Style-Python Transpiled Python Script: " + cStylePythonCode);
	}
}
