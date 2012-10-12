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
	     BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to a java implementation of Quoridor");
		System.out.println("Writen by Luke Pearson and Joseph Tuong");
		String temp = "";
		Integer size =0;
		Player[] players;
		while(size != 2 && size != 4	){
			temp = "";
			while(!tryParseInt(temp)){
				System.out.print("Enter the number of players(2/4): ");
				temp = in.readLine();
			}

			size = Integer.parseInt(temp);
		}
		players = new Player[size];
		
		for (Integer i = 1; i<= size; i++){
			temp = "";
			while (!temp.equals("H") && !temp.equals("AI")){
				System.out.print("Enter the type of player " + i.toString() + " human or AI(H/AI): ");
				temp = in.readLine();
			}
			if (temp.equals("H")){
				players[i - 1] = new Human();
			} else {
				while (!temp.equals("EASY") && !temp.equals("MEDIUM") && !temp.equals("HARD")){
					System.out.println("Enter the difficulty of the AI (EASY/MEDIUM/HARD): ");
					temp = in.readLine();
				}
				Difficulty diff;
				if (temp.equals("EASY")){
					diff = Difficulty.Easy;
				} else if (temp.equals("HARD")){
					diff = Difficulty.Hard;
				} else {
					diff = Difficulty.Normal;
				}
				players[i - 1] = new AI(diff);
			}
			System.out.print("Enter the name of player " + i.toString() + ":");
			temp = in.readLine();
			players[i - 1].setName(temp);
		}
		game = new Game(players);// players);
		
		game.play();
		
		//String winner = game.getWinner();
		//System.out.println("The winner is " + winner);
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