package com.konloch.bythonplusplus.process;

import java.io.File;
import java.io.IOException;

/**
 * @author Konloch
 * @since 7/3/2024
 */
public class Python
{
	/**
	 * Run a file python
	 * @param python the path to your python installation
	 * @param file any valid python file
	 * @return the process wrapper which contains the outputs
	 */
	public ProcessWrapper runPythonFile(String python, File file) throws IOException, InterruptedException
	{
		ProcessWrapper wrapper = new ProcessWrapper();
		ProcessBuilder builder = new ProcessBuilder(python, file.getAbsolutePath());
		Process pythonProcess = builder.start();
		
		//wait for the process
		int exitCode = pythonProcess.waitFor();
		
		//TODO read error and out
		
		return wrapper;
	}
}
