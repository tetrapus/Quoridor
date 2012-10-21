
package quoridor;


import java.util.LinkedList;
import java.util.regex.Pattern;
import java.io.*;

/**
 * Run a game of Quoridor.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 *
 */
public class Game {
    
    LinkedList<String> future = new LinkedList<String>();
    Board              board;
    Player[]           players;
    
    /**
     * Create a new game with an array of players
     * 
     * @param players an array of 2 or 4 player objects
     */
    public Game(Player[] players) {
        this.players = players; 
        this.board = new Board(players);
    }
    
    /**
     * Check if a move is valid at the game level.
     * 
     * @param move move to check for validity/
     * @return whether the move is valid
     */
    public boolean isValidMove(String move) {
        return (move.equals("redo") && future.size() > 0)
                || board.isValidMove(move);
    }
    
    /**
     * Redo the last move. Unprotected.
     */
    private void redo() {
        String command = future.removeLast();
        if (command.length() == 3) {
            board = board.makeMove(command);
        } else {
            board = board.makeMove(command.split(" ")[1]);
        }
    }
    
    /**
     * Get a copy of the current game board.
     * 
     * @return clone of the current board state
     */
    public Board getBoard() {
        return board.clone();
    }
    
    /**
     * Checks each players position and returns a boolean
     * 
     * @return true if game over false if not over
     */
    public boolean finished() {
        boolean finished = false;
        for (Player current : players) {
            Position position = board.positionOf(current);
            if (current.getEnd() == Direction.UP) {
                finished = finished || position.getRow() == 0;
            } else if (current.getEnd() == Direction.DOWN) {
                finished = finished || position.getRow() == 8;
            } else if (current.getEnd() == Direction.LEFT) {
                finished = finished || position.getCol() == 0;
            } else if (current.getEnd() == Direction.RIGHT) {
                finished = finished || position.getCol() == 8;
            }
        }
        return finished;
    }
    
    /**
     * Get the winner of the game (or null if not yet set)
     * 
     * @return winner of the game.
     */
    public Player getWinner() {
        for (Player current : players) {
            Position position = board.positionOf(current);
            if (current.getEnd() == Direction.UP) {
                if (position.getRow() == 0) { return current; }
            } else if (current.getEnd() == Direction.DOWN) {
                if (position.getRow() == 8) { return current; }
            } else if (current.getEnd() == Direction.LEFT) {
                if (position.getCol() == 0) { return current; }
            } else if (current.getEnd() == Direction.RIGHT) {
                if (position.getCol() == 8) { return current; }
            }
        }
        return null;
    }
    
    /**
     * Play a game until finished.
     */
    public void play() {
        while (!finished()) {
            String next = players[board.currentPlayer() - 1].getMove(this);
            if (Pattern.matches("save.*", next)){
            	String[] args = next.split("\\s");
            	
            	try {
					FileWriter writer = new FileWriter(args[1]);
					for (Player p: players){
						gamedata += p.t
					}
					writer.write(players.length+","+)
				} catch (IOException e) {
					
					e.printStackTrace();
				}
            
            } else if (Pattern.matches("load\\s.*", next)){
            	
            } else if (isValidMove(next)) {
                if (next.equals("redo")) {
                    redo();
                } else if (next.equals("undo")) {
                    future.add(board.lastMove());
                    board = board.makeMove(next);
                } else {
                    while (!future.isEmpty()) { future.remove(); }
                    board = board.makeMove(next);
                }
            }
        }
    }
}
