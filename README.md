# The Nerd Project
**Hello!** And welcome, to "The Nerd Project"! This project aims at wrapping the "Processing" creative-coding framework
(and some of its libraries) to make it easier to work with the framework for large projects.

*Really,* nobody spots this little corner of the internet by which I (Brahvim) reside, so I guess it's something only I 
am [(making, and...)] using (not yet, actually)!

<br>Open-sourcing and giving freedom is pretty cool too!
<br>I'd love to see your modifications, if you seem to be able to make any! :grin:
<br>...that's why it even *is* here, I suppose!

Together, we *might* create a ***fantastic*** wrapper for Processing, which will help people create!

Nerd seems to be well-built for video games, but it's totally usable for awesome music-synced (and perhaps interactive!) animations, too!

# The API
Nerd works using "scenes" and "layers". Quite literally. [Check 'em out in the source code!](https://github.com/Brahvim/TheNerdProject/tree/master/src/com/brahvim/nerd/scene_api)

<br>The idea is to extend these classes, overriding their methods, and letting Nerd's code call them when Processing starts certain events.

Following, are *suuuuuuper-long* descriptions *debating* what inspired me to add these in!

## `Scene`s.
I first came across the idea of using these around mid-2021 or so, back when I was building some of the earliest versions of Nerd. *Ah!*

Using Processing, most beginners trying to make video games usually do something like this!:

```processing
int currentScene;
final int MENU, GAME, END;

void draw() {
  if (currentScene == MENU) {
    // Draw some buttons! ..and stuff!
  } else if (currentScene == GAME) {
    // Here's da game, boom!
  } else if (currentScene == END) {
    // Oh hey there, sweet player!
    // Did you win? :D?
  }
  
  // Stuff, stuff, stuff!~
  
}
```

And thus, we see the beginning of a code-mess...? (Is it actually a mess? No?)

```processing
void mousePressed() {
  if (currentScene == MENU) {
    // Uhh, button clicked?
  } else if (currentScene == GAME) {
    // Shoot, shoot! Drive, drive!
  } else if (currentScene == END) {
    // Oh hey there, sweet player!
    // Did you win? :D?
  }
  
  // Stuff, stuff, stuff!~
  
}
```

But then one day, somebody would want to make a BIG game!
<br> But what if they decided to continue with similar code...?!

<br>*Here we go.*

<br>The SHEER number of global variables in that code...
<br>The HIGH number of... `menuScene`, `gameScene`, and `endScene` prefixing...
<br>The fear of tired people accidentally modifying your variables in their all-nighter-coding-session sleepiness!
<br>Loose functions, global variables... *AAAAAAH-*


...dear God. Oh *NO!*

...and that's what we aim to fix here :|
<br>With `Scene`s, this tragedy is ***no more*** than just frightening dreams! ":D!~

## `Layer`s.
A recent addition to Nerd, `Layer`s are "scenes inside scenes", *idr est*, objects in a `Scene` class that may be used to respond to Processing's events *inside of a scene*. This is good for things such as extra text-boxes on the screen, or simply cleaning up your code further. You can even toggle them on and off!

A *major-looking* disadvantage for `Layer`s is their inability to share data ***directly*** with each other. A `Layer` can access its parent `Scene`, which may contain data that all layers can share, but this makes a `Scene` an interface for two layers - direct communication can still not occur between them.

## More about them!
Nerd did not split `Scene`s and `Layer`s into separate classes till I started *this* revision of it. Before this, the scene classes were used in a 'dangerous' way, like so:

```java
class Sketch extends PApplet {
  Scene testScene = new Scene() {
    public void setup() {
    }
    
    public void draw() {
    }
  };
}
```

There wasn't a `SceneManager` either! It's a thing now!
<br>I don't remember *exactly* why, but having a manager **for this case** is very helpful.
<br>The manager allows you to control scenes better! It makes life very easy.
