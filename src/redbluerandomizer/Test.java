package redbluerandomizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;


public class Test {

	/**
	 * @param args
	 */
	
	public static String inputFile = "/home/trey/Documents/pokemon/blue.gb";
	public static String outputFile = "/home/trey/Documents/pokemon/blue_random.gb";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RedBlueRandomizer br = new RedBlueRandomizer();
		
		inputFile = "/home/trey/Documents/pokemon/Pokemon Red Rand.gb";
		
		/*
		br.setNoLegendariesToggle(true);
		br.setOneToOneToggle(true);
		br.swapMapTest();
		//*/
		

		br.readRom(inputFile);
		br.setwildAreasToggle(true);
		br.printROM();
		br.randomize();
		
		//br.randomize();
		//br.saveRom(outputFile);
	}	
	
	public static void generateSet(RedBlueRandomizer br, int total){
		ArrayList<String> pokemon = new ArrayList<String>();
		for(int i=0; i<total; i++){
			pokemon.add(br.getPokemonName(br.getRandomPokemonIndex()));
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0; i<pokemon.size(); i++){
			String key = pokemon.get(i);
			if(map.containsKey(key)){
				map.put(key, (map.get(key)+1));
			}
			else{
				map.put(key, 1);
			}
		}
		for(String key: map.keySet()){
			System.out.println(map.get(key) + "\t\t" + key);
		}
	}

}
