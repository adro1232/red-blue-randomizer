package redbluerandomizer;

import java.io.File;

import javax.swing.JFileChooser;


public class Test {

	/**
	 * @param args
	 */
	
	public static String inputFile = "/home/trey/Temp/blue.gb";
	public static String outputFile = "/home/trey/Temp/blue_random.gb";
	
	public static void main(String[] args) {
		RedBlueRandomizer br = new RedBlueRandomizer();		
		br.readRom(inputFile);
		br.setGymLeadersToggle(true);
		br.randomize();
		br.saveRom(outputFile);		
	}

}
