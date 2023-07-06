/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
public class Bishop extends Piece{

    public Bishop(char color) {
        super(color);
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
    
}
