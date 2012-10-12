package quoridor;

import java.util.List;

public class Validator {

    // TODO complete this class using your project code
    // you must implement the no-arg constructor and the check method
    
    // you may add extra fields and methods to this class
    // but the ProvidedTests code only calls the specified methods
    Player[] ar = new Player[2];

	Game game;
    public Validator() {
        // TODO
        ar[0] = new Human();
        ar[1] = new Human();
        game = new Game(ar);
    }

    /**
     * Check the validity of a given list of moves.
     * Each move is represented by a string.
     * The list is valid if and only if each move in the list is valid,
     * after applying the preceding moves in the list, assuming they are valid,
     * starting from the initial position of the game.
     * When the game has been won, no further moves are valid.
     * @param moves a list of successive moves
     * @return validity of the list of moves
     */
    public boolean check(List<String> moves) {
        // TODO
        return game.checkList(moves);
    }

}