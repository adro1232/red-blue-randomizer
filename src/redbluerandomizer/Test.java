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
		RedBlueRandomizer br = new RedBlueRandomizer();		
		br.readRom(inputFile);
		br.setRivalToggle(true);
		br.setPlayerStartersToggle(true);
		br.randomize();
		br.saveRom(outputFile);		
	}

}
