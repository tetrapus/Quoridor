
package quoridor;


import java.util.LinkedList;
import java.util.Queue;
import quoridor.Move.Wall;


/**
 * Represents a game of Quoridor.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public class Board {
    
    private final int size = 9;
    private Box[][]   boxes;
    
    public Box[][] getBoxes() {
        return boxes;
    }

    public Board() {
        boxes = new Box[size][size];
        // Make the boxes
        
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
    
    public void printBoard() {
        System.out.println(" [4m a b c d e f g h i [24m");
        for (int i = 0; i < boxes.length; i++) {
            System.out.print(i + 1);
            System.out.print("[4m|");
            for (Box cell : boxes[i]) {
                String cellname;
                if (cell.getPlayer() == null) {
                    cellname = " ";
                } else {
                    cellname = cell.getPlayer().getSymbol();
                }
                if (cell.getNeighbour(Direction.DOWN) != null || i == 8) {
                    cellname = "[24m" + cellname + "[4m";
                }
                if (cell.getNeighbour(Direction.RIGHT) == null) {
                    cellname += "|";
                } else {
                    cellname += " ";
                }
                System.out.print(cellname);
            }
            System.out.println("[24m");
        }
    }
    
    public Move positionOf(Player p) {
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                if (p.equals(boxes[i][j].getPlayer())) { return new Move(i, j); }
            }
            
        }
        return null;
    }
    
    public void addPlayer(Move m, Player p) {
        boxes[m.getRow()][m.getCol()].setPlayer(p);
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
    
    public boolean validMove(Move m, Player p) {
        boolean validity = true;
        if (m.isWall()) {
            Box northwest = boxes[m.getRow()][m.getCol()];
            Box northeast = boxes[m.getRow()][m.getCol()+1];
            Box southwest = boxes[m.getRow()+1][m.getCol()];
            if (m.getOrientation() == Wall.Vertical) {
                validity = validity && northwest.getNeighbour(Direction.RIGHT) != null;
                validity = validity && southwest.getNeighbour(Direction.RIGHT) != null;
                validity = validity && !(northwest.getNeighbour(Direction.DOWN) == null && northeast.getNeighbour(Direction.DOWN) == null);

            } else if (m.getOrientation() == Wall.Horizontal) {
                validity = validity && northwest.getNeighbour(Direction.DOWN) != null;
                validity = validity && northeast.getNeighbour(Direction.DOWN) != null;
                validity = validity && !(northwest.getNeighbour(Direction.RIGHT) == null && southwest.getNeighbour(Direction.RIGHT) == null);
            } else {
                validity = false;
            }
            if (validity) {
                // Test if we can flood the board
                placeWall(m);
                if (!flood()) {
                    validity = false;
                }
                removeWall(m);
            }
        } else {
            Move pm = positionOf(p);
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
            validity = validity && adjacent.contains(boxes[m.getRow()][m.getCol()]);
        }
        return validity;
    }
    
    public void placeMove(Move m, Player p) {
        Move from = positionOf(p);
        boxes[from.getRow()][from.getCol()].setPlayer(null);
        boxes[m.getRow()][m.getCol()].setPlayer(p);
    }
    
    public boolean placeWall(Move position) throws IllegalStateException {
        // TODO: Make neater
        // The position specified is the northwest corner of the wall.
        Direction otherside;
        Direction othersquare;
        Direction thisside;
        if (position.getOrientation() == Wall.Horizontal) {
            otherside = Direction.DOWN;
            thisside = Direction.UP;
            othersquare = Direction.RIGHT;
        } else {
            otherside = Direction.RIGHT;
            thisside = Direction.LEFT;
            othersquare = Direction.DOWN;
        }
        
        try {
            /*
             * The following fails iff a wall exists between the northwest and
             * southwest walls.
             */
            Box northwest = boxes[position.getRow()][position.getCol()];
            Box southwest = northwest.getNeighbour(otherside);
            Box southeast = southwest.getNeighbour(othersquare);
            Box northeast = northwest.getNeighbour(othersquare);
            /*
             * For a valid move, one or fewer of {northeast, southeast} are
             * null. The if clause ensures that all boxes are defined.
             */
            if (northeast == null) {
                northeast = southeast.getNeighbour(thisside);
            } else {
                southeast = northeast.getNeighbour(otherside);
            }
            if (northeast == null || southeast == null) {
                // Occurs if there is a wall between northeast and southeast
                return false;
            }
            northeast.setNeighbour(otherside, null);
            northwest.setNeighbour(otherside, null);
            southeast.setNeighbour(thisside, null);
            southwest.setNeighbour(thisside, null);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    
    public void removeWall(Move position) {
        int row = position.getRow();
        int col = position.getCol();
        boxes[row][col].setNeighbour(Direction.DOWN, boxes[row + 1][col]);
        boxes[row + 1][col].setNeighbour(Direction.UP, boxes[row][col]);
        boxes[row][col + 1].setNeighbour(Direction.DOWN,
                boxes[row + 1][col + 1]);
        boxes[row + 1][col + 1].setNeighbour(Direction.UP, boxes[row][col + 1]);
        
    }
}
