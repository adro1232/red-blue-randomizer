//
//  AppDelegate.h
//  RedBlueRandomizer
//
//  Created by Trey on 8/15/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "RedBlueRandomizer.h"

@interface AppDelegate : NSObject <NSApplicationDelegate, NSAlertDelegate>

@property RedBlueRandomizer *randomizer;

@property (assign) IBOutlet NSWindow *window;
@property IBOutlet NSButtonCell *buttonSelectROM;
@property IBOutlet NSTextField *textFieldSelectedROM;
@property IBOutlet NSButton *checkBoxTitleScreenPkmn;
@property IBOutlet NSButton *checkBoxTrainerPkmn;
@property IBOutlet NSButton *checkBoxWildPokemon;
@property IBOutlet NSButton *checkBoxStarterPkmn;
@property IBOutlet NSButtonCell *radioTotallyRandom;
@property IBOutlet NSButtonCell *radioOneToOne;
@property IBOutlet NSButton *checkBoxNoLegendaries;
@property IBOutlet NSButton *buttonRandomize;

- (IBAction)buttonSelectROMClick:(id)sender;


@end
