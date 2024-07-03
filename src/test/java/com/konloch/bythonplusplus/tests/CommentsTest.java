package com.konloch.bythonplusplus.tests;

import com.konloch.bythonplusplus.BythonPlusPlus;
import com.konloch.bythonplusplus.process.ProcessWrapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Konloch
 * @since 7/3/2024
 */
public class CommentsTest
{
	@Test
	public void testTestFunction() throws IOException, InterruptedException
	{
		BythonPlusPlus bpp = new BythonPlusPlus();
		ProcessWrapper wrapper = bpp.runBythonPlusPlusFile(new File("testcases/edgecase-tests/comments_test.bpp"));
		
		//assert wrapper output length
		assertEquals(1, wrapper.out.size());
		
		//assert python interpreter results
		assertEquals("hello///world\"testing escape///part-2", wrapper.out.get(0).trim());
	}
}