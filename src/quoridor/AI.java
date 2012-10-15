
package quoridor;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;


public class AI extends Player {
    
    private Difficulty dif;
    private int        depth;
    
    public Difficulty getDif() {
        return dif;
    }
    
    public void setDif(Difficulty dif) {
        this.dif = dif;
    }
    
    AI(Difficulty diff) {
        this.dif = diff;
    }
    
    public Position getMoveEasy(Board b) {
        Position p = getShortestPath(b);
        if (p.parent != null) {
            while (p.parent.parent != null) {
                p = p.parent;
            }
        }
        return p;
    }
    
    public Position getShortestPath(Board board) {
        Queue<Position> q = new LinkedList<Position>();
        LinkedList<Position> visited = new LinkedList<Position>();
        Position current = board.positionOf(this);
        
        boolean finished = false;
        
        q.add(current);
        visited.add(current);
        
        while (!q.isEmpty() && !finished) {
            current = q.remove();
            
            for (Position p : board.validMoves(current)) {
                if (!visited.contains(p)) {
                    p.parent = current;
                    visited.add(p);
                    q.add(p);
                    if (finished(p)) {
                        current = p;
                        finished = true;
                        break;
                    }
                }
            }
        }
        
        return current;
    }
    
    public boolean finished(Position p) {
        if (this.getEnd() == Direction.UP && p.getRow() == 0) {
            return true;
        } else if (this.getEnd() == Direction.DOWN && p.getRow() == 8) {
            return true;
        } else if (this.getEnd() == Direction.LEFT && p.getCol() == 0) {
            return true;
        } else if (this.getEnd() == Direction.RIGHT && p.getCol() == 8) { return true; }
        return false;
    }
    
    public Position getMoveNormal(Board board) {
        List<Integer> players = board.getPlayers();
        while (players.get(0) != this.getID()) {
            players.add(players.remove(0));
        }
        
        depth = 5;
        
        int maxscore = 0;
        Position maxMove = null;
        List<Position> moves = board.validMoves(board.positionOf(this));
        
        for (Position current : moves) {
            int temp = alphaBeta(players, this.getID(),
                    board.makeMove(current.toString()), 0, -1000000, +1000000);
            if (temp > maxscore) {
                maxscore = temp;
                maxMove = current;
                break;
            }
        }
        return maxMove;
    }
    
    public Position getMoveHard(Board b) {
        Position next = null;
        return next;
    }
    
    @Override
    public String nextMove(Board b) {
        String retval = "";
        if (this.dif == Difficulty.Easy) {
            retval = getMoveEasy(b).toString();
        } else if (this.dif == Difficulty.Normal) {
            retval = getMoveNormal(b).toString();
        } else {
            retval = getMoveHard(b).toString();
        }
        b.printBoard();
        return retval;
    }
    
    public Integer score(Board state) {
        Position move = getShortestPath(state);
        return (8 - move.pathLength());// make longer lengths worth less
    }
    
    // maximises p1, minimises p2
    public Integer alphaBeta(List<Integer> players, Integer max, Board state,
            Integer level, Integer alpha, Integer beta) {
        Integer p = players.remove(0);
        players.add(p); // rotate the list.
        int score = 0;
        
        if (level == depth) {
            return score(state);
        } else {
            List<Position> moves = state.validMoves(state.positionOf(state.currentPlayer()));
            if (p.equals(max)) {
                for (Position next : moves) {
                    score = alphaBeta(players, max,
                            state.makeMove(next.toString()), level + 1, alpha,
                            beta);
                    if (score > alpha) {
                        alpha = score;
                    }
                    if (beta <= alpha) {
                        break;
                        
                    }
                }
                return alpha;
            } else {
                for (Position next : moves) {
                    
                    score = alphaBeta(players, max,
                            state.makeMove(next.toString()), level + 1, alpha,
                            beta);
                    if (score < beta) {
                        beta = score;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                    
                }
                return beta;
            }
        }
    }
}
