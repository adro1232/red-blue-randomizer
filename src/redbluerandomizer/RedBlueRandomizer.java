package redbluerandomizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ec.util.MersenneTwister;

public class RedBlueRandomizer {	
		
	private ArrayList<String> pokemonNames;
	private ArrayList<Integer> pokemonIndexes;
	private Map<Integer, Integer> swapMap;
	private ArrayList<Integer> legendaries;
	private Random rand = new MersenneTwister(new Date().getTime());
	private byte[] rom;
	
	//constants
	private final String redRomName = "POKEMON RED";
	private final String blueRomName =  "POKEMON BLUE";
	
	//lookups
	private final String[] gymLeaderNames = {"Brock", "Misty", "Lt. Surge", "Erika", "Koga", "Sabrina", "Blaine", "Giovanni"};
	private final String[] elite4Names = {"Lorelei", "Bruno", "Agatha", "Lance"};
	
	//offsets
	private final int romNameStart = 0x134;
	private final int romNameEnd = 0x144;	
	private final int[] playerStarters = {0x1D10E, 0x1D11F, 0x1D130};
	private final int[] titleScreenPokemon = {0x4399,0x4588,0x4589,0x458A,0x458B,0x458C,0x458D,0x458E,0x458F,0x4590,0x4591,0x4592,0x4593,0x4594,0x4595,0x4596,0x4597};	
	private final int[] areaOffsets = {0xD0E0,0xD0F6,0xD10C,0xD122,0xD138,0xD14E,0xD164,0xD17A,0xD190,0xD1A6,0xD1BC,0xD1D2,0xD1E8,0xD1FE,0xD214,0xD22A,0xD240,0xD256,0xD26C,0xD282,0xD298,0xD2B2,0xD2C8,0xD2DE,0xD2F4,0xD30A,0xD320,0xD336,0xD34C,0xD362,0xD378,0xD38E,0xD3A4,0xD3BA,0xD3D0,0xD3E6, 0xD3FD,0xD412,0xD428,0xD43E,0xD454,0xD46A,0xD480,0xD496,0xD4AC,0xD4C2,0xD4D8,0xD4EE,0xD502,0xD518,0xD52E,0xD544,0xD55A,0xD570,0xD586,0xD59C,0xD5B2};
	private final int trainerPokemonStart = 0x39DCD;
	private final int trainerPokemonEnd = 0x3A52D;
	
	
	//Regular Trainers start: 0x39DCD
	//Regular Trainers end: 0x3A1E2
	
	//Regular Rival Starters start: 0x3A1E3
	//Regular Rival Starters end: 0x3A1EC
	
	//Special Trainers start: 0x3A1EC (rival first)
	//Special Trainers end: 0x3A235
	
	//?? Regular Trainers start: 0x3A236
	//?? Regular Trainers end: 
	
