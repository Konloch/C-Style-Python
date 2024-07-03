package com.konloch.bythonplusplus.tests.interpreter;

import com.konloch.bythonplusplus.BythonPlusPlus;
import com.konloch.bythonplusplus.process.ProcessWrapper;
import com.konloch.disklib.DiskReader;
import com.konloch.disklib.DiskWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Konloch
 * @since 7/3/2024
 */
public class FunctionInteropTest
{
	@Test
	public void testTestFunction() throws IOException, InterruptedException
	{
		BythonPlusPlus bpp = new BythonPlusPlus();
		
		//temp compile and run
		File tempFile = File.createTempFile("bpp-transpile", "py");
		
		//read from arg, transpile from python to bpp, write to disk
		DiskWriter.write(tempFile, bpp.bythonPlusPlusToPython(DiskReader.readString("testcases/general-tests/function_interop.bpp")));
		
		//run tempFile via python
		ProcessWrapper wrapper = bpp.python.runPythonFile(bpp.config.getPython(), tempFile, "27");
		
		//assert wrapper output length
		assertEquals(1, wrapper.out.size());
		
		//assert python interpreter results
		assertEquals("734", wrapper.out.get(0).trim());
	}
}
