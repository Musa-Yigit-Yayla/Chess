/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
public class EmptyPane extends StackPane{
    private String position;
    private Rectangle outerSquare;
    
    public EmptyPane(String pos){
        this.position = pos;
        this.setEventHandling(); //Set the event handling procedure from the constructor, user can manually set it again aswell
    }
    private void setOnClickedAction(){
        System.out.println("An empty pane has been clicked");
        //If we have a selected piece, try and move that piece to this empty pane
        if(App.selectedPiece != null){
            String selectedColor = App.selectedPiece.getColor();
            
            System.out.println("The selected piece is not null");
            
            boolean turnChecker = (selectedColor.equals(Piece.WHITE_COLOR) && Move.getTurn() == Move.WHITE_TURN) || (selectedColor.equals(Piece.BLACK_COLOR) && Move.getTurn() == Move.BLACK_TURN);  
            System.out.println("Turnchecker is " + turnChecker);
            if(turnChecker){
                
                //Move the selected piece to this location if possible
                
                //!!! WE MUST RETRIEVE THE LOCATION IF THIS EMPTY PANE CAREFULLY !!!!
                String nextPos = this.position;
                /*for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        StackPane currNode = App.getPieceHolderNode(i, j);
                        if(this.equals(currNode)){
                            nextPos = Piece.positions[i][j];
                        }
                    }
                }*/
                //ObservableList<Node> pieceHolderNodes = App.getPieceHolder().getChildren();
                if(nextPos == null){
                    System.out.println("CURRENT EMPTY PANE POSITION IS NULL");
                }
                else{
                    System.out.println("Next pos is " + nextPos);
                }
                
                if(App.selectedPiece instanceof King){
                    App.selectedPiece.move(nextPos);
                }
                else{
                    //before we make our move we must ensure here in-place that we do not expose our king after making this move
                    String friendlyColor = App.selectedPiece.getColor();
                    String currPos = App.selectedPiece.getPosition();
                    double[][] state = App.retrieveGameState(currPos, nextPos);
                    App.printMatrix(state);
                    //Pass the enemy color as the friendly color otherwise there is logic error
                    String enemyColor = App.selectedPiece.getEnemyColor();
                    String kingPos = App.getKingPosition(state, friendlyColor);
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX About that beer I owe ya, king pos is " + kingPos + ", king color is " + friendlyColor);
                    if(!App.isChecked(state, friendlyColor, kingPos)){
                        System.out.println("+++++++++++++++++++++++++++++++++++ NEXT STATE DOES NOT EXPOSE OUR KING APPARENTLY");
                        System.out.println("+++++++++++++++++++++++++++++++++++++ Curr pos is " + currPos + ", next pos is " + nextPos);
                        App.selectedPiece.move(nextPos);
                    }
                    else{
                        System.out.println("NEEEEEEEEEEEEEEEEED SOME HELPPP OVER HEREEEEEE DOC");
                        //We should still check whether we can cover our king from all checking pieces by moving into this designated location
                        //the selected piece, previous if condition is not adequate on its own to cover all cases
                        //ArrayList<Piece> checkingEnemies = new ArrayList<>(); //a container for the pieces which check the selected piece's king
                        //on the given state parameter
                        
                        //retrieve the checking pieces !!!THE FOLLOWING BLOCK IS TAKEN FROM PIECEPANE
            
                        
                        String presentKingPos = App.getKingPosition(state, friendlyColor);
                        King friendlyKing = (King)((PiecePane)(App.getGridNode(App.getPieceHolder(), Piece.getRow(presentKingPos), Piece.getColumn(presentKingPos)))).getPiece();
                        //retrieve each and every piece that checks our king by using getPath function
                        ArrayList<Piece> checkingEnemies = new ArrayList<>();
                        for(int i = 0; i < App.currentPieces.size(); i++){
                           Piece currPiece = App.currentPieces.get(i);
                           if(!(currPiece.getColor().equals(friendlyColor)) && !(currPiece instanceof King )){
                               //we ensured that we have an enemy piece which is not the king, now we must check whether it checks our king
                               String[] path = App.getPath(friendlyKing, currPiece);
                               if(path != null){
                                   checkingEnemies.add(currPiece);
                               }
                           }

                        }
                        //we have managed to retrieve the checking enemies, now we need to check whether we can save our king by moving to this empty pane
                        if(App.evaluateStateForCheck(checkingEnemies, currPos, kingPos)){
                            //the state is not checked, we can move to this square successfully !!!
                            App.selectedPiece.move(nextPos);
                        }
                    }
                }
            }
        }
    }
    //Check whether user intends to castle his/her king
    //Invoke this when user presses an empty pane (square in checker board) which may enable our king to castle providing that king and rooks are not moved
    //You may not have to complete this method, go through implementing move functions of piece subclasses, then take a look at here if necessary
    private boolean isCastling(){
        //retrieve nodes from pieceHolder pane of App class and find this empty pane
        GridPane pieceHolder = App.getPieceHolder();
        
        return false; //ToDo
    }
    public void setOuterFrame(boolean isAdded) {
        if(this.outerSquare != null){
            //remove the previous outer square representation
            this.getChildren().remove(this.outerSquare);
            this.outerSquare = null;
        }
        if(isAdded){
            this.outerSquare = new Rectangle(PiecePane.OUTER_SQUARE_LENGTH, PiecePane.OUTER_SQUARE_LENGTH);
            this.outerSquare.setFill(PiecePane.FILL_COLOR);
            this.outerSquare.setStroke(PiecePane.MOVEABLE_COLOR);
            this.outerSquare.setStrokeWidth(PiecePane.OUTER_SQUARE_STROKE_WIDTH);
            this.getChildren().add(this.outerSquare);
        }
    }
    //Pass the square's coordinates, and it will check whether the square in the given position is threatened by any enemy piece(s)
    //Use when castling and after each move to see whether a king is checked
    //Can be invoked on any square regardless of whether it holds a piece or not
    //@return true when the square is threatened by the enemy
    public static boolean isSquareThreatened(String pos, String friendlyColor){
        String enemyColor = null;
        if(friendlyColor.equals(Piece.WHITE_COLOR)){
            enemyColor = Piece.BLACK_COLOR;
        }
        else{
            enemyColor = Piece.WHITE_COLOR;
        }
        System.out.println("We are about to check whether the square " + pos + " is threatened by color " + enemyColor);
        //Get all enemy colored pieces
        ArrayList<Piece> enemyPieces = new ArrayList<>();
        for(Piece e: App.currentPieces){
            //traverse each and every alive piece and obtain the ones that are considered as enemy
            if(e.getColor().equals(enemyColor)){
                enemyPieces.add(e);
            }
        }
        boolean result = false;
        
        boolean isKingSelected = (App.selectedPiece instanceof King);
        
        if(isKingSelected){
            return EmptyPane.isFriendlySquareGuarded(pos, enemyPieces);
        }
        else{
            for(int i = 0; i < enemyPieces.size(); i++){
                Piece currPiece = enemyPieces.get(i);
                //int currRow = currPiece.getRow();
                //int currColumn = currPiece.getColumn();
                Object[] currMoveables;
                if(!(currPiece instanceof King)){
                    currMoveables = currPiece.showMoveables(); // !! CAREFUL, THIS LINE MAY LEAD TO INFINITE RECURSION DUE TO KING'S METHOD
                    for(int j = 0; j < currMoveables.length; j++){
                        if(((String)(currMoveables[j])).equals(pos)){
                            return true;
                        }
                    }
                }
                else{
                    //check manually so as to avoid infinite recursion
                    //current piece is guaranteed to be the enemy king
                    int row = Piece.getRow(pos);
                    int column = Piece.getColumn(pos);

                    int kingRow = currPiece.getRow();
                    int kingColumn = currPiece.getColumn();
                    for(int k = -1; k < 2; k++){
                        for(int j = -1; j < 2; j++){
                            int currRow = kingRow + k;
                            int currColumn = kingColumn + j;

                            if(currRow == kingRow && currColumn == kingColumn){
                                continue;
                            }
                            if(currRow == row && currColumn == column){
                                return true;
                            }
                        }
                    }

                }
                //else if(currPiece instanceof Bishop){

                //}
            }
            //after having checked each and every enemy piece for whether it threatens our square, if we haven't returned yet
            //we can return the result which is a boolean false
            return result;
        }
    }
    
