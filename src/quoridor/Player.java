
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
    
    public Player(String name) {
        this.name = name;
    }
    
    public Player() {
    }
    
    public final void initialise(int id, Direction d, int walls) {
        if (!initialised) {
            this.id = id;
            this.direction = d;
            this.walls = walls;
            this.initialised = true;
            this.onCreation();
        }
    }
    
    public final Direction getEnd() {
        return this.direction;
    }
    
    public final Integer getID() {
        return id;
    }
    
    public final Integer getNumWalls() {
        return walls;
    }
    
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
    protected void onCreation() {
    }
    
    protected void onFailure(Board b) {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public abstract String nextMove(Board b);
}
