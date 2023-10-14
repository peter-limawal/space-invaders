# Space Invaders
Space Invaders is a simple 2D video game, released by Taito in Japan in 1978 and the US in 1980. It was hugely popular, and helped to launch the commercial video game industry. The object of the game is to move a ship along the bottom of the screen and shoot up at aliens that are descending from the sky, without being hit in return. For my CS 349 User Interfaces course, I implemented a fun, playable, version of Space Invaders in Kotlin/Java FX.

To ensure scalability, maintainability, and a clear separation of concerns, I employed the Model-View-Controller (MVC) design pattern. This architectural pattern allowed me to segregate the game's core logic (Model) from the user interface (View) and the system process that manages user input (Controller). By adopting MVC, I ensured that the game mechanics, visual elements, and user input were isolated from each other, offering a modular approach to game design and simplifying potential future enhancements.

Moreover, I have also integrated immersive audio feedback and rich graphics to enhance the gaming experience, while still preserving the essence of the original. Every alien destroyed, missile launched, or ship moved is accompanied by familiar yet refreshed audio-visual cues. The part I was most proud of (and one that took the longest time too) was using JavaFX in order to handle the drawing and animation of the alien, ship, and missile graphics. I was able to accomplish this using hit-testing algorithms which can detect if a missle touches an alien or ship.

Resources obtained from:  
https://student.cs.uwaterloo.ca/~cs349/1235/schedule/assignments/a3/space-invaders-assets.zip

![space-invaders-main](https://github.com/peter-limawal/space-invaders/assets/59006829/bac74b50-79c3-4620-900d-b726ab951eb9)

## Here's a quick demo of Space Invaders!
Don't forget to unmute the video :)

https://github.com/peter-limawal/space-invaders/assets/59006829/197f1507-04b6-49da-99c3-307f6116d03d
