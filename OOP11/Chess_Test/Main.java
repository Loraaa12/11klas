package Chess_Test;

import java.util.Scanner;

public class Main {
        public static void main(String[] args) {
        Chess game = new Chess();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            game.printBoard();
            System.out.println(game.getCurrentPlayer() + "'s turn:");
            System.out.print("Enter start position: ");
            String start = scanner.next();
            System.out.print("Enter end position: ");
            String end = scanner.next();

            try {
                Figure figure = game.getAt(start);
                if (figure == null) {
                    System.out.println("No piece at start position.");
                    continue;
                }
                figure.move(end);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
