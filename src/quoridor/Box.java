package quoridor;

/**
 * Represent a single square on the quoridor grid.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 *
 */
public class Box {
    int row;
    int col;

    Player contents;

	Box[] adjacent;
    
    /**
     * Create a new box at row, col
     * 
     * @param row row number
     * @param col column number
     */
    public Box(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjacent = new Box[4];
    }
    
    /**
     * Get the position of the box.
     * 
     * @return position of the box
     */
    Position getPosition() {
        return new Position(this.row, this.col);
    }
    
    /**
     * Set (or sever) the neighbouring box node on the graph in a direction
     * 
     * @param direction direction to set
     * @param b adjacent box or null
     */
    public void setNeighbour(Direction direction, Box b) {
        adjacent[direction.index()] = b;
    }
    
    /**
     * Get the adjacent box in a given direction, or null if a wall exists.
     * 
     * @param direction node link id
     * @return adjacent box or null if not reachable
     */
    public Box getNeighbour(Direction direction) {
        return adjacent[direction.index()];
    }
    
    /**
     * Set the player contained by a box (or null if none exists)
     * 
     * @param p player to set
     */
    public void setPlayer(Player p) {
        this.contents = p;
    }
    
    /**
     * Get the player contained by the box.
     * 
     * @return player or null if none exists
     */
    public Player getPlayer() {
        return this.contents;
    }
}
