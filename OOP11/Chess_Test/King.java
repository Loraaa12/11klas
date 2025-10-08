package Chess_Test;

public class King extends Figure {
    
    public King(Chess game, String player, String position) {
        super(game, player, position);
    }

    @Override
    public boolean canMove(String pos) {
        char col = position.charAt(0);
        char row = position.charAt(1);
        char newCol = pos.charAt(0);
        char newRow = pos.charAt(1);

        int colDiff = Math.abs(newCol - col);
        int rowDiff = Math.abs(newRow - row);
        
        if ((colDiff <= 1 && rowDiff <= 1) && (colDiff + rowDiff > 0)) {
            return !wouldBeInCheck(pos);
        }
        return false;
    }

    @Override
    public void move(String pos) {
        if (canMove(pos)) {
            try {
                game.moveFigure(position, pos);
            } catch (Exception e) {
                System.out.println("Error during move: " + e.getMessage());
            }
            this.position = pos;
            
        } else {
            throw new IllegalArgumentException("Invalid move for King.");
        }
    }


    private boolean wouldBeInCheck(String newPos) {
        String originalPosition = position;
        position = newPos;
        boolean inCheck = isCheck();
        position = originalPosition; 
        return inCheck;
    }

    public boolean isCheck() {
        for (Figure piece : game.getAllOpponentPieces(player)) {
            if (piece.canMove(position)) { 
                return true;
            }
        }
        return false;
    }

    public boolean isMate() {
        if (!isCheck()) {
            return false;
        }
        for (String newPos : game.getAdjacentPositions(position)) {
            if (canMove(newPos) && !wouldBeInCheck(newPos)) {
                return false;
            }
        }
        return true; 
    }
}
