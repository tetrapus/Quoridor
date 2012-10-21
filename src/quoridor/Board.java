
package quoridor;


import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import quoridor.Position.Wall;


/**
 * Represents the game board of Quoridor.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public class Board {
    
    private final int    walls   = 20;
    private final int    size    = 9;
    private int          current = 0;
    private Box[][]      boxes;
    private Player[]     players;
    private List<String> history;
    
    /**
     * Construct the graph of boxes.
     */
    private void initBoxGraph() {
        // Make the boxes
        boxes = new Box[size][size];
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j] = new Box(i, j);
            }
        }
        
        for (int i = 1; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.UP, boxes[i - 1][j]);
            }
        }
        for (int i = 0; i < boxes.length - 1; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.DOWN, boxes[i + 1][j]);
            }
        }
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 1; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.LEFT, boxes[i][j - 1]);
            }
        }
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length - 1; j++) {
                boxes[i][j].setNeighbour(Direction.RIGHT, boxes[i][j + 1]);
            }
        }
    }
    
    /**
     * Construct a new board object initialised with a set of players
     * 
     * @param players
     *            an array of 2 or 4 initial players
     */
    public Board(Player[] players) {
        this.history = new LinkedList<String>();
        this.players = players;
        initBoxGraph();
        Direction[] defaults = { Direction.UP, Direction.DOWN, Direction.LEFT,
                Direction.RIGHT };
        Position[] defaultpos = { new Position(8, 4), new Position(0, 4),
                new Position(4, 8), new Position(4, 0) };
        if (players.length != 2 && players.length != 4) { throw new IllegalArgumentException(
                "Only 2 or 4 players are supported."); }
        for (int i = 0; i < players.length; i++) {
            players[i].initialise(i + 1, defaults[i], walls / players.length);
            Position m = defaultpos[i];
            boxes[m.getRow()][m.getCol()].setPlayer(players[i]);
        }
    }
    
    /**
     * Construct a new board and run a sequence of moves on it.
     * 
     * @param players
     *            set of initial players
     * @param history
     *            applied set of moves to get to a state
     */
    private Board(Player[] players, List<String> history) {
        this.history = new LinkedList<String>();
        this.players = players;
        initBoxGraph();
        Position[] defaultpos = { new Position(8, 4), new Position(0, 4),
                new Position(4, 8), new Position(4, 0) };
        for (int i = 0; i < players.length; i++) {
            Position m = defaultpos[i];
            boxes[m.getRow()][m.getCol()].setPlayer(players[i]);
        }
        for (String move : history) {
            Position p = new Position(move.length() == 3 ? move
                    : move.split(" ")[1]);
            this.move(p);
        }
    }
    
    /**
     * Print the current state of the board.
     */
    public void printBoard() {
        for (Player current : players) {
            System.out.println("Player " + current.getID().toString() + " ("
                    + current.getName() + ") has "
                    + current.getNumWalls().toString() + " walls remaining.");
        }
        System.out.println(" [4m a b c d e f g h i [24m");
        for (int i = 0; i < boxes.length; i++) {
            System.out.print(i + 1);
            System.out.print("|");
            for (Box cell : boxes[i]) {
                String cellname;
                if (cell.getPlayer() == null) {
                    cellname = " ";
                } else {
                    cellname = cell.getPlayer().getID().toString();
                    if (cell.getPlayer().getID() == currentPlayer()) {
                        cellname = "[1m" + cellname + "[0m";
                    }
                }
                if (cell.getNeighbour(Direction.DOWN) != null && i != 8) {
                    
                    cellname = "[24m" + cellname;
                } else {
                    
                    cellname = "[4m" + cellname;
                    if (cell.getPlayer() != null
                            && cell.getPlayer().getID() == currentPlayer()) {
                        cellname += "[4m";
                    }
                }
                if (cell.getNeighbour(Direction.RIGHT) == null) {
                    cellname += "|";
                } else if (i != 8) {
                    cellname += ".";
                } else {
                    cellname += " ";
                }
                System.out.print(cellname);
            }
            System.out.println("[24m");
        }
        System.out.println("It is " + players[current].getName() + "'s turn.");
        
    }
    
    /**
     * Get a list of the IDs of all players in the game.
     * 
     * @return list of player ids
     */
    public List<Integer> getPlayers() {
        List<Integer> ps = new LinkedList<Integer>();
        for (Player p : players) {
            ps.add(p.getID());
        }
        return ps;
    }
    
    /**
     * Get the number of walls a given player has remaining.
     * 
     * @param player
     *            the ID of the player
     * @return number of walls the given player has left
     */
    public int remainingWalls(int player) {
        return players[player - 1].getNumWalls();
    }
    
    /**
     * Get the current player.
     * 
     * @return whose turn it is
     */
    public int currentPlayer() {
        return players[current].getID();
    }
    
    /**
     * Get the last move taken.
     * 
     * @return the last move taken
     */
    public String lastMove() {
        return history.get(history.size() - 1);
    }
    
    /**
     * Get the position of a player
     * 
     * @param p
     *            player
     * @return position of player
     */
    public Position positionOf(Player p) {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                if (p.equals(boxes[i][j].getPlayer())) { return new Position(i,
                        j); }
            }
        }
        return null;
    }
    
    /**
     * Get the position of the player with a specified ID
     * 
     * @param id
     *            ID of the player in question
     * @return position of the player
     */
    public Position positionOf(int id) {
        return positionOf(players[id - 1]);
    }
    
    /**
     * Get the id of the player at the specified position
     * 
     * @param p
     *            position whose contents are being checked
     * @return id of the player at position p
     */
    public Integer playerAt(Position p) {
        Player pl = boxes[p.getRow()][p.getCol()].contents;
        if (pl != null) {
            return pl.getID();
        } else {
            return null;
        }
    }
    
    /**
     * Check if a wall exists from position p
     * 
     * @param p
     *            origin square
     * @param d
     *            direction to check
     * @return if the wall exists
     */
    public boolean wallExists(Position p, Direction d) {
        return boxes[p.getRow()][p.getCol()].getNeighbour(d) == null;
    }
    
    /**
     * Get all reachable positions from a square
     * 
     * @param p
     *            origin square
     * @return list of reachable positions
     */
    public List<Position> neighboursOf(Position p) {
        List<Position> neighbours = new LinkedList<Position>();
        for (Direction d : Direction.values()) {
            if (boxes[p.getRow()][p.getCol()].getNeighbour(d) != null) {
                neighbours.add(boxes[p.getRow()][p.getCol()].getNeighbour(d)
                        .getPosition());
            }
        }
        return neighbours;
    }
    
    /**
     * Flood the board
     * 
     * @return if the board is floodable.
     */
    public boolean flood() {
        LinkedList<Box> visited = new LinkedList<Box>();
        Queue<Box> q = new LinkedList<Box>();
        q.add(boxes[0][0]);
        visited.add(boxes[0][0]);
        Box current;
        Box temp;
        while (!q.isEmpty()) {
            current = q.remove();
            for (Direction dir : Direction.values()) {
                temp = current.getNeighbour(dir);
                if (temp != null && !visited.contains(temp)) {
                    visited.add(temp);
                    q.add(temp);
                }
            }
        }
        return visited.size() == size * size;
    }
    
    /**
     * Get a list of valid move squares from a position
     * 
     * @param pm
     *            origin square
     * @return reachable empty squares
     */
    public List<Position> validMoves(Position pm) {
        LinkedList<Position> adjacent = new LinkedList<Position>();
        LinkedList<Position> moves = new LinkedList<Position>();
        LinkedList<Direction> dirs = new LinkedList<Direction>();
        Box current = boxes[pm.getRow()][pm.getCol()];
        Box neighbour;
        Direction d;
        for (Direction dir : Direction.values()) {
            neighbour = current.getNeighbour(dir);
            if (neighbour != null) {
                adjacent.add(neighbour.getPosition());
                dirs.add(dir);
            }
        }
        
        for (Position p : adjacent) {
            d = dirs.removeFirst();
            if (playerAt(p) != null) {
                for (Position pos: jump(p, d, false)) {
                    moves.add(pos);
                }
            } else {
                moves.add(p);
            }
        }
        return moves;
    }
    
    /**
     * Implement the special rules of player adjacency in quoridor.
     * 
     * @param p starting position
     * @param d direction to jump
     * @return list of possible moves
     */
    private Position[] jump(Position p, Direction d, boolean giveUp) {
        if (!wallExists(p, d) && playerAt(p.adjacentSquare(d)) == null) {
            Position[] rval = new Position[1];
            rval[0] = p.adjacentSquare(d);
            return rval;
        } else if (giveUp) {
            Position[] rval = new Position[0];
            return rval;
        } else {
            LinkedList<Position> moves = new LinkedList<Position>();
            for (Direction dir : Direction.values()) {
                if (!dir.equals(d) && !dir.equals(d.reverse())) {
                    for (Position pos : jump(p, dir, true)) {
                        moves.add(pos);
                    }
                }
            }
            Position rval[] = new Position[moves.size()];
            return moves.toArray(rval);
        }
    }
    
    /**
     * Check if a wall/move move is a valid move.
     * 
     * @param m position object to test
     * @return if move is valid.
     */
    private boolean isValidMove(Position m) {
        boolean validity = true;
        if (m.isWall() && players[current].getNumWalls() >= 0) {
            Box northwest = boxes[m.getRow()][m.getCol()];
            Box northeast = boxes[m.getRow()][m.getCol() + 1];
            Box southwest = boxes[m.getRow() + 1][m.getCol()];
            if (m.getOrientation() == Wall.Vertical) {
                validity = validity
                        && northwest.getNeighbour(Direction.RIGHT) != null;
                validity = validity
                        && southwest.getNeighbour(Direction.RIGHT) != null;
                validity = validity
                        && !(northwest.getNeighbour(Direction.DOWN) == null && northeast
                                .getNeighbour(Direction.DOWN) == null);
                
            } else if (m.getOrientation() == Wall.Horizontal) {
                validity = validity
                        && northwest.getNeighbour(Direction.DOWN) != null;
                validity = validity
                        && northeast.getNeighbour(Direction.DOWN) != null;
                validity = validity
                        && !(northwest.getNeighbour(Direction.RIGHT) == null && southwest
                                .getNeighbour(Direction.RIGHT) == null);
            } else {
                validity = false;
            }
            if (validity) {
                // Test if we can flood the board
                Board test = clone();
                test.placeWall(m);
                if (!test.flood()) {
                    validity = false;
                }
            }
        } else {
            List<Position> adjacent = validMoves(positionOf(players[current]));
            validity = validity && adjacent.contains(m);
        }
        return validity;
    }
    
    /**
     * Check if a move is valid.
     * 
     * @param m move
     * @return whether the move is valid.
     */
    public boolean isValidMove(String m) {
        try {
            return isValidMove(new Position(m));
        } catch (IllegalArgumentException e) {
            return (m.equals("undo") && history.size() > 0);
        }
    }
    
    /**
     * Create a copy of the board with the move applied. 
     * 
     * Returns null if the move is invalid.
     * 
     * @param move move to apply
     * @return the resulting board (or null)
     */
    public Board makeMove(String move) {
        if (move.equals("undo") && history.size() > 0) {
            String undo = history.remove(history.size() - 1);
            Board retval = clone();
            if (undo.length() == 3) {
                retval.removeWall(new Position(undo));
            } else {
                retval.placeMove(new Position(undo.split(" ")[0]));
            }
            current = (current - 1 + players.length) % players.length;
            return retval;
        } else {
            Position m;
            try {
                m = new Position(move);
            } catch (IllegalArgumentException e) {
                return null;
            }
            if (isValidMove(m)) {
                Board retval = clone();
                retval.move(m);
                return retval;
            }
        }
        return null;
    }
    
    /**
     * Apply a move to the current board. Unprotected.
     * 
     * @param m move to apply
     */
    private void move(Position m) {
        if (m.isWall()) {
            history.add(m.toString());
            placeWall(m);
        } else {
            history.add(positionOf(players[current]).toString() + " "
                    + m.toString());
            placeMove(m);
        }
        current = (current + 1) % players.length;
    }
    
    /**
     * Move the current player to the specified position.
     * 
     * Unprotected.
     * 
     * @param m target box
     */
    private void placeMove(Position m) {
        Player p = players[current];
        Position from = positionOf(p);
        boxes[from.getRow()][from.getCol()].setPlayer(null);
        boxes[m.getRow()][m.getCol()].setPlayer(p);
    }
    
    /**
     * Place a wall at the given position.
     * 
     * Unprotected.
     * 
     * @param position Position to place the wall in.
     */
    private void placeWall(Position position) {
        // TODO: Make neater
        // The position specified is the northwest corner of the wall.
        Box northwest = boxes[position.getRow()][position.getCol()];
        Box northeast = boxes[position.getRow()][position.getCol() + 1];
        Box southwest = boxes[position.getRow() + 1][position.getCol()];
        Box southeast = boxes[position.getRow() + 1][position.getCol() + 1];
        
        if (position.getOrientation() == Wall.Vertical) {
            northwest.setNeighbour(Direction.RIGHT, null);
            northeast.setNeighbour(Direction.LEFT, null);
            southwest.setNeighbour(Direction.RIGHT, null);
            southeast.setNeighbour(Direction.LEFT, null);
        } else {
            northwest.setNeighbour(Direction.DOWN, null);
            northeast.setNeighbour(Direction.DOWN, null);
            southwest.setNeighbour(Direction.UP, null);
            southeast.setNeighbour(Direction.UP, null);
        }
    }
    
    /**
     * Remove a wall at a specified position. Unprotected.
     * 
     * @param position wall location
     */
    private void removeWall(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        if (position.getOrientation() == Wall.Horizontal) {
            boxes[row][col].setNeighbour(Direction.DOWN, boxes[row + 1][col]);
            boxes[row + 1][col].setNeighbour(Direction.UP, boxes[row][col]);
            boxes[row][col + 1].setNeighbour(Direction.DOWN,
                    boxes[row + 1][col + 1]);
            boxes[row + 1][col + 1].setNeighbour(Direction.UP,
                    boxes[row][col + 1]);
        } else {
            boxes[row][col].setNeighbour(Direction.RIGHT, boxes[row + 1][col]);
            boxes[row + 1][col].setNeighbour(Direction.RIGHT, boxes[row][col]);
            boxes[row][col + 1].setNeighbour(Direction.LEFT,
                    boxes[row + 1][col + 1]);
            boxes[row + 1][col + 1].setNeighbour(Direction.LEFT,
                    boxes[row][col + 1]);
        }
    }
    
    public Board clone() {
        return new Board(players, history);
    }
}
