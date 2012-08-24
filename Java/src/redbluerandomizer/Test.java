package redbluerandomizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;


public class Test {

	/**
	 * @param args
	 */
	
	public static String inputFile = "";
	public static String outputFile = "";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RedBlueRandomizer br = new RedBlueRandomizer();
		br.readRom(inputFile);
		br.setPlayerStartersToggle(true);
		br.setTitleScreenToggle(true);
		br.setTrainersToggle(true);
		br.setwildAreasToggle(true);
		br.randomize();
		
		RomPrinter printer = new RomPrinter(br);
		printer.printROM();
	}	
}
