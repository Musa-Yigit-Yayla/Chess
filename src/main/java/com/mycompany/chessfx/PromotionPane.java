/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 * This class resembles a HBox VBox combination which will be used to
 * create a single pane to enable the user to promote when applicable
 * @author yigit
 */
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
public class PromotionPane extends VBox{
    public static final double PROMOTION_PIECE_LENGTH = 60.0; //60 pixels
    public static final double IMAGE_HOLDER_GAP = 10.0;
    
    private boolean isWhite; //datafield for specifying whether this promotionpane instance contains white pieces and works for white player
    private HBox imageHolder = new HBox();
    private HBox radioButtonBox = new HBox();
    private Button btPromote = new Button("Promote");
    
    public PromotionPane(boolean isWhite){
        this.isWhite = isWhite;
        this.setImageHolder();
    }
    private void setImageHolder(){
        //left to right we go with Queen, Rook, Bishop, and Knight
        ImageView queenImgView = new ImageView();
        ImageView rookImgView = new ImageView();
        ImageView bishopImgView = new ImageView();
        ImageView knightImgView = new ImageView();
        Image queenImg;
        Image rookImg;
        Image bishopImg;
        Image knightImg;
        if(this.isWhite){
           queenImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-white.png"); 
           rookImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-white.png");
           bishopImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-white.png");
           knightImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-white.png");
        }
        else{
            queenImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-black.png");
            rookImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-black.png");
            bishopImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-black.png");
            knightImg = new Image("C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-black.png");
        }
        
    }
}
