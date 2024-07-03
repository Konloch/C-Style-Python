package com.konloch.cstylepython;

import com.konloch.cstylepython.process.ProcessWrapper;
import com.konloch.cstylepython.process.Python;
import com.konloch.cstylepython.transpiler.TranspileStage;
import com.konloch.cstylepython.transpiler.impl.RemoveMultiLineComments;
import com.konloch.cstylepython.transpiler.impl.RemoveSingleLineComments;
import com.konloch.cstylepython.transpiler.impl.ReplaceBraces;
import com.konloch.cstylepython.transpiler.impl.ReplaceForWhileIfTryCatch;
import com.konloch.disklib.DiskReader;
import com.konloch.disklib.DiskWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class CStylePython
{
	public final Config config = new Config();
	public final Python python = new Python();
	public final TranspileStage[] stages = new TranspileStage[]
	{
		new RemoveMultiLineComments(),
		new RemoveSingleLineComments(),
		new ReplaceForWhileIfTryCatch(),
		new ReplaceBraces(),
	};
	
	public static void main(String[] args)
	{
		if(args == null || args.length == 0)
		{
			System.out.println("Incorrect arguments:");
			System.out.println("\t+ To run files: pass a python, bython, or bython++ file.");
			System.out.println("\t+ To transpile: pass the argument `-c` then pass the python, bython or bython++ file.");
			return;
		}
		
		CStylePython cpy = new CStylePython();
		cpy.config.load();
		ArrayList<String> arguments = new ArrayList<>();
		int i = 0;
		
		try
		{
			String arg = args[i].toLowerCase();
			
			switch(arg)
			{
				case "-c": //compile python into cpy
					arg = args[i++];
					
					if(arg.toLowerCase().endsWith(".py"))
					{
						File inputFile = new File(arg);
						File outputFile = new File(inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().length() - 2) + "cpy");
						
						//read from arg, transpile from python to cpy, write to disk
						DiskWriter.write(outputFile, cpy.pythonToBythonPlusPlus(DiskReader.readString(arg)));
					}
					else
					{
						System.out.println("File must end in .py to compile to .cpy");
						return;
					}
					break;
				
				default:
					for(int x = i + 1; x < args.length; x++)
					{
						arguments.add(args[x]);
						i++;
					}
					
					if(arg.endsWith(".py")) //run python files (convert to cpy, convert back to python, then run)
					{
						//temp compile and run
						File tempFile = File.createTempFile("cpy-transpile", "cpy");
						
						//read from arg, transpile from python to cpy, write to disk
						DiskWriter.write(tempFile, cpy.pythonToBythonPlusPlus(DiskReader.readString(arg)));
						
						//run cpy file
						if(!cpy.interpretCPYFile(tempFile, arguments.toArray(new String[0])))
							System.out.println("Error: File " + arg + " could not be ran.");
						
						tempFile.delete();
					}
					else if(arg.endsWith(".by")) //run bython files
					{
						if(!cpy.interpretCPYFile(new File(arg), arguments.toArray(new String[0])))
							System.out.println("Error: File " + arg + " could not be ran.");
					}
					else if(arg.endsWith(".cpy")) //run cpy files
					{
						if(!cpy.interpretCPYFile(new File(arg), arguments.toArray(new String[0])))
							System.out.println("Error: File " + arg + " could not be ran.");
					}
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean interpretCPYFile(File file, String[] arguments) throws Exception
	{
		if(!file.exists() || !file.isFile())
			return false;
		
		//process wrapper
		ProcessWrapper wrapper = runBythonPlusPlusFile(file, arguments);
		
		//print debug
		wrapper.printDebug();
		
		return true;
	}
	
	public String pythonToBythonPlusPlus(String code)
	{
		String buffer = code;
		for(TranspileStage stage : stages)
			buffer = stage.fromPython(this, buffer);
		
		return buffer;
	}
	
	public String bythonPlusPlusToPython(String code)
	{
		String buffer = code;
		for(TranspileStage stage : stages)
			buffer = stage.fromBythonPP(this, buffer);
		
		return buffer;
	}
	
	public ProcessWrapper runBythonPlusPlusFile(File file, String... arguments) throws IOException, InterruptedException
	{
		//temp compile and run
		File tempFile = File.createTempFile("cpy-transpile", "py");
		
		String cpyCode = bythonPlusPlusToPython(DiskReader.readString(file));
		
		//read from arg, transpile from python to cpy, write to disk
		DiskWriter.write(tempFile, cpyCode);
		
		//run tempFile via python
		ProcessWrapper wrapper = python.runPythonFile(cpyCode, config.getPython(), tempFile, arguments);
		
		//delete temp file
		tempFile.delete();
		
		return wrapper;
	}
}