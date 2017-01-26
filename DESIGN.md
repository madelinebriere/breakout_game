CompSci 308: Game Design
===================
##Author: Maddie Briere
##Date: January 25, 2017

> This is the link to the assignment: [Game](http://www.cs.duke.edu/courses/compsci308/current/assign/01_game/)

High Level Design Goals
=======
This is our first assignment, so I knew that it would be unreasonable of me to expect to make a perfect design. Instead, I formulated a few important goals at the start of the project.

1) Looking at this from a game-playing/content perspective, this program should launch an intuitive and entertaining mini-game, a variation of the classic game Breakout. This program should support the following requirements:
* Levels: This game should have levels, and should handle movement from these levels as part of the game flow. 
* Blocks: This game should have at least three different kinds of blocks. 
* Paddle: The paddle should have "abilities" in and of itself (at least three).
* Power-ups: Power-ups should be dispensed, giving the player special powers.
* Status display: The player should be able to see their score, level and number of lives at any time.
* Splash screen: An informational screen should appear at the launch of the program.
* Cheat keys: The player should be able to activate certain "cheats" via the keyboard
* Something extra: For me, "villains" falling from the top of the screen

All of these elements must be incorporated in a logical and cohesive manner. This will require taking in keyboard input from the user and processing it, as well as performing constant position and status updates. Each element on the screen will need to have some sort of "memory," so that these updates can be made. This program will also have to be flexible enough to wait to launch until after the informational page has been exited -- requiring an outside launching class.

2) **The project should have a clear and logical hierarchy of classes. It should take advantage of Java's inheritance to cut down on duplicate code and make the program more readable.**
This was accomplished via the GamePiece class, which is the superclass for Block, Cat, Ball, Paddle and Powerup. It was also, in the refactoring of my code, incorporated into the PopupMenu class, which defines the characteristics and capabilities of a general popup class, with its subclasses StartPopupMenu and InfoPopupMenu defining the content of the popup.

3) **All functions/characteristics should be in the "right" class (e.g., bounceBall() in Ball). If the function and/or characteristics do not seem to have a place, create a new class.** This was meant to limit dependency between classes and assign each class one, cohesive purpose.This reasoning resulted in the Runner class, which performs the high-level launch decisions, InfoBox, which displays the game information, and BlockReader, which reads in the block files. 

4) **The code should be readable.** That is:
* All functions should be consistently named, and should indicate the purpose of the method. 
* All variable names should convey a clear role. 
* Methods should be as concise as possible -- if anything requires a comment, move it to a new method and name it accordingly. 

Classes like PopupMenu, for instance, were able to achieve this standard (or at least my concept of this standard).

5) **The command flow of the method should be very clear to any reader.** Runner launches the starting popups, which pause, wait for user input, and then return command to Runner. Runner then initializes the BreakoutWorld object, sets the stage to "visible" and begins the gameloop, relinquishing control for the rest of the program.


Adding New Features
=======
Adding a new feature in my program takes a bit of finesse, but is certainly not out of reach. One common requirement across all of the features is that it must be added to the informational text files. If it's a new rule, it must go in the instructions file. If it's a new powerup, it must be described in the pieces file. And so on. Here are some specific instructions:

1) **Powerups.** To add a new powerup to the game, you must create a new image and name it reasonably based on what the powerup does. You then store this as a .gif file in the images folder. In Powerup, you must add this file name to the array POWERUPS, at an index corresponding to a valid level, so that it can be added to the Block ArrayList. Then, all that's left to do is implement the actual result of the powerup. This is done in BreakoutWorld, in the method "applyPowerup()." Simply add an extra check, for the type corresponding to your new powerup, and have this divert the program to a new method which applies the changes of the powerup. For instance, the "lives" powerup is applied with the line myLives++. 

2) **Cheat keys.** To add a new cheat key, decide first whether this cheat key will be a level jump (e.g., press the number 5, jump to level 5) or a letter cheat key (e.g., press N and start a new game). Go to either checkLetterCheats() or checkLevelCheats(), depending upon your decision, and add a new if statement for your key. Then write a method to perform the action you desire, and make a call to it in the if-block. 

3) **Characters.** A new character can be added by creating a new class that extends GamePiece. This new class will automatically have access to myImage and myType
(through getters and setters). Hence, you must choose the image you want, add it to the images folder, and then add its name as a String constant in your new class. This can then be passed to the super constructor for GamePiece. All that's left to do is code update(), which will apply changes to your new character during the game loop. In order to actually create instances of this character in your game, you will have to add a method that produces a starting instance or list of instances (such as buildStartBall() in Ball). This must be called in setStartLists() in BreakoutWorld, and passed to the addPiece() or addPieces() method. Now, your instance(s) will be included in the GameManager's list of GamePieces. Any further design changes can either be added in your new class, if they are unique to your character, or added in BreakoutWorld, if they involve an interaction between your character and some other element.

4) **Blocks.** A new Block can be created by creating a new class that extends Block. Then, you must set the hits and points accordingly (within its constructor). Code the isDestroyed() method to check if your block is destroyed (depending on how you want this decision to be made). Code the takeHit() method to define what will happen when this block is hit by the ball. Now that this instance has been properly defined, you must incorporate in into the blocks_.txt files as you wish (placing ___.gif where you want your blocks to show up, by row and column). You must then tell the readBlocks() method in BlockReader how to handle this input, in the same manner as the other block files. This requires a quick if statement and constructor call. Now you're good to go!

5) **Paddle abilities.** Paddle abilities can be implemented in the Paddle class. Both the portal and momentum capabilities are called in the moveRight() and moveLeft() methods, because they pertain to the movement of the paddle. The location-based ball bounce is, at least in this version of code, considered an attribute of Ball, and is coded in the bounceOffPaddle() method in Ball. You can add your new paddle ability in either location, or in any of the other GamePiece classes as you see fit. This type of addition is very case-by-case.

