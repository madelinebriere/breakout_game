Project completed by Madeline Briere 
maddiebriere

I started this assignment on January 9th and finished it on January 23rd, 2017. 

# Estimated number of hours spent on project: 
30 hours

# Role: 
All development

#Online resources used:  
JavaFX Game Tutorial https://carlfx.wordpress.com/2012/03/29/javafx-2-gametutorial-part-1/

#Start Class: 
Runner

#Test Class: 
Runner & Alterations to BreakoutWorld

#Resources Files Required: 
* blocks1.txt
* blocks2.txt
* blocks3.txt
* blocks4.txt
* info.txt
* instructions.txt

#Information:
From PLAN.txt

My game is a Breakout spin-off in that the player controls a paddle (using the left and right keyboard keys) to bounce a ball, trying to destroy all of the destructible blocks on each level and avoid letting their ball fall off the bottom of the screen. 

Players wrack up points when they destroy blocks and catch certain power-ups.
As is commonplace with modern games, my version will become more and more difficult as you pass more levels. I hope to increase difficulty/complexity in at least four ways:
1)	The speed of the ball will increase with each consecutive level, making it harder and harder to stop it from going off the bottom of the screen
2)	The block grid becomes more complicated, with more blocks and a greater variety of blocks (including blocks that, for instance, drop different power-ups)
3)	The paddle will gain a new ability with each level
	a.	Level one: Ball bounces differently on the left, middle and right portions of the paddle
	b.	Level two: Side portal appears allowing paddle to jump from side to side
	c.	Level three: Paddle speeds up when traveling in same direction (“gains momentum”)
4)	As the levels increase, more obstacles will fall from the top of the screen towards the paddle, making it increasingly more difficult to avoid getting hit and losing a life (see “Something Extra”) 
I intend to place these blocks by row and column, reading in from a text file with a string of the desired picture file stored by row and column. 

This means that two rows of six normal blocks will be drawn at the top of the game screen.
The next levels will have more rows of blocks, with more interesting blocks. 

This means that the bottom-most row of blocks is “protected” by two concrete blocks and two strong blocks are embedded in the grid structure.

(See the "blocks" .txt files)
 

Part 3: Blocks
I intend to include at least four different types of blocks:
1)	A “normal” block that is cleared after one hit, rewarding the player with a set number of points (e.g., 10 points)
2)	A “strong” block that is cleared after TWO hits, rewarding the player with a set number of points (same number as the normal block)
3)	A “concrete” block that cannot be cleared from the game (it remains as an obstacle for the entire level)
4)	A “double” block that is cleared after one hit, just like a “normal” block, but rewards the player with double the points (e.g., 20 points)

Part 4: Power-Ups
I intend to include at least three power-ups that drop from blocks and activate when caught:
1)	Paddle size increase – A power-up that increases the size of the paddle temporarily
2)	Life power-up – A power-up that gives you an extra life
3)	Point power-up – A power-up that gives you a set number of extra points

Part 5: Cheat keys
The basic cheat keys I plan to include are as follows:
1)	When you press a number, the game should take you to that level (unless it is not a valid level)
2)	When you press L (for lives), you get an extra life
3)	When you press P (for points), you get an extra point
4)	When you press N (for new game), you restart the entire game and begin at level one again
5)	When you press R (for reset), the ball and paddle return to their starting positions


As my “something extra” element, I include Nyan Cats that fall from the top of the page to the bottom of the page. 
If these obstacles hit the paddle, you lose a life and the level resets. 
This is a substantial change on a game design level because it adds an extra layer of difficulty for the player.
This is a substantial change on a programming level because it will require the addition of an entirely new subset of elements to the screen and 
the constant update of their positions.

#Known Bugs
* When the paddle power is picked up, the image does not update until the character is moved -- fixing this would have required major overhaul
* In very rare scenarios, the game produced unwinnable block configurations.

#Future Changes
Once I better understand software design and implementation, I will be able to remove any dependencies within this project (which I am sure exist). Future changes could also add more levels, and more capabilities for the paddle. More powers would also be wonderful.
The assignment itself was incredibly helpful for learning JavaFX. I could not see any immediate changes to improve it.