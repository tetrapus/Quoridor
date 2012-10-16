package quoridor;

/**
 * @author Luke
 * @author Joey Tuong
 */

import java.io.*;

public class Main {
	private static Game game;
	/**
	 * IO handling function
	 * gets players and starts the game
	 * @throws IOException 
	 */
	public static void main (String[] argv) throws IOException{ 
	    String[] AInames = {"GLaQuOS", "Deep Cyan", "Quoribot", "Digital Turk"};
	     BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to a java implementation of Quoridor");
		System.out.println("THERE IS NOTHING WRONG WITH NOBLOCKJUMP");
		System.out.println("Writen by Luke Pearson and Joseph Tuong");
		String temp = "";
		Integer size =0;
		Player[] players;
		while(size != 2 && size != 4	){
			temp = "";
			while(!tryParseInt(temp)){
				System.out.print("Number of players (2/4): ");
				temp = in.readLine();
			}

			size = Integer.parseInt(temp);
		}
		players = new Player[size];
		
		for (Integer i = 1; i<= size; i++){
			temp = "";
			while (!temp.toLowerCase().matches("(ai?|h(uman)?)")) {
				System.out.print("Player " + i.toString() + " is a (human/ai): ");
				temp = in.readLine();
			}
			if (temp.toLowerCase().matches("h(uman)?")){
				players[i - 1] = new Human();

	            System.out.print("Player " + i.toString() + " name: ");
	            temp = in.readLine();
	            players[i - 1].setName(temp);
			} else {
				while (!temp.toLowerCase().matches("(e(asy)?|m(edium)?|h(ard)?)")){
					System.out.print("AI difficulty (EASY/MEDIUM/HARD): ");
					temp = in.readLine();
				}
				Difficulty diff;
				if (temp.toLowerCase().matches("e(asy)?")){
					diff = Difficulty.Easy;
				} else if (temp.toLowerCase().matches("h(ard)?")){
					diff = Difficulty.Hard;
				} else {
					diff = Difficulty.Normal;
				}
				players[i - 1] = new AI(diff);
				players[i - 1].setName(AInames[i-1]);
				
			}
		}
		game = new Game(players);// players);
		
		game.play();
		game.getBoard().printBoard();
        System.out.println("Player "+ game.getWinner().getName() + " wins!");
	}
	/**
	 * @param string which is meant to be an int
	 * @return false or true if correct parse
	 */
	static boolean tryParseInt(String value)  
	{  
	     try  
	     {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch(NumberFormatException nfe)  
	      {  
	          return false;  
	      }  
	}
}