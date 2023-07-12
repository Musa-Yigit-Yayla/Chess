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
    public void move() {
       
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
        
        Object[] arr1 = (String[])b.showMoveables();
        Object[] arr2 = (String[])r.showMoveables();
        
        //concatenate the two arrays
        Object[] result = Arrays.copyOf(arr1, arr1.length + arr2.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        
        //Explicitly assign null to instantiated helper objects
        b = null;
        r = null;
        
        return result;
    }

    @Override
    public void take() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
