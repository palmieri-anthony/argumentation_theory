package fr.unice.ic.argumentation.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class DynPARTIX {
	private Process process;
	final private File DynPARTIXWindows = new File("." + File.separator + "lib"
			+ File.separator + "dynPARTIX_1.0_win_32bit" + File.separator
			+ "dynpartix.exe");
	final private File DynPARTIXLinux = new File("." + File.separator + "lib"
			+ File.separator + "dynPARTIX_2.0_linux_32bit" + File.separator
			+ "dynpartix");
	final private String OS = System.getProperty("os.name");
	private static DynPARTIX singleton = new DynPARTIX();

	public DynPARTIX() {
	}

	public static DynPARTIX getDynPARTIX() {
		return singleton;
	}

	public String output(File f, String type) {
		Runtime runtime = Runtime.getRuntime();
		StringBuilder result = new StringBuilder();
		try {
			if (OS.contains("Windows")) {
				process = runtime.exec(DynPARTIXWindows + " -f " + f + " "
						+ type);
			} else if (OS.contains("Linux")) {
				process = runtime
						.exec(DynPARTIXLinux + " -f " + f + " " + type);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String read = br.readLine();
			while (read != null) {
				result.append(read);
				read = br.readLine();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result.toString();
	}

	public static void main(String[] args) {
		DynPARTIX d = DynPARTIX.getDynPARTIX();
		File f = new File("." + File.separator + "lib" + File.separator
				+ "dynPARTIX_1.0_win_32bit" + File.separator + "t.txt");
		System.out.println(d.output(f, "-s preferred"));
	}

}
