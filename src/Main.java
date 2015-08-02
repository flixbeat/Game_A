/*
Author: David, Dave Von D.
NOTE: I do not own the sprites and images that have been used in this game.
Credits to: Pio, Mynne Jamaica for the title screen image design
*/
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Main extends JFrame{
    public Main(){
        this.setTitle("Panda Adventures v1");
        this.setSize(782,604);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new JPBase());
        this.setVisible(true);
    }
    
    public static void main(String[] args){
        new Main();
    }
}

class JPBase extends JPanel implements ActionListener, Runnable{
    // tile grid = 32x32px
    // map dimension = 18x18 (of 32px) = 576x576px
    int panelWidth = 782;
    int panelHeight = 604;
    int mapWidth = 576;
    int mapHeight = 576;
    int pixelMove = 32;
    int statusPanelX = mapWidth;
    boolean isGameStart = false;
    boolean firstLaunch = true;
    boolean enableGrid = false;
    boolean enableBGMusic = true;
    boolean isLoggedIn = false;
    String gameState = "title screen";
    BufferedImage imgTitle;
    // Game classes init
    Player player;
    Map map;
    public JPBase(){
        this.setSize(this.panelWidth,this.panelHeight);
        mainThread = new Thread(this);
        try{
            this.imgTitle = ImageIO.read(new File("res/title.jpg"));
        }
        catch(Exception e){}
        showMenu();
    }
    
    // =============== GAME LOOP AND OTHER THREADS===========================
    Thread mainThread;
    Thread playerThread;
    Thread enemiesThread;
    Thread bgSoundThread;
    @Override
    public void run() {
        while(true){
            try {
                repaint();
                mainThread.sleep(10);
            } 
            catch (InterruptedException ex) {
                
            }
        }
    }
    
