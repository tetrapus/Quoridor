
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
    
    private final int      walls   = 20;
    private final int      size    = 9;
    private int            current = 0;
    private Box[][]        boxes;
    private Player[]       players;
    private List<String>   history;
    
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
    
    public Board(Player[] players) {
        this.history = new LinkedList<String>();
        this.players = players;
        initBoxGraph();
        Direction[] defaults = { Direction.UP, Direction.DOWN, Direction.LEFT,
                Direction.RIGHT };
        Position[] defaultpos = { new Position(8, 4), new Position(0, 4), new Position(4, 8), new Position(4, 0)};
        if (players.length != 2 && players.length != 4) { throw new IllegalArgumentException(
                "Only 2 or 4 players are supported."); }
        for (int i = 0; i < players.length; i++) {
            players[i].initialise(i + 1, defaults[i], walls / players.length);
            Position m = defaultpos[i];
            boxes[m.getRow()][m.getCol()].setPlayer(players[i]);
        }
    }
    
    private Board(Player[] players, List<String> history) {
        this.history = new LinkedList<String>();
        this.players = players;
        initBoxGraph();
        Position[] defaultpos = { new Position(8, 4), new Position(0, 4), new Position(4, 8), new Position(4, 0)};
        for (int i = 0; i < players.length; i++) {
            Position m = defaultpos[i];
            boxes[m.getRow()][m.getCol()].setPlayer(players[i]);
        }
        for (String move : history) {
            Position p = new Position(move.length() == 3 ? move : move.split(" ")[1]); 
            this.move(p);
        }
    }
    
    public void printBoard() {
        System.out.println(" [4m a b c d e f g h i [24m");
        for (int i = 0; i < boxes.length; i++) {
            System.out.print(i + 1);
            System.out.print("|");
            for (Box cell : boxes[i]) {
                String cellname;
                if (cell.getPlayer() == null) {
                    cellname = " ";
                } else {
                    cellname = new Integer(cell.getPlayer().getID()).toString();
                }
                if (cell.getNeighbour(Direction.DOWN) != null && i != 8) {
                    cellname = "[24m" + cellname;
                } else {
                    cellname = "[4m" + cellname;
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
        
        for (Player current : players){
            System.out.println("Number of walls remaining for " +
            current.getName() + " is " 
                    + (new Integer(current.getNumWalls())).toString());
        }
    }
    
    public List<Integer> getPlayers() {
        List<Integer> ps = new LinkedList<Integer>();
        for (Player p : players) {
            ps.add(p.getID());
        }
        return ps;
    }
    
    public int currentPlayer() {
        return players[current].getID();
    }
    
    public String lastMove() {
        return history.get(history.size() - 1);
    }
    
    public Position positionOf(Player p) {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                if (p.equals(boxes[i][j].getPlayer())) { return new Position(i,
                        j); }
            }
        }
        return null;
    }
    
    public Position positionOf(int id) {
        return positionOf(players[id-1]);
    }
    
    public int playerAt(Position p) {
        return boxes[p.getRow()][p.getCol()].contents.getID();
    }
    
    public boolean wallExists(Position p, Direction d) {
        return boxes[p.getRow()][p.getCol()].getNeighbour(d) == null;
    }
    
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
    
    public List<Position> validMoves(Position pm) {
        LinkedList<Position> moves = new LinkedList<Position>();
        LinkedList<Box> adjacent = new LinkedList<Box>();
        LinkedList<Box> visited = new LinkedList<Box>();
        Queue<Box> q = new LinkedList<Box>();
        q.add(boxes[pm.getRow()][pm.getCol()]);
        visited.add(boxes[pm.getRow()][pm.getCol()]);
        Box current;
        Box neighbour;
        while (!q.isEmpty()) {
            current = q.remove();
            for (Direction dir : Direction.values()) {
                neighbour = current.getNeighbour(dir);
                if (neighbour != null && !visited.contains(neighbour)) {
                    if (neighbour.getPlayer() == null) {
                        adjacent.add(neighbour);
                    } else {
                        q.add(neighbour);
                    }
                    visited.add(neighbour);
                }
            }
        }
        for (Box b : adjacent) {
            moves.add(b.getPosition());
        }
        return moves;
    }
    
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
    
    public boolean isValidMove(String m) {
        try {
            return isValidMove(new Position(m));
        } catch (IllegalArgumentException e) {
            return (m.equals("undo") && history.size() > 0);
        }
    }
    
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
    
    private void move(Position m) {
        if (m.isWall()) {
            history.add(m.toString());
            placeWall(m);
        } else {
            history.add(positionOf(players[current]).toString() + " " + m.toString());
            placeMove(m);
        }
        current = (current + 1) % players.length;
    }
    
    private void placeMove(Position m) {
        Player p = players[current];
        Position from = positionOf(p);
        boxes[from.getRow()][from.getCol()].setPlayer(null);
        boxes[m.getRow()][m.getCol()].setPlayer(p);
    }
    
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
