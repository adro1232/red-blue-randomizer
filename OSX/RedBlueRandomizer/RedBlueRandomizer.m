//
//  RedBlueRandomizer.m
//  RedBlueRandomizer
//
//  Created by Trey on 8/16/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "RedBlueRandomizer.h"

@implementation RedBlueRandomizer

//options
@synthesize titleScreenToggle;
@synthesize playerStartersToggle;
@synthesize wildAreasToggle;
@synthesize trainersToggle;
@synthesize gymLeadersToggle;
@synthesize eliteFourToggle;
@synthesize rivalToggle;
@synthesize oneToOneToggle;
@synthesize noLegendaryToggle;

//ROM
Byte *rom;
NSUInteger romLength;

//constants
NSString * const redRomName = @"POKEMON RED";
NSString * const blueRomName = @"POKEMON BLUE";

//pokemon
NSUInteger const pokemonIndexes[] = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,
                                     0x0B,0x0C,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x14,
                                     0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,
                                     0x1F,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,
                                     0x2A,0x2B,0x2C,0x2D,0x2E,0x2F,0x30,0x31,0x33,0x35,
                                     0x36,0x37,0x39,0x3A,0x3B,0x3C,0x40,0x41,0x42,0x46,
                                     0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x52,0x53,
                                     0x54,0x55,0x58,0x59,0x5A,0x5B,0x5C,0x5D,0x60,0x61,
                                     0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x6B,
                                     0x6C,0x6D,0x6E,0x6F,0x70,0x71,0x72,0x74,0x75,0x76,
                                     0x77,0x78,0x7B,0x7C,0x7D,0x7E,0x80,0x81,0x82,0x83,
                                     0x84,0x85,0x88,0x8A,0x8B,0x8D,0x8E,0x90,0x91,0x93,
                                     0x94,0x95,0x96,0x97,0x98,0x99,0x9A,0x9B,0x9D,0x9E,
                                     0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xAB,0xAD,
                                     0xB0,0xB1,0xB2,0xB3,0xB4,0xB9,0xBA,0xBB,0xBC,0xBD,
                                     0xBE,};
NSUInteger const pokemonIndexesLength = 151;
NSString const *pokemonNames[] = {@"Rhydon"   ,@"Kangaskhan",@"Nidoran"   ,@"Clefairy"  ,@"Spearow"   ,@"Voltorb"   ,@"Nidoking"  ,@"Slowbro"   ,@"Ivysaur",
                                  @"Exeggutor",@"Lickitung" ,@"Exeggcute" ,@"Grimer"    ,@"Gengar"    ,@"Nidoran"   ,@"Nidoqueen" ,@"Cubone"    ,@"Rhyhorn",
                                  @"Lapras"   ,@"Arcanine"  ,@"Mew"       ,@"Gyarados"  ,@"Shellder"  ,@"Tentacool" ,@"Gastly"    ,@"Scyther"   ,@"Staryu",
                                  @"Blastoise",@"Pinsir"    ,@"Tangela"   ,@"Normal"    ,@"Growlithe" ,@"Onix"      ,@"Fearow"    ,@"Pidgey"    ,@"Slowpoke",
                                  @"Kadabra"  ,@"Graveler"  ,@"Chansey"   ,@"Machoke"   ,@"Mr. Mime"  ,@"Hitmonlee" ,@"Hitmonchan",@"Arbok"     ,@"Parasect",
                                  @"Psyduck"  ,@"Drowzee"   ,@"Golem"     ,@"Magmar"    ,@"Electabuzz",@"Magneton"  ,@"Koffing"   ,@"Mankey"    ,@"Seel",
                                  @"Diglett"  ,@"Tauros"    ,@"Farfetch'd",@"Venonat"   ,@"Dragonite" ,@"Doduo"     ,@"Poliwag"   ,@"Jynx"      ,@"Moltres",
                                  @"Articuno" ,@"Zapdos"    ,@"Ditto"     ,@"Meowth"    ,@"Krabby"    ,@"Vulpix"    ,@"Ninetales" ,@"Pikachu"   ,@"Raichu",
                                  @"Dratini"  ,@"Dragonair" ,@"Kabuto"    ,@"Kabutops"  ,@"Horsea"    ,@"Seadra"    ,@"Sandshrew" ,@"Sandslash" ,@"Omanyte",
                                  @"Omastar"  ,@"Jigglypuff",@"Wigglytuff",@"Eevee"     ,@"Flareon"   ,@"Jolteon"   ,@"Vaporeon"  ,@"Machop"    ,@"Zubat",
                                  @"Ekans"    ,@"Paras"     ,@"Poliwhirl" ,@"Poliwrath" ,@"Weedle"    ,@"Kakuna"    ,@"Beedrill"  ,@"Dodrio"    ,@"Primeape",
                                  @"Dugtrio"  ,@"Venomoth"  ,@"Dewgong"   ,@"Caterpie"  ,@"Metapod"   ,@"Butterfree",@"Machamp"   ,@"Golduck"   ,@"Hypno",
                                  @"Golbat"   ,@"Mewtwo"    ,@"Snorlax"   ,@"Magikarp"  ,@"Muk"       ,@"Kingler"   ,@"Cloyster"  ,@"Electrode" ,@"Clefable",
                                  @"Persian"  ,@"Marowak"   ,@"Haunter"   ,@"Abra"      ,@"Alakazam"  ,@"Pidgeotto" ,@"Pidgeot"   ,@"Starmie"   ,@"Bulbasaur",
                                  @"Venusaur" ,@"Tentacruel",@"Goldeen"   ,@"Seaking"   ,@"Ponyta"    ,@"Rapidash"  ,@"Rattata"   ,@"Raticate"  ,@"Nidorino",
                                  @"Nidorina" ,@"Geodude"   ,@"Porygon"   ,@"Aerodactyl",@"Magnemite" ,@"Charmander",@"Squirtle"  ,@"Charmeleon",@"Wartortle",
                                  @"Charizard",@"Oddish"    ,@"Gloom"     ,@"Vileplume" ,@"Bellsprout",@"Weepinbell",@"Victreebel"};
