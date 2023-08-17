/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import java.util.ArrayList;
/**
 *
 * @author yigit
 */
public class Knight extends Piece{
    
    public Knight(char color){
        super(color, 3);
        if(this.getColor().equals(WHITE_COLOR)){
            this.setPoints(WHITE_KNIGHT_POINTS);
        }
        else{
            this.setPoints(BLACK_KNIGHT_POINTS);
        }
    }
    public Knight(char color, double value, int row, int column){
        super(color, value, row, column);
    }
    @Override
    public void move(String nextPos) {
       String currPos = super.getPosition();
       super.move(nextPos);
       String newCurrPos = super.getPosition();
       boolean isMoved = (nextPos.equals(newCurrPos) && (!currPos.equals(newCurrPos)));
       if(isMoved){
           //alternate the move turn and create a new Move instance that will be used as the last move
           Move move = new Move(this, currPos, newCurrPos);
       }
    }
    
    //Get the moveable squares into a local arraylist of strings, no need to show the moveable squares on screen
    //We only check for bounds validation here, other validations such as check status must be done during the event handling process
    @Override
    public Object[] showMoveables() {
        ArrayList<String> moveables = new ArrayList<String>();
        String pos = super.getPosition();
        int row = 0, column = 0;
        boolean broken = false;
        /*for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(positions[i][j].equals(pos)){
                    row = i;
                    column = j;
                    broken = true;
                    break;
                }
            }
            if(broken){
                break;
            }
        }*/
        row = this.getRow();
        column = this.getColumn();
        if(row + 2 < 8 && column + 1 < 8){
            moveables.add(positions[row + 2][column + 1]);
        }
        //1 right 2 up
        if(row + 1 < 8 && column + 2 < 8){
            moveables.add(positions[row + 1][column + 2]);
        }
        //1 left 2 up
        if(row - 1 >= 0 && column + 2 < 8){
            moveables.add(positions[row -1][column + 2]);
        }
        //2 left 1 up
        if(row - 2 >= 0 && column + 1 < 8){
            moveables.add(positions[row - 2][column + 1]);
        }//2 left 1 down
        if(row - 2 >= 0 && column - 1 >= 0){
            moveables.add(positions[row - 2][column - 1]);
        }
        //1 left 2 down
        if(row - 1 >= 0 && column - 2 >= 0 ){
            moveables.add(positions[row - 1][column - 2]);
        }
        //1 right 2 down
        if(row + 1 < 8 && column - 2 >= 0 ){
            moveables.add(positions[row + 1][column - 2]);
        }
        //2 right 1 down
        if(row + 2  < 8 && column - 1 >= 0 ){
            moveables.add(positions[row + 2][column - 1]);
        }
        return moveables.toArray();
    }
    public static String[] showMoveables(double[][] state, int row, int column){
        ArrayList<String> moveables = new ArrayList<String>();
        //String pos = Piece.positions[row][column];
        //row = 0; column = 0;
        //boolean broken = false;
        /*for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(positions[i][j].equals(pos)){
                    row = i;
                    column = j;
                    broken = true;
                    break;
                }
            }
            if(broken){
                break;
            }
        }*/
        
        if(row + 2 < 8 && column + 1 < 8){
            moveables.add(positions[row + 2][column + 1]);
        }
        //1 right 2 up
        if(row + 1 < 8 && column + 2 < 8){
            moveables.add(positions[row + 1][column + 2]);
        }
        //1 left 2 up
        if(row - 1 >= 0 && column + 2 < 8){
            moveables.add(positions[row -1][column + 2]);
        }
        //2 left 1 up
        if(row - 2 >= 0 && column + 1 < 8){
            moveables.add(positions[row - 2][column + 1]);
        }//2 left 1 down
        if(row - 2 >= 0 && column - 1 >= 0){
            moveables.add(positions[row - 2][column - 1]);
        }
        //1 left 2 down
        if(row - 1 >= 0 && column - 2 >= 0 ){
            moveables.add(positions[row - 1][column - 2]);
        }
        //1 right 2 down
        if(row + 1 < 8 && column - 2 >= 0 ){
            moveables.add(positions[row + 1][column - 2]);
        }
        //2 right 1 down
        if(row + 2  < 8 && column - 1 >= 0 ){
            moveables.add(positions[row + 2][column - 1]);
        }
        String[] returnArr = new String[moveables.size()];
        for(int i = 0; i < returnArr.length; i++){
            returnArr[i] = moveables.get(i);
        }
        return returnArr;
    }
    @Override
    public void take(Piece taker) {
       super.take(taker);
    }
    
}
