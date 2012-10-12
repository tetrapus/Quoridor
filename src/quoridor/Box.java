package quoridor;

/**
 * Represent a single square on the quoridor grid.
 * 
 * @author Joey Tuong
 *
 */
public class Box {
    int row;
    int col;
    Box parent = null;
    // TODO: private/public problems

    Player contents;
    public Box getParent() {
		return parent;
	}

	public void setParent(Box parent) {
		this.parent = parent;
	}

	Box[] adjacent;
    
    public Box(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjacent = new Box[4];
    }
    
    public void setNeighbour(Direction direction, Box b) {
        adjacent[direction.index()] = b;
    }
    public Box getNeighbour(Direction direction) {
        return adjacent[direction.index()];
    }
    
    public void setPlayer(Player p) {
        this.contents = p;
    }
    
    public Player getPlayer() {
        return this.contents;
    }
}
