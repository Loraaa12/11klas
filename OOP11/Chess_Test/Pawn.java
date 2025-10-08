package Chess_Test;

public class Pawn extends Figure {
    public Pawn(Chess game, String player, String position) {
        super(game, player, position);
    }

    @Override
    public boolean canMove(String newPosition) {
        int direction = player.equals("white") ? 1 : -1;
        int rowDiff = newPosition.charAt(1) - position.charAt(1);
        int colDiff = newPosition.charAt(0) - position.charAt(0);

        if (colDiff == 0 && rowDiff == direction) {
            try {
                return game.getAt(newPosition) == null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Math.abs(colDiff) == 1 && rowDiff == direction) {
            try {
                Figure target = game.getAt(newPosition);
                return target != null && !target.getPlayer().equals(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
