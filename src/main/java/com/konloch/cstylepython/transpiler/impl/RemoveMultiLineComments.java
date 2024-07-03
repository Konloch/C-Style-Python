package com.konloch.cstylepython.transpiler.impl;

import com.konloch.cstylepython.CStylePython;
import com.konloch.cstylepython.transpiler.TranspileStage;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class RemoveMultiLineComments implements TranspileStage
{
	@Override
	public String fromPython(CStylePython cpy, String code)
	{
		//TODO
		return code;
	}
	
	@Override
	public String fromBythonPP(CStylePython cpy, String code)
	{
		StringBuilder buffer = new StringBuilder();
		StringBuilder transpiledCode = new StringBuilder();
		char[] codeArray = code.toCharArray();
		int max = codeArray.length;
		boolean commentFlag = false;
		for(int i = 0; i < max; i++)
		{
			char c = codeArray[i];
			
			//function support
			if (c == '/' && i + 1 < max
					&& codeArray[i + 1] == '*')
			{
				commentFlag = true;
				i++;
			}
			//if branch support
			else if (c == '*' && i + 1 < max
					&& codeArray[i + 1] == '/')
			{
				//handle edge-case of multi-line being on the same line, such as /* ok */
				if(buffer.length() != 0)
				{
					if(commentFlag)
						transpiledCode.append("#");
					
					transpiledCode.append(buffer);
					buffer.setLength(0);
				}
				
				commentFlag = false;
				i++;
			}
			else if (c == '\n' || c == '\r')
			{
				buffer.append(c);
				
				if(commentFlag)
					transpiledCode.append("#");
				
				transpiledCode.append(buffer);
				
				buffer.setLength(0);
			}
			else
			{
				buffer.append(c);
			}
		}
		
		if(buffer.length() != 0)
		{
			transpiledCode.append(buffer);
			buffer.setLength(0);
		}
		
		return transpiledCode.toString();
	}
}
