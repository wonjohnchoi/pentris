package gygd.pentris.choi;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

/**
 * enum to manage different piece types
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
 * 
 * @author Wonjohn Choi
 *
 */
enum TYPE{
	NOTYPE, 
	F, F2, I, L, L2, N, N2, P, P2, T, U, V, W, X, Y, Y2, Z,Z2, //18 Pentris pieces (12 different shapes + 6 mirrored shapes)
	TI,TL,TL2,TD,TS,TS2,TT; //7 Tetris pieces (5 different shapes + 2 mirrored shapes)
	
	private static final TYPE values[]= values();
	private static final int nPentris = 18;
	private static final int nTetris = 7;
	private static final Random rand = new Random();
	
	/**
	 * @return a random Pentris type
	 */
	public static TYPE getRandomPentrisType(){
		return values[rand.nextInt(nPentris)+1];
	}
	
	/**
	 * @return a random Tetris type
	 */
	public static TYPE getRandomTetrisType(){
	    return values[rand.nextInt(nTetris)+1+nPentris];
	}
	
	/**
	 * @return a random type
	 */
	public static TYPE getRandomType(){
	    return values[rand.nextInt(nPentris+nTetris)+1];
	}
}

/**
 * @author Wonjohn Choi
 */
enum Direction{
	DOWN, UP, RIGHT, LEFT, ROTATE_LEFT, ROTATE_RIGHT;
}

/**
 * Piece class that models a piece, either tetris piece or pentris piece
 * @author Wonjohn Choi
 */
public class Piece {
	protected TYPE type; //type of the piece
	protected int coord[][]; //coordinates of each block
	protected int SIZE; //# of block
	protected Color color; //color of the piece
	protected int x = 0, y = 0; //location of the piece
	
	public static HashMap<TYPE, int[][]> coords;
	public static HashMap<TYPE, Color> colors;
	public static HashMap<TYPE, int[]> min, max;
	
	/**
	 * add coordinates data (format==(x,y))
	 */
	private static void addCoords(){
		coords = new HashMap<TYPE, int[][]>();
		coords.put(TYPE.NOTYPE, new int[][]{{0,0},{0,0},{0,0},{0,0},{0,0}});
		coords.put(TYPE.F, new int[][]{{-1,0},{0,-1},{0,0},{0,1},{1,-1}});
		coords.put(TYPE.F2, new int[][]{{-1,0},{0,1},{0,0},{0,-1},{1,1}});
		coords.put(TYPE.I, new int[][]{{0,2},{0,1},{0,0},{0,-1},{0,-2}});
		coords.put(TYPE.L, new int[][]{{0,-2},{0,-1},{0,0},{0,1},{1,1}});
		coords.put(TYPE.L2, new int[][]{{0,2},{0,1},{0,0},{0,-1},{1,-1}});
		coords.put(TYPE.N, new int[][]{{-1,0},{-1,1},{0,0},{0,-1},{0,-2}});
		coords.put(TYPE.N2, new int[][]{{-1,0},{-1,-1},{0,0},{0,1},{0,2}});
		coords.put(TYPE.P, new int[][]{{0,-1},{0,0},{0,1},{1,-1},{1,0}});
		coords.put(TYPE.P2, new int[][]{{0,1},{0,0},{0,-1},{1,1},{1,0}});
		coords.put(TYPE.T, new int[][]{{-1,-1},{0,-1},{0,0},{0,1},{1,-1}});
		coords.put(TYPE.U, new int[][]{{-1,0},{-1,1},{0,1},{1,0},{1,1}});
		coords.put(TYPE.V, new int[][]{{-1,-1},{-1,0},{-1,1},{0,1},{1,1}});
		coords.put(TYPE.W, new int[][]{{-1,-1},{-1,0},{0,0},{0,1},{1,1}});
		coords.put(TYPE.X, new int[][]{{-1,0},{0,-1},{0,0},{0,1},{1,0}});
		coords.put(TYPE.Y, new int[][]{{-1,0},{0,-1},{0,0},{0,1},{0,2}});
		coords.put(TYPE.Y2, new int[][]{{-1,0},{0,1},{0,0},{0,-1},{0,-2}});
		coords.put(TYPE.Z, new int[][]{{-1,-1},{0,-1},{0,0},{0,1},{1,1}});
		coords.put(TYPE.Z2, new int[][]{{-1,1},{0,1},{0,0},{0,-1},{1,-1}});
		
		coords.put(TYPE.TI, new int[][]{{0,-1},{0,0},{0,1},{0,2}});
		coords.put(TYPE.TL, new int[][]{{-1,-1},{-1,0},{0,0},{1,0}});
		coords.put(TYPE.TL2, new int[][]{{-1,1},{-1,0},{0,0},{1,0}});
		coords.put(TYPE.TD, new int[][]{{0,-1},{0,0},{1,-1},{1,0}});
		coords.put(TYPE.TS, new int[][]{{-1,0},{0,0},{0,-1},{1,-1}});
		coords.put(TYPE.TS2, new int[][]{{-1,0},{0,0},{0,1},{1,1}});
		coords.put(TYPE.TT, new int[][]{{-1,0},{0,-1},{0,0},{1,0}});
	}
	
