package com.konloch.bythonplusplus.transpiler.impl;

import com.konloch.bythonplusplus.BythonPlusPlus;
import com.konloch.bythonplusplus.transpiler.TranspileStage;

import java.util.Arrays;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class ReplaceBraces implements TranspileStage
{
	@Override
	public String fromPython(BythonPlusPlus bpp, String code)
	{
		//TODO
		return code;
	}
	
	@Override
	public String fromBythonPP(BythonPlusPlus bpp, String code)
	{
		StringBuilder buffer = new StringBuilder();
		StringBuilder transpiledCode = new StringBuilder();
		int instructionScope = 0;
		int newLineSkip = 0;
		boolean bodyIdentifierSuffix = false;
		
		char[] codeArray = code.toCharArray();
		int max = codeArray.length;
		for(int i = 0; i < max; i++)
		{
			char c = codeArray[i];
			
			//function support
			if (c == 'd' && i + 2 < max
					&& codeArray[i + 1] == 'e'
					&& codeArray[i + 2] == 'f')
				bodyIdentifierSuffix = true;
			//if branch support
			else if (c == 'i' && i + 1 < max
					&& codeArray[i + 1] == 'f')
				bodyIdentifierSuffix = true;
			//for loop support
			else if (c == 'f' && i + 2 < max
					&& codeArray[i + 1] == 'o'
					&& codeArray[i + 2] == 'r')
				bodyIdentifierSuffix = true;
			//while loop support
			else if (c == 'w' && i + 4 < max
					&& codeArray[i + 1] == 'h'
					&& codeArray[i + 2] == 'i'
					&& codeArray[i + 3] == 'l'
					&& codeArray[i + 4] == 'e')
				bodyIdentifierSuffix = true;
			//try support
			else if (c == 't' && i + 2 < max
					&& codeArray[i + 1] == 'r'
					&& codeArray[i + 2] == 'y')
				bodyIdentifierSuffix = true;
			//catch support
			else if (c == 'e' && i + 5 < max
					&& codeArray[i + 1] == 'x'
					&& codeArray[i + 2] == 'c'
					&& codeArray[i + 3] == 'e'
					&& codeArray[i + 4] == 'p'
					&& codeArray[i + 5] == 't')
				bodyIdentifierSuffix = true;
			
			if (c == '\r' || c == '\n')
			{
				//skip \r\n or \n\r
				if(i + 1 < codeArray.length
						&& (c == '\r' && codeArray[i + 1] == '\n')
						|| c == '\n' && codeArray[i + 1] == '\r')
					i++;
				
				//skip lines for syntax sugar
				if(newLineSkip > 0)
				{
					newLineSkip--;
					continue;
				}
				
				if(bodyIdentifierSuffix) //while
				{
					String temp = buffer.toString().trim();
					buffer.setLength(0);
					buffer.append(temp);
					buffer.append(":");
				}
				
				//EOL insert buffer and reset
				transpiledCode.append(getTabs(instructionScope)).append(buffer.toString().trim()).append(bpp.config.getNewLine());
				buffer.setLength(0);
				bodyIdentifierSuffix = false;
			}
			else if (c == '{')
			{
				//decrement tab depth and insert buffer
				newLineSkip = insertTabDepth(buffer, transpiledCode, instructionScope, newLineSkip);
				instructionScope++;
			}
			else if (c == '}')
			{
				//increment tab depth and insert buffer
				newLineSkip = insertTabDepth(buffer, transpiledCode, instructionScope, newLineSkip);
				instructionScope--;
			}
			else //add character to buffer
			{
				buffer.append(c);
			}
		}
		
		//insert any remaining buffer contents
		if (buffer.length() > 0)
			transpiledCode.append(getTabs(instructionScope)).append(buffer.toString().trim());
		
		return transpiledCode.toString();
	}
	
	private int insertTabDepth(StringBuilder buffer, StringBuilder transpiledCode, int tabDepth, int newLineSkip)
	{
		if(buffer.length() > 0)
			transpiledCode.append(getTabs(tabDepth)).append(buffer.toString().trim());
		else
			newLineSkip++; //prevent the ending from creating an empty line
		
		buffer.setLength(0);
		
		return newLineSkip;
	}
	
	private String getTabs(int depth)
	{
		StringBuilder tabs = new StringBuilder();
		
		for (int i = 0; i < depth; i++)
			tabs.append("\t");
		
		return tabs.toString();
	}
}
