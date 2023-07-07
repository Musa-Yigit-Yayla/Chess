/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 * Class for representing chess moves
 */
public class Move {
    public static final int WHITE_TURN = 1;
    public static final int BLACK_TURN = -1;
    
    private static Move lastMove; // last valid move that has been made
    private static int turn = WHITE_TURN;// white's turn initially
    
    
}
