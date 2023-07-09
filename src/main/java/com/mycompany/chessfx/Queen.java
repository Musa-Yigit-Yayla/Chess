/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

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

    @Override
    public void move() {
       
    }
    public void castle(){
        
    }

    @Override
    public Object[] showMoveables() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
