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
public class Bishop extends Piece{

    public Bishop(char color) {
        super(color, 3.15);
        if(this.getColor().equals(Piece.WHITE_COLOR)){
            this.setPoints(WHITE_BISHOP_POINTS);
        }
        else{
            this.setPoints(BLACK_BISHOP_POINTS);
        }
    }

    @Override
    public void move() {
        
    }

    //Logic is to traverse squares that we can go diagonally, and we will finish that diagonal direction as soon as we encounter
    //a friendly piece (we exclude that), or an enemy piece(we incorporate that)
    //also bounds checking is done
    //@return Object[] holding individual String objects
    @Override
    public Object[] showMoveables() {
        final int row = this.getRow();
        final int column = this.getColumn();
        
        String friendlyColor = super.getColor();
        
        
        ArrayList<String> moveables = new ArrayList<>();
        
        //start from upper left move with clockwise direction
        //going towards upper left
        int currRow = row - 1;
        int currColumn = column - 1;
        
    }
    
}
