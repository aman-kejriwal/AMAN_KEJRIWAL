
// package waste.java.Project;
import java.awt.Shape;
import java.awt.geom.*;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;

class Graphics
        extends JPanel
        implements ActionListener {
    private Timer t = new Timer(100, this);
    public String state;
    private Snake s;
    private Food f;
    private Game game;

    public Graphics(Game g) {

        t.start();
        state = "START";

        game = g;
        s = g.getPlayer();
        f = g.getFood();

        // add a keyListner
        this.addKeyListener(g);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, Game.width * Game.dimension + 5, Game.height * Game.dimension + 5);

        if (state == "START") {
            g2d.setColor(Color.white);
            g2d.drawString("Press Any Key", Game.width / 2 * Game.dimension - 40,
                    Game.height / 2 * Game.dimension - 20);
        } else if (state == "RUNNING") {
            g2d.setColor(Color.red);
            // Shape circle = new Ellipse2D.Double(100, 100, 100, 100);
            g2d.fillRect(f.getX() * Game.dimension, f.getY() * Game.dimension,
                    Game.dimension, Game.dimension);

            g2d.setColor(Color.orange);
            for (Rectangle r : s.getBody()) {
                g2d.fill(r);
            }
        } else {
            g2d.setColor(Color.white);
            g2d.drawString("Your Score: " + (s.getBody().size() - 3), Game.width / 2 * Game.dimension - 40,
                    Game.height / 2 * Game.dimension - 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        game.update();
    }

}

class Food {
    private int x;
    private int y;

    public Food(Snake player) {
        this.random_spawn(player);
    }

    public void random_spawn(Snake player) {
        boolean onSnake = true;
        while (onSnake) {
            onSnake = false;

            x = (int) (Math.random() * Game.width - 1);
            y = (int) (Math.random() * Game.height - 1);

            for (Rectangle r : player.getBody()) {
                if (r.x == x && r.y == y) {
                    onSnake = true;
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

class Snake {
    private ArrayList<Rectangle> body;
    private int w = Game.width;
    private int h = Game.height;
    private int d = Game.dimension;

    private String move; // NOTHING, UP, DOWN, LEFT, RIGHT

    public Snake() {
        body = new ArrayList<>();

        Rectangle temp = new Rectangle(Game.dimension, Game.dimension);
        temp.setLocation(Game.width / 2 * Game.dimension, Game.height / 2 * Game.dimension);
        body.add(temp);

        temp = new Rectangle(d, d);
        temp.setLocation((w / 2 - 1) * d, (h / 2) * d);
        body.add(temp);

        temp = new Rectangle(d, d);
        temp.setLocation((w / 2 - 2) * d, (h / 2) * d);
        body.add(temp);

        move = "NOTHING";
    }

    public void move() {
        if (move != "NOTHING") {
            Rectangle first = body.get(0);

            Rectangle temp = new Rectangle(Game.dimension, Game.dimension);

            if (move == "UP") {
                temp.setLocation(first.x, first.y - Game.dimension);
            } else if (move == "DOWN") {
                temp.setLocation(first.x, first.y + Game.dimension);
            } else if (move == "LEFT") {
                temp.setLocation(first.x - Game.dimension, first.y);
            } else {
                temp.setLocation(first.x + Game.dimension, first.y);
            }

            body.add(0, temp);
            body.remove(body.size() - 1);
        }
    }

    public void grow() {
        Rectangle first = body.get(0);

        Rectangle temp = new Rectangle(Game.dimension, Game.dimension);

        if (move == "UP") {
            temp.setLocation(first.x, first.y - Game.dimension);
        } else if (move == "DOWN") {
            temp.setLocation(first.x, first.y + Game.dimension);
        } else if (move == "LEFT") {
            temp.setLocation(first.x - Game.dimension, first.y);
        } else {
            temp.setLocation(first.x + Game.dimension, first.y);
        }

        body.add(0, temp);
    }

    public ArrayList<Rectangle> getBody() {
        return body;
    }

    public void setBody(ArrayList<Rectangle> body) {
        this.body = body;
    }

    public int getX() {
        return body.get(0).x;
    }

    public int getY() {
        return body.get(0).y;
    }

    public String getMove() {
        return move;
    }

    public void up() {
        move = "UP";
    }

    public void down() {
        move = "DOWN";
    }

    public void left() {
        move = "LEFT";
    }

    public void right() {
        move = "RIGHT";
    }
}

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Game newGame = new Game();
    }

}

class Game
        implements KeyListener {
    private Snake player;
    private Food food;
    private Graphics graphics;

    private JFrame window;

    public static final int width = 30;
    public static final int height = 30;
    public static final int dimension = 20;

    public Game() {
        window = new JFrame();

        player = new Snake();

        food = new Food(player);

        graphics = new Graphics(this);

        window.add(graphics);

        window.setTitle("Snake");
        window.setSize(width * dimension + 2, height * dimension + dimension + 4);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void start() {
        graphics.state = "RUNNING";
    }

    public void update() {
        if (graphics.state == "RUNNING") {
            if (check_food_collision()) {
                player.grow();
                food.random_spawn(player);
            } else if (check_wall_collision() || check_self_collision()) {
                graphics.state = "END";
            } else {
                player.move();
            }
        }
    }

    private boolean check_wall_collision() {
        if (player.getX() < 0 || player.getX() >= width * dimension
                || player.getY() < 0 || player.getY() >= height * dimension) {
            return true;
        }
        return false;
    }

    private boolean check_food_collision() {
        if (player.getX() == food.getX() * dimension && player.getY() == food.getY() * dimension) {
            return true;
        }
        return false;
    }

    private boolean check_self_collision() {
        for (int i = 1; i < player.getBody().size(); i++) {
            if (player.getX() == player.getBody().get(i).x &&
                    player.getY() == player.getBody().get(i).y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (graphics.state == "RUNNING") {
            if (keyCode == KeyEvent.VK_W && player.getMove() != "DOWN") {
                player.up();
            }

            if (keyCode == KeyEvent.VK_S && player.getMove() != "UP") {
                player.down();
            }

            if (keyCode == KeyEvent.VK_A && player.getMove() != "RIGHT") {
                player.left();
            }

            if (keyCode == KeyEvent.VK_D && player.getMove() != "LEFT") {
                player.right();
            }
        } else {
            this.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public Snake getPlayer() {
        return player;
    }

    public void setPlayer(Snake player) {
        this.player = player;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }
}