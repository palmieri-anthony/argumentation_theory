package fr.unice.ic.argumentation.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.unice.ic.argumentation.ui.GraphTransformator;
import fr.unice.ic.argumentation.ui.MxGraph;

public class IOStreamASPARTIX {
	public void writer(File f, String s) {
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
	
	public void delete(File f) {
		f.delete();
	}
	
	public static void main(String[] args) {
		IOStreamASPARTIX io = new IOStreamASPARTIX();
		File f = new File("new.txt");
		io.writer(f, "r2");
	}
}