    public synchronized void startPlayerThread(){
        this.playerThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    // player movement and sprite anim
                    if(pixelMove>0){
                        if(player.dir=='w'){
                            player.y-=1;
                            player.rect.y-=1;
                            player.sprite_y = 32;
                        }
                        else if(player.dir=='s'){
                            player.y+=1;
                            player.rect.y+=1;
                            player.sprite_y = 0;
                        }
                        else if(player.dir=='a'){
                            player.x-=1;
                            player.rect.x-=1;
                            player.sprite_y = 96;
                        }
                        else if(player.dir=='d'){
                            player.x+=1;
                            player.rect.x+=1;
                            player.sprite_y = 64;
                            // change character sprite
                        }
                        // controls sprite animation speed
                        if(pixelMove%6==0){
                            player.sprite_x+=32;
                        }
                        
                        if(player.sprite_x>64){
                            player.sprite_x=0;
                        }
                        pixelMove-=1;
                    }
                    else if(pixelMove==0){
                        player.sprite_x=0;
                    }
                    // collision detection
                    checkCollision();
                    // check items status
                    if(map.item.itemGatheredStatus.equals("incomplete"))
                        checkGatheredItemsStatus();
                    try{
                        this.sleep(player.speed);
                    }
                    catch(Exception e){
                    
                    }
                }
            }
        };
        this.playerThread.start();
    }
    
    public synchronized void startEnemiesThread(){
        this.enemiesThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    for(int i=0;i<map.enemy.limit;i+=1){
                        // map.enemy.dir[enemyIndex][characterArrayIndex]
                        if(map.enemy.dir[i][map.enemy.dirIndex[i]]=='a'){
                            int nextTile = map.array[map.enemy.tile[i]-1]; // check next tile if passable
                            if(nextTile==0){ // if passable
                                map.enemy.pixelMove[i]-=map.enemy.moveSpeed[i]; // 32px - 1px
                                map.enemy.coord[i].x-=map.enemy.moveSpeed[i]; // enemy x coord -1
                                map.enemy.rect[i].x-=map.enemy.moveSpeed[i]; // enemy rect coord -1
                                if(map.enemy.pixelMove[i]==0){ // if enemy completed 32px move
                                    map.enemy.pixelMove[i]=32; // regen next px
                                    map.enemy.tile[i]-=1; // update enemy map location
                                }
                                // sprite animation
                                map.enemy.spriteY[i]=96;
                                if(map.enemy.pixelMove[i]%6==0)
                                    map.enemy.spriteX[i]+=32;
                                if(map.enemy.spriteX[i]==96)
                                    map.enemy.spriteX[i]=0;
                            }
                            else{
                                if(map.enemy.isDirRandom[i]){ // check if enemy moves randomly
                                    Random ran = new Random();
                                    int randIndex = ran.nextInt(4);
                                    map.enemy.dirIndex[i]=randIndex;
                                }
                                else{
                                    map.enemy.dirIndex[i]+=1; // move index of direction set
                                    if(map.enemy.dirIndex[i]>3) // reset index of direction set to 0
                                        map.enemy.dirIndex[i]=0;
                                }
                                
                            }
                        }
                        else if(map.enemy.dir[i][map.enemy.dirIndex[i]]=='d'){
                            int nextTile = map.array[map.enemy.tile[i]+1];
                            if(nextTile==0){
                                map.enemy.pixelMove[i]-=map.enemy.moveSpeed[i];
                                map.enemy.coord[i].x+=map.enemy.moveSpeed[i];
                                map.enemy.rect[i].x+=map.enemy.moveSpeed[i];
                                if(map.enemy.pixelMove[i]==0){
                                    map.enemy.pixelMove[i]=32;
                                    map.enemy.tile[i]+=1;
                                }
                                // sprite animation
                                map.enemy.spriteY[i]=64;
                                if(map.enemy.pixelMove[i]%6==0)
                                    map.enemy.spriteX[i]+=32;
                                if(map.enemy.spriteX[i]==96)
                                    map.enemy.spriteX[i]=0;
                            }
                            else{
                                if(map.enemy.isDirRandom[i]){ // check if enemy moves randomly
                                    Random ran = new Random();
                                    int randIndex = ran.nextInt(4);
                                    map.enemy.dirIndex[i]=randIndex;
                                }
                                else{
                                    map.enemy.dirIndex[i]+=1; // move index of direction set
                                    if(map.enemy.dirIndex[i]>3) // reset index of direction set to 0
                                        map.enemy.dirIndex[i]=0;
                                }
                            }
                        }
                        else if(map.enemy.dir[i][map.enemy.dirIndex[i]]=='w'){
                            int nextTile = map.array[map.enemy.tile[i]-map.dim];
                            if(nextTile==0){
                                map.enemy.pixelMove[i]-=map.enemy.moveSpeed[i];
                                map.enemy.coord[i].y-=map.enemy.moveSpeed[i];
                                map.enemy.rect[i].y-=map.enemy.moveSpeed[i];
                                if(map.enemy.pixelMove[i]==0){
                                    map.enemy.pixelMove[i]=32;
                                    map.enemy.tile[i]-=map.dim;
                                }
                                // sprite animation
                                map.enemy.spriteY[i]=32;
                                if(map.enemy.pixelMove[i]%6==0)
                                    map.enemy.spriteX[i]+=32;
                                if(map.enemy.spriteX[i]==96)
                                    map.enemy.spriteX[i]=0;
                            }
                            else{
                                if(map.enemy.isDirRandom[i]){ // check if enemy moves randomly
                                    Random ran = new Random();
                                    int randIndex = ran.nextInt(4);
                                    map.enemy.dirIndex[i]=randIndex;
                                }
                                else{
                                    map.enemy.dirIndex[i]+=1; // move index of direction set
                                    if(map.enemy.dirIndex[i]>3) // reset index of direction set to 0
                                        map.enemy.dirIndex[i]=0;
                                }
                            }
                        }
                        else if(map.enemy.dir[i][map.enemy.dirIndex[i]]=='s'){
                            int nextTile = map.array[map.enemy.tile[i]+map.dim];
                            if(nextTile==0){
                                map.enemy.pixelMove[i]-=map.enemy.moveSpeed[i];
                                map.enemy.coord[i].y+=map.enemy.moveSpeed[i];
                                map.enemy.rect[i].y+=map.enemy.moveSpeed[i];
                                if(map.enemy.pixelMove[i]==0){
                                    map.enemy.pixelMove[i]=32;
                                    map.enemy.tile[i]+=map.dim;
                                }
                                // sprite animation
                                map.enemy.spriteY[i]=0;
                                if(map.enemy.pixelMove[i]%6==0)
                                    map.enemy.spriteX[i]+=32;
                                if(map.enemy.spriteX[i]==96)
                                    map.enemy.spriteX[i]=0;
                            }
                            else{
                                if(map.enemy.isDirRandom[i]){ // check if enemy moves randomly
                                    Random ran = new Random();
                                    int randIndex = ran.nextInt(4);
                                    map.enemy.dirIndex[i]=randIndex;
                                }
                                else{
                                    map.enemy.dirIndex[i]+=1; // move index of direction set
                                    if(map.enemy.dirIndex[i]>3) // reset index of direction set to 0
                                        map.enemy.dirIndex[i]=0;
                                }
                            }
                        }
                        else if(map.enemy.dir[i][map.enemy.dirIndex[i]]=='x'){
                            map.enemy.dirIndex[i]+=1; // move index of direction set
                            if(map.enemy.dirIndex[i]>3){ // reset index of direction set to 0
                                map.enemy.dirIndex[i]=0;
                            }
                        }
                    }
                    try{
                        this.sleep(map.enemy.globalSpeed);
                    }
                    catch(Exception e){}
                }
            }
        };
        this.enemiesThread.start();
    }
    
    public synchronized void startSfxSoundThread(String sfx){
        final String sfxd = sfx;
        Thread bgSoundThread = new Thread(){
            @Override
            public void run(){
                if(sfxd.equals("item_get"))
                    new MakeSound().playSound("res/sounds/item_get.wav");
                else if(sfxd.equals("faint"))
                    new MakeSound().playSound("res/sounds/faint.wav");
            }
        };
        bgSoundThread.start();
    }
    
    public synchronized void startBgSoundThread(){
        this.bgSoundThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        if(enableBGMusic)
                            new MakeSound().playSound("res/sounds/bgmusic.wav");
                        this.sleep(10);
                    }
                    catch(Exception e){}
                }
            }
        };
        this.bgSoundThread.start();
    }
    
    // ===================== GAME PROGRESS CHECK =============================
    public void checkGatheredItemsStatus(){
        if(!map.item.itemGatheredStatus.equals("unordered")){
            if(map.item.gatheredIndex!=-1){ // check if no items has gathered yet
                if(map.item.gatheredSequence[map.item.gatheredIndex]!=map.item.sequence[map.item.gatheredIndex]){
                    map.item.itemGatheredStatus="unordered";
                    JOptionPane.showMessageDialog(null, "You fail to collect the food in order, the level will be restarted.", "Objective Failed",  JOptionPane.WARNING_MESSAGE);
                    try{
                        Robot robot = new Robot();
                        robot.keyPress(KeyEvent.VK_F5); 
                    }
                    catch(Exception e){}
                }
            }
        }
        if(map.item.itemGatheredStatus.equals("incomplete") || map.item.itemGatheredStatus.equals("inorder")){
            try{
                if(map.item.gatheredSequence[map.item.limit-1]==map.item.sequence[map.item.limit-1]){
                    this.player.dir='x';
                    this.enemiesThread.stop();
                    JOptionPane.showMessageDialog(null, "You have completed the objective to collect the food inorder!", "HURRAY!",  JOptionPane.INFORMATION_MESSAGE);
                    map.item.itemGatheredStatus="inorder";
                    this.player.map_level+=1;
                    this.map = new Map(this.player.map_level);
                    this.player.resetLocation();
                    startEnemiesThread();
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Congratulations! You beat the game! Your score has been recorded.", "WELL DONE!", JOptionPane.INFORMATION_MESSAGE);
                /*
                try{
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    System.exit(0); // to be removed!
                }
                catch(Exception er){}
                */
                this.player.map_level=1;
                this.map = new Map(this.player.map_level);
            }
        }
    }
    // ====================== COLLISION DETECTION ============================
    public void checkCollision(){
        // enemy collision
        for(int i=0;i<map.enemy.limit;i+=1){
            if(player.rect.intersects(map.enemy.rect[i])){
                this.player.dir='x';
                this.player.sprite_x=0;
                this.player.sprite_y=128;
                this.player.life-=1;
                startSfxSoundThread("faint");
                if(player.life>0){
                    int choice = JOptionPane.showConfirmDialog(null, "Do you wish to continue?","You have fainted!",JOptionPane.YES_NO_OPTION);
                    if(choice==0){ // YES
                        player.resetLocation();
                    }
                    else{
                        try{
                            Robot robot = new Robot();
                            robot.keyPress(KeyEvent.VK_ESCAPE); 
                        }
                        catch(Exception e){}
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "No more retries left!", "GAME OVER", JOptionPane.WARNING_MESSAGE);
                    try{
                        Robot robot = new Robot();
                        robot.keyPress(KeyEvent.VK_ESCAPE);
                    }
                    catch(Exception e){}
                }
            }
        }
        
        // item collision
        // loop through array of item rects
        for(int i=0;i<map.item.limit;i+=1){
            if(player.rect.intersects(map.item.rect[i])){
                player.score += map.item.score[i];
                // update gathered items
                map.item.gatheredIndex+=1;
                map.item.gatheredSequence[map.item.gatheredIndex] = i;
                // remove item on the map
                map.item.rect[i].x = -1;
                map.item.rect[i].y = -1;
                this.startSfxSoundThread("item_get");
            }
        }
        
        /*
        if(player.rect.intersects(map.item.rect[0])){
            System.out.println("YOU GOT APPLE!");
            player.score += map.item.score[0];
            // remove item on the map
            map.item.rect[0].x = -1;
            map.item.rect[0].y = -1;
        }
        else if(player.rect.intersects(map.item.rect[1])){
            System.out.println("YOU GOT BLUE BERRY!");
            player.score += map.item.score[1];
            map.item.rect[1].x = -1;
            map.item.rect[1].y = -1;
        }
        else if(player.rect.intersects(map.item.rect[2])){
            System.out.println("YOU GOT CARROT!");
            player.score += map.item.score[2];
            map.item.rect[2].x = -1;
            map.item.rect[2].y = -1;
        }
        */

        
    }
    
    // =============================== PAINT =================================
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(this.gameState.equals("Help") && !this.isGameStart){
            paintHelp(g);
        }
        else if(this.gameState.equals("options") && !this.isGameStart){
            paintOptions(g);
        }
        else if(this.gameState.equals("title screen") && !this.isGameStart){
            paintMenu(g);
        }
        else if(this.isGameStart==true){
            paintGame(g);
            if(this.enableGrid) paintGrid(g);
            //System.out.println("asdf");
            
        }
    }
    
    public void paintMenu(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, this.panelWidth, this.panelHeight);
        g.drawImage(this.imgTitle,80,-17,null);
    }
    
    public void paintOptions(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, this.panelWidth, this.panelHeight);
        g.setColor(Color.white);
        g.drawString("Options",30,30);
    }
    
    public void paintHelp(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, this.panelWidth, this.panelHeight);
        g.setColor(Color.white);
        g.drawString("Controls", 30, 30);
        g.drawString("w - move player up", 30, 50);
        g.drawString("s - move player down", 30, 70);
        g.drawString("a - move player left", 30, 90);
        g.drawString("d - move player right", 30, 110);
        
        g.drawString("Instructions", 30, 160);
        g.drawString("You will be playing as a role of a panda and you need to collect the all the food that are plotted on the map, you need to take", 30, 180);
        g.drawString("all the food in a sequence prior to what is displayed on the right side of the screen in order to move to the next level, failure", 30, 200);
        g.drawString("to do this will result in restarting the current level.", 30, 220);
        
        g.drawString("You also need to avoid the monsters that walks throughout the map, having physical contact with the monsters will make your player", 30, 250);
        g.drawString("faint and your position will be restarted. You are only given 3 life or retries before game over.", 30, 270);
        g.drawString("****************************************************************About****************************************************************", 30, 370);
        g.drawString("Game Developer: David, Dave Von D.", 30, 400);
        g.drawString("Credits to: Pio, Mynne Jamaica for the title screen image design.", 30, 420);
        g.drawString("NOTE: I do not own any of the sprites and images that have been used in this game. I believe there will still be bugs but expect that the", 30, 440);
        g.drawString("development of this game will be continous, thus reducing the amount of bugs and adding more features to the game. I'm planning to", 30, 460);
        g.drawString("release this game in public as open source, but as for this beta version, the code won't be available. The source will be released sooner", 30, 480);
        g.drawString("or later once this game achieved great stability and scalability, and it would be available on my website :) cheers!. ~ Dave", 30, 500);
        
    }
    
    public void paintGame(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, this.mapWidth, this.mapHeight);
        g.setColor(new Color(30,120,50));
        g.fillRect(this.mapWidth, 0, 200, this.mapHeight);
        // paint map
        g.drawImage(map.imgMap, 0, 0, null);
        // paint items
        paintMapItems(g);
        // paint player
        g.drawImage(player.sprite.getSubimage(player.sprite_x, player.sprite_y, 32, 32), player.x, player.y, null);
        // paint enemies
        paintEnemies(g);
        // paint status
        paintStatus(g);
    }
    
    public void paintEnemies(Graphics g){
        for(int i=0;i<map.enemy.limit;i+=1){
            if(map.enemy.rect[i].x!=-1 && map.enemy.rect[i].y!=-1)
                g.drawImage(map.enemy.sprite[i].getSubimage(map.enemy.spriteX[i], map.enemy.spriteY[i], 32, 32), map.enemy.coord[i].x, map.enemy.coord[i].y, null);
        }
    }
    
    public void paintMapItems(Graphics g){
        for(int i=0;i<map.item.limit;i+=1){
            if(map.item.rect[i].x!=-1 && map.item.rect[i].y!=-1) // if rectangle x is not -1 (item rect x gets -1 if it intersects player rect)
                g.drawImage(map.item.items[i], map.item.coord[i].x, map.item.coord[i].y, null); // draw item
        }/*
        if(map.item.rect[0].x!=-1) // if rectangle x is not -1 (item rect x gets -1 if it intersects player rect)
            g.drawImage(map.item.items[0], map.item.coord[0].x, map.item.coord[0].y, null); // draw apple
        if(map.item.rect[1].x!=-1)
            g.drawImage(map.item.items[1], map.item.coord[1].x, map.item.coord[1].y, null); // draw blue berry
        if(map.item.rect[2].x!=-1)
            g.drawImage(map.item.items[2], map.item.coord[2].x, map.item.coord[2].y, null); // draw blue berry
        */ 
    }
    
    int itemSequenceX = this.statusPanelX+10;
    int itemSequenceY = 190;
    int gatheredItemSequenceX = this.statusPanelX+10;
    int gatheredItemSequenceY = 330;
    public void paintStatus(Graphics g){
        g.setColor(new Color(225,225,200));
        g.drawString("Player name: "+player.name, this.statusPanelX+10, 30);
        g.drawString("Score: "+player.score, this.statusPanelX+10, 45);
        // paint life
        g.drawImage(player.sprite.getSubimage(0, 0, 32, 32), this.statusPanelX+10, 55, null);
        g.drawString("x"+player.life, this.statusPanelX+50, 80);
        // paint item status
        g.drawString("Item gathered status: "+map.item.itemGatheredStatus, statusPanelX+10, 105);
        // paint items sequence
        g.drawString("Get these items in order:", this.statusPanelX+10, 175);
        for(int i=0;i<map.item.sequence.length;i+=1){
            g.drawImage(map.item.items[map.item.sequence[i]], this.itemSequenceX, this.itemSequenceY, null);
            this.itemSequenceX += 35;
        }
        this.itemSequenceX=this.statusPanelX+10;
        // paint gathered items
        g.drawString("Gathered items:", this.statusPanelX+10, 315);
        for(int i=0;i<map.item.gatheredSequence.length;i+=1){
            if(map.item.gatheredSequence[i]!=-1){ // check if -1 (-1 means nothing or no item)
                g.drawImage(map.item.items[map.item.gatheredSequence[i]], this.gatheredItemSequenceX, this.gatheredItemSequenceY, null);
                this.gatheredItemSequenceX += 35;
            }
        }
        this.gatheredItemSequenceX = this.statusPanelX+10;
    }
    
    public void paintGrid(Graphics g){
        g.setColor(new Color(90,90,90));
        // horizontal
        for(int y=0;y<=this.mapHeight;y+=32){
            g.drawLine(0,y,this.mapWidth,y);
        }
        // vertical
        for(int x=0;x<=this.mapWidth;x+=32){
            g.drawLine(x,0,x,this.mapHeight);
        }
        
    }
    // ============================ KEY EVENT =================================
    
    public void addKeyHandler(){
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                // System.out.println(e.getKeyCode());
                if(pixelMove==0){
                    pixelMove=32;
                    if(key=='w'){ // up
                        int ind = map.array[player.arrayIndex-map.dim];
                        if(ind==0){
                            player.dir = 'w';
                            player.arrayIndex-=map.dim;
                        }
                        else{
                            player.sprite_y = 32;
                            player.dir = 'x'; // x = no direction || don't move
                        }
                            
                    }
                    else if(key=='s'){ // down
                        int ind = map.array[player.arrayIndex+map.dim];
                        if(ind==0){
                            player.dir = 's';
                            player.arrayIndex+=map.dim;
                        }
                        else{
                            player.sprite_y = 0;
                            player.dir = 'x'; 
                        }
                    }
                    else if(key=='a'){ // left
                        int ind = map.array[player.arrayIndex-1];
                        if(ind==0){
                            player.dir = 'a';
                            player.arrayIndex-=1;
                        }
                        else{
                            player.sprite_y = 96;
                            player.dir = 'x'; 
                        }
                    }
                    else if(key=='d'){ // right
                       int ind = map.array[player.arrayIndex+1];
                       if(ind==0){
                           player.dir = 'd';
                           player.arrayIndex+=1;
                       }
                       else{
                           player.sprite_y = 64;
                           player.dir = 'x';
                       }
                    }
                    else{
                        player.dir = 'x';
                    }
                }
                // TILDE KEY (~) cheat activate
                if(e.getKeyCode()==192){
                    String cheat = JOptionPane.showInputDialog("Enter cheat code");
                    if(cheat.equals("revive")){
                        if(!playerThread.isAlive()){
                            player.life=3;
                            startPlayerThread();
                            player.resetLocation();
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Unable to perform cheat. Player is still alive.", "Cheat error",  JOptionPane.ERROR_MESSAGE); 
                    }
                    else if(cheat.equals("speedup")){
                        player.speed -= 3;
                    }
                    else if(cheat.equals("speeddown")){
                        player.speed += 3;
                    }
                    else if(cheat.equals("speedmax")){
                        player.speed = 3;
                    }
                    else if(cheat.equals("resetlocation")){
                        if(playerThread.isAlive()){
                            player.resetLocation();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Unable to perform cheat. Player is dead.", "Cheat error",  JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if(cheat.equals("slowallenemies")){
                        for(int i=0;i<map.enemy.limit;i+=1)
                            map.enemy.moveSpeed[i]=1;
                    }
                    else if(cheat.equals("freezeallenemies")){
                        enemiesThread.stop();
                    }
                    else if(cheat.equals("unfreezeallenemies")){
                        startEnemiesThread();
                    }
                    else if(cheat.equals("killallenemies")){
                        for(int i=0;i<map.enemy.limit;i+=1){
                            map.enemy.rect[i].x=-1;
                            map.enemy.rect[i].y=-1;
                        }
                        enemiesThread.stop();
                    }
                    else if(cheat.equals("oneup")){
                        player.life+=1;
                    }
                    else if(cheat.equals("stage1")){
                        map = new Map(1);
                        player.map_level=1;
                        player.resetLocation();
                    }
                    else if(cheat.equals("stage2")){
                        map = new Map(2);
                        player.map_level=2;
                        player.resetLocation();
                    }
                    else if(cheat.equals("stage3")){
                        map = new Map(3);
                        player.map_level=3;
                        player.resetLocation();
                    }
                    else if(cheat.equals("?")){
                        JOptionPane.showMessageDialog(null, "revive - revives player and resets life to 3\n"
                                + "speedup - increases player speed by 3 fps\n"
                                + "speeddown - decreases player speed by 3 fps\n"
                                + "speedmax - set's player maximum speed\n"
                                + "resetlocation - teleports player to starting location on map\n"
                                + "slowallenememies - decreases all enemies' speed by 1 fps\n"
                                + "freezeallenemies - stops all enemy movement\n"
                                + "unfreezeallenemies - start enemy movemeny if enemies are in freeze mode\n"
                                + "killallenemies - remove all enemies on map\n"
                                + "oneup - adds 1 to life\n"
                                + "stage1 - play in stage 1\n"
                                + "stage2 - play in stage 2", "List of cheat codes",  JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                // F5 key
                else if(e.getKeyCode()==116){
                    resetLevel();
                }
                // ESC key
                else if(e.getKeyChar()==27){
                    backToMenu();
                    saveScore();
                }
            }
        });
    }
    
    // ============================ MENU ===================================
    
    JButton btnStart = new JButton("Start");
    JButton btnLoad = new JButton("Load Game");
    JButton btnOption = new JButton("Options"); 
    JCheckBox cbShowGrid = new JCheckBox("Display Grid");
    JCheckBox cbEnableMusic = new JCheckBox("Enable Background Music");
    JButton btnHelp = new JButton("Help"); JButton btnBack = new JButton("< Back");
    JButton btnExit = new JButton("Exit");
    public void showMenu(){
        if(this.bgSoundThread==null || !this.bgSoundThread.isAlive()) this.startBgSoundThread();
        this.setLayout(new FlowLayout());
        this.add(btnStart);
        this.add(btnLoad);
        this.add(btnOption);
        this.add(btnHelp);
        this.add(btnExit);
        btnStart.addActionListener(this);
        btnLoad.addActionListener(this);
        btnOption.addActionListener(this);
        btnHelp.addActionListener(this);
        btnExit.addActionListener(this);
        repaint();
    }
    
    public void removeMenu(){
        this.remove(btnStart);
        this.remove(btnLoad);
        this.remove(btnOption);
        this.remove(btnHelp);
        this.remove(btnExit);
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals("Start")||cmd.equals("Load Game")){
            String name = null;
            String password = null;
            if(!this.isLoggedIn){
                name = JOptionPane.showInputDialog("Enter your name");
                password = JOptionPane.showInputDialog("Enter your password");
                this.isLoggedIn=true;
            }
            else{
                name = this.player.name;
                password = this.player.pword;
            }
            if(!name.equals("")){
                if(cmd.equals("Start")){
                    removeMenu();
                    this.setLayout(null);
                    this.isGameStart=true;
                    this.player = new Player(name);
                    this.player.pword = password;
                    this.map = new Map(1);
                    if(!this.mainThread.isAlive()) this.mainThread.start();
                    if(this.playerThread!=null && this.playerThread.isAlive()) this.playerThread.stop();
                    if(this.enemiesThread!=null && this.enemiesThread.isAlive()) this.enemiesThread.stop();
                    this.startPlayerThread();
                    this.startEnemiesThread();
                    this.setFocusable(true); // needed for the events to be fired
                    this.requestFocusInWindow(); // needed for the events to be fired
                    // enable key event
                    if(this.firstLaunch==true){
                        addKeyHandler();
                        this.firstLaunch=false;
                    }
                }
                else if(cmd.equals("Load Game")){
                    loadGame(name,password);
                }
                //this.revalidate();
            }
            else
                JOptionPane.showMessageDialog(null, "You must specify a name!", "Error",  JOptionPane.ERROR_MESSAGE);
        }
        // HELP
        else if(cmd.equals("Help")){
            removeMenu();
            this.gameState="Help";
            this.btnBack.addActionListener(this);
            this.setLayout(null);
            this.btnBack.setBounds(600, 520, 150, 30);
            this.add(this.btnBack);
            repaint();
        }
        else if(cmd.equals("< Back")){
            this.gameState="title screen";
            this.remove(this.btnBack);
            this.remove(this.cbShowGrid);
            this.remove(this.cbEnableMusic);
            showMenu();
        }
        // OPTIONS
        else if(cmd.equals("Options")){
            removeMenu();
            this.gameState="options";
            this.btnBack.addActionListener(this);
            this.setLayout(null);
            this.btnBack.setBounds(600, 520, 150, 30);
            this.add(this.btnBack);
            this.cbShowGrid.setBounds(30,50,100,20);
            this.cbShowGrid.addActionListener(this);
            this.cbEnableMusic.setBounds(30,80,200,20);
            this.cbEnableMusic.addActionListener(this);
            this.add(cbShowGrid);
            this.add(cbEnableMusic);
            if(this.enableBGMusic)
                 this.cbEnableMusic.setSelected(true);
             else
                 this.cbEnableMusic.setSelected(false);
            repaint();
        }
        else if(cmd.equals("Display Grid")){
            JCheckBox cbShowGrid = (JCheckBox) e.getSource();
            if(cbShowGrid.isSelected())
                this.enableGrid=true;
            else
                this.enableGrid=false;
        }
        else if(cmd.equals("Enable Background Music")){
             JCheckBox cbEnableMusic = (JCheckBox) e.getSource();
             if(cbEnableMusic.isSelected())
                 this.enableBGMusic=true;
             else
                 this.enableBGMusic=false;
        }
        else if(cmd.equals("Exit")){
            System.exit(0);
        }
    }
    
    
    // ========================= SCENE CONTROL ==============================
    public void resetLevel(){
        this.isGameStart=false;
        this.playerThread.stop();
        this.enemiesThread.stop();
        this.isGameStart=true;
        this.player = new Player("Flixbeat");
        this.map = new Map(1);
        this.startPlayerThread();
        this.startEnemiesThread();
    }
    
    public void backToMenu(){
        this.isGameStart=false;
        this.playerThread.stop();
        this.enemiesThread.stop();
        showMenu();
    }
    
    // ========================= SAVE SCORE | LOAD GAME =====================
    
    public void saveScore(){
        String name = this.player.name;
        String pword = this.player.pword;
        int score = this.player.score;
        int life = this.player.life;
        int level = this.player.map_level;
        try{
            //FileOutputStream file = new FileOutputStream("scores.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt",true)); // true = append
            writer.write(name+","+pword+","+score+","+life+","+level);
            writer.newLine();
            writer.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    public void loadGame(String name,String pword){
        String account = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File("scores.txt")));
            String line;
            while((line = br.readLine())!=null){
                if(line.contains(name+","+pword))
                    account = line;
            }
            br.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
        if(account!=null){
            String[] data = account.split(",");
            String p_name = data[0];
            String p_pword = data[1];
            int p_score = Integer.parseInt(data[2]);
            int p_life = Integer.parseInt(data[3]);
            int p_level = Integer.parseInt(data[4]);
            //
            removeMenu();
            this.setLayout(null);
            this.isGameStart=true;
            this.player = new Player(p_name);
            this.player.pword = p_pword;
            this.player.score = p_score;
            this.player.life = p_life;
            this.player.map_level = p_level;
            this.map = new Map(p_level);
            if(!this.mainThread.isAlive()) this.mainThread.start();
            if(this.playerThread!=null && this.playerThread.isAlive()) this.playerThread.stop();
            if(this.enemiesThread!=null && this.enemiesThread.isAlive()) this.enemiesThread.stop();
            this.startPlayerThread();
            this.startEnemiesThread();
            this.setFocusable(true); // needed for the events to be fired
            this.requestFocusInWindow(); // needed for the events to be fired
            // enable key event
            if(this.firstLaunch==true){
                addKeyHandler();
                this.firstLaunch=false;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Record does not exist!", "Error",  JOptionPane.ERROR_MESSAGE);
            this.isLoggedIn=false;
        }
    }
}