    public void setEventHandling() {
       this.setOnMouseClicked( e -> {
          this.setOnClickedAction();
       });
    }
    //Static utility function to check whether a piece is guarded by friendly pieces
    //We won't use showMoveables of friendly pieces as it doesn't regard a square occupied by a friendly piece as a moveable spot
    //Check manually and inline instead of using methods we have built so far
    //Invoke from isSquareThreatened when we check for a king move
    public static boolean isFriendlySquareGuarded(String pos, ArrayList<Piece> friendlyPieces){
        if(friendlyPieces != null){
            String friendlyColor = friendlyPieces.get(0).getColor();
            char chColor = 'w';
            if(friendlyColor.equals(Piece.BLACK_COLOR)){
                chColor = 'b';
            }
            int posRow = Piece.getRow(pos);
            int posColumn = Piece.getColumn(pos);
            for(Piece p: friendlyPieces){
                int pieceRow = p.getRow();
                int pieceColumn = p.getColumn();
                if(p instanceof Pawn){
                    //determine the movement direction of the pawn based on its color
                    int dy = -1; // for white value
                    if(chColor == 'b'){
                        dy = 1;
                    }
                    if(pieceRow + dy == posRow && Math.abs(posColumn - pieceColumn) == 1){
                        return true;
                    }
                }
                else if(p instanceof Knight){
                    Object[] knightMoveables = p.showMoveables();
                    for(int i = 0; i < knightMoveables.length; i++){
                        String currMoveable = (String)(knightMoveables[i]);
                        if(pos.equals(currMoveable)){
                            return true;
                        }
                    }
                }
                else if(p instanceof Bishop){
                    //If the absolute value of the slope is 1 and path is clear
                    int slope = 0;
                    if(pieceColumn != posColumn){
                        slope = Math.abs((pieceRow - posRow) / (pieceColumn - posColumn));
                    }
                    
                    if(slope == 1){
                        String closePos = null;
                        Object[] bishopMoveables = p.showMoveables();
                        
                        //check all 4 possible directions between the path between bishop and the given pos and set the closePos accordingly
                        
                        //check for bishop on top left
                        if(posRow > pieceRow && posColumn > pieceColumn){
                            closePos = Piece.positions[posRow - 1][posColumn - 1];
                        }
                        //check for bishop on top right
                        else if(posRow > pieceRow && posColumn < pieceColumn){
                            closePos = Piece.positions[posRow - 1][posColumn + 1];
                        }
                        //check for bottom left
                        else if(posRow < pieceRow && posColumn > pieceColumn){
                            closePos = Piece.positions[posRow + 1][posColumn - 1];
                        }
                        else if(posRow < pieceRow && posColumn < pieceColumn){
                            closePos = Piece.positions[posRow + 1][posColumn + 1];
                        }
                        //Iterate over bishop's moveables and check whether we have the closePos
                        for(Object o: bishopMoveables){
                            //we have to exclude the positions which are occupied by the enemy pieces of bishop
                            String curr = (String)o;
                            int currRow = Piece.getRow(curr);
                            int currColumn = Piece.getColumn(curr);
                            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
                            if(closePos.equals(curr) && !(currSquare instanceof PiecePane)){
                                return true;
                            }
                        }
                    }
                }
                else if(p instanceof Rook){
                    Object[] rookMoveables = p.showMoveables();
                    //again set the close pos
                    String closePos = null;
                    int dx = Integer.MAX_VALUE;
                    int dy = Integer.MAX_VALUE; //the direction of path along we will be checking for obstructions
                    if(pieceRow == posRow){
                        //check the columns
                        //rook is positioned at right
                        if(pieceColumn > posColumn){
                            dx = -1;
                            closePos = Piece.positions[posRow][posColumn + 1];
                        }
                        //the rook is positioned at left
                        else if(pieceColumn < posColumn){
                            dx = 1;
                            closePos = Piece.positions[posRow][posColumn - 1];
                        }
                    }
                    else if(pieceColumn == posColumn){
                        //rook is positioned at above
                        if(pieceRow < posRow){
                            dy = 1;
                            closePos = Piece.positions[posRow - 1][posColumn];
                        }
                        //rook is positioned at below
                        else if(pieceRow > posRow){
                            dy = -1;
                            closePos = Piece.positions[posRow + 1][posColumn];
                        }
                    }
                    //check whether we have the closePos as one of our moveables excluding the squares which are occuppied
                    if(closePos != null){
                        for(Object o: rookMoveables){
                            String curr = (String)o;
                            int currRow = Piece.getRow(curr);
                            int currColumn = Piece.getColumn(curr);
                            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
                            if(closePos.equals(curr) && !(currSquare instanceof PiecePane)){
                                return true;
                            }
                        }
                    }
                }
                else if(p instanceof Queen){
                    //apply the same procedures with queen and rook based on the slope
                    //If the absolute value of the slope is 1 and path is clear
                    int slope = 0;
                    if(pieceColumn != posColumn){
                        slope = Math.abs((pieceRow - posRow) / (pieceColumn - posColumn));
                    }
                    
                    if(slope == 1){
                        String closePos = null;
                        Object[] bishopMoveables = p.showMoveables();
                        
                        //check all 4 possible directions between the path between bishop and the given pos and set the closePos accordingly
                        
                        //check for bishop on top left
                        if(posRow > pieceRow && posColumn > pieceColumn){
                            closePos = Piece.positions[posRow - 1][posColumn - 1];
                        }
                        //check for bishop on top right
                        else if(posRow > pieceRow && posColumn < pieceColumn){
                            closePos = Piece.positions[posRow - 1][posColumn + 1];
                        }
                        //check for bottom left
                        else if(posRow < pieceRow && posColumn > pieceColumn){
                            closePos = Piece.positions[posRow + 1][posColumn - 1];
                        }
                        else if(posRow < pieceRow && posColumn < pieceColumn){
                            closePos = Piece.positions[posRow + 1][posColumn + 1];
                        }
                        //Iterate over bishop's moveables and check whether we have the closePos
                        for(Object o: bishopMoveables){
                            //we have to exclude the positions which are occupied by the enemy pieces of bishop
                            String curr = (String)o;
                            int currRow = Piece.getRow(curr);
                            int currColumn = Piece.getColumn(curr);
                            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
                            if(closePos.equals(curr) && !(currSquare instanceof PiecePane)){
                                return true;
                            }
                        }
                    }
                    else{
                        Object[] rookMoveables = p.showMoveables();
                        //again set the close pos
                        String closePos = null;
                        int dx = Integer.MAX_VALUE;
                        int dy = Integer.MAX_VALUE; //the direction of path along we will be checking for obstructions
                        if(pieceRow == posRow){
                            //check the columns
                            //rook is positioned at right
                            if(pieceColumn > posColumn){
                                dx = -1;
                                closePos = Piece.positions[posRow][posColumn + 1];
                            }
                            //the rook is positioned at left
                            else if(pieceColumn < posColumn){
                                dx = 1;
                                closePos = Piece.positions[posRow][posColumn - 1];
                            }
                        }
                        else if(pieceColumn == posColumn){
                            //rook is positioned at above
                            if(pieceRow < posRow){
                                dy = 1;
                                closePos = Piece.positions[posRow - 1][posColumn];
                            }
                            //rook is positioned at below
                            else if(pieceRow > posRow){
                                dy = -1;
                                closePos = Piece.positions[posRow + 1][posColumn];
                            }
                        }
                        //check whether we have the closePos as one of our moveables excluding the squares which are occuppied
                        if(closePos != null){
                            for(Object o: rookMoveables){
                                String curr = (String)o;
                                int currRow = Piece.getRow(curr);
                                int currColumn = Piece.getColumn(curr);
                                StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
                                if(closePos.equals(curr) && !(currSquare instanceof PiecePane)){
                                    return true;
                                }
                            }
                        }
                    }
                }
                else if(p instanceof King){ //the if condition is just for increasing the readability
                    //check the adjacent squares of the king
                    System.out.println("~~~WE CHECK WHETHER " + p.getColor() + " KING IS GUARDING THE SQUARE " + pos);
                    for(int i = pieceColumn - 1; i <= pieceColumn + 1; i++){
                        for(int j = pieceRow - 1; j <= pieceRow + 1; j++){
                            boolean isValidPos = ((j >= 0 && j < 8) && (i >= 0 && i < 8) && !(pieceRow == j && pieceColumn == i));
                            if(isValidPos){
                                String currPos = Piece.positions[j][i];
                                if(currPos.equals(pos)){
                                    System.out.println("~~~APPARENTLY IT DOES");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
