package calder.game;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    private final int boardWidth = 300;
    private final int boardHieght = 300;
    private final int dotSize = 10;
    private final int allDots = 900;
    private final int randomPos = 30;
    private final int delay = 150;

    private final int x[] = new int[allDots];
    private final int y[] = new int[allDots];

    private int dots;
    private int appleX;
    private int appleY;
    private int score;

    private boolean dLeft = false;
    private boolean dRight = false;
    private boolean dUp = true;
    private boolean dDown = false;
    private boolean inGame = true;

    private Timer timer;
    private Image sBody;
    private Image apple;
    private Image sHead;

    public Board() {

        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(boardWidth, boardHieght));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon sBodyImage = new ImageIcon("src/images/SnakeBody.png");
        sBody = sBodyImage.getImage();

        ImageIcon appleImage = new ImageIcon("src/images/Apple.png");
        apple = appleImage.getImage();

        ImageIcon sHeadImage = new ImageIcon("src/images/SnakeHead.png");
        sHead = sHeadImage.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z=0; z<dots; z++){
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(sHead, x[z], y[z], this);
                } else {
                    g.drawImage(sBody, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (boardWidth - metr.stringWidth(msg)) / 2, boardHieght / 2);
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++;
            locateApple();
            score++;
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }

        if (dLeft) {
            x[0] -= dotSize;
        }

        if (dRight) {
            x[0] += dotSize;
        }

        if (dUp) {
            y[0] -= dotSize;
        }

        if (dDown) {
            y[0] += dotSize;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }

        }

        if (y[0] >= boardHieght) {
            inGame = false;
        }

        if (y[0] < 0){
            inGame = false;
        }

        if (x[0] >= boardWidth) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }

    }
    private void locateApple() {
        int r = (int) (Math.random() * randomPos);
        appleX = (( r * dotSize));

        r = (int) (Math.random() * randomPos);
        appleY = (( r * dotSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!dRight)) {
                dLeft = true;
                dUp = false;
                dDown = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!dLeft)) {
                dRight = true;
                dUp = false;
                dDown = false;
            }
            if ((key == KeyEvent.VK_UP) && (!dDown)) {
                dUp = true;
                dLeft = false;
                dRight = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!dUp)) {
                dDown = true;
                dLeft = false;
                dRight = false;
            }
        }
    }

}