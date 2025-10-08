package Chess_Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chess {
    private final int BOARD_SIZE = 8;
    private Map<String, Figure> board = new HashMap<>();
    private String currentPlayer = "white";
    private boolean gameEnded = false;

    public Chess() {
        setupBoard();
    }

    public String getCurrentPlayer(){
        return currentPlayer;
    }

    private void setupBoard() {
   
        String[] whiteBackRow = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
        String[] blackBackRow = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
    
        for (int i = 0; i < 8; i++) {
            String col = String.valueOf((char) ('A' + i)); // pravi cifrite na bukvi
            
            // za peshki, slaga cql red, za drugite polzva stringa backrow za reda
            addPiece(whiteBackRow[i], "white", col + "1");
            addPiece("Pawn", "white", col + "2");
            addPiece(blackBackRow[i], "black", col + "8");
            addPiece("Pawn", "black", col + "7");
        }
    }
    
    private void addPiece(String type, String player, String position) {

    switch (type) {
        // case "Rook":
        //     board.put(position, new Rook(this, player, position));
        //     break;
        // case "Knight":
        //     board.put(position, new Knight(this, player, position));
        //     break;
        // case "Bishop":
        //     board.put(position, new Bishop(this, player, position));
        //     break;
        // case "Queen":
        //     board.put(position, new Queen(this, player, position));
        //     break;
        case "King":
            board.put(position, new King(this, player, position));
            break;
        case "Pawn":
            board.put(position, new Pawn(this, player, position));
            break;
        default:
            System.out.println("Unknown figure type: " + type);
            break;
}

    }

    public Figure getAt(String pos) throws Exception {
        if (!isValidPosition(pos)) {
            throw new Exception("Izvun duskata");
        }
        return board.get(pos);
    }

    public void moveFigure(String pos1, String pos2) throws Exception {
        if (gameEnded) {
            throw new Exception("Le fin");
        }
        Figure figure = getAt(pos1);
        if (figure == null) {
            throw new Exception("trqbva da ima figura na pos1");
        }
        if (!figure.getPlayer().equals(currentPlayer)) {
            throw new Exception("ne e pravilniq player");
        }

        Figure target = getAt(pos2);
        if (target != null && target.getPlayer().equals(currentPlayer)) {
            throw new Exception("ne e chujda figura");
        }

        board.remove(pos1);
        board.put(pos2, figure);
        figure.setPosition(pos2);
        System.out.println(currentPlayer + " moves " + figure.getClass().getSimpleName() + " from " + pos1 + " to " + pos2);

        if (target != null) {
            System.out.println(target.getClass().getSimpleName() + " captured!");
        }

        checkForWinner();

        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
    }

    private boolean isValidPosition(String pos) {
        if (pos.length() != 2) return false;
        char col = pos.charAt(0);
        char row = pos.charAt(1);
        return col >= 'A' && col <= 'H' && row >= '1' && row <= '8';
    }

    public void printBoard() {
        System.out.println("  A B C D E F G H");
        for (int row = BOARD_SIZE; row >= 1; row--) {
            System.out.print(row + " ");
            for (char col = 'A'; col <= 'H'; col++) {
                String pos = col + Integer.toString(row);
                Figure figure = board.get(pos);
                if (figure != null) {
                    System.out.print((figure.getPlayer().equals("white") ? figure.getClass().getSimpleName().toUpperCase().charAt(0) : figure.getClass().getSimpleName().toLowerCase().charAt(0)) + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public List<Figure> getAllOpponentPieces(String player) {
        List<Figure> opponentPieces = new ArrayList<>();
        for (Figure piece : board.values()) {
            if (!piece.getPlayer().equals(player)) {
                opponentPieces.add(piece);
            }
        }
        return opponentPieces;
    }

    public List<String> getAdjacentPositions(String pos) {
        List<String> adjacentPositions = new ArrayList<>();
        char col = pos.charAt(0);
        char row = pos.charAt(1);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                char newCol = (char) (col + i);
                char newRow = (char) (row + j);
                if (newCol >= 'A' && newCol <= 'H' && newRow >= '1' && newRow <= '8') {
                    adjacentPositions.add("" + newCol + newRow);
                }
            }
        }
        return adjacentPositions;
    }

    public void checkForWinner() {
        King whiteKing = null;
        King blackKing = null;

        for (Figure piece : board.values()) {
            if (piece instanceof King) {
                if (piece.getPlayer().equals("white")) {
                    whiteKing = (King) piece;
                } else {
                    blackKing = (King) piece;
                }
            }

            // (King) piece castva che piecea e king, ne samo figure, i mu dava isCheck i isMate funkcii
        }

        if (whiteKing != null && whiteKing.isMate()) {
            System.out.println("Black wins! White king is in checkmate.");
        } else if (blackKing != null && blackKing.isMate()) {
            System.out.println("White wins! Black king is in checkmate.");
        } else {
            if (whiteKing != null && whiteKing.isCheck()) {
                System.out.println("White king is in check!");
            }
            if (blackKing != null && blackKing.isCheck()) {
                System.out.println("Black king is in check!");
            }
        }
    }
}

    