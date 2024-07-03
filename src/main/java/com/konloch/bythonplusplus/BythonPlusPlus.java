package com.konloch.bythonplusplus;

import com.konloch.bythonplusplus.process.ProcessWrapper;
import com.konloch.bythonplusplus.process.Python;
import com.konloch.bythonplusplus.transpiler.TranspileStage;
import com.konloch.bythonplusplus.transpiler.impl.RemoveComments;
import com.konloch.bythonplusplus.transpiler.impl.ReplaceBraces;
import com.konloch.bythonplusplus.transpiler.impl.ReplaceForWhileIfTryCatch;
import com.konloch.disklib.DiskReader;
import com.konloch.disklib.DiskWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Konloch
 * @since 7/2/2024
 */
public class BythonPlusPlus
{
	public final BPPConfig config = new BPPConfig();
	public final Python python = new Python();
	public final TranspileStage[] stages = new TranspileStage[]
	{
		new RemoveComments(),
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
		
		BythonPlusPlus bpp = new BythonPlusPlus();
		bpp.config.load();
		ArrayList<String> arguments = new ArrayList<>();
		
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
						for(int x = i + 1; x < args.length; x++)
						{
							arguments.add(args[x]);
							i++;
						}
						
						if(arg.endsWith(".py")) //run python files (convert to bpp, convert back to python, then run)
						{
							//temp compile and run
							File tempFile = File.createTempFile("bpp-transpile", "bpp");
							
							//read from arg, transpile from python to bpp, write to disk
							DiskWriter.write(tempFile, bpp.pythonToBythonPlusPlus(DiskReader.readString(arg)));
							
							//run bpp file
							if(!bpp.interpretBPPFile(tempFile, arguments.toArray(new String[0])))
								System.out.println("Error: File " + arg + " could not be ran.");
							
							tempFile.delete();
						}
						else if(arg.endsWith(".by")) //run bython files
						{
							if(!bpp.interpretBPPFile(new File(arg), arguments.toArray(new String[0])))
								System.out.println("Error: File " + arg + " could not be ran.");
						}
						else if(arg.endsWith(".bpp")) //run bython++ files
						{
							if(!bpp.interpretBPPFile(new File(arg), arguments.toArray(new String[0])))
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
	
	private boolean interpretBPPFile(File file, String[] arguments) throws Exception
	{
		if(!file.exists() || !file.isFile())
			return false;
		
		//process wrapper
		ProcessWrapper wrapper = runBythonPlusPlusFile(file, arguments);
		
		//output sys out
		for(String out : wrapper.out)
			System.out.println(out);
		
		//output sys err
		for(String err : wrapper.err)
			System.err.println(err);
		
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
		File tempFile = File.createTempFile("bpp-transpile", "py");
		
		//read from arg, transpile from python to bpp, write to disk
		DiskWriter.write(tempFile, bythonPlusPlusToPython(DiskReader.readString(file)));
		
		//run tempFile via python
		ProcessWrapper wrapper = python.runPythonFile(config.getPython(), tempFile, arguments);
		
		//delete temp file
		tempFile.delete();
		
		return wrapper;
	}
}