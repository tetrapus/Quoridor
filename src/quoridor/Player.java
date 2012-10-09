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
    public String next(Board b);

	public String getName();
	public void setName(String name);
}
