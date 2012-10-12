package quoridor;

public class FakePlayer implements Player {
    public String getName() {
        return name;
    }

    public void setName(String name) {    }

    public int getNumWalls() {
        return numWalls;
    }

    public void setNumWalls(int numWalls) {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
    }

    public Direction getEnd() {
        return end;
    }

    public void setEnd(Direction end) {
    }
    
    public Move getMove(Game g) {
        return null;
    }

    private String name;
    private int numWalls;
    private String symbol;
    private Direction end;
    
    public FakePlayer(Player p) {
        symbol = p.getSymbol();
        numWalls = p.getNumWalls();
        name = p.getName();
        end = p.getEnd();
    }
    
}
