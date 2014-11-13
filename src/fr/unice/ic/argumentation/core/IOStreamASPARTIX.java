package fr.unice.ic.argumentation.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOStreamASPARTIX {
	void writer(File f, String s) {
		try {
			// if file doesnt exists, then create it
			if (!f.exists()) {
				f.createNewFile();
			}
	 
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void delete(File f) {
		f.delete();
	}
}