6) **New levels.** This topic is covered extensively in the Analysis document. Here is an excerpt:

*Adding a level requires the following steps:*
1. Add a new text file labeled blocks[levelnumber].txt
2. Type in the desired blocks by .gif file, recognizing that only six blocks will fit in one row. If no block is desired, type anything that isn't a block file (e.g., None). Each new line will start a new row.
3. Add an additional powerup to the array POWERUPS in Powerup
4. Add in a cheat key for that level digit in the same way as other levels (in handleKeyInput)
5. Change LAST_LEVEL in Breakout to the desired level
6. Add a constant Color object in BreakoutWorld labeled COLOR_[level]

7) **Miscellaneous.** If your new ability does not fit into any of these categories, fear not! Because of the design of this program, you can pretty intuitively figure out where to make changes. For instance, if you want to alter the way the Ball moves, you would change the motion methods in the Ball class. If you wanted to change the way collisions are handled, you would refer to the handleCollisions() method in BreakoutWorld. If you wanted to change the way a level is lost, you would change the checkLevel() method. And so on, ad nauseum. 

Major Design Choices
=======
1) One major design choice made in this program was to have much of the game-specific information read in from text-files rather than hard-coded. This does present a bit of trouble for the user in that they must provide all of the necessary files to proceed, or alter the code. Furthermore, each file must be formatted in a specific way (which is described in each corresponding read-in class) so that the information can be parsed correctly. However, this, in the long run, in much easier for the user than having to change the actual code every time they want to alter the instructions or add a label. As long as the specifications are properly conveyed to the user, which I believe them to be, this is the better choice in the long run.

2) Another major design choice was to use an EventHandler to deal with all updates during animation rather than the step function, with the elapsedTime parameter, as discussed in class. While the latter was certainly familiar and well-documented, it also presented problems when it came to refactoring. Certain lines of code that used the elapsedTime parameter could not be moved from the method, and elapsedTime could not be passed as a parameter to another method (as this threw an elapsedTime error). After struggling with this for a while, I decided to emulate the design choice made in Carl Dea's [Game Tutorial](https://carlfx.wordpress.com/2012/03/29/javafx-2-gametutorial-part-1/) and use an EventHandler, with a customized handle() method. This allowed for extensive refactoring and did away with the elapsedTime parameter that had been causing me so much trouble. The only con of switching to the EventHandler was that it took some time to figure out -- Dea's tutorial was written using an earlier version of JavaFX, so certain lines had to be altered to avoid errors.

3) I decided to have all game pieces inherit from a common superclass, GamePiece. This design decision was made in order to take advantage of inheritance in Java, and also make the structure of my program more transparent. This allowed me to cut down on a lot of duplicate code and ultimately facilitated a concise update method in BreakoutWorld, as all instances of a class extending GamePiece would call their respective update() methods, allowing me to pass in a List of GamePieces and call update() on each. This did have some unwanted results, however. For instance, the Block class does not use its update method at all (the method is blank). Hence, some of the relationships were a little rough. On the whole, however, the convenience of this hierarchy structure outweighed the negatives of a few awkward elements.

Assumptions
=======
1) Following the general timeline of this game, the first assumption within the code appears with the start popup menus. These menus are created with the assumption that the user has supplied the following documents:

___info.txt (description of levels/cheat keys)

___instructions.txt (how to play)

___pieces.txt (describing the game pieces)

___message.txt (opening message)

Where the blank is filled in with the prefix specific to that game (for my version of Breakout, this prefix is fido). This assumption is made to eliminate the necessity of hardcoding every Label, TextArea and Button into the system. It requires a bit more of the user, but ultimately allows more flexibility.

2) The next assumption made was that all GamePieces would be identified by ImageViews (instead of Nodes, higher up the abstraction chain. In all honesty, this was likely not the best design choice, in that it limits choices for new game pieces. However, by the time I identified this assumption, it was already deeply embedded in the game design. Extensive changes would have been required to change the ImageView variable to a Node variable. Hence, all of the GamePieces (Ball, Paddle, Cat, Block) are defined by their ImageViews. This meant that InfoBox, which could have otherwise been incorporated into the GamePiece design, had to be its own separate entity. This assumption limits the amount of change that can be applied to this system.

3) Another assumption was that the user desires to place the blocks in a grid format. This assumption was made to make reading in the block text files easier, and to avoid complications during set-up. Following this assumption, the program also assumes that the user has supplied a blocks_.txt file for each level.

4) Another assumption is that there is only one player. Adding another player would make the code extremely messy, and require the creation of a Player class (or some other solution) to fix it.

5) This project also requires that the user supply all of the necessary images for the program to run. This includes...

The blocks:
* concrete.gif
* double.gif
* strong.gif
* normal.gif

The paddle images:
* longl.gif (long paddle, facing left)
* longr.gif (long paddle, facing right)
* shortl.gif (short paddle, facing left)
* shortr.gif (short paddle, facing right)

The main character:
* __.gif (Where the blank is the supplied prefix)

The powerups:
* pointpower.gif
* paddlepower.gif
* lifepower.gif

The ball:
* dogball.gif

The cat:
* nyancat.gif

And any other images corresponding to added features.


6) The final assumption is that, following winning or losing, the player does not wish to play another round of the game. Although it would have been nice to use the PopupMenu class to produce a final popup asking "Would you like to play again?" I ran into significant problems with this. A bit of research revealed that this is actually a bug in the current version of Java -- the showAndWait() command I use in my code is not allowed while animations are running. This made it almost impossible to jump to a popup, even after the game loop was closed. Hence, the final state is just a screen that says "You lose" or "You win." The only following option is the exit the program.