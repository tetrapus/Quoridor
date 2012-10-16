
package quoridor;


import java.io.*;


public class Human extends Player {
    
    boolean suppressPrompt;
    
    public Human() {
        super();
        suppressPrompt = false;
    }
    
    @Override
    public void onFailure(Board b) {
        System.out.println("Invalid move!");
    }
    
    @Override
    public String nextMove(Board b) {
        String temp = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        b.printBoard();
        System.out.print("Enter the move for player "
                + new Integer(this.getID()).toString() + ": ");
        try {
            temp = in.readLine();
        } catch (IOException e) {
        }
        return temp;
    }
    
}
