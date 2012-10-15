
package quoridor;


import java.util.LinkedList;


public class Game {
    
    LinkedList<String> future = new LinkedList<String>();
    Board              board;
    Player[]           players;
    
    public Game(Player[] players) {
        this.players = players;
        this.board = new Board(players);
    }
    
    public boolean isValidMove(String move) {
        return (move.equals("redo") && future.size() > 0)
                || board.isValidMove(move);
    }
    
    private void redo() {
        String command = future.removeLast();
        if (command.length() == 3) {
            board = board.makeMove(command);
        } else {
            board = board.makeMove(command.split(" ")[1]);
        }
    }
    
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
    
    public void play() {
        while (!finished()) {
            String next = players[board.currentPlayer() - 1].getMove(this);
            if (isValidMove(next)) {
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
