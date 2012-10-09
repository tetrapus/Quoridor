package quoridor;

/**
 * @author Luke
 * @author Joey Tuong
 */

import java.io.*;

public class Main {
	private static Board game;
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
		
		Player[] players = new Player[2];
		
		while (!temp.equals("H") && !temp.equals("AI")){
			System.out.print("Enter the type of player 1 human or AI(H/AI): ");
			temp = in.readLine();
		}
		if (temp == "H"){
			players[0] = new Human();
		} else {
			players[0] = new AI();
		}
		System.out.print("Enter the name of player 1: ");
		temp = in.readLine();
	
		players[0].setName(temp);
		temp = "";
		while (!temp.equals("H") && !temp.equals("AI")){
			System.out.print("Enter the type of player 2 human or AI(H/AI): ");
			temp = in.readLine();
		}
		if (temp == "H"){
			players[1] = new Human();
		} else {
			players[1] = new AI();
		}
		System.out.print("Enter the name of player 2: ");
		temp = in.readLine();
		int size = 0;
		players[1].setName(temp);
		while (size % 2 == 0){
			temp = "";
			while(!tryParseInt(temp)){
				System.out.print("Enter the board size, must be odd number: ");
				temp = in.readLine();
			}
			size = Integer.parseInt(temp);
		}

		
		game = new Board(size, players);// players);
		while(!game.finished()){
			game.makeMove();
			game.printState();
		}
		
		String winner = game.getWinner();
		System.out.println("The winner is " + winner);
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