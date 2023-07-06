/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
public class Rook extends Piece{
    private boolean isMoved = false;
    
    public Rook(char color){
        super(color);
        if(this.getColor().equals(WHITE_COLOR)){
            this.setPoints(WHITE_ROOK_POINTS);
        }
        else{
            this.setPoints(BLACK_ROOK_POINTS);
        }
    }
    @Override
    public void move() {
        
    }
}
