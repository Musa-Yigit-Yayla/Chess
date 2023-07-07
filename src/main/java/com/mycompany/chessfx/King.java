/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
public class King extends Piece{
    private boolean isMoved = false;
    
    public King(char color){
        super(color, 90);
        if(super.getColor().equals(Piece.WHITE_COLOR)){
            this.setPoints(WHITE_KING_POINTS);
        }
        else{
            this.setPoints(BLACK_KING_POINTS);
        }
    }
    @Override
    public void move() {
    
    }
    public void castle(){
        
    }
    public boolean isChecked(){
        return true;
    }

    @Override
    public void showMoveables() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
