
package quoridor;


/**
 * Behold, a wall.
 * 
 * @author Joey Tuong
 * @author Luke Pearson
 */
public class Wall {
    enum Orientation {
        Horizontal, Vertical
    };
    
    int         row;
    int         col;
    Orientation orientation;
    
    public Wall(String position) {
        row = position.charAt(0) - 'a';
        col = position.charAt(1) - '0';
        orientation = position.charAt(2) == 'h' ? Orientation.Horizontal
                : Orientation.Vertical;
    }

    
}
