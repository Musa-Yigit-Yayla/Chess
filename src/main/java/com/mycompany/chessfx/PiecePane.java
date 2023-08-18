/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//Holds individual pieces
public class PiecePane extends StackPane{
    public final double PANE_WIDTH = 80.0;
    public final double PANE_HEIGHT = 80.0;
    
    public static final Color SELECTED_COLOR = Color.GREEN;
    public static final Color MOVEABLE_COLOR = Color.DARKBLUE;
    public static final Color CHECKED_COLOR = Color.CRIMSON;
    public static final Color FILL_COLOR = Color.TRANSPARENT;
    public static final double OUTER_SQUARE_LENGTH = App.SQUARE_LENGTH - 6.0;
    public static final double OUTER_SQUARE_STROKE_WIDTH = 3.0;
    
    private Piece piece;
    private Image image;
    private ImageView imgView = new ImageView();
    private GridPane pieceHolder; // retrieve from App.java
    private Rectangle outerSquare;
    
    public PiecePane(Piece piece){
        this.piece = piece;
        this.pieceHolder = App.getPieceHolder();
        this.setPrefSize(App.SQUARE_LENGTH, App.SQUARE_LENGTH);
        this.setImage();
        this.setEventHandlers();
        //this.getChildren().add(imgView);
    }
    //Sets the image with respect to color and type of the piece
    //Must be invoked when a pawn is about to be promoted, notice that first you must update the pawn's point data field
    public void setImage(){
        //this.getChildren().remove(this.imgView);// first delete the current image if exists
        double p = this.piece.getPoints();
        String filePath = "";
        if(p == Piece.WHITE_PAWN_POINTS){
            //white pawn
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-white.png";
        }
        else if(p == Piece.BLACK_PAWN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-black.png";
        }
        else if(p == Piece.WHITE_KNIGHT_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-white.png";
        }
        else if(p == Piece.BLACK_KNIGHT_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-black.png";
        }
        else if(p == Piece.WHITE_BISHOP_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-white.png";
        }
        else if(p == Piece.BLACK_BISHOP_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-black.png";
        }
        else if(p == Piece.WHITE_ROOK_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-white.png";
        }
        else if(p == Piece.BLACK_ROOK_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-black.png";
        }
        else if(p == Piece.WHITE_QUEEN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-white.png";
        }
        else if(p == Piece.BLACK_QUEEN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-black.png";
        }
        else if(p == Piece.WHITE_KING_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-white.png";
        }
        else if(p == Piece.BLACK_KING_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-black.png";
        }
        //else{
            //ilePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-black.png";
        //}
        this.image = new Image(new File(filePath).toURI().toString());
        this.imgView.setImage(image);
        
        this.imgView.setFitHeight(this.PANE_HEIGHT);
        this.imgView.setFitWidth(this.PANE_WIDTH);
        this.getChildren().add(this.imgView);
        //this.getChildren().add(new Circle(45));
        
        //add the PiecePane to the pieceHolder 
        //this.pieceHolder.add(this, 0, 0);
    }
    
