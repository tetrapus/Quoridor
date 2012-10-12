package quoridor;

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
		// TODO Auto-generated method stub
		this.numWalls = numWalls;
	}
	@Override
	public String getMove() {
		// TODO Auto-generated method stub
		return null;
	}
}
