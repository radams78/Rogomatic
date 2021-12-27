# Rog-O-Matic
## A Modern Beligerent Expert System
## by Robin Adams

### Introduction

#### Rogue

In 1980, the game Rogue appeared on the PLATO system, written by Michael C. Toy and Ken Arnold. It was one of the first computer RPGs and extremely influential - we still describe games as "roguelike" if, like Rogue, they have procedurally generated worlds and permadeath. Rogue used the new "curses" library (written by Ken Arnold) for placing characters at an arbitrary location on the screen, which allowed Rogue to display a 2D display of the dungeon map.

```
wielding a +1,+1 mace  
      -------------------
      |.................+
      |......@)...S.....|
      -----------+-------


















Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0
```

The game had the feature that magic items were unidentified when first picked up. A potion would just be described as, say, "a red potion", and which colour has which effect is randomized at the start of the game. Apart from Scrolls of Identify (which are rare!), the only way to find out what something does is to try it and see, a dangerous process!

The game is also extremely difficult as the strength of the monsters increases rapidly as the dungeon level increases. The game has permadeath - on death, your save game file is deleted. Of course you can get around this by copying the save file, but this is cheating - you haven't beaten the game as the authors intended until you have done it in one run.

#### Rog-O-Matic

In 1984, Michael L. Mauldin, Guy Jacobson, Andrew Appel and Leonard Hamey presented their system called Rog-O-Matic, a bot for playing Rogue:

https://www.cs.princeton.edtheu/~appel/papers/rogomatic.html

They presented the design of the system and some experimental data, proving that Rog-O-Matic plays the game better than the best human player they tested (as judged by average score). But even given this, it was an extremely rare event that Rog-O-Matic wins the game. The paper only mentions that it has happened twice.

#### My Motivation

I am on a project to finish every game that I started as a child but never completed. The project has been on pause for a few years as a much bigger game in the form of a toddler came along for my wife and myself, but I now occasionally have a bit of time to start working on it again. You can follow my progress here:

https://beatallthegames.blogspot.com/

I got to 1980 and was having trouble beating Rogue and started reading about the game. I found out about Rog-O-Matic, and in particular learned that it plays the game better than the best human player and very rarely wins. At that point, I gave up on beating the game "manually". I also had trouble compiling the source code for Rog-O-Matic on a modern machine, so I decided instead to create my own version of Rog-O-Matic. If I can write a version that beats Rogue, that counts for me.

As I started, I quickly found that the design of Rog-O-Matic does not really match the neat diagram displayed in Figure 3-1 in the paper. Everything depends on everything else: the Behavior Rules update the World Model and each other's parameters, the Sensory Interface changes the Behavior Rules' parameters, the Behavior Rules call each other not necessarily in the sequence shown in Figure 3-2, etc. This was not going to be a simple port - I was going to have to start from scratch, but with the old source code as a guide.

#### Technologies Used

* Scala - Following the Pragmatic Programmers' advice that every new project should use one unfamiliar technology, I am using this project to teach myself Scala. The system is written in Scala 3.1.0

#### How to Play Rogue

If you want to play Rogue yourself and you have a Linux system, it is in the bsdgames-nonfree package. Or you can play it online here: https://www.myabandonware.com/game/rogue-4n/play-4n