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
        Queue<Box> q = new LinkedList<Box>();
        Board board = g.getBoard();
        Box current = null;
        Box[][] boxes = board.getBoxes();
		LinkedList<Box> visited = new LinkedList<Box>();
        for (Box[] box: boxes){
        	for (Box b: box){
        		try {
	        		if (b.getPlayer().getSymbol().equals(this.symbol)){
	        			current = b;
	        		}
        		} catch (NullPointerException n) {}
        	}
        }
		boolean finished = false;
		q.add(current);
		visited.add(current);
		while(finished == false && q.size() != 0){
			current =q.remove();
			Box next = null;
			for (Direction i: Direction.values()){
				next = current.getNeighbour(i);
				if (next != null && next.getPlayer() == null) {
					//System.out.println("(" +next.row + ", "+next.col + ")");
					if (!visited.contains(next)){
						next.setParent(current);
						visited.add(next);
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
		String retval = "";
		if (this.dif == Difficulty.Easy){
			retval = getMoveEasy(g).toString();
		} else if (this.dif == Difficulty.Normal) {
			retval = getMoveNormal(g).toString();
		} else {
			retval = getMoveHard(g).toString();
		}
		g.printState();
		return retval;
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
