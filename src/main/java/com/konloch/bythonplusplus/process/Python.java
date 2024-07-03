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
	public ProcessWrapper runPythonFile(String python, File file, String... arguments) throws IOException, InterruptedException
	{
		ProcessWrapper wrapper = new ProcessWrapper();
		ProcessBuilder builder = new ProcessBuilder(python, file.getAbsolutePath());
		
		for(String argument : arguments)
			builder.command().add(argument);
		
		Process pythonProcess = builder.start();
		
		//wait for the process
		int exitCode = pythonProcess.waitFor();
		
		//copy streams to wrapper
		wrapper.copyFromInputStream(wrapper.out, pythonProcess.getInputStream());
		wrapper.copyFromInputStream(wrapper.err, pythonProcess.getErrorStream());
		
		//return wrapper
		return wrapper;
	}
}
