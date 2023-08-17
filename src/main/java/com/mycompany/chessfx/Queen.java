/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import java.util.Arrays;
/**
 *
 * @author yigit
 */
public class Queen extends Piece{

    public Queen(char color) {
        super(color, 9);
        if(this.getColor().equals(WHITE_COLOR)){
            this.setPoints(WHITE_QUEEN_POINTS);
        }
        else{
            this.setPoints(BLACK_QUEEN_POINTS);
        }
    }
    public Queen(char color, double value, int row, int column){
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
    
    //Queen behaves as a combination of a bishop and a rook, so we will use their corresponding showMoveables methods by instantiating
    //a friendly bishop and a rook that is outside the game
    //Later we will leave these new objects to jvm garbage collection
    @Override
    public Object[] showMoveables() {
        final int row = super.getRow();
        final int column = super.getColumn();
        
        String friendlyColor = this.getColor();
        String enemyColor = super.getEnemyColor();
        
        char color;
        //instantiate a bishop and a rook of the same color independent from our game
        if(friendlyColor.equals(Piece.WHITE_COLOR)){
            color = 'w';
        }
        else{
            color = 'b';
        }
        Bishop b = new Bishop(color);
        Rook r = new Rook(color);
        
        b.setPosition(row, column);
        r.setPosition(row, column);
        
        Object[] arr1 = b.showMoveables();
        Object[] arr2 = r.showMoveables();
        
        //concatenate the two arrays
        Object[] result = Arrays.copyOf(arr1, arr1.length + arr2.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        
        String[] returnArr = new String[result.length];
        for(int i = 0; i < returnArr.length; i++){
            returnArr[i] = (String)result[i];
        }
        
        //Explicitly assign null to instantiated helper objects
        b = null;
        r = null;
        
        return returnArr;
    }
    
    public static String[] showMoveables(double[][] state, int row, int column){
        String friendlyColor = Piece.WHITE_COLOR;
        String enemyColor = Piece.BLACK_COLOR;
        
        if(state[row][column] < 0){
            String temp = friendlyColor;
            friendlyColor = enemyColor;
            enemyColor = temp;
        }
        
        char color;
        //instantiate a bishop and a rook of the same color independent from our game
        if(friendlyColor.equals(Piece.WHITE_COLOR)){
            color = 'w';
        }
        else{
            color = 'b';
        }
        Bishop b = new Bishop(color);
        Rook r = new Rook(color);
        
        b.setPosition(row, column);
        r.setPosition(row, column);
        
        Object[] arr1 = b.showMoveables();
        Object[] arr2 = r.showMoveables();
        
        String[] resultArr1 = new String[arr1.length];
        for(int i = 0; i < resultArr1.length; i++){
            resultArr1[i] = (String)(arr1[i]);
        }
        
        String[] resultArr2 = new String[arr2.length];
        for(int i = 0; i < resultArr2.length; i++){
            resultArr2[i] = (String)(arr2[i]);
        }
        //concatenate the two arrays
        String[] result = Arrays.copyOf(resultArr1, resultArr1.length + resultArr2.length);
        System.arraycopy(resultArr2, 0, result, resultArr1.length, resultArr2.length);
        
        //Explicitly assign null to instantiated helper objects
        b = null;
        r = null;
        
        
        return result;

    }
    @Override
    public void take(Piece taker) {
        super.take(taker);
    }
}
