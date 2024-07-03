package com.konloch.cstylepython.transpiler.impl;

import com.konloch.cstylepython.CStylePython;
import com.konloch.cstylepython.transpiler.TranspileStage;

/**
 * @author Konloch
 * @since 7/3/2024
 */
public class ReplaceForWhileIfTryCatch implements TranspileStage
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
		boolean bodyFunction = false;
		boolean bodyFunction2 = false;
		
		char[] codeArray = code.toCharArray();
		int max = codeArray.length;
		for(int i = 0; i < max; i++)
		{
			char c = codeArray[i];
			
			//if support
			if (c == 'i' && i + 1 < max
					&& codeArray[i + 1] == 'f')
				bodyFunction = true;
			//for loop support
			else if (c == 'f' && i + 2 < max
					&& codeArray[i + 1] == 'o'
					&& codeArray[i + 2] == 'r')
				bodyFunction = true;
			//while loop support
			else if (c == 'w' && i + 4 < max
					&& codeArray[i + 1] == 'h'
					&& codeArray[i + 2] == 'i'
					&& codeArray[i + 3] == 'l'
					&& codeArray[i + 4] == 'e')
				bodyFunction = true;
			//try support
			else if (c == 't' && i + 2 < max
					&& codeArray[i + 1] == 'r'
					&& codeArray[i + 2] == 'y')
				bodyFunction = true;
			//catch support
			else if (c == 'e' && i + 5 < max
					&& codeArray[i + 1] == 'x'
					&& codeArray[i + 2] == 'c'
					&& codeArray[i + 3] == 'e'
					&& codeArray[i + 4] == 'p'
					&& codeArray[i + 5] == 't')
				bodyFunction = true;
			
			if (c == '\r' || c == '\n')
			{
				//skip \r\n or \n\r
				if(i + 1 < codeArray.length
						&& (c == '\r' && codeArray[i + 1] == '\n')
						|| c == '\n' && codeArray[i + 1] == '\r')
					i++;
				
				buffer.append(cpy.config.getNewLine());
				
				bodyFunction = false;
				bodyFunction2 = false;
			}
			else if (c == '(' && bodyFunction)
			{
				bodyFunction = false;
				bodyFunction2 = true;
			}
			else if (c == ')' && bodyFunction2)
			{
				bodyFunction2 = false;
			}
			else if (c == ' ' && (bodyFunction || bodyFunction2))
			{
				buffer.append(c);
			}
			else //add character to buffer
			{
				bodyFunction = false;
				buffer.append(c);
			}
		}
		
		return buffer.toString();
	}
}
