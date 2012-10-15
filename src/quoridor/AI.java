package quoridor;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
public class AI implements Player {
	private String name;
	private int numWalls;
	private Difficulty dif;
	private String symbol;
	private Direction end;
	private int depth;
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
		return getShortestPath(g.getBoard());
	}
	
	public MoveLength getShortestPath(Board board){
        Queue<Box> q = new LinkedList<Box>();
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
		current.setParent(null);
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
							current = next;
							finished = true;
							break;
						}
					}
				}				
			}
		} 
		Move last = null;
		int count = 0;
		while(current.getParent() != null){
			count ++;
			//System.out.println("(" + current.row + "," + current.col + ") ");
			last = new Move(current.row, current.col);
			
			current = current.getParent();
		}
		MoveLength lastLength = new MoveLength(last.getRow(), last.getCol());
		lastLength.length = count;
		// TODO Auto-generated method stub
		return lastLength;
	}
	
	public boolean finished(Box b){
		if (this.end == Direction.UP && b.row == 0){
			return true;
		} else if (this.end == Direction.DOWN && b.row == 8){
			return true;
		} else if (this.end == Direction.LEFT && b.col == 0){
			return true;
		} else if (this.end == Direction.RIGHT && b.col == 8){
			return true;
		}
		return false;
	}
	
	public Move getMoveNormal(Game g){
		Move next = null;
		Board board = g.getBoard();
        Box[][] boxes = board.getBoxes();
        Player p1 = null, p2 = null;
        for (Box[] box: boxes){
        	for (Box b: box){
        		try {
	        		if (b.getPlayer() != null){
	        			if (b.getPlayer().getSymbol().equals(this.symbol)){
	        				p1 = b.getPlayer();
	        			} else {
	        				p2 = b.getPlayer();
	        			}
	        		}
        		} catch (NullPointerException n) {}
        	}
        }
        
        depth = 1;
		int score = alphaBeta(p1, p2, p2, board,0, -1000000, +1000000);
		ArrayList<Move> moves = generateMoves(board, p1);
		for (Move current: moves){
			if (score(board.makeMove(current, p1)) == score){
				 next = current;
				 break;
			 }
		}
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
	public Integer score(Board state){
		MoveLength move = getShortestPath(state);
		return (8 - move.length);//make longer lengths worth less
	}
		
	public ArrayList<Move> generateMoves(Board state, Player current){
		ArrayList<Move> moves = new ArrayList<Move>();
		Move initial = state.positionOf(current);
		int y = initial.getCol();
		int x = initial.getRow();
		int addY = 1, addX = 1;
		if (y < 8){
		moves.add(new Move(x, y+1));
		}
		if (y > 0){
		moves.add(new Move(x, y-1));
		}
		if (x > 0){
		moves.add(new Move(x-1, y));
		}
		if (x < 8){
		moves.add(new Move(x+1, y));//generate the moves around the player
		}
		return moves;
	}
	//maximises p1, minimises p2
	public Integer alphaBeta(Player p1, Player p2, Player last, Board state,Integer level,Integer alpha,Integer beta){
		int score = 0;
		if (last.equals(p1)){
			last = p2;
		} else {
			last = p1;
		}
		if (level == depth){
			return score(state);
		} else {
			ArrayList<Move> moves = generateMoves(state, last);
			if (last.equals(p1)) {
				for (Move next: moves){
					
					score = alphaBeta(p1, p2, last, state.makeMove(next, last)
							, level + 1, alpha, beta);
					if (score > alpha){
						alpha = score;
					}
					if (beta <= alpha){
						break;
					}
				}
				return alpha;
			} else if (last.equals(p2)) {
				for (Move next: moves){
					score = alphaBeta(p1, p2, last, state.makeMove(next, last),
							level + 1, alpha, beta);
					if (score < beta){
						beta = score;
					}
					if (beta <= alpha) {
						break;
					}
				}
				return beta;
			}
		}
		return null;
	}
}
