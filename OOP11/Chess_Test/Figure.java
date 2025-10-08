package Chess_Test;

abstract class Figure {
    protected Chess game;
    protected String player; // "white" ili "black"
    protected String position;

    public Figure(Chess game, String player, String position) {
        this.game = game;
        this.player = player;
        this.position = position;
    }

    public String getPlayer() {
        return player;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public abstract boolean canMove(String newPosition);

    public void move(String newPosition) throws Exception {
        if (!canMove(newPosition)) {
            throw new Exception("Invalid move");
        }
        game.moveFigure(position, newPosition);
        setPosition(newPosition);
    }
}
