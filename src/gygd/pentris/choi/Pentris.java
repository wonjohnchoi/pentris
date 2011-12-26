package gygd.pentris.choi;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Pentris class to manage the whole frame
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
 */
public class Pentris implements KeyListener {
    private JFrame window;
    private Board gameBoard;
    private GameThread gameThread;
    private Piece curPiece;
    private GameInfo gameInfo;

    private boolean isPlaying;
    private boolean isPaused;

    private String mode;
    protected final static String TETRIS = "Play Tetris";
    protected final static String PENTRIS = "Play Pentris";
    protected final static String BOTH = "Play Tetris+Pentris";
    protected final static String ABOUT = "About This Program";
    protected final static String HOTKEY = "Hot Keys";
    private final int BLOCK_SIZE = 28;
    private int WIDTH = 12, HEIGHT = 25;

    /**
     * constructor
     */
    public Pentris() {
        mode = getMode(); // get play mode

        // if mode is "About" mode
        while (mode.equals(ABOUT) || mode.equals(HOTKEY)) {
            if (mode.equals(ABOUT)) {
                JOptionPane.showMessageDialog(null,
                                "\'Pentris (+Tetris)\' was developed in 08/2010,\n"
                                        + "by Wonjohn Choi, a Gr.12 student in St. Francis Xavier S. S.\n"
                                        + "Check http://gygd.wordpress.com/ for more info!",

                                "About", JOptionPane.INFORMATION_MESSAGE);
            } else if (mode.equals(HOTKEY)) {
                JOptionPane.showMessageDialog(null, "P: Pause\n"
                        + "R: Restart\n" + "SPACE: drop\n" + "UP: rotate\n"
                        + "LEFT, RIGHT, DOWN: move to a direction",

                "Hot Keys", JOptionPane.INFORMATION_MESSAGE);
            }
            mode = getMode();
        }

        window = new JFrame("Pentris by Wonjohn Choi"); // create frame
        window.setLayout(new BorderLayout());

        gameInfo = new GameInfo();
        window.add(gameInfo, BorderLayout.NORTH);

        gameBoard = new Board(HEIGHT, WIDTH, BLOCK_SIZE); // create game board
        window.add(gameBoard, BorderLayout.CENTER); // add board to frame

        window.setSize(WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE); // set size of window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close on exit
        window.addKeyListener(this); // add keyboard listener
        window.setVisible(true);
        window.setResizable(false);

        Piece.addAllData(); // add data on color, coordinates, min/max

        isPlaying = false;
        gameThread = new GameThread();
        gameThread.start();
    }

    /**
     * Start a Tetris game
     */
    public void startGame() {
        if (!isPlaying) {
            gameBoard.reset();
            isPlaying = true;
            isPaused = false;
            curPiece = null;
            gameInfo.reset();
            // info = new GameInfo();
            // Initialize the game thread.

        }
    }

    /**
     * try to move a piece to a direction by one
     * 
     * @param piece
     *            piece to move
     * @param dir
     *            direction
     */
    public boolean tryMove(Piece piece, Direction dir) {
        // if it's ok to move the piece
        if (isSafeToMove(piece, dir)) {
            gameBoard.remove(piece); // graphically take out piece to board
            piece.move(dir); // /move piece data to down
            gameBoard.add(piece); // graphically add piece to board
            return true;
        } else {
            return false;
        }
    }

    /**
     * check if it's safe to move a piece to a direction
     * 
     * @param piece
     *            piece to move
     * @param dir
     *            direction
     * @return false only if the next section is occupied by other piece or piece reaches bottom
     */
    public boolean isSafeToMove(Piece piece, Direction dir) {
        int coord[][]; // get the coordinate data

        // rotate piece only to get the rotated data
        if (dir == Direction.ROTATE_LEFT) {
            coord = Piece.rotateLeft(piece.type, piece.coord);
        } else if (dir == Direction.ROTATE_RIGHT) {
            coord = Piece.rotateRight(piece.type, piece.coord);
        } else {
            coord = piece.coord;
        }

        boolean isSafe = true;
        int nextX, nextY;

        // for each block
        for (int block = 0; block < coord.length && isSafe; block++) {
            nextX = coord[block][0] + piece.x; // get x of block
            nextY = coord[block][1] + piece.y; // get y of block

            // find next position
            switch (dir) {
                case DOWN:
                    nextY += 1;
                    break;
                case UP:
                    nextY -= 1;
                    break;
                case LEFT:
                    nextX -= 1;
                    break;
                case RIGHT:
                    nextX += 1;
            }

            // if piece is in screen and the piece is on a part of other piece,
            if (gameBoard.inWidthRange(nextX) && gameBoard.inHeightRange(nextY)) {
                // if the piece is on a part of other piece
                if (!piece.isPart(nextY, nextX)
                        && !gameBoard.isEmpty(nextY, nextX)) {
                    isSafe = false; // it's not safe

                    // if part of the piece goes outside of the screen,
                    if ((Piece.min.get(piece.type)[1] + piece.y) <= 0) {
                        isPlaying = false;// game ends
                        gameInfo.updateStatus("Lost");
                        // JOptionPane.showMessageDialog(window, String.format("Score: %d\nLines: %d",score,
                        // totalRemovedLines));
                    }
                }
                // piece touches bottom
            } else if (nextY == gameBoard.HEIGHT) {
                isSafe = false;
                // piece goes out of left, right screen
            } else if (!gameBoard.inWidthRange(nextX)) {
                isSafe = false;
            }

        }

        return isSafe;
    }

