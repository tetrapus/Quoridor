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
	public int getNumWalls();
	public void setNumWalls(int numWalls);
	public String getName();
	public void setName(String name);
	public String getMove();
}

