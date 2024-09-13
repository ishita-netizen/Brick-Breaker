package brick_breaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private final Timer timer;
    private final int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 7);
        Gameplay gameplay = this;
        addKeyListener(gameplay);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // background
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);

        //drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.gray);
        g.fillRect(0, 0, 3, 600); // left
        g.fillRect(0, 0, 700, 3); // top
        g.fillRect(691, 0, 3, 600); // right

        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        // paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(totalBricks <= 0)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won: "+score, 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 190, 350);  
        }

        if(ballposY > 570)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over "+score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 190, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);

            if (ballRect.intersects(paddleRect)) {
                ballYdir = -ballYdir;
            }

            A: for(int i = 0;i<map.map.length;i++)
            {
                for(int j = 0; j<map.map[0].length;j++)
                {
                    if(map.map[i][j]>0)
                    {
                        int brickX =j* map.brickWidth + 80;
                        int brickY =i* map.brickHight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHight = map.brickHight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHight);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x +brickRect.width)
                            {
                                ballXdir= -ballXdir;
                            }
                            else
                            {
                                ballYdir= -ballYdir;
                            }
                            break A;
                        }    
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            // wall collision detection
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode()== KeyEvent.VK_ENTER)
        {
            if (!play){
                play = true;
                playerX = 310;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
