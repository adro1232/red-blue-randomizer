package redbluerandomizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ec.util.MersenneTwister;

public class RedBlueRandomizer {	
		
	private Map<Integer, Integer> swapMap;
	private Random rand = new MersenneTwister(new Date().getTime());
	private byte[] rom;
	
	//constants
	public final String redRomName = "POKEMON RED";
	public final String blueRomName =  "POKEMON BLUE";
	public final int mew = 0x15;
	
	//lookups	
	public final String[] names = {"Rhydon"    ,"Kangaskhan","NidoranM"  ,"Clefairy"  ,"Spearow"  ,"Voltorb"   ,"Nidoking","Slowbro"   ,"Ivysaur"   ,"Exeggutor",
								   "Lickitung" ,"Exeggcute" ,"Grimer"    ,"Gengar"    ,"NidoranF" ,"Nidoqueen" ,"Cubone"  ,"Rhyhorn"   ,"Lapras"    ,"Arcanine",
								   "Mew"       ,"Gyarados"  ,"Shellder"  ,"Tentacool" ,"Gastly"   ,"Scyther"   ,"Staryu"  ,"Blastoise" ,"Pinsir"    ,"Tangela",
								   "Growlithe" ,"Onix"      ,"Fearow"    ,"Pidgey"    ,"Slowpoke" ,"Kadabra"   ,"Graveler","Chansey"   ,"Machoke"   ,"Mr. Mime",
								   "Hitmonlee" ,"Hitmonchan","Arbok"     ,"Parasect"  ,"Psyduck"  ,"Drowzee"   ,"Golem"   ,"Magmar"    ,"Electabuzz","Magneton",
								   "Koffing"   ,"Mankey"    ,"Seel"      ,"Diglett"   ,"Tauros"   ,"Farfetch'd","Venonat" ,"Dragonite" ,"Doduo"     ,"Poliwag",
								   "Jynx"      ,"Moltres"   ,"Articuno"  ,"Zapdos"    ,"Ditto"    ,"Meowth"    ,"Krabby"  ,"Vulpix"    ,"Ninetales" ,"Pikachu",
								   "Raichu"    ,"Dratini"   ,"Dragonair" ,"Kabuto"    ,"Kabutops" ,"Horsea"    ,"Seadra"  ,"Sandshrew" ,"Sandslash" ,"Omanyte",
								   "Omastar"   ,"Jigglypuff","Wigglytuff","Eevee"     ,"Flareon"  ,"Jolteon"   ,"Vaporeon","Machop"    ,"Zubat"     ,"Ekans",
								   "Paras"     ,"Poliwhirl" ,"Poliwrath" ,"Weedle"    ,"Kakuna"   ,"Beedrill"  ,"Dodrio"  ,"Primeape"  ,"Dugtrio"   ,"Venomoth",
								   "Dewgong"   ,"Caterpie"  ,"Metapod"   ,"Butterfree","Machamp"  ,"Golduck"   ,"Hypno"   ,"Golbat"    ,"Mewtwo"    ,"Snorlax",
								   "Magikarp"  ,"Muk"       ,"Kingler"   ,"Cloyster"  ,"Electrode","Clefable"  ,"Weezing" ,"Persian"   ,"Marowak"   ,"Haunter",
								   "Abra"      ,"Alakazam"  ,"Pidgeotto" ,"Pidgeot"   ,"Starmie"  ,"Bulbasaur" ,"Venusaur","Tentacruel","Goldeen"   ,"Seaking",
								   "Ponyta"    ,"Rapidash"  ,"Rattata"   ,"Raticate"  ,"Nidorino" ,"Nidorina"  ,"Geodude" ,"Porygon"   ,"Aerodactyl","Magnemite",
								   "Charmander","Squirtle"  ,"Charmeleon","Wartortle" ,"Charizard","Oddish"    ,"Gloom"   ,"Vileplume" ,"Bellsprout","Weepinbell",
								   "Victreebel"};
	public final int[] indices = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,
								  0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x14,
								  0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,
								  0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,
								  0x2B,0x2C,0x2D,0x2E,0x2F,0x30,0x31,0x33,0x35,0x36,
								  0x37,0x39,0x3A,0x3B,0x3C,0x40,0x41,0x42,0x46,0x47,
								  0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x52,0x53,0x54,
								  0x55,0x58,0x59,0x5A,0x5B,0x5C,0x5D,0x60,0x61,0x62,
								  0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x6B,0x6C,
								  0x6D,0x6E,0x6F,0x70,0x71,0x72,0x74,0x75,0x76,0x77,
								  0x78,0x7B,0x7C,0x7D,0x7E,0x80,0x81,0x82,0x83,0x84,
								  0x85,0x88,0x8A,0x8B,0x8D,0x8E,0x8F,0x90,0x91,0x93,
								  0x94,0x95,0x96,0x97,0x98,0x99,0x9A,0x9B,0x9D,0x9E,
								  0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xAB,0xAD,
								  0xB0,0xB1,0xB2,0xB3,0xB4,0xB9,0xBA,0xBB,0xBC,0xBD,
								  0xBE};
	public final int[] legendaryIndices = {0x49,0x4A,0x4B,0x83,0x15};
	
	//offsets
	public final int romNameStart = 0x134;
	public final int romNameEnd = 0x144;	
	public final int[] playerStarters = {0x1D10E, 0x1D11F, 0x1D130};
	public final int[] titleScreenPokemon = {0x4399,0x4588,0x4589,0x458A,0x458B,0x458C,0x458D,0x458E,0x458F,0x4590,
			                                 0x4591,0x4592,0x4593,0x4594,0x4595,0x4596,0x4597};	
	public final int[] areaOffsets = {0xD0E0,0xD0F6,0xD10C,0xD122,0xD138,0xD14E,0xD164,0xD17A,0xD190,0xD1A6,
									  0xD1BC,0xD1D2,0xD1E8,0xD1FE,0xD214,0xD22A,0xD240,0xD256,0xD26C,0xD282,
									  0xD298,0xD2B2,0xD2C8,0xD2DE,0xD2F4,0xD30A,0xD320,0xD336,0xD34C,0xD362,
									  0xD378,0xD38E,0xD3A4,0xD3BA,0xD3D0,0xD3E6,0xD3FD,0xD412,0xD428,0xD43E,
									  0xD454,0xD46A,0xD480,0xD496,0xD4AC,0xD4C2,0xD4D8,0xD4ED,0xD502,0xD518,
									  0xD52E,0xD544,0xD55A,0xD570,0xD586,0xD59C,0xD5B2};
	public final int trainerPokemonStart = 0x39DCD;
	public final int trainerPokemonEnd = 0x3A52D;
	
	//options
	private boolean titleScreenToggle    = false;
	private boolean playerStartersToggle = false;
	private boolean wildAreasToggle      = false;
	private boolean trainersToggle       = false;
	private boolean oneToOneToggle       = false;
	private boolean noLegendariesToggle  = false;	

	/*******************************************/
	// Randomize
	/*******************************************/
	
	//performs the randomization (duh...)
	public void randomize(){
		//setup
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
	
	/*******************************************/
	// Randomize Support Methods
	/*******************************************/
	
	//randomizes a regular trainer
	private int randomizeRegularTrainer(int offset){
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
	private int randomizeSpecialTrainer(int offset){	
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
	
	//returns a random pokemon index
	private byte getRandomPokemonIndex(){
		shuffle();		
		if(noLegendariesToggle){
			int randomIndex;
			int randomPokemon;
			while(true){
				randomIndex = rand.nextInt(indices.length);
				randomPokemon = indices[randomIndex];
				if(!isLegendaryPokemon(randomPokemon)){
					return (byte)randomPokemon;
				}
			}		
		}
		else{
			int randomIndex = rand.nextInt(indices.length);	
			return (byte)indices[randomIndex];
		}	
	}	
	
	//progresses the RNG a random number of times to add to the randomness
	private void shuffle(){
		int loop = rand.nextInt(10);		
		for(int i=0; i<loop; i++){
			rand.nextInt(indices.length);
		}
	}
	
	/*******************************************/
	// Lookup Methods
	/*******************************************/	
	
	//creates a one-to-one randomization of the pokemon list
	public HashMap<Integer, Integer> getOneToOneMap(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> temp = getPokemonIndexes();
		if(noLegendariesToggle){
			for(Integer legendaryIndex: legendaryIndices){
				int i = temp.indexOf(legendaryIndex);				
				temp.set(i, byteToInt(getRandomPokemonIndex()));				
			}
		}
		
		Integer newIndex;
		for(Integer oldIndex: indices){
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
	
	//determines if a given pokemon index belongs to a legendary
	public boolean isLegendaryPokemon(int index){
		for(int legendaryIndex: legendaryIndices){
			if(index == legendaryIndex){
				return true;
			}
		}
		return false;
	}
	
	//returns a list of all the pokemon's indexes
	public ArrayList<Integer> getPokemonIndexes(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int index: indices){
			indexes.add(index);			
		}
		return indexes;
	}		
	
	//returns a Map of all pokemon indexes and names
	public HashMap<Integer, String> getPokemonNameMap(){
		HashMap<Integer, String> nameMap = new HashMap<Integer, String>();
		for(int i=0; i<indices.length;i++){
			nameMap.put(indices[i], names[i]);
		}
		return nameMap;
	}
	
	/*******************************************/
	// File I/O
	/*******************************************/	
	
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
	
	/*******************************************/
	// Misc.
	/*******************************************/
	
	//converts a byte to an int
	private int byteToInt(byte b){
		return b & 0xFF;
	}

	/*******************************************/
	// Setters/Getters
	/*******************************************/
	
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
	public void setOneToOneToggle(boolean toggle){
		this.oneToOneToggle = toggle;
	}
	public void setNoLegendariesToggle(boolean toggle){
		this.noLegendariesToggle = toggle;
	}
	
	public byte[] getRom(){
		return rom;
	}
}
