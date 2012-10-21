
package quoridor;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


/**
 * Artificial Intelligence Player
 * 
 * The AI has 3 settings, easy, normal and hard.
 * 
 * @author Joey Tuong, Luke Pearson
 *
 */
public class AI extends Player {
    
    private Difficulty dif;
    private int        depth;
    private Random     rand = new Random();
    
    /**
     * Get the difficulty setting of the AI.
     * 
     * @return difficulty of the AI
     */
    public Difficulty getDif() {
        return dif;
    }
    
    /**
     * Set the difficulty of the AI.
     * 
     * @param dif difficulty of the AI
     */
    public void setDif(Difficulty dif) {
        this.dif = dif;
    }
    
    /**
     * Create a new AI with the specified difficulty setting.
     * 
     * @param diff difficulty setting
     */
    AI(Difficulty diff) {
        this.dif = diff;
    }
    
    /**
     * Gets the next move using the 'easy' algorithm (weighted by distance to end)
     * 
     * @param b input board
     * @return move to apply
     */
    public Position getMoveEasy(Board b) {
        Position p = getShortestPath(b, b.currentPlayer());
        if (p.parent != null) {
            while (p.parent.parent != null) {
                p = p.parent;
            }
        }
        return p;
    }
    
    /**
     * Shortest path algorithm from a position to the end of the board.
     * 
     * @param board input board
     * @param pl player id
     * @return child node of the generated tree of positions.
     */
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
    
    /**
     * Check if a position p is at the winning end of the board from player pl's
     * perspective.
     * 
     * @param p position to check
     * @param pl player
     * @return if position is at the correct end of the board
     */
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
    
    /**
     * Gets the next move using the 'normal' algorithm (weighted by relative distance to end)
     * 
     * @param board input board
     * @return move to apply
     */
    public Position getMoveNormal(Board board) {
        List<Integer> players = board.getPlayers();
        while (players.get(0) != this.getID()) {
            players.add(players.remove(0));
        }
        
        List<Position> moves = generateMoves(board);
        Position maxMove = moves.remove(0);
        int maxscore = score(board.makeMove(maxMove.toString()), this.getID());
        for (Position current : moves) {
            int temp = score(board.makeMove(current.toString()), this.getID());
            if (temp > maxscore) {
                maxscore = temp;
                maxMove = current;
                break;
            }
        }
        return maxMove;
    }
    
    /**
     * Gets the next move using the 'hard' algorithm (weighted by butchered alpha-beta)
     * 
     * @param board input board
     * @return move to apply
     */
    public Position getMoveHard(Board board) {
        List<Integer> players = board.getPlayers();
        while (players.get(0) != this.getID()) {
            players.add(players.remove(0));
        }
        
        depth = 1;
        
        List<Position> moves = generateMoves(board);
        Position maxMove = moves.remove(0);
        int maxscore = alphaBeta(players, this.getID(),
                board.makeMove(maxMove.toString()), 0, -1000000, +1000000);
        for (Position current : moves) {
            int temp = alphaBeta(players, this.getID(),
                    board.makeMove(current.toString()), 0, -1000000, +1000000);
            if (temp >= maxscore) {
                maxscore = temp;
                maxMove = current;
                break;
            }
        }
        return maxMove;
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
    
    /**
     * Get a heuristic score for a given board state from a given player's perspective
     * 
     * @param state current board state
     * @param player perspective player
     * @return heuristic score of a board state
     */
    public Integer score(Board state, int player) {
        Position move = getShortestPath(state, player);
        List<Integer> others = state.getPlayers();
        others.remove(player - 1);
        int min = getShortestPath(state, others.remove(0)).pathLength();
        for (int i : others) {
            int s = getShortestPath(state, i).pathLength();
            if (s > min) {
                min = s;
            }
        }
        return (min - move.pathLength());
    }
    
    /**
     * Get all possible valid moves for the current player at a given state.
     * 
     * @param state board state
     * @return list of valid moves
     */
    public List<Position> generateMoves(Board state) {
        int player = state.currentPlayer();
        List<Position> moves = state.validMoves(state.positionOf(player));
        if (state.remainingWalls(player) > 0) {
            for (char i = '1'; i < '9'; i++) {
                for (char j = 'a'; j < 'i'; j++) {
                    Position horstate = new Position(String.valueOf(j)
                            + String.valueOf(i) + "h");
                    Position verstate = new Position(String.valueOf(j)
                            + String.valueOf(i) + "v");
                    if (state.isValidMove(horstate.toString())) {
                        moves.add(horstate);
                    }
                    if (state.isValidMove(verstate.toString())) {
                        moves.add(verstate);
                    }
                }
            }
        }
        if (moves.size() > 2) {
            moves.remove(rand.nextInt(moves.size()));
        }
        return moves;
    }
    
    /**
     * Alpha-beta algorithm
     * 
     * @param players List of players
     * @param max Player to maximise
     * @param state Current board state
     * @param level Number of levels of recursion
     * @param alpha alpha score
     * @param beta beta score
     * @return min-max score
     */
    public Integer alphaBeta(List<Integer> players, Integer max, Board state,
            Integer level, Integer alpha, Integer beta) {
        Integer p = players.remove(0);
        players.add(p);
        int score = 0;
        
        if (level == depth) {
            return score(state, p);
        } else {
            List<Position> moves = generateMoves(state);
            if (p.equals(max)) {
                for (Position next : moves) {
                    // System.out.println(next);
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