	private final int[] gymLeaders = {0x3A3B6,0x3A3BC,0x3A3C2,0x3A3CA,0x3A3D2,0x3A3E6,0x3A3DC,0x3A291};
	private final int[] eliteFour = {0x3A4BC, 0x3A3AA, 0x3A517, 0x3A523};
	private final int[] rivalStarters = {0x3A1E5,0x3A1E8,0x3A1EB};
	private final int[][]rivalPokemon = {
											//player chose bulbasaur
											{0x3a1fb, 0x3a1fd}, 									//pidgey, charmander
											{0x3a215, 0x3a217, 0x3a219, 0x3a21b}, 					//pidgeotto, abra, rattata, charmander
											{0x3a417, 0x3a419, 0x3a41b, 0x3a41d},					//pidgeotto, ratticate, kadabra, charmeleon
											{0x3a439, 0x3a43b, 0x3a43d, 0x3a43f, 0x3a441},			//pidgeotto, exeggucute, gyrados, kadabra, charmeleon
											{0x3a45d, 0x3a45f, 0x3a461, 0x3a463, 0x3a465}, 			//pidgeot, exeggucute, gyrados, alakazam, charizard
											{0x3a485, 0x3a487, 0x3a489, 0x3a48b, 0x3a48d, 0x3a48f}, //pidgeot, ryhorn, exeggucute, gyrados, alakazam, charizard
											{0x3a4af, 0x3a4b1, 0x3a4b3, 0x3a4b5, 0x3a4b7, 0x3a4b9}, //pidgeot, alakazam, rhydon, exeggcutor, gyrados, charizard
									        
									        //player chose charmander
											{0x3a1ef, 0x3a1f1}, 									//Pidgey, Squirtle
											{0x3a201, 0x3a203, 0x3a205, 0x3a207}, 					//Pidgeotto, Abra, Rattata, Squirtle
											{0x3a403, 0x3a405, 0x3a407, 0x3a409},					//Pidgeotto, Raticate, Kadabra, Wartortle
											{0x3a421, 0x3a423, 0x3a425, 0x3a427, 0x3a429},			//Pidgeotto, Growlithe, Exeggcute, Kadabra, Wartortle
											{0x3a445, 0x3a447, 0x3a449, 0x3a44b, 0x3a44d},			//Pidgeot, Growlithe, Exeggcute, Alakazam, Blastoise
											{0x3a469, 0x3a46b, 0x3a46d, 0x3a46f, 0x3a471, 0x3a473}, //Pidgeot, Rhyhorn, Growlithe, Exeggcute, Alakazam, Blastoise
											{0x3a493, 0x3a495, 0x3a497, 0x3a499, 0x3a49b, 0x3a49d}, //Pidgeot, Alakazam, Rhydon, Arcanine, Exeggutor, Blastoise
									        
									        //player chose squirtle
											{0x3a1f5, 0x3a1f7},										//Pidgey, Bulbasaur
											{0x3a20b, 0x3a20d, 0x3a20f, 0x3a211},					//Pidgeotto, Abra, Rattata, Bulbasaur
											{0x3a40d, 0x3a40f, 0x3a411, 0x3a413},					//Pidgeotto, Raticate, Kadabra, Ivysaur 
											{0x3a42d, 0x3a42f, 0x3a431, 0x3a433, 0x3a435},			//Pidgeotto, Gyarados, Growlithe, Kadabra, Ivysaur
											{0x3a451, 0x3a453, 0x3a455, 0x3a457, 0x3a459},			//Pidgeot, Gyarados, Growlithe, Alakazam, Venusaur
											{0x3a477, 0x3a479, 0x3a47b, 0x3a47d, 0x3a47f, 0x3a481},	//Pidgeot, Rhyhorn, Gyarados, Growlithe, Alakazam, Venusaur
											{0x3a4a1, 0x3a4a3, 0x3a4a5, 0x3a4a7, 0x3a4a9, 0x3a4ab}	//Pidgeot, Alakazam, Rhydon, Gyarados, Arcanine, Venusaur
									    };		
	
	//options
	private boolean titleScreenToggle = false;
	private boolean playerStartersToggle = false;
	private boolean wildAreasToggle = false;
	private boolean trainersToggle = false;
	private boolean gymLeadersToggle = false;
	private boolean eliteFourToggle = false;
	private boolean rivalToggle = false;
	private boolean oneToOneToggle = false;
	private boolean noLegendariesToggle = false;	
	
