
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
        Position p = getShortestPath(b, b.currentPlayer());
        if (p.parent != null) {
            while (p.parent.parent != null) {
                p = p.parent;
            }
        }
        return p;
    }
    
    public Position getShortestPath(Board board, int pl) {
        Queue<Position> q = new LinkedList<Position>();
        LinkedList<Position> visited = new LinkedList<Position>();
        Position current = board.positionOf(pl);
        
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
                    if (finished(p, pl)) {
                        current = p;
                        finished = true;
                        break;
                    }
                }
            }
        }
        
        return current;
    }
    
    public boolean finished(Position p, Integer pl) {
        if (pl == 1 && p.getRow() == 0) {
            return true;
        } else if (pl == 2 && p.getRow() == 8) {
            return true;
        } else if (pl == 3 && p.getCol() == 0) {
            return true;
        } else if (pl == 4 && p.getCol() == 8) { return true; }
        return false;
    }
    
    public Position getMoveNormal(Board board) {
        List<Integer> players = board.getPlayers();
        while (players.get(0) != this.getID()) {
            players.add(players.remove(0));
        }
        
        depth = 2;
        
        List<Position> moves = board.validMoves(board.positionOf(this));
        Position maxMove = moves.remove(0);
        int maxscore = alphaBeta(players, this.getID(),
                board.makeMove(maxMove.toString()), 0, -1000000, +1000000);
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
        Position move = getShortestPath(state, state.currentPlayer());
        List<Integer> others = state.getPlayers();
        others.remove(state.currentPlayer() - 1);
        int max = getShortestPath(state, others.remove(0)).pathLength();
        for (int i : others) {
            int s = getShortestPath(state, i).pathLength();
            if (s > max) {
                max = s;
            }
        }
        return (max - move.pathLength());
    }
    
    public List<Position> generateMoves(Board state) {
        List<Position> moves = state.validMoves(state.positionOf(state
                .currentPlayer()));
        if (state.remainingWalls(state.currentPlayer()) > 0) {
            for (char i='1'; i<'9'; i++) {
                for (char j='a'; j<'i'; j++) {
                    Position horstate = new Position(String.valueOf(j)+String.valueOf(i)+"h");
                    Position verstate = new Position(String.valueOf(j)+String.valueOf(i)+"v");
                    if (state.isValidMove(horstate.toString())) {
                        moves.add(horstate);
                    }
                    if (state.isValidMove(verstate.toString())) {
                        moves.add(verstate);
                    }
                }
            }
        }
        return moves;
    }
    
    public Integer alphaBeta(List<Integer> players, Integer max, Board state,
            Integer level, Integer alpha, Integer beta) {
        Integer p = players.remove(0);
        players.add(p);
        int score = 0;
        
        if (level == depth) {
            return score(state);
        } else {
            List<Position> moves = generateMoves(state);
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
