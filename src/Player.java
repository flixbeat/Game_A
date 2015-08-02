/*
Author: David, Dave Von D.
NOTE: I do not own the sprites and images that have been used in this game.
Credits to: Pio, Mynne Jamaica for the title screen image design
*/
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.Rectangle;

public class Player {
    String name;
    String pword;
    BufferedImage sprite;
    Rectangle rect;
    int life = 3;
    int x = 32;
    int y = 32;
    int sprite_x = 0;
    int sprite_y = 0;
    char dir; // w,s,a,d
    int speed = 15;
    int arrayIndex = 19;
    int score = 0;
    int map_level = 1;
    public Player(String name){
        this.name = name;
        this.pword = "password";
        this.rect = new Rectangle(this.x,this.y,32,32);
        try{
            this.sprite = ImageIO.read(new File("res/sprites/panda2.png"));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    // moves player to beginning location
    public void resetLocation(){
        this.x=32;
        this.y=32;
        this.sprite_x = 0;
        this.sprite_y = 0;
        this.arrayIndex = 19;
        this.rect = new Rectangle(this.x,this.y,32,32);
    }
}
