package quoridor;
import java.util.LinkedList;
import java.util.Queue;

import lab10.Board;
import lab10.BoardOperator;
import lab10.Direction;
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
		Move next = null;
		
		return next;
	}
	
	public Move getShortestPath(Game g){
		Move next = null;
        Queue<Box> q = new LinkedList<Box>();
        Board board = g.getBoard();
        
		Queue<Board> toSearch = new LinkedList<Board> ();
		BoardOperator b = new BoardOperator();
		Board startB = new Board(start);
		Board endB = new Board(goal);
		Board current = startB;
		b.printBoard(current);
		int count = 0;
		boolean finished = false;
		toSearch.add(current);
		if (b.solveable(startB, endB)){
			if (!b.equals(current, endB)){
				while(finished == false && toSearch.size() != 0 && current.movSeq.size() != maxMoves){
					current = toSearch.remove();
					Board next = null;
					for (Direction i: Direction.values()){
						next = b.moveBoard(current, i);
						if (next != null){
							next.setParent(current);
							if (!b.checkIfVisited(next)){
								toSearch.add(next);
								count ++;
								if (b.equals(next, endB)){
									finished = true;
									current = next;
								}
							} else {
								next = null;
							}
						}
					}
				} 
				if (current.movSeq.size() == maxMoves){
					System.out.println("Max moves hit stopping search");
				}
			}
			System.out.println(current.movSeq.toString());
			System.out.println("Solveable");
		} else {
			System.out.println("Puzzle is not solvable");
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	BreadthFirst(int size){
		this.size = size;
	}
		return next;
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
	public Move getMove(Game g) {
		if (this.dif == Difficulty.Easy){
			return getMoveEasy(g);
		} else if (this.dif == Difficulty.Normal) {
			return getMoveNormal(g);
		} else {
			return getMoveHard(g);
		}
		// TODO Auto-generated method stub
		
		return null;
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
