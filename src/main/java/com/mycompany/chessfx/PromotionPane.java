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
import javafx.scene.control.ToggleGroup;
public class PromotionPane extends VBox{
    public static final double PROMOTION_PIECE_LENGTH = 60.0; //60 pixels
    public static final double IMAGE_HOLDER_GAP = 10.0;
    public static final String QUEEN_SELECTION_STRING = "Queen";
    public static final String ROOK_SELECTION_STRING = "Rook";
    public static final String BISHOP_SELECTION_STRING = "Bishop";
    public static final String KNIGHT_SELECTION_STRING = "Knight";
    
    private boolean isWhite; //datafield for specifying whether this promotionpane instance contains white pieces and works for white player
    private HBox imageHolder = new HBox();
    private HBox radioButtonBox = new HBox();
    private Button btPromote = new Button("Promote");
    private ToggleGroup tg;
    
    public PromotionPane(boolean isWhite){
        this.isWhite = isWhite;
        this.setImageHolder();
        this.setRadioButtonBox();
        this.setPromoteHandler();
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
        queenImgView.setImage(queenImg);
        rookImgView.setImage(rookImg);
        bishopImgView.setImage(bishopImg);
        knightImgView.setImage(knightImg);
        imageHolder.getChildren().addAll(queenImgView, rookImgView, bishopImgView, knightImgView);
        imageHolder.setSpacing(IMAGE_HOLDER_GAP);
    }
    private void setRadioButtonBox(){
        this.tg = new ToggleGroup();
        
        RadioButton btQueen = new RadioButton(PromotionPane.QUEEN_SELECTION_STRING);
        RadioButton btRook = new RadioButton(PromotionPane.ROOK_SELECTION_STRING);
        RadioButton btBishop = new RadioButton(PromotionPane.BISHOP_SELECTION_STRING);
        RadioButton btKnight = new RadioButton(PromotionPane.KNIGHT_SELECTION_STRING);
        
        btQueen.setToggleGroup(this.tg);
        btRook.setToggleGroup(this.tg);
        btBishop.setToggleGroup(this.tg);
        btKnight.setToggleGroup(this.tg);
    }
    private void setPromoteHandler(){
        this.btPromote.setOnAction( e-> {
            //retrieve the last move and if it was a friendly pawn moving into its designated promotion row then proceed
            Move lastMove = Move.getLastMove();
            Piece movedPiece = lastMove.getPiece();
            if(movedPiece instanceof Pawn && ((this.isWhite && movedPiece.getColor().equals(Piece.WHITE_COLOR)) || (!this.isWhite && movedPiece.getColor().equals(Piece.BLACK_COLOR)))){
                //Apparently the last move was made by a pawn and its color matches this PromotionPane instance
                int newRow = Piece.getRow(lastMove.getNewPos());
                if(newRow == 0 || newRow == 7){
                    //We need to promote this piece with respect to promotion selection in the radio buttons
                    RadioButton selectedBt = (RadioButton)this.tg.getSelectedToggle();
                    String selectedString = selectedBt.getText();
                    ((Pawn)(movedPiece)).promote(selectedString);
                }
            }
        });
    }
}
