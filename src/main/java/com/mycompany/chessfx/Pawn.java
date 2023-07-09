/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
public class Pawn extends Piece {
   private boolean isMoved = false;
   public Pawn(char color){
       super(color, 1);
       if(this.getColor().equals(Piece.WHITE_COLOR)){
           this.setPoints(WHITE_PAWN_POINTS);
       }
       else{
           this.setPoints(BLACK_PAWN_POINTS);
       }
   }
   public void promote(){
           
   }
   @Override
   public void move(){
       
   }

    @Override
    public Object[] showMoveables() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
