package quoridor;
import java.io.*;
public class Human implements Player {
	private String name;
	private int numWalls;
	private String symbol;
	private Direction end;
	public Direction getEnd() {
		return end;
	}
	public void setEnd(Direction end) {
		this.end = end;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int getNumWalls() {
		// TODO Auto-generated method stub
		return numWalls;
	}
	@Override
	public void setNumWalls(int numWalls) {
		// TODO Auto-generated method stubdirection
		this.numWalls = numWalls;
	}
	@Override
	public Move getMove(Game g) throws IOException {
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("Number of walls remaining for player " +
	    		this.name + " is " + this.numWalls);
	    while(true){
	    	System.out.print("Enter the move for player " + this.symbol + ":");
	    	String temp = in.readLine();
		    try {
		    	Move m = new Move(temp);
		    	if (g.validMove(m, this)){
		    		return m;
		    	} else {
		    		System.out.println("Invalid move!");
		    	}
		    } catch (IllegalArgumentException e) {}
	    }
		// TODO Auto-generated method stub
		return null;
	}
}
