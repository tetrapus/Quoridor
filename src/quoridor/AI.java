package quoridor;
import java.util.LinkedList;
import java.util.Queue;

public class AI implements Player {
	private String name;
	private int numWalls;
	private Difficulty dif;
	private String symbol;
	private Direction end;
	public Direction getEnd() {
		return end;
	}
	public void setEnd(Direction end) {
		this.end = end;
	}
	public int getNumWalls() {
		return numWalls;
	}
	public void setNumWalls(int numWalls) {
		this.numWalls = numWalls;
	}
	public Difficulty getDif() {
		return dif;
	}
	public void setDif(Difficulty dif) {
		this.dif = dif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	AI(Difficulty diff){
		this.dif = diff;
	}
	
	public Move getMoveEasy(Game g){
		return getShortestPath(g);
	}
	
	public Move getShortestPath(Game g){
		FakePlayer fake = new FakePlayer(this);
        Queue<Box> q = new LinkedList<Box>();
        Board board = g.getBoard();
        Move startPos = board.positionOf(fake);
        Box[][] boxes = board.getBoxes();
        Box start = boxes[startPos.getRow()][startPos.getCol()];
		Box current = start;
		boolean finished = false;
		q.add(current);
		while(finished == false && q.size() != 0){
			current =q.remove();
			Box next = null;
			for (Direction i: Direction.values()){
				next = current.getNeighbour(i);
				if (next != null){
					next.setParent(current);
					if (!checkIfVisited(next)){
						q.add(next);
						if (finished(next)){
							finished = true;
							current = next;
						}
					} else {
						next = null;
					}
				}
			}
		} 
		Move last = null;
		while(current.getParent() != null){

			System.out.println("(" + current.row + "," + current.col + ") ");
			last = new Move(current.row, current.col);
			current = current.getParent();
		}
		// TODO Auto-generated method stub
		return last;
	}
	
	public boolean finished(Box b){
		if (this.end == Direction.UP){
			return b.row == 0;
		} else if (this.end == Direction.DOWN){
			return b.row == 8;
		} else if (this.end == Direction.LEFT){
			return b.col == 0;
		} else if (this.end == Direction.RIGHT){
			return b.col == 8;
		}
		return false;
	}
	
	public boolean checkIfVisited(Box b){
		boolean retval = false;
		Box parent = b.parent;
		while(!retval && parent!=null){
			retval = equals(b, parent);
			parent =  parent.parent;
		}
		return retval;
	}
	
	public boolean equals(Box a, Box b){
		return a.row == b.row && b.col == a.col;
	}
	
	public Move getMoveNormal(Game g){
		Move next = null;
		return next;
	}
	
	public Move getMoveHard(Game g){
		Move next = null;
		return next;
	}
	
	@Override
	public String getMove(Game g) {
		if (this.dif == Difficulty.Easy){
			return getMoveEasy(g).toString();
		} else if (this.dif == Difficulty.Normal) {
			return getMoveNormal(g).toString();
		} else {
			return getMoveHard(g).toString();
		}
	}
	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return this.symbol;
	}
	@Override
	public void setSymbol(String symbol) {
		// TODO Auto-generated method stub
		this.symbol = symbol;
	}

}
