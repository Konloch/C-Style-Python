package com.konloch.cstylepython.transpiler.impl;

import com.konloch.cstylepython.CStylePython;
import com.konloch.cstylepython.transpiler.TranspileStage;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class RemoveSingleLineComments implements TranspileStage
{
	@Override
	public String fromPython(CStylePython cpy, String code)
	{
		//TODO
		return code;
	}
	
	/*
	 * Removes all comments that start with /// it will throw the code away entirely
	 */
	@Override
	public String fromBythonPP(CStylePython cpy, String code)
	{
		StringBuilder transpiledCode = new StringBuilder();
		boolean inString = false;
		boolean escapeFlag = false;
		int commentSlashCount = 0;
		StringBuilder buffer = new StringBuilder();
		
		char[] codeArray = code.toCharArray();
		int max = codeArray.length;
		for(int i = 0; i < max; i++)
		{
			char c = codeArray[i];
			if (c == '"' && !escapeFlag)
			{
				buffer.append(c);
				inString =! inString;
			}
			else if (c == '\\' && inString)
			{
				buffer.append(c);
				escapeFlag =! escapeFlag;
			}
			else if (c == '/' && i + 2 < max && !inString
					&& codeArray[i + 1] == '/'
					&& codeArray[i + 2] == '/')
			{
				commentSlashCount++;
				buffer.append("#");
				transpiledCode.append(buffer);
				buffer.setLength(0);
				i += 2;
			}
			else if (c == '\n' || c == '\r')
			{
				if(inString)
					inString = false;
				if(escapeFlag)
					escapeFlag = false;
				
				if (commentSlashCount > 0)
					commentSlashCount = 0;
				else
				{
					transpiledCode.append(buffer).append("\n");
					buffer.setLength(0);
				}
			}
			else
			{
				if(escapeFlag)
					escapeFlag = false;
				
				buffer.append(c);
			}
		}
		
		if (buffer.length() > 0)
			transpiledCode.append(buffer);
		
		return transpiledCode.toString();
	}
}
