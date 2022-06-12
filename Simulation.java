
package com.mycompany.gameoflifesimulator;

/**
 *
 * @author Master
 */
public class Simulation {
    
    public static final int DEAD = 0;
    public static final int ALIVE = 1;

    int width;
    int height;
    int[][] board;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
    }

    public void printBoard() {
        for (int y = 0; y < height; y++) {
            String line = "|";
            for (int x = 0; x < width; x++) {
                if (this.board[x][y] == DEAD) {
                    line += ".";
                } else {
                    line += "*";
                }
            }
            line += "|";
            System.out.println(line);
        }
        System.out.println("---------------\n");
    }
    
    public void setAlive(int x, int y){
        this.setState(x, y, ALIVE);
    }
    
    public void setDead(int x, int y){
        this.setState(x, y, DEAD);
    }
    
    public void setState(int x, int y, int state){
        if((x<0 || x>=width) || (y<0 || y>=height)){
            return;
        }
        
        this.board[x][y] = state;
    }
    
    public int countAliveNeighbours(int x, int y){
        int count = 0;
        
        for(int i=-1; i<=+1; i++){
            for(int j=-1; j<=+1; j++){
                if(!(i==0 && j==0)){
                    count+=this.board[(x+i+width)%width][(y+j+height)%height];
                }
            }
        }
        return count;
    }
    
    public int getState(int x, int y){
        if(x<0 || x>=width){
            return DEAD;
        }
        if(y<0 || y>=height){
            return DEAD;
        }
        return board[x][y];
    }
    
    public void step(){
        int [][] newBoard = new int[width][height];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aliveNeighbours = countAliveNeighbours(x, y);
                if(this.board[x][y]==ALIVE){
                    if(aliveNeighbours < 2 || aliveNeighbours>3){
                        newBoard[x][y]=DEAD;
                    } else {
                        newBoard[x][y]=ALIVE;
                    }
                } else {
                    if(aliveNeighbours == 3){
                        newBoard[x][y]=ALIVE;
                    } 
                }
            }
        } 
        this.board =  newBoard;
    }
    
    public static void main(String[] args) throws InterruptedException{
        Simulation simulation = new Simulation(8, 5);
        simulation.setAlive(3, 0);
        simulation.setAlive(3, 1);
        simulation.setAlive(3, 4);
        
        System.out.println(simulation.countAliveNeighbours(2, 2));
        
        simulation.printBoard();
        for(int i=0; i<10; i++){
            simulation.step();
            simulation.printBoard();
        }
    }
}
