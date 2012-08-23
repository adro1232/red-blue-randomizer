//
//  AppDelegate.m
//  RedBlueRandomizer
//
//  Created by Trey on 8/15/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "AppDelegate.h"

@implementation AppDelegate

@synthesize randomizer;

@synthesize window = _window;
@synthesize buttonSelectROM = _buttonSelectROM;
@synthesize textFieldSelectedROM = _textFieldSelectedROM;
@synthesize checkBoxTitleScreenPkmn = _checkBoxTitleScreenPkmn;
@synthesize checkBoxTrainerPkmn = _checkBoxTrainerPkmn;
@synthesize checkBoxWildPokemon = _checkBoxWildPokemon;
@synthesize checkBoxStarterPkmn = _checkBoxStarterPkmn;
@synthesize radioTotallyRandom = _radioTotallyRandom;
@synthesize radioOneToOne = _radioOneToOne;
@synthesize checkBoxNoLegendaries = _checkBoxNoLegendaries;
@synthesize buttonRandomize = _buttonRandomize;

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
    //initialize randomzer
    randomizer = [[RedBlueRandomizer alloc] init];
}

/**********************************/
//Button Click Handlers
/**********************************/

//Select ROM Button
- (IBAction)buttonSelectROMClick:(id)sender {
    //create and configure panel
    NSOpenPanel *panel = [NSOpenPanel openPanel];
    [panel setCanChooseDirectories:NO];
    [panel setCanCreateDirectories:NO];
    [panel setAllowsMultipleSelection:NO];
    [panel setCanHide:NO];    
    [panel setCanChooseFiles:YES];
    [panel setAllowedFileTypes:[[NSArray alloc] initWithObjects:@"gb", nil]];
        
    //display open panel
    if([panel runModal] == NSFileHandlingPanelOKButton){
        //read selected file
        NSURL *fileURL = [panel URL];
        [randomizer readRomAtURL:fileURL];
        
        //check if valid ROM
        if([randomizer isPokemonRedBlue]){
            //update UI
            [self.textFieldSelectedROM setStringValue:[[fileURL path] lastPathComponent]];
            [self.buttonRandomize setEnabled:YES];
        }
        else{
            //alert user of invalid ROM
            NSAlert *alert = [[NSAlert alloc] init];
            [alert setAlertStyle:NSWarningAlertStyle];
            [alert setMessageText:@"This ROM does not appear to be Pokemon Red or Blue..."];
            [alert setIcon:[NSImage imageNamed:@"MissingnoIcon.png"]];
            [alert runModal];
        }
    }
}

//Randomize Button
- (IBAction)buttonRandomizeClick:(id)sender {
    //create and configure panel
    NSSavePanel *panel = [NSSavePanel savePanel];
    [panel setCanCreateDirectories:NO];
    [panel setCanHide:NO];
    [panel setAllowedFileTypes:[[NSArray alloc] initWithObjects:@"gb", nil]];
    
    //display save panel
    if([panel runModal] == NSFileHandlingPanelOKButton){
        //set toggles
        if([[self checkBoxTitleScreenPkmn] state] == NSOnState){
            [randomizer setTitleScreenToggle:YES];
        }
        if([[self checkBoxTrainerPkmn] state] == NSOnState){
            [randomizer setTrainersToggle:YES];
        }
        if([[self checkBoxWildPokemon] state] == NSOnState){
            [randomizer setWildAreasToggle:YES];
        }
        if([[self checkBoxStarterPkmn] state] == NSOnState){
            [randomizer setPlayerStartersToggle:YES];
        }
        if([[self radioOneToOne] state] == NSOnState){
            [randomizer setOneToOneToggle:YES];
        }
        if([[self checkBoxNoLegendaries] state] == NSOnState){
            [randomizer setNoLegendaryToggle:YES];
        }
    
        //perform randomization
        [randomizer randomize];
            
        //save file
        NSURL *fileURL = [panel URL];
        [randomizer saveRomAtURL:fileURL];
        
        //play sound
        NSSound *player = [[NSSound alloc] initWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"sound" ofType:@"mp3"] byReference:NO];
        [player play];
        
        //alert user of success
        NSAlert *alert = [[NSAlert alloc] init];
        [alert setAlertStyle:NSWarningAlertStyle];
        [alert setMessageText:@"The ROM has been successfully randomized!"];
        [alert setIcon:[NSImage imageNamed:@"MissingnoIcon.png"]];
        [alert runModal];
    }
}

/**********************************/
//Close App When Red X Clicked
/**********************************/
- (BOOL)applicationShouldTerminateAfterLastWindowClosed:(NSApplication *)sender
{
    return YES;
}


@end
