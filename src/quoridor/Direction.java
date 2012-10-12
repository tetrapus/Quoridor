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

}
