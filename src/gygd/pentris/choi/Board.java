package gygd.pentris.choi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Board class (part of Pentris)
 * 
 * Copyright (C) 2010 Wonjohn Choi
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * @author Wonjohn Choi
 *
 */
public class Board extends JPanel {
	/**
     * UID
     */
    private static final long serialVersionUID = 8735251292056356635L;

    //array to control each block
	protected JButton[][] blockGrid;

	//width and height of the board
	protected int HEIGHT, WIDTH;
	
	//default color of board
	protected static final Color NULLCOLOR = new JButton().getBackground();
	
	/**
	 * constructor
	 * @param height height of the board
	 * @param width width of the board
	 */
	public Board(int height, int width){
		HEIGHT = height; WIDTH = width;
		
		blockGrid = new JButton[HEIGHT][WIDTH]; //instantiate button array
		setLayout(new GridLayout(HEIGHT, WIDTH)); //set its layout using layout manager
		
		//for each and every block,
		for(int row=0;row<HEIGHT;row++){
			for(int col=0;col<WIDTH;col++){
				blockGrid[row][col] = new JButton();//set each block as a button
				blockGrid[row][col].setBorder(new LineBorder(Color.getHSBColor(0, 0,0.8F)));//set its border color
				blockGrid[row][col].setEnabled(false);//disable it
				add(blockGrid[row][col]);//add to the panel
			}
		}
		
		setPreferredSize(new Dimension(WIDTH*30, HEIGHT*30)); //set the size (each block has a size length of 30)
	}
	
	/**
	 * another version of constructor
	 * @param height height of board
	 * @param width width of board
	 * @param size size of block
	 */
	public Board(int height, int width, int size){
	    this(height, width);
	    setPreferredSize(new Dimension(WIDTH*size, HEIGHT*size));
	}

	/**
	 * check if a grid is empty
	 */
	public boolean isEmpty(int row, int col){
		return blockGrid[row][col].getBackground().equals(NULLCOLOR);
	}
	
	/**
	 * set a block empty
	 * @param row
	 * @param col
	 */
	public void setEmpty(int row, int col){
	    blockGrid[row][col].setBackground(NULLCOLOR);
	}
	
	/**
	 * fill a grid
	 */
	public void fill(int row, int col, Color c){
		blockGrid[row][col].setBackground(c);
	}
	
	/**
	 * try a block to move with a direction
	 */
	public void tryMove(int row, int col, Direction dir){
		if(dir==Direction.DOWN || dir==Direction.UP){
			int nextRow = row+(dir==Direction.DOWN?1:-1);
			
			//Failed Case
			if(!inHeightRange(nextRow)){
				return;
			}
			
			blockGrid[nextRow][col].setBackground(blockGrid[row][col].getBackground()); //set bottom's color as current's
			blockGrid[row][col].setBackground(NULLCOLOR); //set current no background
		}else if(dir==Direction.RIGHT || dir==Direction.LEFT){
			int nextCol = row + (dir==Direction.RIGHT?1:-1);
			
			//Failed Case
			if(!inWidthRange(nextCol)){
				return;
			}
			
			blockGrid[row][nextCol].setBackground(blockGrid[row][col].getBackground()); //set left or right color as current's
			blockGrid[row][col].setBackground(NULLCOLOR); //set current no background
		}
	}
	
	
	/**
	 * reset board
	 */
    public void reset() {
        
       //for every index
        for(int r=0;r<HEIGHT;r++){
            for(int c=0;c<WIDTH;c++){
                blockGrid[r][c].setBackground(NULLCOLOR); //reset color
                //grid[r][c] = EMPTY; //reset data
            }
        }
        
    }

    /**
     * remove a row
     * @param row
     */
    public void removeRow(int row) {
        //if row is not the most top row
        if(row!=0){
            //move lines down
            for(int i=row-1;i>=0;i--){
                for(int col=0;col<WIDTH;col++){
                    blockGrid[i+1][col].setBackground(blockGrid[i][col].getBackground());
                }
            }
        }
            
        //make empty the top line
        for(int col=0;col<WIDTH;col++){
            blockGrid[0][col].setBackground(NULLCOLOR);
        }
        
    }
    
    /**
     * check range in width
     */
    public boolean inWidthRange(int x){
        return x>=0 && x<WIDTH;
    }
    
    /**
     * check range in height
     */
    public boolean inHeightRange(int y){
        return y>=0 && y<HEIGHT;
    }

    /**
     * graphically add pieces without checking any danger
     * @param curPiece piece to be added
     */
    public void add(Piece curPiece) {
        int [][] coord = curPiece.coord; //get coordinates of current piece's blocks
        
        //for each block
        for(int block=0;block<coord.length;block++){
            int y = coord[block][1]+curPiece.y; //get current block's y
            int x = coord[block][0]+curPiece.x; //get current block's x
            
            //check index
            if(inHeightRange(y) && inWidthRange(x)){
                //set desired color
                blockGrid[y][x].setBackground(curPiece.color);
                
            }
        }
        
    }

    /**
     * graphically remove a piece without checking any danger
     * @param curPiece piece to be removed
     */
    public void remove(Piece curPiece) {
        int [][]  coord = curPiece.coord;//get coordinates of current piece's blocks
        
        //for every block
        for(int block=0;block<coord.length;block++){
            int y = coord[block][1]+curPiece.y; //get current block's y
            int x = coord[block][0]+curPiece.x; //get current block's x
            
            //check index
            if(inHeightRange(y) && inWidthRange(x)){
                //take out color
                blockGrid[y][x].setBackground(NULLCOLOR);
            }
        }
        
    }

    /**
     * main test method
     * @param args
     */
    public static void main(String args[]){
    	JFrame test = new JFrame();
    	Board board = new Board(20, 10);
    	test.add(board);
    	test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	test.setVisible(true);
    }
}