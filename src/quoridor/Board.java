
package quoridor;


/**
 * Represents a game of Quoridor.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public class Board {
    
    Player[] players;
    Box[][]  boxes;
    
    public Board(int size) {
        assert !(size % 2 == 1);
        boxes = new Box[size][size];
        // Make the boxes
        
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j] = new Box(i, j);
            }
        }
        
        for (int i = 1; i < boxes.length; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.Up, boxes[i - 1][j]);
            }
        }
        for (int i = 0; i < boxes.length - 1; i++) {
            for (int j = 0; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.Down, boxes[i + 1][j]);
            }
        }
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 1; j < boxes.length; j++) {
                boxes[i][j].setNeighbour(Direction.Left, boxes[i][j - 1]);
            }
        }
        for (int i = 0; i < boxes.length - 1; i++) {
            for (int j = 0; j < boxes.length - 1; j++) {
                boxes[i][j].setNeighbour(Direction.Right, boxes[i][j + 1]);
            }
        }
        
        boxes[0][size / 2].setPlayer(players[0]);
        boxes[size - 1][size / 2].setPlayer(players[1]);
        
    }
    
    public int getDistanceToEnd(int row, int col) {
        return 0;
    }
    
    public boolean isLegalMove(String move) {
        return true;
    }
    
    public boolean placeWall(String position) throws IllegalArgumentException {
        // TODO: Make neater
        // Fix absolute compass position variable names
        int row = position.charAt(0) - 'a';
        int col = position.charAt(1) - '0';
        // The position specified is the northwest corner of the wall.
        Direction otherside;
        Direction othersquare;
        Direction thisside;
        if (position.charAt(2) == 'h') {
            otherside = Direction.Down;
            thisside = Direction.Up;
            othersquare = Direction.Right;
        } else if (position.charAt(2) == 'v') {
            otherside = Direction.Right;
            thisside = Direction.Left;
            othersquare = Direction.Down;
        } else {
            throw new IllegalArgumentException("Invalid move string.");
        }
        
        try {
            /*
             * The following fails iff a wall exists between the northwest and
             * southwest walls.
             */
            Box northwest = boxes[row][col];
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
}
