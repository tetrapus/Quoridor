package quoridor;

import java.util.LinkedList;

public class Game {
    LinkedList<Move> history;
    Board board = new Board();
    Player[] players;
    public Game(Player[] players) {
    	Integer count = 0;
    	for (Player current: players){
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
    	board.printBoard();
        board = new Board();
        this.players = players;
    }
    
    public void makeMove(){
    	
    }
    
    public void printState(){
    	
    }
    
    public boolean finished(){
    	boolean finished = false;
    	for (Player current: players){
    		//board.getPositionOf(current);
    		
    	}
    	return finished;
    }
    
    public Player getWinner(){
    	Player winner = null;
    	return winner;
    }
    
    public void play(){
    	while (!finished()){
    		makeMove();
    		printState();
    	}
    }
}
