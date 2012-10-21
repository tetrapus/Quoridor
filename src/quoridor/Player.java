
package quoridor;


/**
 * Generates new quoridor moves.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public abstract class Player {
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Player other = (Player) obj;
        if (id != other.id) return false;
        return true;
    }

    private Direction direction;
    protected String  name;
    private int       walls;
    private int       id;
    private boolean   initialised = false;
    
    /**
     * Create a player object and set its name.
     * 
     * @param name Name of the player
     */
    public Player(String name) {
        this.name = name;
    }
    
    public Player() {
    }
    
    /**
     * Initialise the player with an ID, direction, and walls.
     * 
     * Initialise may only be called once, and will generally be called by the
     * board class when the game is initialised.
     * 
     * @param id Player id
     * @param d Target direction
     * @param walls Number of walls
     */
    public final void initialise(int id, Direction d, int walls) {
        if (!initialised) {
            this.id = id;
            this.direction = d;
            this.walls = walls;
            this.initialised = true;
            this.onCreation();
        }
    }
    
    /**
     * Get the direction of the target wall.
     * @return direction of target wall
     */
    public final Direction getEnd() {
        return this.direction;
    }
    
    /**
     * Get the player's ID.
     * 
     * @return player's id
     */
    public final Integer getID() {
        return id;
    }
    
    /**
     * Get the number of remaining walls.
     * 
     * @return number of remaining walls.
     */
    public final Integer getNumWalls() {
        return walls;
    }
    
    /**
     * Template method to get the player's next move given a certain game state.
     * 
     * @param g Game object
     * @return player's move.
     */
    public final String getMove(Game g) {
        String move = this.nextMove(g.getBoard());
        while (!g.isValidMove(move) || (move.length() == 3 && getNumWalls() <= 0)) {
            onFailure(g.getBoard());
            move = this.nextMove(g.getBoard());
        }
        
        if (move.length() == 3) {
            this.walls--;
        }
        if (move == "undo" && g.getBoard().lastMove().length() == 3) {
            this.walls++;
        }
        
        return move;
    }
    
    // Overridable
    /**
     * Called on initialisation.
     */
    protected void onCreation() {
    }
    
    /**
     * Called when a move returned by nextMove is not valid.
     * @param b current board state
     */
    protected void onFailure(Board b) {
    }
    
    /**
     * Get the name of the player.
     * @return name of the player
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Set the player's name.
     * 
     * @param name name of the player
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Generate the player's next move.
     * @param b board state
     * @return next move
     */
    public abstract String nextMove(Board b);
}
