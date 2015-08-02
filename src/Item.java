/*
Author: David, Dave Von D.
NOTE: I do not own the sprites and images that have been used in this game.
Credits to: Pio, Mynne Jamaica for the title screen image design
*/
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

public class Item {
    BufferedImage[] items = new BufferedImage[8];
    Point[] coord = new Point[8];
    Rectangle[] rect = new Rectangle[8];
    int[] score = new int[8];
    int limit; // max item limit - varies on map (will be used in for loops on display and collision detection)
    int[] sequence; // sequence of item to get - values and length varies on map
    int gatheredIndex = -1; // determines which array index of gatheredSequence to give value
    int[] gatheredSequence; // sequence of item gathered - value varies on player, length varies on map
    String itemGatheredStatus = "incomplete"; // "incomplete","inorder","unordered"
    public Item(){
        try{
            this.items[0] = ImageIO.read(new File("res/items/apple.png"));
            this.items[1] = ImageIO.read(new File("res/items/blue_berry.png"));
            this.items[2] = ImageIO.read(new File("res/items/carrot.png"));
            this.items[3] = ImageIO.read(new File("res/items/cherry.png"));
            this.items[4] = ImageIO.read(new File("res/items/fish.png"));
            this.items[5] = ImageIO.read(new File("res/items/grapes.png"));
            this.items[6] = ImageIO.read(new File("res/items/meat.png"));
            this.items[7] = ImageIO.read(new File("res/items/orange.png"));
        }
        catch(Exception e){
            System.out.println(e);
        }
        // item equivalent scores
        score[0] = 10;
        score[1] = 25;
        score[2] = 60;
        score[3] = 80;
        score[4] = 165;
        score[5] = 120;
        score[6] = 200;
        score[7] = 15;
    }
    // (item array index, x position, y position)
    public void setItemCoord(int itemInd, int x, int y){
        this.coord[itemInd] = new Point(x,y);
        this.rect[itemInd] = new Rectangle(x,y,32,32);
        this.limit += 1;
    }
}
