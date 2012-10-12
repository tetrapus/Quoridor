package quoridor;

public class Move {
    
    public enum Wall { Horizontal, Vertical };
    
    private int row;
    private int col;
    private boolean isWall;
    private Wall orientation;
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        char row = '1';
        row += this.row;
        char col = 'a';
        col += this.col;
        s.append(col);
        s.append(row);
        if (isWall()) {
            s.append(this.orientation == Wall.Vertical ? 'v' : 'h');
        }
        return s.toString();
    }
    
    public Move(String move) throws IllegalArgumentException {
        if (!isValidMoveString(move)) {
            throw new IllegalArgumentException("Invalid move string.");
        }
        this.col = move.charAt(0) - 'a';
        this.row = move.charAt(1) - '1';
        this.isWall = false;
        if (move.length() == 3) {
            this.isWall = true;
            this.orientation = move.charAt(2) == 'h' ? Wall.Horizontal : Wall.Vertical;
        }
    }
    
    @Override
    public boolean equals(Object m) {
        if (!(m instanceof Move)) {
            return false;
        } else {
            Move move = (Move) m;
            boolean same = move.getRow() == getRow() && move.getCol() == getCol();
            if (move.isWall() && isWall()) {
                return same && this.getOrientation() == move.getOrientation();
            } else if (move.isWall() != isWall()) {
                return false;
            }
            return same;
        }
    }
    
    public Move(int row, int col) {
        char rowMove = 'a';
        rowMove += row;
        char colMove = '1';
        colMove += col;
        if (!isValidMoveString(String.valueOf(rowMove) + String.valueOf(colMove))) {
            throw new IllegalArgumentException("Invalid move.");
        }
        this.row = row;
        this.col = col;
        this.isWall = false;
    }

    public boolean isValidMoveString(String move) {
        boolean validity = (move.length() >= 2);
        validity = validity && move.charAt(0) >= 'a' && move.charAt(1) <= 'i';
        validity = validity && move.charAt(1) >= '1' && move.charAt(1) <= '9';
        if (move.length() == 3) {
            validity = validity && (move.charAt(2) == 'v' ||  move.charAt(2) == 'h');
            validity = validity && move.charAt(1) <= 'h';
            validity = validity && move.charAt(1) <= '8';

        } else if (move.length() > 3) {
            return false;
        }
        return validity;
    }
    
    public boolean isWall() {
        return isWall;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public Wall getOrientation() throws IllegalStateException {
        if (!isWall()) { throw new IllegalStateException("Not defined for this type of move."); }
        return this.orientation;
    }
    
}
