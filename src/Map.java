/*
Author: David, Dave Von D.
NOTE: I do not own the sprites and images that have been used in this game.
Credits to: Pio, Mynne Jamaica for the title screen image design
*/
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Map {
    int number;
    BufferedImage imgMap;
    int[] array;
    int dim = 18;
    Item item = new Item();
    Enemy enemy = new Enemy();
    public Map(int number){
        this.number = number;
        this.item = new Item();
        item.limit=0; // reset item limit
        item.gatheredIndex=-1; // reset gathered items (-1 means no items gathered yet) 
        try{
            if(number==1)
                this.imgMap = ImageIO.read(new File("res/maps/map1.png"));
            else if(number==2)
                this.imgMap = ImageIO.read(new File("res/maps/map2.png"));
            else if(number==3)
                this.imgMap = ImageIO.read(new File("res/maps/map3.png"));
        }
        catch(Exception e){
            System.out.println(e);
        }
        if(number==1){
            // setup needed items for this map
            // setItemCoord(BufferedImageArrayIndex, x_pos, y_pos)
            item.setItemCoord(0, 64, 160); // apple
            item.setItemCoord(1, 352, 192); // blue_berry
            item.setItemCoord(2, 512, 416); // carrot
            item.setItemCoord(3, 64, 416); // cherry
            item.setItemCoord(4, 512, 288); // fish
            // initialize number of items
            item.sequence = new int[5];
            item.gatheredSequence = new int[5];
            for(int i=0;i<item.gatheredSequence.length;i+=1)
                item.gatheredSequence[i] = -1; // assign all index values to -1 (-1 means nothing or no item)
            // items to get in order
            item.sequence[0] = 1; // blue berry
            item.sequence[1] = 0; // apple
            item.sequence[2] = 2; // carrot
            item.sequence[3] = 4; // fish
            item.sequence[4] = 3; // cherry
            // setup enemies on this map
            //enemy.setEnemyCoord(BufferedImageArrayIndex, tile, x_pos, y_pos);
            enemy.setEnemyCoord(0, 61, 224, 96); // blue slime
            enemy.setEnemyCoord(1, 34, 512, 32); // hornet
            enemy.setEnemyCoord(2, 255, 96, 448); // bat
            char[] path0 = {'a','x','d','x'};
            enemy.setEnemyPath(0, path0);
            char[] path1 = {'a','s','a','d'};
            enemy.setEnemyPath(1, path1);
            //char[] path2 = {'w','a','d','s'};
            //enemy.setEnemyPath(2, path2);
            enemy.setRandomDir(2);
            // initialize map path
            this.array = new int[] 
                   {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                    1,0,0,1,1,0,0,1,0,0,1,1,0,0,0,0,0,1,
                    1,0,0,0,1,1,0,0,1,0,1,1,0,1,1,1,0,1,
                    1,1,0,0,0,0,0,0,1,0,1,1,0,0,1,1,0,1,
                    1,1,1,1,1,0,1,1,1,0,0,1,0,0,1,1,0,1,
                    1,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,1,
                    1,0,1,1,1,0,0,1,0,1,1,0,1,1,0,0,0,1,
                    1,0,0,0,0,0,0,1,1,1,0,1,0,0,0,1,1,1,
                    1,0,1,1,1,0,0,0,0,0,0,1,0,0,1,1,1,1,
                    1,1,1,1,0,1,1,0,0,0,1,1,0,0,1,0,0,1,
                    1,0,0,0,0,0,1,1,0,0,1,0,0,1,1,0,0,1,
                    1,0,1,0,1,0,0,0,0,1,1,0,0,1,0,0,1,1,
                    1,1,1,0,1,1,0,1,1,1,0,0,1,0,0,1,1,1,
                    1,1,0,0,0,0,1,1,0,0,0,1,1,0,0,1,0,1,
                    1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1,0,1,
                    1,0,1,1,0,1,1,0,1,1,0,0,1,1,0,1,0,1,
                    1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,
                    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        }
        else if(number==2){
            item.setItemCoord(0, 96, 32); // apple
            item.setItemCoord(1, 192, 160); // blue berry
            item.setItemCoord(2, 192, 288); // carrot
            //item.setItemCoord(2, 96, 32); // apple
            // initialize number of items
            item.sequence = new int[3];
            item.gatheredSequence = new int[3];
            for(int i=0;i<item.gatheredSequence.length;i+=1)
                item.gatheredSequence[i] = -1; // assign all index values to -1 (-1 means nothing or no item)
            // items to get in order
            item.sequence[0] = 0; // apple
            item.sequence[1] = 1; // blue berry
            item.sequence[2] = 2; // blue berry
            // setup enemies on this map
            //enemy.setEnemyCoord(BufferedImageArrayIndex, tile, x_pos, y_pos);
            enemy.setEnemyCoord(0, 59, 160, 96); // slime blue
            enemy.setRandomDir(0);
            enemy.setEnemyCoord(1, 289, 32, 512); // slime green
            char[] path0 = {'a','x','d','x'};
            enemy.setEnemyPath(1, path0);
            this.array = new int[]
            { //0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, // 0 | 17
                1,0,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1, // 1 | 35
                1,0,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1, // 2 | 53
                1,0,1,0,0,0,0,0,1,1,0,0,0,1,1,0,0,1, // 3 | 71
                1,0,1,1,0,1,1,0,0,0,1,0,0,0,0,0,1,1, // 4 | 89
                1,0,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,1, // 5 | 107
                1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,0,1, // 6 | 125
                1,0,0,0,0,0,0,0,1,1,0,0,0,1,0,1,0,1, // 7 | 143
                1,0,1,1,1,1,1,1,0,0,1,1,0,1,0,1,0,1, // 8 | 161
                1,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1, // 9 | 179
                1,1,1,1,0,0,1,0,1,1,1,1,1,1,0,1,0,1, // 10 | 197
                1,0,0,0,1,0,1,0,0,0,1,0,0,0,0,1,0,1, // 11 | 215
                1,0,0,1,0,0,1,1,1,0,1,0,0,1,0,1,0,1, // 12 | 233
                1,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1,0,1, // 13 | 251
                1,0,0,0,1,0,0,0,1,1,0,0,0,0,1,0,0,1, // 14 | 269
                1,0,1,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1, // 15 | 287
                1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1, // 16 | 305
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1  // 17 | 323
            };
        }
        else if(this.number==3){
            item.setItemCoord(0, 160, 32); // apple
            item.setItemCoord(1, 32, 288); // blue berry
            item.setItemCoord(2, 32, 384); // carrot
            item.setItemCoord(3, 512, 128); // cherry
            item.setItemCoord(4, 32, 160); // fish
            // initialize number of items
            item.sequence = new int[5];
            item.gatheredSequence = new int[5];
            for(int i=0;i<item.gatheredSequence.length;i+=1)
                item.gatheredSequence[i] = -1; // assign all index values to -1 (-1 means nothing or no item)
            // items to get in order
            item.sequence[0] = 0; // apple
            item.sequence[1] = 3; // cherry
            item.sequence[2] = 2; // carrot
            item.sequence[3] = 1; // blue berry
            item.sequence[4] = 4; // fish
            //enemy.setEnemyCoord(BufferedImageArrayIndex, tile, x_pos, y_pos);
            enemy.setEnemyCoord(0, 21, 96, 32); // slime blue
            enemy.setEnemyCoord(1, 24, 192, 32); // slime green
            enemy.setEnemyCoord(2, 29, 352, 32); // slime red
            enemy.setEnemyCoord(3, 111, 96, 192); // bear
            enemy.setEnemyCoord(4, 115, 224, 192); // yellow duh
            char[] path0 = {'s','x','w','x'};
            enemy.setEnemyPath(0, path0);
            char[] path1 = {'d','x','a','x'};
            enemy.setEnemyPath(1, path1);
            enemy.setEnemyPath(2, path0);
            char[] path2 = {'d','s','a','w'};
            enemy.setEnemyPath(3, path2);
            enemy.setEnemyPath(4, path2);
            this.array = new int[]
            { //0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, // 0 | 17
                1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1, // 1 | 35
                1,1,1,0,0,1,0,1,0,0,1,0,1,1,1,1,0,1, // 2 | 53
                1,0,0,0,1,1,1,0,0,1,1,0,1,0,0,0,1,1, // 3 | 71
                1,0,0,0,1,0,0,0,1,0,1,0,1,1,0,1,0,1, // 4 | 89
                1,0,1,0,1,0,1,1,1,0,1,0,0,1,0,1,0,1, // 5 | 107
                1,1,0,0,0,1,0,0,0,1,1,0,1,1,0,1,0,1, // 6 | 125
                1,1,0,1,0,0,0,1,0,0,0,0,1,0,0,1,0,1, // 7 | 143
                1,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,1, // 8 | 161
                1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,1,0,1, // 9 | 179
                1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1, // 10 | 197
                1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1, // 11 | 215
                1,0,1,0,0,0,0,0,0,0,0,1,0,0,0,1,0,1, // 12 | 233
                1,0,0,0,1,1,1,0,0,0,1,0,1,0,0,1,0,1, // 13 | 251
                1,0,1,1,1,0,0,0,0,1,0,0,0,1,0,1,0,1, // 14 | 269
                1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,1,0,1, // 15 | 287
                1,0,1,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1, // 16 | 305
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1  // 17 | 323
            };
        }
    }
}