NSUInteger const pokemonNamesLength = 151;
NSUInteger const legendaryIndexes[] = {0x49,0x4A,0x4B,0x83,0x15};
NSUInteger const legendaryIndexesLength = 5;
NSDictionary *swapMap;

//trainer names
NSString const *gymLeaderNames[] = {@"Brock", @"Misty", @"Lt. Surge", @"Erika", @"Koga", @"Sabrina", @"Blaine", @"Giovanni"};
NSUInteger const gymLeaderNamesLength = 8;
NSString const *elite4Names[] = {@"Lorelei", @"Bruno", @"Agatha", @"Lance"};
NSUInteger const elite4NamesLength = 4;

//offsets
NSUInteger const romNameStart = 0x134;
NSUInteger const romNameEnd = 0x144;
NSUInteger const playerStarters[] = {0x1D10E, 0x1D11F, 0x1D130};
NSUInteger const playerStartersLength = 3;
NSUInteger const titleScreenPokemon[] = {0x4399,0x4588,0x4589,0x458A,0x458B,0x458C,0x458D,0x458E,0x458F,0x4590,
                                         0x4591,0x4592,0x4593,0x4594,0x4595,0x4596,0x4597};
NSUInteger const titleScreenPokemonLength = 27;
NSUInteger const areaOffsets[] = {0xD0E0,0xD0F6,0xD10C,0xD122,0xD138,0xD14E,0xD164,0xD17A,0xD190,0xD1A6,
                                  0xD1BC,0xD1D2,0xD1E8,0xD1FE,0xD214,0xD22A,0xD240,0xD256,0xD26C,0xD282,
                                  0xD298,0xD2B2,0xD2C8,0xD2DE,0xD2F4,0xD30A,0xD320,0xD336,0xD34C,0xD362,
                                  0xD378,0xD38E,0xD3A4,0xD3BA,0xD3D0,0xD3E6,0xD3FD,0xD412,0xD428,0xD43E,
                                  0xD454,0xD46A,0xD480,0xD496,0xD4AC,0xD4C2,0xD4D8,0xD4ED,0xD502,0xD518,
                                  0xD52E,0xD544,0xD55A,0xD570,0xD586,0xD59C,0xD5B2};
NSUInteger const areaOffsetsLength = 57;
NSUInteger const trainerPokemonStart = 0x39DCD;
NSUInteger const trainerPokemonEnd = 0x3A52D;
NSUInteger const gymLeaders[] = {0x3A3B6,0x3A3BC,0x3A3C2,0x3A3CA,0x3A3D2,0x3A3E6,0x3A3DC,0x3A291};
NSUInteger const gymeLeadersLength = 8;
NSUInteger const eliteFour[] = {0x3A4BC, 0x3A3AA, 0x3A517, 0x3A523};
NSUInteger const eliteFourLength = 4;