	/**
	 * add color data
	 */
	private static void addColors(){
		colors = new HashMap<TYPE, Color>();
		colors.put(TYPE.NOTYPE, null);
		colors.put(TYPE.F, Color.getHSBColor(255/360.0f, 35/100.0f, 80/100.0f)); //emerald
		colors.put(TYPE.F2, Color.getHSBColor(255/360.0f, 35/100.0f, 80/100.0f));
		colors.put(TYPE.I, Color.BLUE);
		colors.put(TYPE.L, Color.GREEN);
		colors.put(TYPE.L2, Color.GREEN);
		colors.put(TYPE.N, Color.getHSBColor(18/360.0f, 1.0f, 48/100.0f));//brown
		colors.put(TYPE.N2, Color.getHSBColor(18/360.0f, 1.0f, 48/100.0f)); 
		colors.put(TYPE.P, Color.PINK);
		colors.put(TYPE.P2, Color.PINK);
		colors.put(TYPE.T, new Color(0,214,254)); //weak blue
		colors.put(TYPE.U, Color.ORANGE);
		colors.put(TYPE.V, new Color(0,120,0)); //strong green
		colors.put(TYPE.W, new Color(0,0,120)); //strong blue
		colors.put(TYPE.X, Color.RED);
		colors.put(TYPE.Y, Color.YELLOW);
		colors.put(TYPE.Y2, Color.YELLOW);
		colors.put(TYPE.Z, new Color(120,0,120)); //purple
		colors.put(TYPE.Z2, new Color(120,0,120)); 
		
		colors.put(TYPE.TI, new Color(0,200,220));  //weak blue vari
		colors.put(TYPE.TL, new Color(10,10,100)); //strong blue vari
		colors.put(TYPE.TL2, new Color(10,10,100));
		colors.put(TYPE.TD, Color.YELLOW); //yellow
		colors.put(TYPE.TS, Color.RED);
		colors.put(TYPE.TS2, Color.RED);
		colors.put(TYPE.TT, new Color(140,0,140)); //purple vari
	}
	
	/**
	 * add min/max data
	 */
	private static void addMinMax(){
	    min = new HashMap<TYPE, int[]>();
        max = new HashMap<TYPE, int[]>();
	    
        for(TYPE t: TYPE.values()){
            //coordinates of the TYPE
            int [][] coord = coords.get(t);
	       
            //variables to store min & max
            int minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE;
            int maxX=Integer.MIN_VALUE, maxY=Integer.MIN_VALUE;
	       
            for(int block=0;block<coord.length;block++){
                //get minimal values
                if(coord[block][0]<minX){
                    minX = coord[block][0];
                }
                if(coord[block][1]<minY){
                    minY = coord[block][1];
                }
	           
                //get maximal values
                if(coord[block][0]>maxX){
                    maxX = coord[block][0];
                }
                if(coord[block][1]>maxY){
                    maxY = coord[block][1];
                }
            }
            
            //put data
            min.put(t, new int[]{minX,minY});
            max.put(t, new int[]{maxX,maxY});
	       
	   }
	}
	
	/**
	 * organize all data
	 */
	public static void addAllData(){
	    addCoords();
	    addColors();
	    addMinMax();
	}
	
	/**
	 * constructor
	 * @param nBlock
	 */
	public Piece(String mode){
	    //get type
	    if(mode.equals(Pentris.PENTRIS)){
	        setType(TYPE.getRandomPentrisType());
	    }else if(mode.equals(Pentris.TETRIS)){
	        setType(TYPE.getRandomTetrisType());
		}else if(mode.equals(Pentris.BOTH)){
		    setType(TYPE.getRandomType());
		}else{
		    try{
		        throw new Exception("Non-existent mode error");
		    }catch(Exception e){
		        e.printStackTrace();
		    }
		}
	    
	    
	}
	
	/**
	 * method to set the type of piece
	 * @param t
	 */
	public void setType(TYPE t){
		int [][] table = coords.get(t).clone();
		coord = table;
		type = t;
		color = colors.get(t);
	}
	
	/**
	 * return a copy of rotated coordinate
	 */
	public static int[][] rotateLeft(TYPE t, int[][] coordinate){
		//type that does not change after rotating
		if(t!=TYPE.X && t!=TYPE.TD){
			int [][] newCoord = new int[coordinate.length][2]; //new coord
			
			//algorithm to rotate (MATH!)
			for(int i=0;i<coordinate.length;i++){
				newCoord[i][0] = coordinate[i][1];
				newCoord[i][1] = -coordinate[i][0];
			}
			
			return newCoord;
		}
		return coordinate.clone();
		
	
	}
	
	/**
	 * return a copy of rotated coordinate
	 */
	public static int[][] rotateRight(TYPE t, int[][] coordinate){
		//type that does not change after rotating
		if(t!=TYPE.X && t!=TYPE.TD){
			int [][] newCoord = new int[coordinate.length][2]; //new coord
			
			//algorithm to rotate (MATH!)
			for(int i=0;i<coordinate.length;i++){
				newCoord[i][0] = -coordinate[i][1];
				newCoord[i][1] = coordinate[i][0];
			}
			
			return newCoord;
		}
		return coordinate.clone();
	}
	
	/**
	 * check if a given x and y are part of the current piece
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public boolean isPart(int yPos, int xPos){
	    for(int block=0;block<coord.length;block++){
	        if((coord[block][0]+x) == xPos && (coord[block][1]+y)==yPos){
	            return true;
	        }
	    }
	    
	    return false;
	}
	
    /**
     * move a piece to a direction only in data
     * @param dir direction
     */
    public void move(Direction dir){
        switch(dir){
            case DOWN:
                y+=1;
                break;
            case UP:
                y-=1;
                break;
            case LEFT:
                x-=1;
                break;
            case RIGHT:
                x+=1;
                break;
            case ROTATE_LEFT:
                coord = Piece.rotateLeft(type, coord);
                break;
            case ROTATE_RIGHT:
                coord = Piece.rotateRight(type, coord);
        }

    }
	
}
