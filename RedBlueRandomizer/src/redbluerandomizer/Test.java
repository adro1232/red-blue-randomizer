package redbluerandomizer;

import java.io.File;

import javax.swing.JFileChooser;


public class Test {

	/**
	 * @param args
	 */
	
	public static String inputFile = "";
	public static String outputFile = "";
	
	public static void main(String[] args) {
		BlueRandomizer br = new BlueRandomizer();		
		br.readRom(inputFile);
		br.randomize();
		br.saveRom(outputFile);		
	}

}
