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
    @Override
    public void move() {
        
    }
    
    //Get the moveable squares into a local arraylist of strings, no need to show the moveable squares on screen
    @Override
    public void showMoveables() {
        ArrayList<String> moveables = new ArrayList<String>();
        //check the current state of the board
        
    }
    
}
