/*
Author: David, Dave Von D.
NOTE: I do not own the sprites and images that have been used in this game.
Credits to: Pio, Mynne Jamaica for the title screen image design
*/
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
    BufferedImage[] sprite; // enemy image
    int[] spriteX; // subimage x
    int[] spriteY; // subimage y
    Rectangle[] rect;
    Point[] coord;
    char[][] dir; // enemy direction {w,s,a,d(,x)}
    int moveSpeed[];
    int globalSpeed = 30; // base speed of all enemies (thread.sleep)
    int dirIndex[]; // enemy index on direction set
    int tile[]; // enemy location on map
    int pixelMove[]; // enemy pixelMove
    int limit; // number of enemies in a map
    boolean[] isDirRandom; // check if enemy moves randomly
    int[] randomIndex; // determines what array index/direction of isDirRandom to execute
    
    public Enemy(){
        this.sprite = new BufferedImage[7];
        this.spriteX = new int[7];
        this.spriteY = new int[7];
        this.rect = new Rectangle[7];
        this.coord = new Point[7];
        this.tile = new int[7];
        this.pixelMove = new int[7];
        this.dir = new char[7][4]; 
        this.moveSpeed = new int[7];
        this.dirIndex = new int[7];
        this.isDirRandom = new boolean[7];
        this.randomIndex = new int[7];
        try{
            this.sprite[0] = ImageIO.read(new File("res/sprites/slime_blue.png"));
            this.sprite[1] = ImageIO.read(new File("res/sprites/slime_green.png"));
            this.sprite[2] = ImageIO.read(new File("res/sprites/slime_red.png"));
            this.sprite[3] = ImageIO.read(new File("res/sprites/bear.png"));
            this.sprite[4] = ImageIO.read(new File("res/sprites/yellow_duh.png"));
            this.sprite[5] = ImageIO.read(new File("res/sprites/hornet.png"));
            this.sprite[6] = ImageIO.read(new File("res/sprites/bat.png"));
        }
        catch(Exception e){
            
        }
        // default all enemy has defined movespeed
        for(int i=0;i<this.isDirRandom.length;i+=1)
            this.isDirRandom[i] = false;
        
        // default enemy movespeed
        setMoveSpeed(0,1); // blue slime
        setMoveSpeed(1,1); // green slime
        setMoveSpeed(2,1); // red slime
        setMoveSpeed(3,2); // bear
        setMoveSpeed(4,2); // yellow_duh
        setMoveSpeed(5,4); // hornet
        setMoveSpeed(6,4); // bat
    }
    
    public void setEnemyCoord(int enemyInd, int tile, int x, int y){
        this.tile[enemyInd] = tile;
        this.pixelMove[enemyInd] = 32;
        this.rect[enemyInd] = new Rectangle(x,y,32,32);
        this.coord[enemyInd] = new Point(x,y);
        this.limit+=1;
    }
    
    public void setEnemyPath(int enemyInd, char[] directionSet){
        this.dir[enemyInd] = directionSet;
        this.dirIndex[enemyInd] = 0;
    }
    
    public void setMoveSpeed(int enemyInd, int speed){
        this.moveSpeed[enemyInd] = speed;
    }
    
    public void setRandomDir(int enemyInd){
        this.isDirRandom[enemyInd] = true;
        char[] random = {'w','s','a','d'};
        this.dir[enemyInd] = random;
        Random ran = new Random();
        int randIndex = ran.nextInt(4); // generate number from 0-3
        this.randomIndex[enemyInd] = randIndex;
    }
}
