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
		int[] insideFunction = new int[5];
		
		char[] codeArray = code.toCharArray();
		for(int i = 0; i < codeArray.length; i++)
		{
			char c = codeArray[i];
			
			//function support
			if (c == 'd' && insideFunction[0] == 0
					|| c == 'e' && insideFunction[0] == 1
					|| c == 'f' && insideFunction[0] == 2)
			{
				insideFunction[0]++;
			}
			//if branch support
			else if (c == 'i' && insideFunction[1] == 0
					|| c == 'f' && insideFunction[1] == 1)
			{
				insideFunction[1]++;
			}
			//for loop support
			else if (c == 'f' && insideFunction[2] == 0
					|| c == 'o' && insideFunction[2] == 1
					|| c == 'r' && insideFunction[2] == 2)
			{
				insideFunction[2]++;
			}
			//while loop support
			else if (c == 'w' && insideFunction[3] == 0
					|| c == 'h' && insideFunction[3] == 1
					|| c == 'i' && insideFunction[3] == 2
					|| c == 'l' && insideFunction[3] == 3
					|| c == 'e' && insideFunction[3] == 4)
			{
				insideFunction[3]++;
			}
			
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
				
				if(insideFunction[0] == 3 //def
					|| insideFunction[1] == 2 //if
					|| insideFunction[2] == 3 //for
					|| insideFunction[3] == 5) //while
				{
					String temp = buffer.toString().trim();
					buffer.setLength(0);
					buffer.append(temp);
					buffer.append(":");
				}
				
				//EOL insert buffer and reset
				transpiledCode.append(getTabs(instructionScope)).append(buffer.toString().trim()).append(bpp.config.getNewLine());
				buffer.setLength(0);
				Arrays.fill(insideFunction, 0);
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
