package quoridor;

/**
 * Generates new quoridor moves.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public interface Player {
	
    /**
     * Generates the next move made by the player.
     * @param b current game state
     * @return move, in Glendinnings format.
     */
	public Direction getEnd();
	public void setEnd(Direction end);
	public String getSymbol();
	public void setSymbol(String symbol);
	public int getNumWalls();
	public void setNumWalls(int numWalls);
	public String getName();
	public void setName(String name);
	public String getMove(Game g);
}

