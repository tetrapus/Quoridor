package quoridor;

import java.util.LinkedList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
public class Game {
	
    LinkedList<Move> history = new LinkedList<Move>();
    Board board = new Board();
    Player[] players;
    Integer[] wallCount;
    public Game(Player[] players) {
    	Integer count = 0;
    	this.wallCount = new Integer[players.length];
    	for (Player current: players){
    		if (players.length == 4){
    			wallCount[count] = 5;
    			current.setNumWalls(5);
    		} else {
    			wallCount[count] = 10;
    			current.setNumWalls(10);
    		}
    		count ++;
    		current.setSymbol(count.toString());
    	}
		players[0].setEnd(Direction.DOWN);
		board.addPlayer(new Move(0, 4), players[0]);
		players[1].setEnd(Direction.UP);
		board.addPlayer(new Move(8, 4), players[1]);
    	if (players.length == 4){
    		players[2].setEnd(Direction.RIGHT);
    		board.addPlayer(new Move(4, 0), players[2]);
    		players[3].setEnd(Direction.LEFT);
    		board.addPlayer(new Move(4, 8), players[3]);
    	}
    	
        this.players = players;
        
    }
    
    public void printState(){
    	int count = 0;
    	for (Player current :players){
    			System.out.println("Number of walls remaining for player " +
	    		current.getName() + " is " + wallCount[count].toString());
    			count++;
    	}
    	board.printBoard();
    }
    
    public boolean checkList(List<String> moves){
    	int count = 0;
    	for (String move: moves){
    		Move m = new Move(move);
    		if (validMove(m, players[count])){
    			makeMove(m, players[count]);
    		} else {
    			return false;
    		}
    		count ++;
    		if (count == players.length){
    			count = 0;
    		}	
    	}
    	return true;
    }
    
    private void makeMove(Move move, Player p){
    	
    	if (move.isWall()){
    		history.add(move);
    		board.placeWall(move);
    		int walls = wallCount[Integer.parseInt(p.getSymbol())];
    		wallCount[Integer.parseInt(p.getSymbol())] = walls - 1;
    		p.setNumWalls(walls - 1);
    	} else {
    		history.add(board.positionOf(p));
    		board.placeMove(move, p);
    	}
    }	
    
    public Board getBoard(){
    	Board retval = new Board();
    	Integer count = 0;
    	Player[] fakePlayers = new Player[4];
    	for (Player current: players){
    		fakePlayers[count] = new FakePlayer(current);
    		count++;
    	}
    	count = 0;
    	for (Player current: fakePlayers){
    		if (fakePlayers.length == 4){
    			current.setNumWalls(5);
    		} else {
    			current.setNumWalls(10);
    		}
    		count ++;
    		current.setSymbol(count.toString());
    	}
    	fakePlayers[0].setEnd(Direction.DOWN);
		board.addPlayer(new Move(0, 4), fakePlayers[0]);
		fakePlayers[1].setEnd(Direction.UP);
		board.addPlayer(new Move(8, 4), fakePlayers[1]);
    	if (fakePlayers.length == 4){
    		fakePlayers[2].setEnd(Direction.RIGHT);
    		board.addPlayer(new Move(4, 0), fakePlayers[2]);
    		fakePlayers[3].setEnd(Direction.LEFT);
    		board.addPlayer(new Move(4, 8), fakePlayers[3]);
    	}
    	count = 0;
    	for (Move next: history){
    		if (next.isWall()){
    			retval.placeWall(next);
    			count ++;
    		} else {
    			retval.placeMove(next, fakePlayers[count]);
    			count ++;
    		}
    		if (count == fakePlayers.length){
    			count = 0;
    		}
    	}
    	return retval;
    }
    /**Checks each players position and returns a boolean 
     * 
     * @return true if game over false if not over
     */
    public boolean finished(){
    	boolean finished = false;
    	for (Player current: players){
    		Move position = board.positionOf(current);
    		if (current.getEnd() == Direction.UP){
    			finished = finished || position.getRow() == 0;
    		} else if (current.getEnd() == Direction.DOWN){
    			finished = finished || position.getRow() == 8;
    		} else if (current.getEnd() == Direction.LEFT){
    		    finished = finished || position.getCol() == 0;
    		} else if (current.getEnd() == Direction.RIGHT){
    		    finished = finished || position.getCol() == 8;
    		}
    	}
    	return finished;
    }
    public boolean validMove(Move m, Player p){
    	if (m.isWall()){
    		if (wallCount[Integer.parseInt(p.getSymbol())] != 0){
    			if (board.validMove(m, p)){
    				return true;
    			}
    		}
    	} else {
    		if (board.validMove(m, p)){
    			return true;
    		}
    	}
    	return false;
    }
    public Player getWinner(){
    	for (Player current: players){
    		Move position = board.positionOf(current);
    		if (current.getEnd() == Direction.UP){
    			if (position.getRow() == 0) {
    				return current;
    			}
    		} else if (current.getEnd() == Direction.DOWN){
    			if (position.getRow() == 8){
    				return current;
    			}
    		} else if (current.getEnd() == Direction.LEFT){
    			if (position.getCol() == 0){
    				return current;
    			}
    		} else if (current.getEnd() == Direction.RIGHT){
    			if (position.getCol() == 8){
    				return current;
    			}
    		}
    	}
    	return null;
    }
    
    public void play(){
    	Integer curPlayer = 0;
    	while (!finished()){
    		Move next = players[curPlayer].getMove(this);
    		if (validMove(next, players[curPlayer])){
    			makeMove(next, players[curPlayer]);
    		}
    		curPlayer++;
    		if (curPlayer == players.length){
    			curPlayer = 0;
    		}
    	}
    }
}
