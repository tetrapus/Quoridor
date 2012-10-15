
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
        suppressPrompt = true;
    }
    
    @Override
    public String nextMove(Board b) {
        String temp = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        if (!suppressPrompt) { 
            b.printBoard();
            suppressPrompt = false;
        }
        System.out.print("Enter the move for player "
                + new Integer(this.getID()).toString() + ":");
        try {
            temp = in.readLine();
        } catch (IOException e) { }
        return temp;
    }
    
}
