package gygd.pentris.choi;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * class to manage information
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
 * Class to show information to players
 * @author Wonjohn Choi
 *
 */
public class GameInfo extends JPanel{

    /**
     * UID
     */
    private static final long serialVersionUID = 6027980366501916643L;
    
    private JLabel score = new JLabel();
    private JLabel lines = new JLabel();
    private JLabel status = new JLabel();
    private JLabel level = new JLabel();
    private JLabel time = new JLabel();
    
    private int nLevel;
    private int nScore;
    private int nLines;
    private long nDelay;
    private long nTime;
    
    /**
     * constructor
     */
    public GameInfo(){
        setLayout(new FlowLayout());
        add(status);
        add(score);
        add(lines);
        add(level);
        add(time);
        reset();
    }
    
    /**
     * method to update score
     * @param s score
     */
    public void updateScore(int s){
        nScore+=s;
        score.setText("Score: "+nScore);
    }
    
    /**
     * method to update line #
     * @param # of lines
     */
    public void updateLines(int l){
        nLines+=l;
        lines.setText("Lines: "+nLines);
    }
    
    /**
     * method to update status
     * @param s status
     */
    public void updateStatus(String s){
        status.setText("Status: "+s);
    }
    
    public long getDelay(){
        return nDelay;
    }
    
    public int getLines(){
        return nLines;
    }
    
    /**
     * method to update level
     */
    public void updateLevel(){
        if(nDelay>50 && nLines!=0 && nLines%10==0){
            nDelay-=10;
            nLevel++;
            level.setText("Level: "+nLevel);
        }
        
    }
    
    /**
     * method to update time
     */
    public void updateTime(){
        long needs = ((nTime/1000)+1)*1000-nTime;
        nTime+=nDelay;
        
        if(nDelay>=needs){
            time.setText(String.format("Time: %ds", (nTime/1000)));
        }
        
    }

    /**
     * reset
     */
    public void reset(){
        score.setText("Score: 0");
        lines.setText("Lines: 0");
        status.setText("Status: Playing");
        level.setText("Level: 1");
        time.setText("Time: 0s");
        nLevel = 1;
        nScore = 0;
        nLines = 0;
        nDelay = 500;
        nTime = 0;
    }
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        JFrame window = new JFrame();
        GameInfo info = new GameInfo();
        info.updateStatus("Playing");
        info.updateLines(5);
        info.updateScore(400);
        
        
        window.add(info); 
        window.pack();
        window.setVisible(true);
       

    }

}