/**************************************/
//Initializer
/**************************************/

-(id)init
{
    if (self = [super init])
    {               
        //set options
        [self setTitleScreenToggle:NO];
        [self setPlayerStartersToggle:NO];
        [self setWildAreasToggle:NO];
        [self setTrainersToggle:NO];
        [self setGymLeadersToggle:NO];
        [self setEliteFourToggle:NO];
        [self setRivalToggle:NO];
        [self setOneToOneToggle:NO];
        [self setNoLegendaryToggle:NO];
    }
    return self;
}

/**************************************/
//Randomizer
/**************************************/

//performs the randomization (duh...)
-(void)randomize{
    //setup
    swapMap = [self getOneToOneMap];
    
    NSUInteger offset;
    //intro pokemon
    if(titleScreenToggle){
        for(int i=0; i<titleScreenPokemonLength; i++){
            offset = titleScreenPokemon[i];
            if(oneToOneToggle){
                rom[offset] = [self getReplacementForIndex:rom[offset]];
            }
            else{
                rom[offset] = [self getRandomPokemonIndex];
            }
        }
    }
    //player starters
    if(playerStartersToggle){
        for(int i=0; i<playerStartersLength; i++){
            offset = playerStarters[i];
            if(oneToOneToggle){
                rom[offset] = [self getReplacementForIndex:rom[offset]];
            }
            else{
                rom[offset] = [self getRandomPokemonIndex];
            }
        }
    }
    //wild pokemon areas
    if(wildAreasToggle){
        for(int i=0; i<areaOffsetsLength; i++){
            for(int j = 0; j < 20; j+=2){
                offset = areaOffsets[i];
                if(oneToOneToggle){
                    rom[offset + j + 1] = [self getReplacementForIndex:rom[offset + j + 1]];
                }
                else{
                    rom[offset + j + 1] = [self getRandomPokemonIndex];
                }
            }
        }
    }
    //pokemon trainers
    if(trainersToggle){
        NSUInteger i = trainerPokemonStart;
        while(i < trainerPokemonEnd){
            if(rom[i] == 0x0 && rom[i+1] != 0xFF){
                i = [self randomizeRegularTrainerAtOffset:i];
            }
            else{
                i = [self randomizeSpecialTrainerAtOffset:i];
            }
        }
    }
}

/**************************************/
//Misc. Randomizer Support Methods
/**************************************/

//returns a random level between 1-100
-(NSUInteger)getRandomLevel{
    return arc4random_uniform(100);
}

//returns a random pokemon index
-(NSUInteger)getRandomPokemonIndex{
    NSUInteger randomIndex;
    if(noLegendaryToggle){
        NSUInteger randomPokemon;
        while(true){
            randomIndex = arc4random_uniform(pokemonIndexesLength);
            randomPokemon = pokemonIndexes[randomIndex];
            if(![self isLegendaryPokemonIndex:randomPokemon]){
                return randomPokemon;
            }
        }
    }
    else{
        randomIndex = arc4random_uniform(pokemonIndexesLength);
        return pokemonIndexes[randomIndex];
    }
}

//gets the replacement for a pokemon using the swap map generated
-(NSUInteger)getReplacementForIndex:(NSUInteger) oldIndex{
    return [[swapMap objectForKey:[NSNumber numberWithUnsignedInteger:oldIndex]] unsignedIntegerValue];
}

//randomizes a regular trainer
-(NSUInteger)randomizeRegularTrainerAtOffset:(NSUInteger) offset{
    offset += 2;
    BOOL loop = YES;
    while(loop){
        if(rom[offset] == 0x0){
            loop = NO;
            break;
        }
        else{
            if(oneToOneToggle){
                rom[offset] = [self getReplacementForIndex:rom[offset]];
            }
            else{
                rom[offset] = [self getRandomPokemonIndex];
            }
            offset++;
        }
    }
    return offset;
}