	//checks the ROM's name
	public boolean isPokemonRedBlue(){
		try{
			String romName = "";
			for(int i=romNameStart; i<romNameEnd; i++){
				romName += (char)byteToInt(rom[i]);
			}
			romName = romName.trim();		
			if(romName.equals(redRomName) || romName.equals(blueRomName)){
				return true;
			}
			return false;
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	//performs the randomization (duh...)
	public void randomize(){
		//setup
		pokemonNames = getPokemonNames();
		pokemonIndexes = getPokemonIndexes();
		legendaries = getLegendaries();
		swapMap = getOneToOneMap();	
		
		int offset;
		//intro pokemon
		if(titleScreenToggle){
			for(int i=0; i<titleScreenPokemon.length; i++){
				offset = titleScreenPokemon[i];
				if(oneToOneToggle){
					rom[offset] = getReplacement(rom[offset]);
				}
				else{
					rom[offset] = getRandomPokemonIndex();
				}
			}			
		}
		//player starters
		if(playerStartersToggle){
			for(int i=0; i<playerStarters.length; i++){
				offset = playerStarters[i];
				if(oneToOneToggle){					
					rom[offset] = getReplacement(rom[offset]);
				}
				else{
					rom[offset] = getRandomPokemonIndex();
				}
			}
		}		
		//wild pokemon areas
		if(wildAreasToggle){
			for(int i=0; i<areaOffsets.length; i++){				
				for(int j = 0; j < 20; j+=2){
					offset = areaOffsets[i];
					if(oneToOneToggle){
						rom[offset + j + 1] = getReplacement(rom[offset + j + 1]);
					}
					else{
						rom[offset + j + 1] = getRandomPokemonIndex();
					}
				}
			}			
		}		
		//pokemon trainers
		if(trainersToggle){
			int i = trainerPokemonStart;
			while(i < trainerPokemonEnd){
				if(byteToInt(rom[i]) == 0x0 && byteToInt(rom[i+1]) != 0xFF){
					i = randomizeRegularTrainer(i);
				}
				else{
					i = randomizeSpecialTrainer(i);
				}			
			}			
		}			
	}
	
	//randomizes a regular trainer
	public int randomizeRegularTrainer(int offset){
		offset+=2;
		boolean loop = true;
		while(loop){		
			if(rom[offset] == 0x0){
				loop = false;
				break;									
			}
			else{
				if(oneToOneToggle){
					rom[offset] = getReplacement(rom[offset]);
				}
				else{
					rom[offset] = getRandomPokemonIndex();
				}
				offset++;
			}
		}
		return offset;
	}
	
	//randomizer a special trainer
	public int randomizeSpecialTrainer(int offset){	
		offset += 2;
		boolean loop = true;
		while(loop){		
			if(rom[offset] == 0x0){
				loop = false;
				break;									
			}
			else{
				if(oneToOneToggle){
					rom[offset + 1] = getReplacement(rom[offset + 1]);
				}
				else{
					rom[offset + 1] = getRandomPokemonIndex();
				}
				offset+=2;
			}
		}
		return offset;
	}
	
	//returns a random level between 1-100
	public byte getRandomLevel(){
		shuffle();
		return (byte)(rand.nextInt(100)+1);
	}
	
	//returns a random pokemon index
	public byte getRandomPokemonIndex(){
		shuffle();		
		if(noLegendariesToggle){
			int randomIndex;
			int randomPokemon;
			while(true){
				randomIndex = rand.nextInt(pokemonIndexes.size());
				randomPokemon = pokemonIndexes.get(randomIndex).intValue();
				if(!legendaries.contains(randomPokemon)){
					return (byte)randomPokemon;
				}
			}		
		}
		else{
			int randomIndex = rand.nextInt(pokemonIndexes.size());		
			return (byte)pokemonIndexes.get(randomIndex).intValue();
		}	
	}	
	
	//progresses the RNG a random number of times to add to the randomness
	public void shuffle(){
		int loop = rand.nextInt(10);		
		for(int i=0; i<loop; i++){
			rand.nextInt(pokemonIndexes.size());
		}
	}
	
	//returns a pokemon's name based on its index
	public String getPokemonName(int pokemonIndex){
		return pokemonNames.get(pokemonIndexes.indexOf(pokemonIndex));
	}
	
	//returns a pokemon's name based on its index
	public String getPokemonName(byte pokemonIndex){
		return getPokemonName(byteToInt(pokemonIndex));
	}
	
	//creates a one-to-one randomization of the pokemon list
	public HashMap<Integer, Integer> getOneToOneMap(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> temp = getPokemonIndexes();
		legendaries = getLegendaries();
		if(noLegendariesToggle){
			for(Integer legendaryIndex: legendaries){
				int i = temp.indexOf(legendaryIndex);				
				temp.set(i, byteToInt(getRandomPokemonIndex()));				
			}
		}
		
		Integer newIndex;
		for(Integer oldIndex: pokemonIndexes){
			newIndex = temp.get(rand.nextInt(temp.size()));
			map.put(oldIndex, newIndex);
			temp.remove(newIndex);
		}		
		return map;
	}
	
	//gets the replacement for a pokemon using the swap map generated	
	public byte getReplacement(byte oldIndex){
		return (byte)swapMap.get(byteToInt(oldIndex)).intValue();
	}	
	
	//reads in the ROM given a filepath
	public void readRom(String filePath){
		try {
			FileInputStream stream = new FileInputStream(filePath);
			rom = new byte[stream.available()];
			stream.read(rom, 0, stream.available());
	        stream.close();	  
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	//saves the ROM to a specified filepath
	public void saveRom(String filePath) {
		try {
	    	FileOutputStream stream = new FileOutputStream(new File(filePath));
	    	stream.write(rom);
	    	stream.close();	      
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//closes a stream without fear of an exception thrown	
	public void closeStream(InputStream stream){
		try{
			stream.close();			
		}
		catch(Exception e){}
	}
	
	//converts a byte to an int
	private int byteToInt(byte b){
		return b & 0xFF;
	}
	
	//converts int to a hex string
	private String intToHexStr(int i){
		return "0x" + Integer.toHexString(i).toUpperCase();
	}
	
	private void printPokemon(int offset, int level, byte pokemonIndex){
		String output = intToHexStr(offset) + "\t";
		if(("Level " + level).length() == 7){
			output += ("Level " + level + "    ");
		}
		else{
			output += ("Level " + level) + "   ";
		}
		output += this.getPokemonName(pokemonIndex);
		System.out.println(output);
	}
	
	private void printPokemon(int offset, byte pokemonIndex){
		String output = intToHexStr(offset) + "\t";
		output += this.getPokemonName(pokemonIndex);
		System.out.println(output);
	}
	
	//prtints contents of the ROM
	public void printROM(){
		//setup
		pokemonNames = this.getPokemonNames();
		pokemonIndexes = this.getPokemonIndexes();	
		swapMap = getOneToOneMap();
		
		int offset;
		//intro pokemon		
		System.out.println("\nTitle Screen Pokemon**********************************\n");
		for(int i=0; i<titleScreenPokemon.length; i++){
			offset = titleScreenPokemon[i];
			printPokemon(offset, rom[offset]);
		}			
		
		//player starters
		System.out.println("\nPlayer Starter ***************************************\n");
		for(int i=0; i<playerStarters.length; i++){
			offset = playerStarters[i];
			printPokemon(offset, rom[offset]);			
		}
				
		//wild pokemon areas
		System.out.println("\nWild Pokemon Areas ***********************************");
		for(int i=0; i<areaOffsets.length; i++){
			System.out.println("\nWild Pokemon Area " + i + "\t" + intToHexStr(areaOffsets[i]) + "\n");			
			for(int j = 0; j < 20; j+=2){
				offset = areaOffsets[i];
				printPokemon((offset+j+1), rom[offset + j], rom[offset + j + 1]);
			}
		}
		
		//trainer pokemon		
		System.out.println("\nTraner Pokemon **************************************\n");
		int a = trainerPokemonStart;
		while(a < trainerPokemonEnd){
			System.out.println("\nOffset:" + intToHexStr(a));
			if(byteToInt(rom[a]) == 0x0 && byteToInt(rom[a+1]) != 0xFF){
				a = printRegularTrainer(a);
			}
			else{
				a = printSpecialTrainer(a);
			}
		}	
	}	
	
	//prints a regular trainer
	public int printRegularTrainer(int offset){	
		offset++;
		System.out.println("Level: " + this.byteToInt(rom[offset]));
		offset++;
		boolean loop = true;
		while(loop){		
			if(rom[offset] == 0x0){
				loop = false;
				break;									
			}
			else{
				printPokemon(offset, rom[offset]);
				offset++;
			}
		}
		return offset;
	}
	
	//prints a special trainer
	public int printSpecialTrainer(int offset){	
		offset += 2;
		boolean loop = true;
		while(loop){		
			if(rom[offset] == 0x0){
				loop = false;
				break;									
			}
			else{
				printPokemon(offset+1, byteToInt(rom[offset]), rom[offset+1]);
				offset+=2;
			}
		}
		return offset;
	}
	
	//toggle setters
	public void setTitleScreenToggle(boolean toggle){
		this.titleScreenToggle = toggle;
	}
	public void setPlayerStartersToggle(boolean toggle){
		this.playerStartersToggle = toggle;
	}
	public void setwildAreasToggle(boolean toggle){
		this.wildAreasToggle = toggle;
	}
	public void setTrainersToggle(boolean toggle){
		this.trainersToggle = toggle;
	}	
	public void setGymLeadersToggle(boolean toggle){
		this.gymLeadersToggle = toggle;
	}
	public void setEliteFourToggle(boolean toggle){
		this.eliteFourToggle = toggle;
	}	
	public void setRivalToggle(boolean toggle){
		this.rivalToggle = toggle;
	}
	public void setOneToOneToggle(boolean toggle){
		this.oneToOneToggle = toggle;
	}
	public void setNoLegendariesToggle(boolean toggle){
		this.noLegendariesToggle = toggle;
	}
	
	//returns a list of the legendary pokemon's indexes
	private ArrayList<Integer> getLegendaries(){
		ArrayList<Integer> theLegendaries = new ArrayList<Integer>();
		theLegendaries.add(0x49);
		theLegendaries.add(0x4A);
		theLegendaries.add(0x4B);
		theLegendaries.add(0x83);
		theLegendaries.add(0x15);
		return theLegendaries;
	}
	
	//returns a list of all pokemon names
	private ArrayList<String> getPokemonNames(){
		ArrayList<String> names = new ArrayList<String>();
		names.add("Rhydon");
		names.add("Kangaskhan");
		names.add("NidoranM");
		names.add("Clefairy");
		names.add("Spearow");
		names.add("Voltorb");
		names.add("Nidoking");
		names.add("Slowbro");
		names.add("Ivysaur");
		names.add("Exeggutor");
		names.add("Lickitung");
		names.add("Exeggcute");
		names.add("Grimer");
		names.add("Gengar");
		names.add("NidoranF");
		names.add("Nidoqueen");
		names.add("Cubone");
		names.add("Rhyhorn");
		names.add("Lapras");
		names.add("Arcanine");
		names.add("Mew");
		names.add("Gyarados");
		names.add("Shellder");
		names.add("Tentacool");
		names.add("Gastly");
		names.add("Scyther");
		names.add("Staryu");
		names.add("Blastoise");
		names.add("Pinsir");
		names.add("Tangela");
		names.add("Growlithe");
		names.add("Onix");
		names.add("Fearow");
		names.add("Pidgey");
		names.add("Slowpoke");
		names.add("Kadabra");
		names.add("Graveler");
		names.add("Chansey");
		names.add("Machoke");
		names.add("Mr. Mime");
		names.add("Hitmonlee");
		names.add("Hitmonchan");
		names.add("Arbok");
		names.add("Parasect");
		names.add("Psyduck");
		names.add("Drowzee");
		names.add("Golem");
		names.add("Magmar");
		names.add("Electabuzz");
		names.add("Magneton");
		names.add("Koffing");
		names.add("Mankey");
		names.add("Seel");
		names.add("Diglett");
		names.add("Tauros");
		names.add("Farfetch'd");
		names.add("Venonat");
		names.add("Dragonite");
		names.add("Doduo");
		names.add("Poliwag");
		names.add("Jynx");
		names.add("Moltres");
		names.add("Articuno");
		names.add("Zapdos");
		names.add("Ditto");
		names.add("Meowth");
		names.add("Krabby");
		names.add("Vulpix");
		names.add("Ninetales");
		names.add("Pikachu");
		names.add("Raichu");
		names.add("Dratini");
		names.add("Dragonair");
		names.add("Kabuto");
		names.add("Kabutops");
		names.add("Horsea");
		names.add("Seadra");
		names.add("Sandshrew");
		names.add("Sandslash");
		names.add("Omanyte");
		names.add("Omastar");
		names.add("Jigglypuff");
		names.add("Wigglytuff");
		names.add("Eevee");
		names.add("Flareon");
		names.add("Jolteon");
		names.add("Vaporeon");
		names.add("Machop");
		names.add("Zubat");
		names.add("Ekans");
		names.add("Paras");
		names.add("Poliwhirl");
		names.add("Poliwrath");
		names.add("Weedle");
		names.add("Kakuna");
		names.add("Beedrill");
		names.add("Dodrio");
		names.add("Primeape");
		names.add("Dugtrio");
		names.add("Venomoth");
		names.add("Dewgong");
		names.add("Caterpie");
		names.add("Metapod");
		names.add("Butterfree");
		names.add("Machamp");
		names.add("Golduck");
		names.add("Hypno");
		names.add("Golbat");
		names.add("Mewtwo");
		names.add("Snorlax");
		names.add("Magikarp");
		names.add("Muk");
		names.add("Kingler");
		names.add("Cloyster");
		names.add("Electrode");
		names.add("Clefable");
		names.add("Weezing");
		names.add("Persian");
		names.add("Marowak");
		names.add("Haunter");
		names.add("Abra");
		names.add("Alakazam");
		names.add("Pidgeotto");
		names.add("Pidgeot");
		names.add("Starmie");
		names.add("Bulbasaur");
		names.add("Venusaur");
		names.add("Tentacruel");
		names.add("Goldeen");
		names.add("Seaking");
		names.add("Ponyta");
		names.add("Rapidash");
		names.add("Rattata");
		names.add("Raticate");
		names.add("Nidorino");
		names.add("Nidorina");
		names.add("Geodude");
		names.add("Porygon");
		names.add("Aerodactyl");
		names.add("Magnemite");
		names.add("Charmander");
		names.add("Squirtle");
		names.add("Charmeleon");
		names.add("Wartortle");
		names.add("Charizard");
		names.add("Oddish");
		names.add("Gloom");
		names.add("Vileplume");
		names.add("Bellsprout");
		names.add("Weepinbell");
		names.add("Victreebel");
		return names;
	}
	
	//returns a list of all the pokemon's indexes
	private ArrayList<Integer> getPokemonIndexes(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		indexes.add(0x1);
		indexes.add(0x2);
		indexes.add(0x3);
		indexes.add(0x4);
		indexes.add(0x5);
		indexes.add(0x6);
		indexes.add(0x7);
		indexes.add(0x8);
		indexes.add(0x9);
		indexes.add(0x0A);
		indexes.add(0x0B);
		indexes.add(0x0C);
		indexes.add(0x0D);
		indexes.add(0x0E);
		indexes.add(0x0F);
		indexes.add(0x10);
		indexes.add(0x11);
		indexes.add(0x12);
		indexes.add(0x13);
		indexes.add(0x14);
		indexes.add(0x15);
		indexes.add(0x16);
		indexes.add(0x17);
		indexes.add(0x18);
		indexes.add(0x19);
		indexes.add(0x1A);
		indexes.add(0x1B);
		indexes.add(0x1C);
		indexes.add(0x1D);
		indexes.add(0x1E);
		indexes.add(0x21);
		indexes.add(0x22);
		indexes.add(0x23);
		indexes.add(0x24);
		indexes.add(0x25);
		indexes.add(0x26);
		indexes.add(0x27);
		indexes.add(0x28);
		indexes.add(0x29);
		indexes.add(0x2A);
		indexes.add(0x2B);
		indexes.add(0x2C);
		indexes.add(0x2D);
		indexes.add(0x2E);
		indexes.add(0x2F);
		indexes.add(0x30);
		indexes.add(0x31);
		indexes.add(0x33);
		indexes.add(0x35);
		indexes.add(0x36);
		indexes.add(0x37);
		indexes.add(0x39);
		indexes.add(0x3A);
		indexes.add(0x3B);
		indexes.add(0x3C);
		indexes.add(0x40);
		indexes.add(0x41);
		indexes.add(0x42);
		indexes.add(0x46);
		indexes.add(0x47);
		indexes.add(0x48);
		indexes.add(0x49);
		indexes.add(0x4A);
		indexes.add(0x4B);
		indexes.add(0x4C);
		indexes.add(0x4D);
		indexes.add(0x4E);
		indexes.add(0x52);
		indexes.add(0x53);
		indexes.add(0x54);
		indexes.add(0x55);
		indexes.add(0x58);
		indexes.add(0x59);
		indexes.add(0x5A);
		indexes.add(0x5B);
		indexes.add(0x5C);
		indexes.add(0x5D);
		indexes.add(0x60);
		indexes.add(0x61);
		indexes.add(0x62);
		indexes.add(0x63);
		indexes.add(0x64);
		indexes.add(0x65);
		indexes.add(0x66);
		indexes.add(0x67);
		indexes.add(0x68);
		indexes.add(0x69);
		indexes.add(0x6A);
		indexes.add(0x6B);
		indexes.add(0x6C);
		indexes.add(0x6D);
		indexes.add(0x6E);
		indexes.add(0x6F);
		indexes.add(0x70);
		indexes.add(0x71);
		indexes.add(0x72);
		indexes.add(0x74);
		indexes.add(0x75);
		indexes.add(0x76);
		indexes.add(0x77);
		indexes.add(0x78);
		indexes.add(0x7B);
		indexes.add(0x7C);
		indexes.add(0x7D);
		indexes.add(0x7E);
		indexes.add(0x80);
		indexes.add(0x81);
		indexes.add(0x82);
		indexes.add(0x83);
		indexes.add(0x84);
		indexes.add(0x85);
		indexes.add(0x88);
		indexes.add(0x8A);
		indexes.add(0x8B);
		indexes.add(0x8D);
		indexes.add(0x8E);
		indexes.add(0x8F);
		indexes.add(0x90);
		indexes.add(0x91);
		indexes.add(0x93);
		indexes.add(0x94);
		indexes.add(0x95);
		indexes.add(0x96);
		indexes.add(0x97);
		indexes.add(0x98);
		indexes.add(0x99);
		indexes.add(0x9A);
		indexes.add(0x9B);
		indexes.add(0x9D);
		indexes.add(0x9E);
		indexes.add(0xA3);
		indexes.add(0xA4);
		indexes.add(0xA5);
		indexes.add(0xA6);
		indexes.add(0xA7);
		indexes.add(0xA8);
		indexes.add(0xA9);
		indexes.add(0xAA);
		indexes.add(0xAB);
		indexes.add(0xAD);
		indexes.add(0xB0);
		indexes.add(0xB1);
		indexes.add(0xB2);
		indexes.add(0xB3);
		indexes.add(0xB4);
		indexes.add(0xB9);
		indexes.add(0xBA);
		indexes.add(0xBB);
		indexes.add(0xBC);
		indexes.add(0xBD);
		indexes.add(0xBE);
		return indexes;
	}		
}