    //Invoke for once and only for each piece during the creation of the pieces
    public void setContainer(GridPane pieceHolder){
        this.pieceHolder = pieceHolder;
        String pos = this.piece.getPosition();
        int i, j = 0;
        for(i = 0; i < 8; i++){
            boolean broken = false;
            for(j = 0; j < 8; j++){
                if(Piece.positions[i][j].equals(pos)){
                    broken = true;
                    break;
                }
            }
            if(broken){
                break;
            }
        }
        //i = 7 - i; // THIS LINE IS VERY CRUCIAL SINCE IT ALLOWS US TO HAVE WHITE PIECES AT BOTTOM AND BLACK AT THE TOP, NOTICE
        //WE DON'T ALTERNATE THE DATA FIELD OF POSITION AT OBJECT LEVEL
        //revert i and j since we have column then row on add function
        this.pieceHolder.add(this, j, i);
    }
    public void setPiece(Piece piece){
        this.piece = piece;
    }
    public Piece getPiece(){
        return this.piece;
    }
    //@isAdded: boolean, if it's true then we are drawing the frame otherwise assigning null to the data field after having removed from the piecePane
    //@isSelectedPiece: boolean, it will help us to determine which color this piece pane instance should have an outer stroke of
    public void setOuterFrame(boolean isAdded, boolean isSelectedPiece){
        if(this.outerSquare != null){
                //remove the previous outer square representation
                this.getChildren().remove(this.outerSquare);
                this.outerSquare = null;
        }
        if(isAdded){
            this.outerSquare = new Rectangle(PiecePane.OUTER_SQUARE_LENGTH, PiecePane.OUTER_SQUARE_LENGTH);
            this.outerSquare.setFill(PiecePane.FILL_COLOR);
            this.outerSquare.setStrokeWidth(PiecePane.OUTER_SQUARE_STROKE_WIDTH);
            if(isSelectedPiece){
                this.outerSquare.setStroke(PiecePane.SELECTED_COLOR);
            }
            else{
                this.outerSquare.setStroke(PiecePane.MOVEABLE_COLOR);
            }
            this.getChildren().add(this.outerSquare);
        }
        else if(isSelectedPiece){
            this.outerSquare = new Rectangle(PiecePane.OUTER_SQUARE_LENGTH, PiecePane.OUTER_SQUARE_LENGTH);
            this.outerSquare.setFill(PiecePane.FILL_COLOR);
            this.outerSquare.setStrokeWidth(PiecePane.OUTER_SQUARE_STROKE_WIDTH);
            this.outerSquare.setStroke(PiecePane.SELECTED_COLOR);
            this.getChildren().add(this.outerSquare);
        }
    }
    //Currently sets on clicked, setOnDragged and drag exit could be implemented in future
    public void setEventHandlers(){
        //set on click
        this.setOnMouseClicked(e->{
            //remove the previosuly displayed moveables for providing better user interface
            App.eraseMoveablesFromPane();
            
            boolean turnChecker = (this.piece.getColor().equals(Piece.WHITE_COLOR) && Move.getTurn()) || (this.piece.getColor().equals(Piece.BLACK_COLOR) && !Move.getTurn());
            System.out.println("Turn checker is: " + turnChecker);
            System.out.println("Curr piece color is: " + this.piece.getColor() + ". Move turn is " + Move.getTurn());
            if(turnChecker){
                //turn checker checks whether the piece contained by this PiecePane instance satisfies the color required for the current turn
                //Select the current piece that this piecePane holds
                App.selectedPiece = this.piece;
                System.out.println("We have selected the initial piece, current selected piece color is " + App.selectedPiece.getColor());
                
                //display the moveables of the recently selected piece
                App.displayMoveables();
            }
            else if(App.selectedPiece != null){
                //We already have a selected piece, now the user might have clicked an enemy piece contained in this pane in which we can take
                String selectedPieceColor = App.selectedPiece.getColor();
                
                System.out.println("We are trying to take a clicked piece");
                if(!selectedPieceColor.equals(this.piece.getColor()) && !(this.piece instanceof King)){ //colors do not match hence we have an enemy piece
                    //before performing the take operation make sure that we do have the selected piece in the reach of this piece
                    //!!!!!!MIGHT NEED TO DO EXTRA WORK FOR KING'S SHOW MOVEABLES OR OTHER STUFF !!!!!!!
                    System.out.println("We are inside the first condition for take operation");
                    Object[] selectedPieceMoveables = App.selectedPiece.showMoveables();
                    boolean isReachable = false;
                    String currPos = this.piece.getPosition();
                    for(int i = 0; i < selectedPieceMoveables.length; i++){
                        if(currPos.equals((String)selectedPieceMoveables[i])){
                            isReachable = true;
                        }
                    }
                    
                    if(isReachable){
                        //take the piece contained by this guy the panes will be updated accordingly                    
                        System.out.println("We are invoking the take method on a piece");
                        this.piece.take(App.selectedPiece);
                    }
                }
                else{
                    //display the moveables of the recently selected piece after having selecting the piece contained by this piece pane
                    App.displayMoveables();
                }
            }
        });
    }
}

//This comment is for converting File objects into respective images or media
//Media media = new Media(new File(filePath).toURI().toString());