//randomize a special trainer
-(NSUInteger)randomizeSpecialTrainerAtOffset:(NSUInteger) offset{
    offset += 2;
    BOOL loop = YES;
    while(loop){
        if(rom[offset] == 0x0){
            loop = NO;
            break;
        }
        else{
            if(oneToOneToggle){
                rom[offset + 1] = [self getReplacementForIndex:rom[offset + 1]];
            }
            else{
                rom[offset + 1] = [self getRandomPokemonIndex];
            }
            offset += 2;
        }
    }
    return offset;
}

/**************************************/
//File I/O
/**************************************/

//reads in the ROM given a fileURL
-(void)readRomAtURL:(NSURL *) fileURL{
    //read in file
    NSData *fileData = [[NSData alloc] initWithContentsOfURL:fileURL];
    romLength = [fileData length];
    rom = (Byte*)malloc(romLength);
    memcpy(rom, [fileData bytes], romLength);
}

//saves the ROM to a specified fileURL
-(void)saveRomAtURL:(NSURL *) fileURL{
    NSData *fileData = [[NSData alloc] initWithBytes:rom length:romLength];
    [fileData writeToURL:fileURL atomically:YES];
}

//checks the ROM's name
-(BOOL)isPokemonRedBlue{
    NSMutableString *romName = [[NSMutableString alloc] init];
    for(int i=romNameStart; i<romNameEnd;i++){
        [romName appendString:[NSString stringWithFormat:@"%c", rom[i]]];
    }
    romName = [NSMutableString stringWithString: [romName stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
    
    if([romName isEqualToString:redRomName] || [romName isEqualToString:blueRomName]){
        return true;
    }
    else{
        return false;
    }
}

/**************************************/
//Generate Pokemon Lists
/**************************************/

//returns a list of all pokemon names
-(NSArray*)getPokemonNames{
    NSMutableArray *pokemonNamesList = [[NSMutableArray alloc] init];
    for(int i=0; i<pokemonNamesLength; i++){
        [pokemonNamesList addObject:pokemonNames[i]];
    }
    return pokemonNamesList;
}

//returns a list of all the pokemon's indexes
-(NSArray*)getPokemonIndexes{
    NSMutableArray *pokemonIndexesList = [[NSMutableArray alloc] init];
    for(int i=0; i<pokemonIndexesLength; i++){
        [pokemonIndexesList addObject:[NSNumber numberWithUnsignedInteger:pokemonIndexes[i]]];
    }
    return pokemonIndexesList;
}

//returns a list of the legendary pokemon's indexes
-(NSArray*)getLegendaries{
    NSMutableArray *legendariesList = [[NSMutableArray alloc] init];
    for(int i=0; i<legendaryIndexesLength; i++){
        [legendariesList addObject:[NSNumber numberWithUnsignedInteger:legendaryIndexes[i]]];
    }
    return legendariesList;
}

//creates a one-to-one randomization of the pokemon list
-(NSDictionary*)getOneToOneMap{
    NSMutableDictionary *swapMap = [[NSMutableDictionary alloc] init];
    NSMutableArray *pokemonIndexes = [self getPokemonIndexes];
    NSMutableArray *temp = [self getPokemonIndexes];
    if(noLegendaryToggle){
        for(int i=0; i<legendaryIndexesLength; i++){
            NSUInteger indexOfLegendary = [temp indexOfObject:[NSNumber numberWithUnsignedInteger:legendaryIndexes[i]]];
            [temp setObject:[NSNumber numberWithUnsignedInteger:[self getRandomPokemonIndex]] atIndexedSubscript:indexOfLegendary];
        }
    }
    
    NSUInteger newPokemonIndex;
    for(NSUInteger i = 0; i<pokemonIndexesLength; i++){
        NSUInteger oldPokemonIndex = [pokemonIndexes[i] unsignedIntegerValue];
        newPokemonIndex = [[temp objectAtIndex:arc4random_uniform([temp count])] unsignedIntegerValue];
        [swapMap setObject:[NSNumber numberWithUnsignedInteger:oldPokemonIndex] forKey:[NSNumber numberWithUnsignedInteger:newPokemonIndex]];
        [temp removeObjectAtIndex:[temp indexOfObject:[NSNumber numberWithUnsignedInteger:newPokemonIndex]]];
    }
    return swapMap;
}


//determines if a pokemon is legendary
-(BOOL)isLegendaryPokemonIndex:(NSUInteger) pokemonIndex{
    for(int i=0; i<legendaryIndexesLength; i++){
        if(pokemonIndex == legendaryIndexes[i]){
            return true;
        }
    }
    return false;
}

@end