    /**
     * thread to run game
     * 
     * @author Wonjohn Choi
     */
    private class GameThread extends Thread {
        public void run() {

            while (true) {
                // while playing game
                while (isPlaying) {
                    // while it's not paused
                    if (!isPaused) {
                        // if piece stopped
                        if (curPiece == null) {
                            int removedLines = 0;
                            int row = gameBoard.HEIGHT - 1;

                            while (row >= 0) {
                                // System.out.println("Detecting completed lines");
                                boolean isRemovable = true;

                                // check if removable a line
                                for (int col = 0; col < gameBoard.WIDTH
                                        && isRemovable; col++) {
                                    if (gameBoard.isEmpty(row, col)) {
                                        isRemovable = false;
                                    }
                                }

                                // if removable
                                if (isRemovable) {
                                    // Remove the row.
                                    gameBoard.removeRow(row);

                                    removedLines++;
                                    gameInfo.updateLines(1);

                                    // change speed of play for every 10 lines
                                    gameInfo.updateLevel();

                                } else {
                                    // search next row
                                    row--;
                                }
                            }

                            // calculator score if removed lines exist
                            if (removedLines > 0) {
                                gameInfo.updateScore(removedLines
                                        * removedLines * 10);
                            }

                            curPiece = new Piece(mode);
                            curPiece.x = gameBoard.WIDTH / 2 - 1; // locate piece in the middle
                            curPiece.y = -Piece.max.get(curPiece.type)[1]; // locate piece at the top
                            gameBoard.add(curPiece);

                            // if piece is working fine,
                        } else {
                            // if piece cannot move down due to other pieces
                            if (!tryMove(curPiece, Direction.DOWN)) {
                                curPiece = null; // piece stops
                            }

                        }
                    }
                    // if piece didn't stop
                    if (curPiece != null) {
                        // stop for a while (delay)
                        try {
                            sleep(gameInfo.getDelay());
                        } catch (InterruptedException e) {
                            System.err.println("Exception e: " + e);
                        }
                    }

                    if (!isPaused && isPlaying) {
                        gameInfo.updateTime();
                    }
                }

            }
        }
    }

    /**
     * when key is pressed
     * P: pause
     * R: restart
     * Space: drop
     * 
     * @Override
     */
    public void keyPressed(KeyEvent e) {
        // use switch to compare with given key code
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                isPlaying = false;
                startGame();
                break;
            case KeyEvent.VK_P:
                isPaused = !isPaused;
                if (isPaused) {
                    gameInfo.updateStatus("Paused");
                } else {
                    gameInfo.updateStatus("Playing");
                }
                break;
        }

        // if piece does not exist exit from the function
        if (curPiece == null || isPaused || !isPlaying) {
            return;
        }

        // use switch to compare with given key code
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                tryMove(curPiece, Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                tryMove(curPiece, Direction.RIGHT);
                break;
            case KeyEvent.VK_UP:
                tryMove(curPiece, Direction.ROTATE_RIGHT);
                break;
            /*
             * case KeyEvent.VK_Z:
             * tryMove(curPiece, Direction.ROTATE_LEFT);
             * break;
             * case KeyEvent.VK_X:
             * tryMove(curPiece, Direction.ROTATE_RIGHT);
             * break;
             */
            case KeyEvent.VK_DOWN:
                // if unable to move down anymore
                if (!tryMove(curPiece, Direction.DOWN)) {
                    curPiece = null; // stop piece
                }
                break;
            case KeyEvent.VK_SPACE:
                gameBoard.remove(curPiece); // take out piece graphic

                // keep piece down till it is destroyed
                while (isSafeToMove(curPiece, Direction.DOWN)) {
                    curPiece.move(Direction.DOWN);
                }

                gameBoard.add(curPiece); // add piece graphic
                curPiece = null; // stop piece
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * class to get the mode of game from user
     * 
     * @return mode
     */
    private String getMode() {
        // possible modes
        JButton[][] modes = { { new JButton(TETRIS), new JButton(PENTRIS) },
                { new JButton(BOTH), null },
                { new JButton(ABOUT), new JButton(HOTKEY) } };

        // call mode chooser to choose mode
        ModeChooser mc = new ModeChooser(modes, 1, "Choose Mode");

        Thread chooser = new Thread(mc);
        chooser.run();
        // wait till choosing process ends
        synchronized (mc.out) {
            try {
                System.out.println("W1");
                mc.out.wait();
                System.out.println("W2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String mode = mc.out[0].getText();
        return mode;
    }

    /**
     * main method to run Pentris
     * 
     * @param args
     */
    public static void main(String args[]) {
        new Pentris().startGame();
    }
}
