//
//  RedBlueRandomizer.h
//  RedBlueRandomizer
//
//  Created by Trey on 8/16/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RedBlueRandomizer : NSObject

//constants
extern NSString * const redRomName;
extern NSString * const blueRomName;

//options
@property BOOL titleScreenToggle;
@property BOOL playerStartersToggle;
@property BOOL wildAreasToggle;
@property BOOL trainersToggle;
@property BOOL gymLeadersToggle;
@property BOOL eliteFourToggle;
@property BOOL rivalToggle;
@property BOOL oneToOneToggle;
@property BOOL noLegendaryToggle;

-(void)randomize;

-(NSUInteger)getRandomLevel;
-(NSUInteger)getRandomPokemonIndex;
-(NSUInteger)getReplacementForIndex:(NSUInteger) oldIndex;
-(NSUInteger)randomizeRegularTrainerAtOffset:(NSUInteger) offset;
-(NSUInteger)randomizeSpecialTrainerAtOffset:(NSUInteger) offset;

-(void)readRomAtURL:(NSURL *) fileURL;
-(void)saveRomAtURL:(NSURL *) fileURL;
-(BOOL)isPokemonRedBlue;

-(NSMutableArray*)getLegendaries;
-(NSMutableArray*)getPokemonNames;
-(NSMutableArray*)getPokemonIndexes;
-(NSDictionary*)getOneToOneMap;
-(BOOL)isLegendaryPokemonIndex:(NSUInteger) pokemonIndex;

@end
