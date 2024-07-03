package com.konloch.bythonplusplus;

import com.konloch.bythonplusplus.process.ProcessWrapper;
import com.konloch.bythonplusplus.process.Python;
import com.konloch.bythonplusplus.transpiler.TranspileStage;
import com.konloch.bythonplusplus.transpiler.impl.RemoveComments;
import com.konloch.bythonplusplus.transpiler.impl.ReplaceBraces;
import com.konloch.disklib.DiskReader;
import com.konloch.disklib.DiskWriter;

import java.io.File;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class BythonPlusPlus
{
	public final BPPConfig config = new BPPConfig();
	public final TranspileStage[] stages = new TranspileStage[]
	{
		new RemoveComments(),
		new ReplaceBraces(),
	};
	public final Python python = new Python();
	
	public static void main(String[] args)
	{
		if(args == null || args.length == 0)
		{
			System.out.println("Incorrect arguments:");
			System.out.println("\t+ To run files: pass a python, bython, or bython++ file.");
			System.out.println("\t+ To transpile: pass the argument `-c` then pass the python, bython or bython++ file.");
			return;
		}
		
		BythonPlusPlus bpp = new BythonPlusPlus();
		bpp.config.load();
		
		for(int i = 0; i < args.length; i++)
		{
			try
			{
				String arg = args[i].toLowerCase();
				
				switch(arg)
				{
					case "-c": //compile python into bpp
						arg = args[i++];
						
						if(arg.toLowerCase().endsWith(".py"))
						{
							File inputFile = new File(arg);
							File outputFile = new File(inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().length() - 2) + "bpp");
							
							//read from arg, transpile from python to bpp, write to disk
							DiskWriter.write(outputFile, bpp.pythonToBythonPlusPlus(DiskReader.readString(arg)));
						}
						else
						{
							System.out.println("File must end in .py to compile to .bpp");
							return;
						}
						break;
						
					default:
						if(arg.endsWith(".py")) //run python files (convert to bpp, convert back to python, then run)
						{
							//temp compile and run
							File tempFile = File.createTempFile("bpp-transpile", "bpp");
							
							//read from arg, transpile from python to bpp, write to disk
							DiskWriter.write(tempFile, bpp.pythonToBythonPlusPlus(DiskReader.readString(arg)));
							
							//run bpp file
							if(!bpp.runBPPFile(tempFile))
								System.out.println("Error: File " + arg + " could not be ran.");
							
							tempFile.delete();
						}
						else if(arg.endsWith(".by")) //run bython files
						{
							if(!bpp.runBPPFile(new File(arg)))
								System.out.println("Error: File " + arg + " could not be ran.");
						}
						else if(arg.endsWith(".bpp")) //run bython++ files
						{
							if(!bpp.runBPPFile(new File(arg)))
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
	}
	
	public boolean runBPPFile(File file) throws Exception
	{
		if(!file.exists() || !file.isFile())
			return false;
		
		//temp compile and run
		File tempFile = File.createTempFile("bpp-transpile", "py");
		
		//read from arg, transpile from python to bpp, write to disk
		DiskWriter.write(tempFile, bythonPlusPlusToPython(DiskReader.readString(file)));
		
		//run tempFile via python
		ProcessWrapper wrapper = python.runPythonFile(config.getPython(), tempFile);
		
		//output sys out
		for(String out : wrapper.out)
			System.out.println(out);
		
		//output sys err
		for(String err : wrapper.err)
			System.err.println(err);
		
		//delete temp file
		tempFile.delete();
		
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
}