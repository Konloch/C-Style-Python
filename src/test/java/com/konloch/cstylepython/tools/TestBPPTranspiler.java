package com.konloch.cstylepython.tools;

import com.konloch.cstylepython.CStylePython;
import com.konloch.disklib.DiskReader;

import java.io.File;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class TestBPPTranspiler
{
	public static void main(String[] args) throws Exception
	{
		if(args == null || args.length == 0)
		{
			System.out.println("Incorrect arguments:");
			System.out.println("\t+ To run files: pass a bython++ file.");
			return;
		}
		
		CStylePython bpp = new CStylePython();
		File file = new File(args[0]);
		
		System.out.println("Compile Example: " + bpp.bythonPlusPlusToPython(DiskReader.readString(file)));
	}
}
