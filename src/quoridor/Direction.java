package quoridor;

public enum Direction {
	UP(0), DOWN(1), LEFT(2), RIGHT(3);
    private final int index;   

    Direction(int index) {
        this.index = index;
    }

    public int index() { 
        return index; 
    }
    
    /**
     * Get the opposite direction
     * 
     * @return opposite direction
     */
    public Direction reverse() {
        if (this == Direction.DOWN) {
            return Direction.UP;
        } else if (this == Direction.UP) {
            return Direction.DOWN;
        } else if (this == Direction.LEFT) {
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
        }
    }

}
