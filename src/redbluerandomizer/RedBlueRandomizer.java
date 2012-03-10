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
	Map<Integer, Integer> swapMap;
	private Random rand = new MersenneTwister(new Date().getTime());
	//private Random rand = new Random(new Date().getTime());
	
	//constants
	private final String redRomName = "POKEMON RED";
	private final String blueRomName =  "POKEMON BLUE";
	
	//offsets
	private final int romNameStart = 0x134;
	private final int romNameEnd = 0x144;
	private final int[] introPokemon = {0x4399,0x4588,0x4589,0x458A,0x458B,0x458C,0x458D,0x458E,0x458F,0x4590,0x4591,0x4592,0x4593,0x4594,0x4595,0x4596,0x4597};
	private final int[] startersOffsets = {0x3A1E5,0x3A1E8,0x3A1EB};	
	private final int[] areaOffsets = {53472,53494,53516,53538,53560,53582,53604,53626,53648,53670,53692,53714,53736,53758,53780,53802,53824,53846,53868,53890,53912,53938,53960,53982,54004,54026,54048,54070,54092,54114,54136,54158,54180,54202,54224,54246,54270,54290,54312,54334,54356,54378,54400,54422,54444,54466,54488,54510,54530,54552,54574,54596,54618,54640,54662,54684,54706,};
	private final int trainerPokemonStart = 0x39DCD;
	private final int trainerPokemonEnd = 0x3A1E3;
	private final int[] gymLeaders = {0x3A3B6,0x3A3BC,0x3A3C2,0x3A3CA,0x3A3D2,0x3A3DC,0x3A3E6,0x3A291};
	
	//options
	private boolean introToggle = false;
	private boolean startersToggle = false;
	private boolean wildAreasToggle = false;
	private boolean trainersToggle = false;
	private boolean gymLeadersToggle = false;
	private boolean oneToOneToggle = false;
	
	private byte[] rom;
	
	public RedBlueRandomizer(){
		pokemonNames = getPokemonNames();
		pokemonIndexes = getPokemonIndexes();
		swapMap = getOneToOneMap();
	}
	
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
	
	public void randomize(){	
		swapMap = getOneToOneMap();
		int offset;
		//intro pokemon
		if(introToggle){
			for(int i=0; i<introPokemon.length; i++){
				offset = introPokemon[i];
				if(oneToOneToggle){
					rom[offset] = getReplacement(rom[offset]);
				}
				else{
					rom[offset] = getRandomPokemonIndex();
				}
				
			}			
		}		
		//starters
		if(startersToggle){
			for(int i=0; i<startersOffsets.length; i++){
				offset = startersOffsets[i];
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
		//trainer pokemon
		if(trainersToggle){
			for(int i=trainerPokemonStart; i<trainerPokemonEnd; i++){
				if(rom[i] == 0x0){
					i++;
				}
				else{
					if(oneToOneToggle){
						rom[i] = getReplacement(rom[i]);
					}
					else{
						rom[i] = getRandomPokemonIndex();
					}
				}
			}
		}
		//gym leaders pokemon
		if(gymLeadersToggle){
			boolean loop = true;
			for(int i=0; i<gymLeaders.length; i++){
				offset = gymLeaders[i];			
				while(loop){
					if(rom[offset] == 0x0){
						break;
					}
					else{
						if(oneToOneToggle){
							rom[offset+1] = getReplacement(rom[offset+1]);
						}
						else{
							rom[offset+1] = getRandomPokemonIndex();
						}
						offset += 2;
					}
				}				
			}		
		}
	}
	
	public byte getRandomLevel(){
		return (byte)(rand.nextInt(100)+1);
	}
	
	public byte getRandomPokemonIndex(){
		int randomIndex = rand.nextInt(pokemonIndexes.size());
		return (byte)pokemonIndexes.get(randomIndex).intValue();
	}
	
	public String getPokemonName(int pokemonIndex){
		return pokemonNames.get(pokemonIndexes.indexOf(pokemonIndex));
	}
	
	public String getPokemonName(byte pokemonIndex){
		return getPokemonName(byteToInt(pokemonIndex));
	}
	
	public HashMap<Integer, Integer> getOneToOneMap(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> temp = getPokemonIndexes();
		Integer newIndex;
		for(Integer oldIndex: pokemonIndexes){
			newIndex = temp.get(rand.nextInt(temp.size()));
			map.put(oldIndex, newIndex);
			temp.remove(newIndex);
		}		
		return map;
	}
	
	public byte getReplacement(byte oldIndex){
		return (byte)swapMap.get(byteToInt(oldIndex)).intValue();
	}	
	
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
	
	public void closeStream(InputStream stream){
		try{
			stream.close();			
		}
		catch(Exception e){}
	}
	
	private int byteToInt(byte b){
		return b & 0xFF;
	}
	
	public void setIntroToggle(boolean toggle){
		this.introToggle = toggle;
	}
	public void setStartersToggle(boolean toggle){
		this.startersToggle = toggle;
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
	public void setOneToOneToggle(boolean toggle){
		this.oneToOneToggle = toggle;
	}
	
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
