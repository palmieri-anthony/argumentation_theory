package fr.unice.ic.argumentation.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DynPARTIXService {
	private Process process;
	final private File DynPARTIXWindows = new File("." + File.separator + "lib" + File.separator + "dynPARTIX_1.0_win_32bit" + File.separator + "dynpartix.exe");
	final private File DynPARTIXLinux = new File("." + File.separator + "lib" + File.separator + "dynPARTIX_2.0_linux_32bit" + File.separator + "dynpartix");
	final private String OS = System.getProperty("os.name");
	private static DynPARTIXService singleton = new DynPARTIXService();
	
	public DynPARTIXService() {
	}
		
	public static DynPARTIXService getDynPARTIX(){
		return singleton;
	}

	public List<List<String>> output(File f, String type) {
		Runtime runtime = Runtime.getRuntime();
		StringBuilder out = new StringBuilder();
		try{
			if(OS.contains("Windows")){
				process = runtime.exec(DynPARTIXWindows + " -f " + f + " " + type);
			}
			else if(OS.contains("Linux")){
				process = runtime.exec(DynPARTIXLinux + " -f " + f + " " + type);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String read = br.readLine();
			while(read != null) {
			    out.append(read);
			    read = br.readLine();
			}
		}
		catch (Exception e){
				System.err.println(e.toString());
		}
		
		List<List<String>> result = new ArrayList<List<String>>();
		//parse
		try {
			String s = out.toString();
			int a = s.indexOf("{");
			s = s.substring(a, s.length());
			
			String[] split = Pattern.compile("\\},\\{").split(s);
			String[] splitSol = null;
			for(int i = 0; i < split.length; ++i) {
				split[i] = split[i].replaceAll("\\}", "");
				split[i] = split[i].replaceAll("\\{", "");
				splitSol = split[i].split(",");
				
				List<String> list = new ArrayList<String>();
				for(int w = 0; w < splitSol.length; ++w){
					/*
					if(splitSol[w].equals("")){
						list.add("");
					}*/
					//else {
						list.add(splitSol[w]);
					//}
				}
				result.add(list);
			}
		}
		catch (Exception e){
			System.err.println(e.toString());
			result = null;
		}
		return result;
	}
}
