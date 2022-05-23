package xjx.game;
import java.awt.Image;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import xjx.snake.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import xjx.game.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import xjx.game.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.LinkedList;
import java.awt.Image;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import xjx.game.*;
public class PlayerSnake {
    private Scene GameUI;//母窗体,即游戏主界面
    private Direction direction = Direction.RIGHT;//蛇当前前进的方向,初始化默认向右移动
    private int speed = 300;//用于描述蛇移动速度的变量，其实是作为蛇刷新线程的时间用的
    private int defaultSpeed = 300;//默认速度
    private int speed = 500;//用于描述蛇移动速度的变量，其实是作为蛇刷新线程的时间用的
    private int defaultSpeed = 500;//默认速度
    private Deque<Body> body = new LinkedList<>();//用于描述蛇身体节点的数组，保存蛇身体各个节点的坐标
    private int point = 0;//当前蛇得了多少分
    private int bulletNumber = 5;//蛇的子弹数目
@@ -87,14 +87,33 @@ public void move(){
                    GameUI.remove(tail.label);
                    //添头去尾实现移动
                }
            }else{
                GameUI.removeFood(new Coordinate(next_node.coor.y, next_node.coor.x));

                if(GameUI.gameMode == 2){//AI和玩家同时存在
                    Coordinate AI_target = GameUI.getAITarget();
                    if(AI_target.x == next_coor.y && AI_target.y == next_coor.x){
                        //把AI的食物吃掉了
                        GameUI.FindNewPath();
                    }
                }
            }
        }
    }

    //判断一个坐标位置是否是蛇死亡的位置
    public boolean checkDeath(Coordinate coor){
        int rows = GameUI.getMap().length, cols = GameUI.getMap()[0].length;
        return coor.x < 0 || coor.x >= cols || coor.y < 0 || coor.y >= rows || GameUI.getMap()[coor.y][coor.x] == 3;
        int[][] map = GameUI.getMap();
        int x = coor.y, y = coor.x;
        /*
        * 死亡条件：
        * 1. 跑出游戏界面
        * 2. 碰到障碍物
        * 3. 碰到自己
        * 4. 碰到AI
        * */
        return x < 0 || x >= rows || y < 0 || y >= cols || map[x][y] == 1 || map[x][y] == 3 || map[x][y] == 4;
    }

    public boolean checkEat(Coordinate coor){
@@ -129,6 +148,8 @@ public Direction getDirection(){

    public void resetSpeed(){
        this.speed = defaultSpeed;
        executor.shutdownNow();
        run();
    }

    public void setDefaultSpeed(int speed){
@@ -137,10 +158,14 @@ public void setDefaultSpeed(int speed){

    public void setHeadIcon(int tag){
        headIconTag = tag;
        show();
        GameUI.repaint();
    }

    public void setBodyIcon(int tag){
        bodyIconTag = tag;
        show();
        GameUI.repaint();
    }

    public void removeAll(){
        for (Body node : body) {
            node.label.setVisible(false);
            GameUI.remove(node.label);
        }
    }
    public Coordinate searchTarget(Coordinate coor, Direction d){
        int row = coor.y, col = coor.x;
        int[][] gameMap = GameUI.getMap();
        int rows = gameMap.length, cols = gameMap[0].length;
        switch (d){
            case UP:
            {
                for(int j = row; j >= 0; j--){
                    if(gameMap[j][col] == 3){
                        return new Coordinate(col, j);
                    }
                }
            }
            case DOWN:
            {
                for(int j = row; j < rows; j++){
                    if(gameMap[j][col] == 3){
                        return new Coordinate(col, j);
                    }
                }
            }
            case LEFT:{
                for(int i = col; i >= 0; i--){
                    if(gameMap[row][i] == 3){
                        return new Coordinate(i, row);
                    }
                }
            }
            case RIGHT:{
                for(int i = col; i < cols; i++){
                    if(gameMap[row][i] == 3){
                        return new Coordinate(i, row);
                    }
                }
            }
        }
        return new Coordinate(0, 0);
    }
    public void fire(){
        if(bulletNumber > 0){
            System.out.println("Fire a bullet");
            Direction d = getDirection();
            Coordinate coor = body.getFirst().coor;
            Coordinate target = searchTarget(coor, d);//找到火焰的目标
            System.out.println("Target is:" + target.x + "," +target.y);
            new Fire(coor, target, d);
            bulletNumber--;
            GameUI.updateInfos("Weapon", "" + bulletNumber);//刷新界面上显示的子弹数目
        }
    }
    public void show(){
        for (Body node : body) {
            node.label.setBounds(GameUI.getPixel(node.coor.x, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(node.coor.y, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            node.label.setIcon(bodyIcon[bodyIconTag]);
        }
        Body node = body.getFirst();
        node.label.setIcon(headIcon[headIconTag]);
    }
    public void quit(){
        executor.shutdownNow();//退出线程
    }
    public void run(){
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!GameUI.pause && !GameUI.quit) {
                move();
                GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
                show();
            }
        }, 0, speed, TimeUnit.MILLISECONDS);
    }
    public class Fire {
        private Coordinate fireCoor;
        private JLabel fireLabel;
        private Coordinate target;
        private Direction moveDirection;
        private boolean quit = false;
        public Fire(Coordinate snakehead,Coordinate target,Direction d){
            ImageIcon fireIcon = new ImageIcon("image//fire.png");//射击子弹时产生的火焰图标
            fireIcon.setImage(fireIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
            fireLabel = new JLabel(fireIcon);
            this.target = target;
            this.moveDirection = d;
            //初始化火焰起始坐标
            if(moveDirection == Direction.UP) {
                fireCoor = new Coordinate(snakehead.x,snakehead.y-1);
            } else if(moveDirection == Direction.DOWN) {
                fireCoor = new Coordinate(snakehead.x,snakehead.y+1);
            } else if(moveDirection == Direction.LEFT) {
                fireCoor = new Coordinate(snakehead.x-1,snakehead.y);
            } else if(moveDirection == Direction.RIGHT) {
                fireCoor = new Coordinate(snakehead.x+1,snakehead.y);
            }
            GameUI.add(fireLabel);
            show();
            moveThread();
        }
        public void show(){
            if(fireCoor.x == target.x && fireCoor.y == target.y) {
                int rows = GameUI.getMap().length, cols = GameUI.getMap()[0].length;
                if( target.x >= 0 && target.x < cols && target.y >= 0 && target.y < rows){
                    System.out.println("hit target " + target.x + "," + target.y);
                    new Music("music//explode.wav").start();//击中障碍物播放音效
                    //地图上该位置标记为0
                    GameUI.setMap(target.y, target.x, 0);
                    GameUI.removeBrick(new Coordinate(target.y, target.x));
                }
                fireLabel.setVisible(false);
                GameUI.remove(fireLabel);
                quit = true;
            }
            fireLabel.setVisible(false);
            fireLabel.setBounds(GameUI.getPixel(fireCoor.x, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(fireCoor.y, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            fireLabel.setVisible(true);
        }
        public void move(){
            if(moveDirection == Direction.UP) {
                fireCoor.y--;
            } else if(moveDirection == Direction.DOWN) {
                fireCoor.y++;
            } else if(moveDirection == Direction.LEFT) {
                fireCoor.x--;
            } else if(moveDirection == Direction.RIGHT) {
                fireCoor.x++;
            }
        }
        public void moveThread(){
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                if(!quit && !GameUI.pause){
                    move();
                    show();
                }else {
                    return;
                }
            }, 0, 50, TimeUnit.MILLISECONDS);
        }
    }
}
class pos{
    int x;
    int y;
    pos(int x, int y){
        this.x = x;
        this.y = y;
    }
    public pos sub(pos p){
        return new pos(this.x-p.x, this.y-p.y);
    }
    public pos sub(int[] v){
        return new pos(this.x-v[0], this.y-v[1]);
    }
    public pos add(pos p){
        return new pos(this.x+p.x, this.y+p.y);
    }
    public pos add(int[] v){
        return new pos(this.x+v[0], this.y+v[1]);
    }
    public boolean equal(pos p){
        return p.x == this.x && p.y == this.y;
    }
}
class pair{
    pos p;
    double cost;
    pair(pos p, double cost){
        this.p = p;
        this.cost = cost;
    }
    int compare(pair p2){
        return Double.compare(this.cost, p2.cost);
    }
}
class path{
    HashMap<Integer, pos> came_from;
    Double cost;
    path(HashMap<Integer, pos> came_from, Double cost){
        this.came_from = came_from;
        this.cost = cost;
    }
}
public class AISnake {
    private Scene GameUI;//母窗体,即游戏主界面
    private Deque<Body> body = new LinkedList<>();//用于描述蛇身体节点的数组，保存蛇身体各个节点的坐标
    private ImageIcon headIcon;
    private ImageIcon bodyIcon;
    private Vector<pos> path_pos = new Vector<>();
    private Vector<JLabel> path_labels = new Vector<>();
    private pos target;
    private ScheduledExecutorService executor;
    public AISnake(Scene GameUI) {
        this.GameUI = GameUI;
        headIcon = new ImageIcon("head//AI_head.png");
        headIcon.setImage(headIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        bodyIcon = new ImageIcon("body//AI_body.png");
        bodyIcon.setImage(bodyIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        int cols = GameUI.getMap()[0].length;
        Body head = new Body(cols-1,0,headIcon);
        body.addFirst(head);
        GameUI.add(head.label);
        head.label.setBounds(GameUI.getPixel(head.coor.x, GameUI.padding, GameUI.pixel_per_unit),
                GameUI.getPixel(head.coor.y, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
        GameUI.setMap(0, cols-1, 1);
        GameUI.setMap(0, cols-1, 4);

        long stime = System.currentTimeMillis();
        FindPath(new pos(0, cols-1));
        long etime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
        run();
    }
    public void show(){
        for (Body node : body) {
            node.label.setBounds(GameUI.getPixel(node.coor.x, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(node.coor.y, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            node.label.setIcon(bodyIcon);
        }
        Body node = body.getFirst();
        node.label.setIcon(headIcon);
    }
    public int coor_trans(pos p){
        //把gamemap里面的二维坐标(x,y)转化成一维坐标
        int x = p.x, y = p.y;
        int cols = GameUI.getMap()[0].length;
        return x * cols + y;
    }
    static Comparator<pair> cmp = pair::compare;
    Vector<pos> getNeighbors(int[][] map, pos now){
        int rows = map.length, cols = map[0].length;
        Vector<pos> res = new Vector<>();
        int[][] dir = new int[][]{ {-1, 0}, {1, 0}, {0, -1}, {0, 1} };//上、下、左、右
        long etime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
        run();
    }
    public void show(){
        for (Body node : body) {
            node.label.setBounds(GameUI.getPixel(node.coor.x, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(node.coor.y, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            node.label.setIcon(bodyIcon);
        }
        Body node = body.getFirst();
        node.label.setIcon(headIcon);
    }
    public int coor_trans(pos p){
        //把gamemap里面的二维坐标(x,y)转化成一维坐标
        int x = p.x, y = p.y;
        int cols = GameUI.getMap()[0].length;
        return x * cols + y;
    }
    static Comparator<pair> cmp = pair::compare;
    Vector<pos> getNeighbors(int[][] map, pos now){
        int rows = map.length, cols = map[0].length;
        Vector<pos> res = new Vector<>();
        int[][] dir = new int[][]{ {-1, 0}, {1, 0}, {0, -1}, {0, 1} };//上、下、左、右
        for (int i = 0; i < 4; i++) {
            pos tmp = now.add(dir[i]);
            if (tmp.x >= 0 && tmp.x < rows && tmp.y >= 0 && tmp.y < cols &&
                    map[tmp.x][tmp.y] != 1 && map[tmp.x][tmp.y] != 3 ) {
                    map[tmp.x][tmp.y] != 4 && map[tmp.x][tmp.y] != 3 ) {
                //1表示是玩家蛇，4表示是AI蛇，3表示是障碍物
                //目前允许AI蛇穿过玩家蛇，但是玩家蛇不能穿过AI蛇
                res.add(tmp);
            }
        }
        return res;
    }
    double calCost(pos current, pos next) {
        //返回曼哈顿距离
        return Math.abs(current.x-next.x) + Math.abs(current.y-next.y);
    }
    double heuristic(pos goal, pos next) {
        //返回曼哈顿距离
        return Math.abs(goal.x - next.x) + Math.abs(goal.y - next.y);
    }
    public path AStar(int[][] map, pos start, pos goal){
        PriorityQueue<pair> frontier = new PriorityQueue<>(cmp);
        frontier.add(new pair(start, 0));
        HashMap<Integer, pos> came_from = new HashMap<>();
        came_from.put(coor_trans(start), new pos(-1, -1));
        //从起始点到当前点的代价
        HashMap<Integer, Double> cost_so_far = new HashMap<>();
        cost_so_far.put(coor_trans(start), 0.);
        while(!frontier.isEmpty()){
            pair current = frontier.poll();
            pos current_pos = current.p;
            if(current_pos == goal) break;
            Vector<pos> neighbors = getNeighbors(map, current_pos);
            for (pos next : neighbors) {
                double new_cost = cost_so_far.get(coor_trans(current_pos)) + calCost(current_pos, next);
                if (!came_from.containsKey(coor_trans(next)) || new_cost < cost_so_far.get(coor_trans(next))) {
                    cost_so_far.put(coor_trans(next), new_cost);
                    double priority = new_cost + heuristic(goal, next);
                    frontier.add(new pair(next, priority));
                    came_from.put(coor_trans(next), current_pos);
                }
        return res;
    }
    double calCost(pos current, pos next) {
        //返回曼哈顿距离
        return Math.abs(current.x-next.x) + Math.abs(current.y-next.y);
    }
    double heuristic(pos goal, pos next) {
        //返回曼哈顿距离
        return Math.abs(goal.x - next.x) + Math.abs(goal.y - next.y);
    }
    public path AStar(int[][] map, pos start, pos goal){
        PriorityQueue<pair> frontier = new PriorityQueue<>(cmp);
        frontier.add(new pair(start, 0));
        HashMap<Integer, pos> came_from = new HashMap<>();
        came_from.put(coor_trans(start), new pos(-1, -1));
        //从起始点到当前点的代价
        HashMap<Integer, Double> cost_so_far = new HashMap<>();
        cost_so_far.put(coor_trans(start), 0.);
        while(!frontier.isEmpty()){
            pair current = frontier.poll();
            pos current_pos = current.p;
            if(current_pos == goal) break;
            Vector<pos> neighbors = getNeighbors(map, current_pos);
            for (pos next : neighbors) {
                double new_cost = cost_so_far.get(coor_trans(current_pos)) + calCost(current_pos, next);
                if (!came_from.containsKey(coor_trans(next)) || new_cost < cost_so_far.get(coor_trans(next))) {
                    cost_so_far.put(coor_trans(next), new_cost);
                    double priority = new_cost + heuristic(goal, next);
                    frontier.add(new pair(next, priority));
                    came_from.put(coor_trans(next), current_pos);
                }
            }
        }

        return new path(came_from, cost_so_far.get(coor_trans(goal)));
        return new path(came_from, cost_so_far.getOrDefault(coor_trans(goal), -1.));
    }

    int checkDirection(pos now, pos other){
        int[][] direction = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };//上、下、左、右
        for(int i = 0; i < 4; i++){
            if(now.add(direction[i]).equal(other))
                return i;
        }
        return -1;
    }
    public void PrintArrow(pos prior, pos now, pos next){
        if(prior != null && next != null){
            int prior_now = checkDirection(prior, now);
//        int now_next = checkDirection(now, next);
            //0上、1下、2左、3右
            String[] dir = new String[]{"up", "down", "left", "right"};
            String icon = "image//arrow_" + dir[prior_now] + "_" + dir[prior_now] + ".png";
            ImageIcon arrow_icon = new ImageIcon(icon);
            arrow_icon.setImage(arrow_icon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
            JLabel arrow_label = new JLabel(arrow_icon);
            GameUI.add(arrow_label);
            arrow_label.setBounds(GameUI.getPixel(now.y, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(now.x, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            path_labels.add(arrow_label);
        }
    }
    public void PrintPath(pos start, pos goal, Vector<pos> path){
//        for(pos p : path){
//            System.out.println(p.x + " " + p.y);
//        }
        System.out.println("path len: " + path.size());
        for(int i = 0; i < path.size(); i++){
            if(i == 0){
                PrintArrow(null, path.get(i), path.get(i+1));
            } else if(i == path.size()-1){
                PrintArrow(path.get(i-1), path.get(i), null);
            } else {
                PrintArrow(path.get(i-1), path.get(i), path.get(i+1));
            }
        }
    }
    public void CalPath(path p, pos goal, pos start) {
        HashMap<Integer, pos> came_from = p.came_from;
        Stack<pos> s = new Stack<>();
        Vector<pos> res = new Vector<>();
        pos tmp = new pos(goal.x, goal.y);
        while(!tmp.equal(start)){
            s.add(tmp);
            tmp = came_from.get(coor_trans(tmp));
        }
        s.add(start);
        while(!s.empty()){
            pos p_ = s.pop();
            res.add(p_);
            if(!p_.equal(start)) path_pos.add(p_);
        }
        PrintPath(start, goal, res);
    }
        int[][] direction = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };//上、下、左、右
        for(int i = 0; i < 4; i++){
            if(now.add(direction[i]).equal(other))
                return i;
        }
        return -1;
    }
    public void PrintArrow(pos prior, pos now, pos next){
        if(prior != null && next != null){
            int prior_now = checkDirection(prior, now);
//        int now_next = checkDirection(now, next);
            //0上、1下、2左、3右
            String[] dir = new String[]{"up", "down", "left", "right"};
            String icon = "image//arrow_" + dir[prior_now] + "_" + dir[prior_now] + ".png";
            ImageIcon arrow_icon = new ImageIcon(icon);
            arrow_icon.setImage(arrow_icon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
            JLabel arrow_label = new JLabel(arrow_icon);
            GameUI.add(arrow_label);
            arrow_label.setBounds(GameUI.getPixel(now.y, GameUI.padding, GameUI.pixel_per_unit),
                    GameUI.getPixel(now.x, GameUI.padding, GameUI.pixel_per_unit), 20, 20);
            path_labels.add(arrow_label);
        }
    }
    public void PrintPath(pos start, pos goal, Vector<pos> path){
//        for(pos p : path){
//            System.out.println(p.x + " " + p.y);
//        }
        System.out.println("path len: " + path.size());
        for(int i = 0; i < path.size(); i++){
            if(i == 0){
                PrintArrow(null, path.get(i), path.get(i+1));
            } else if(i == path.size()-1){
                PrintArrow(path.get(i-1), path.get(i), null);
            } else {
                PrintArrow(path.get(i-1), path.get(i), path.get(i+1));
            }
        }
    }
    public void CalPath(path p, pos goal, pos start) {
        HashMap<Integer, pos> came_from = p.came_from;
        Stack<pos> s = new Stack<>();
        Vector<pos> res = new Vector<>();
        pos tmp = new pos(goal.x, goal.y);
        while(!tmp.equal(start)){
            s.add(tmp);
            tmp = came_from.get(coor_trans(tmp));
        }
        s.add(start);
        while(!s.empty()){
            pos p_ = s.pop();
            res.add(p_);
            if(!p_.equal(start)) path_pos.add(p_);
        }
        PrintPath(start, goal, res);
    }

    public void FindPath(pos start){
        Vector<Coordinate> food_coors = GameUI.getFoodCoor();
        Vector<path> path_set = new Vector<>();
        double min_cost = 99999999.;//需要保证足够大
        path min_path = null;
        pos end = null;
        for (Coordinate next : food_coors) {
            pos goal = new pos(next.x, next.y);
            path p = AStar(GameUI.getMap(), start, goal);
            if(p.cost < min_cost) {
                min_cost = p.cost;
                min_path = p;
                end = goal;
                target = new pos(goal.x, goal.y);
            if(p.cost > 0){
                if(p.cost < min_cost) {
                    min_cost = p.cost;
                    min_path = p;
                    end = goal;
                    target = new pos(goal.x, goal.y);
                }
            }else{
                //证明没有找到路径
                System.out.println("unable to find path");
                show();
                GameUI.repaint();
                goDie();
            }
        }

        //在界面上显示食物坐标
        GameUI.updateInfos("FoodCoor", "(" + target.x + "," + target.y + ")");
        //在界面上显示食物坐标
        GameUI.updateInfos("FoodCoor", "(" + target.x + "," + target.y + ")");
        CalPath(min_path, end, start);
    }

    public void FindNewPath(){
        removeAllPath();
        path_labels.clear();
        path_pos.clear();
        Coordinate head = body.getFirst().coor;
        FindPath(new pos(head.y, head.x));
    }

    public Coordinate getTarget() {
        return new Coordinate(target.x, target.y);
    }

    public void goDie(){
        quit();
        GameUI.pause = true;
        GameUI.quit = true;
        GameUI.ai_die = true;
    }

    public void move(){
        if(!path_pos.isEmpty()){
            pos next = path_pos.get(0);
            path_pos.remove(0);
            Coordinate next_coor = new Coordinate(next.y, next.x);
            if(!next.equal(target)){
                //去除界面上的箭头
                JLabel arrow_label = path_labels.get(0);
                path_labels.remove(0);
                arrow_label.setVisible(false);
                GameUI.remove(arrow_label);
            }
            path_pos.remove(0);
            Coordinate next_coor = new Coordinate(next.y, next.x);
            if(!next.equal(target)){
                //去除界面上的箭头
                JLabel arrow_label = path_labels.get(0);
                path_labels.remove(0);
                arrow_label.setVisible(false);
                GameUI.remove(arrow_label);
            }

            Body next_node = new Body(next_coor, headIcon);
            body.addFirst(next_node);//添头
            //GameUI.map[next_node.coor.y][next_node.coor.x] = 1;//标记为蛇身体节点
            GameUI.setMap(next_node.coor.y, next_node.coor.x, 1);
            GameUI.setMap(next_node.coor.y, next_node.coor.x, 4);
            GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
            next_node.label.setVisible(true);
            GameUI.add(next_node.label);
            if (!next.equal(target)){//没有吃到食物
                Body tail = body.pollLast();//去尾
                if (tail != null) {
                    GameUI.setMap(tail.coor.y, tail.coor.x, 0);
                    GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
                    tail.label.setVisible(false);
                    GameUI.remove(tail.label);
                    //添头去尾实现移动
                }
            } else {//吃到了食物
            if (!next.equal(target)){//没有吃到食物
                Body tail = body.pollLast();//去尾
                if (tail != null) {
                    GameUI.setMap(tail.coor.y, tail.coor.x, 0);
                    GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
                    tail.label.setVisible(false);
                    GameUI.remove(tail.label);
                    //添头去尾实现移动
                }
            } else {//吃到了食物
                new Music("music//eat.wav").start();
                GameUI.updateInfos("AILength", "" + body.size());
                GameUI.removeFood(new Coordinate(next_node.coor.y, next_node.coor.x));
                GameUI.updateInfos("Amount", "" + GameUI.getFoodCoor().size());
                System.out.println("AI eat food!");

                path_labels.clear();
                path_pos.clear();
                target = null;
                //重新寻路
                FindPath(new pos(next.x, next.y));
            }
        }
    }
    public void removeAll(){
        for (Body node : body) {
            node.label.setVisible(false);
            GameUI.remove(node.label);
        }
    }
    public void removeAllPath(){
        for (JLabel label : path_labels){
            label.setVisible(false);
            GameUI.remove(label);
        }
    }
    public void quit(){
        executor.shutdownNow();//退出线程
    }
    public void run(){
        executor = Executors.newSingleThreadScheduledExecutor();
        //用于描述蛇移动速度的变量，其实是作为蛇刷新线程的时间用的
        int speed = 500;
        executor.scheduleAtFixedRate(() -> {
            if (!GameUI.pause && !GameUI.quit) {
                move();
                GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
                show();
            }
        }, 0, speed, TimeUnit.MILLISECONDS);
    }
}
                path_pos.clear();
                target = null;
                //重新寻路
                FindPath(new pos(next.x, next.y));
            }
        }
    }
    public void removeAll(){
        for (Body node : body) {
            node.label.setVisible(false);
            GameUI.remove(node.label);
        }
    }
    public void removeAllPath(){
        for (JLabel label : path_labels){
            label.setVisible(false);
            GameUI.remove(label);
        }
    }
    public void quit(){
        executor.shutdownNow();//退出线程
    }
    public void run(){
        executor = Executors.newSingleThreadScheduledExecutor();
        //用于描述蛇移动速度的变量，其实是作为蛇刷新线程的时间用的
        int speed = 500;
        executor.scheduleAtFixedRate(() -> {
            if (!GameUI.pause && !GameUI.quit) {
                move();
                GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
                show();
            }
        }, 0, speed, TimeUnit.MILLISECONDS);
    }
}
class pos{
    int x;
    int y;
    pos(int x, int y){
        this.x = x;
        this.y = y;
    }
    public pos sub(pos p){
        return new pos(this.x-p.x, this.y-p.y);
    }
    public pos sub(int[] v){
        return new pos(this.x-v[0], this.y-v[1]);
    }
    public pos add(pos p){
        return new pos(this.x+p.x, this.y+p.y);
    }
    public pos add(int[] v){
        return new pos(this.x+v[0], this.y+v[1]);
    }
    public boolean equal(pos p){
        return p.x == this.x && p.y == this.y;
    }
}
class pair{
    pos p;
    double cost;
    pair(pos p, double cost){
        this.p = p;
        this.cost = cost;
    }
    int compare(pair p2){
        return Double.compare(this.cost, p2.cost);
    }
}
class path{
    HashMap<Integer, pos> came_from;
    Double cost;
    path(HashMap<Integer, pos> came_from, Double cost){
        this.came_from = came_from;
        this.cost = cost;
    }
}
public class AISnake {
    private Scene GameUI;//母窗体,即游戏主界面
    private Deque<Body> body = new LinkedList<>();//用于描述蛇身体节点的数组，保存蛇身体各个节点的坐标
    private ImageIcon headIcon;
    private ImageIcon bodyIcon;
    private Vector<pos> path_pos = new Vector<>();
    private Vector<JLabel> path_labels = new Vector<>();
    private pos target;
    private ScheduledExecutorService executor;
    public AISnake(Scene GameUI) {
        this.GameUI = GameUI;
        headIcon = new ImageIcon("head//AI_head.png");
        headIcon.setImage(headIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        bodyIcon = new ImageIcon("body//AI_body.png");
        bodyIcon.setImage(bodyIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        int cols = GameUI.getMap()[0].length;
        Body head = new Body(cols-1,0,headIcon);
        body.addFirst(head);
public class Scene extends JFrame{
    private final Font f = new Font("微软雅黑",Font.PLAIN,15);
    private final Font f2 = new Font("微软雅黑",Font.PLAIN,12);
    private JRadioButtonMenuItem[] modeItems;
    private JRadioButtonMenuItem[] speedItems;
    private JRadioButtonMenuItem[] headItems;
    private JRadioButtonMenuItem[] bodyItems;
    private JLabel background_label;
    private JPanel paintPanel;//画板，画线条用的
    private final JLabel label  = new JLabel("当前长度：");
    private final JLabel label2 = new JLabel("所花时间：");
    private final JLabel label3 = new JLabel("当前得分：");
    private final JLabel label4 = new JLabel("食物个数：");
    private final JLabel label5 = new JLabel("剩余子弹：");
    private final JLabel label6 = new JLabel("AI长度：");
    private final JLabel label7 = new JLabel("食物坐标：");
    private final JLabel label8 = new JLabel("下一步：");

    private final JLabel FoodCoor = new JLabel("");
    private final JLabel NextStepCoor = new JLabel("");
    private final JLabel AILength = new JLabel("1");
    private final JLabel Length = new JLabel("1");
    private final JLabel Score = new JLabel("0");
    private final JLabel Time = new JLabel("");
    private final JLabel Amount = new JLabel("0");
    private final JLabel Weapon = new JLabel("5");
    private final HashMap<String, JLabel> infos = new HashMap<>();
    private final HashMap<Integer, JLabel> walls = new HashMap<>();
    private final JPanel p = new JPanel();
    private BGPanel backGroundPanel;
    private Timer timer;

    public boolean pause = false;
    public boolean pause = true;
    public boolean quit = false;
    public boolean die = false;
    public boolean ai_die = false;
    private boolean show_grid = true;       //标记是否显示界面上的网格线，默认显示
    private boolean show_padding = true;    //标记是否显示界面上的边框线，默认显示

    private PlayerSnake snake;
    private AISnake ai;
    private Foodset food;
    public final int pixel_per_unit = 22;       //每个网格的像素数目
    public final int pixel_rightBar = 110;      //右边信息栏的宽度(像素)
    public final int padding = 5;               //内边框宽度

    private int[][] gameMap;                    //map数组标记当前地图的使用情况
    /*0表示空闲
     *1表示蛇身体节点
    /*0表示空闲（道路）
     *1表示玩家蛇身体节点
     *2表示食物
     *3表示障碍物
     *4表示AI蛇身体结点
     */

    private int gameMode = 0;                   //游戏模式
    public int gameMode = 0;                   //游戏模式
    /*0表示只有player snake
    * 1表示只有ai snake
    * 2表示player snake 和 ai snake都存在
    * */
    public synchronized int[][] getMap(){
        return gameMap;
    }
    public synchronized void setMap(int i, int j, int e){
        gameMap[i][j] = e;
    }

    public void resetLabel(){
        FoodCoor.setText("");
        NextStepCoor.setText("");
        AILength.setText("1");
        Length.setText("1");
        Score.setText("0");
        Time.setText("");
        Amount.setText("0");
        Weapon.setText("5");
    }
    public void restart(){//重新开始游戏
        quit = true;
        resetLabel();
        speedItems[2].setSelected(true);
        headItems[0].setSelected(true);
        bodyItems[0].setSelected(true);
        food.removeAll();
        food = null;
        food = new Foodset(this);
        removeAllBrick();
        loadGameMap("map//map.txt");//加载游戏地图
        PrintMap(gameMap,"debug//map.txt");
        initWalls();
        /*0表示只有player snake
         * 1表示只有ai snake
         * 2表示player snake 和 ai snake都存在
         * */
        if(gameMode == 0){
            snake.removeAll();
            snake.quit();
            snake = null;
            snake = new PlayerSnake(this);
        }else if(gameMode == 1){
            ai.removeAll();
            ai.removeAllPath();
            ai.quit();
            ai = null;
            ai = new AISnake(this);
        }else if(gameMode == 2){
            snake.removeAll();
            snake.quit();
            snake = null;
            snake = new PlayerSnake(this);

            ai.removeAll();
            ai.removeAllPath();
            ai.quit();
            ai = null;
            ai = new AISnake(this);
        }

        timer.reset();

        die = false;
        ai_die = false;
        quit = false;
        pause = false;

        System.out.println("\nGame restart...\t" + getSysTime());
    }
    public void changeGameMode(int current_mode, int new_mode){
        quit = true;
        resetLabel();
        speedItems[2].setSelected(true);
        headItems[0].setSelected(true);
        bodyItems[0].setSelected(true);
        food.removeAll();
        food = null;
        food = new Foodset(this);
        removeAllBrick();
        loadGameMap("map//map.txt");//加载游戏地图
        PrintMap(gameMap,"debug//map.txt");
        initWalls();
        /*0表示只有player snake
         * 1表示只有ai snake
         * 2表示player snake 和 ai snake都存在
         * */
        if(current_mode == 0){
            snake.removeAll();
            snake.quit();
            snake = null;
        }else if(current_mode == 1){
            ai.removeAll();
            ai.removeAllPath();
            ai.quit();
            ai = null;
        }else if(current_mode == 2){
            snake.removeAll();
            snake.quit();
            snake = null;
            ai.removeAll();
            ai.removeAllPath();
            ai.quit();
            ai = null;
        }

        if(new_mode == 0){
            snake = new PlayerSnake(this);
        }else if(new_mode == 1){
            ai = new AISnake(this);
        }else if(new_mode == 2){
            snake = new PlayerSnake(this);
            ai = new AISnake(this);
        }
        gameMode = new_mode;
        initRightBar();
        timer.reset();

        die = false;
        ai_die = false;
        quit = false;
        pause = false;

        System.out.println("\nGame restart...\t" + getSysTime());
    }
    public Coordinate randomCoor(){
        int rows = gameMap.length, cols = gameMap[0].length;
        Random rand = new Random();
        Coordinate res;
        int x = rand.nextInt(rows-1);
        int y = rand.nextInt(cols-1);
        while(true) {
            /*
            * 保证身体节点，食物，障碍物都不能和该坐标重合
            * 产生的点尽可能远离原点(0, 0)
            * */
            if(gameMap[x][y] != 0 || y == cols-1) {
                x = rand.nextInt(rows-1);
                y = rand.nextInt(cols-1);
            } else {
                break;
            }
        }
        res = new Coordinate(x,y);
        return res;
    }
    public void loadGameMap(String file){
        Vector<String> v = new Vector<>();
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader reader = null;
        if (fin != null) {
            reader = new InputStreamReader(fin);
        }
        BufferedReader buffReader = null;
        if (reader != null) {
            buffReader = new BufferedReader(reader);
        }
        String line = "";
        while(true){
            try {
                if (buffReader != null && (line = buffReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(line);
//            System.out.println(line.length());
            v.add(line);
        }
        try {
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rows = v.size(), cols = v.get(0).length()/2;
//        System.out.println("rows: " + rows + " cols: " + cols);
        gameMap = new int[rows][cols];
        for(int i = 0; i < rows; i++) {
            line = v.get(i);
            for (int j = 0; j < cols; j++) {
                gameMap[i][j] = line.charAt(2*j)-'0';
            }
        }
    }
    public void initWalls(){
        int rows = gameMap.length, cols = gameMap[0].length;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(gameMap[i][j] == 3){
                    //加载砖块图片
                    ImageIcon brickIcon = new ImageIcon("image//brick.png");
                    brickIcon.setImage(brickIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
                    Coordinate coor = new Coordinate(j, i);
                    JLabel label = new JLabel(brickIcon);
                    this.add(label);
                    label.setBounds(getPixel(coor.x,padding,pixel_per_unit),
                            getPixel(coor.y,padding,pixel_per_unit), 20, 20);
                    walls.put(coor_trans(new Coordinate(i, j)), label);
                }
            }
        }
    }
    //注意，序号从0开始
    public int getPixel(int i, int paddind, int pixels_per_unit) {
        //通过方格序号返回其横坐标
        return 1+paddind+i*pixels_per_unit;
    }
    public static String getSysTime(){
        String Time = "";
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR) ;
        int MM = Cld.get(Calendar.MONTH)+1;
        int DD = Cld.get(Calendar.DATE);
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        int mm = Cld.get(Calendar.MINUTE);
        int SS = Cld.get(Calendar.SECOND);
        int MI = Cld.get(Calendar.MILLISECOND);
        Time += YY + "/" + MM + "/" + DD + "-" + HH + ":" + mm + ":" + SS + ":" + MI;
        return Time;
    }
    //字符串写出到文本
    public static void  Write2Txt(String str,String filepath) {
        FileWriter fw;
        File f = new File(filepath);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            // FileOutputStream fos = new FileOutputStream(f);
            // OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
            //System.out.println("===========写入文本成功========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void PrintMap(int[][] map,String filepath){
        String temp = "";
        temp += "\t";
        for(int i = 0;i < map[0].length;i++)
            temp += i + "\t";
        temp += "\n";
        for(int i = 0;i < map.length;i++)
        {
            temp += i + "\t";
            for(int j = 0;j <map[0].length;j++)
                temp += map[i][j] + "\t";
            temp += "\n";
        }
        try {
            Write2Txt(temp,filepath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void removeBrick(Coordinate coor){
        JLabel label = walls.get(coor_trans(coor));
        label.setVisible(false);
        this.remove(label);
        walls.remove(coor_trans(coor));
    }
    public void removeAllBrick(){
        for(int key : walls.keySet()){
            JLabel label = walls.get(key);
            label.setVisible(false);
            this.remove(label);
        }
        walls.clear();
    }
    public void initMenuBar(){
        //菜单栏
        JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.white);
        setJMenuBar(bar);
        JMenu Settings = new JMenu("设置");
        Settings.setFont(f);
        JMenu Help = new JMenu("帮助");
        Help.setFont(f);
        JMenu About = new JMenu("关于");
        About.setFont(f);
        bar.add(Settings);
        bar.add(Help);
        bar.add(About);
        JMenu change_mode = new JMenu("切换游戏模式");
        change_mode.setFont(f2);
        JMenuItem set_background = new JMenuItem("更换背景");
        set_background.setFont(f2);
        JMenu set_head = new JMenu("更换蛇头");
        set_head.setFont(f2);
        JMenu set_body = new JMenu("更换蛇身");
        set_body.setFont(f2);
        JMenu set_speed= new JMenu("设置速度");
        set_speed.setFont(f2);
        JMenuItem remove_net= new JMenuItem("移除网格");
        remove_net.setFont(f2);
        JMenuItem remove_padding= new JMenuItem("移除边框");
        remove_padding.setFont(f2);
        Settings.add(change_mode);
        Settings.add(set_background);
        Settings.add(set_head);
        Settings.add(set_body);
        Settings.add(set_speed);
        Settings.add(remove_net);
        Settings.add(remove_padding);
        JMenuItem help = new JMenuItem("Guide...");
        help.setFont(f2);
        Help.add(help);
        JMenuItem about = new JMenuItem("About...");
        about.setFont(f2);
        About.add(about);
        initWalls();
        this.addKeyListener(new MyKeyListener());
        remove_net.addActionListener(e -> {
            if(!show_grid) {
                show_grid = true;
                remove_net.setText("移除网格");
            } else {
                show_grid = false;
                remove_net.setText("显示网格");
            }
            paintPanel.repaint();
        });
        remove_padding.addActionListener(e -> {
            if(!show_padding) {
                show_padding = true;
                remove_padding.setText("移除边框");
            } else {
                show_padding = false;
                remove_padding.setText("显示边框");
            }
            paintPanel.repaint();
        });
        String[] modes = {"仅玩家蛇", "仅AI蛇", "玩家蛇和AI蛇"};
        modeItems = new JRadioButtonMenuItem[modes.length];
        ButtonGroup modeGroup = new ButtonGroup();
        for(int i = 0; i < modes.length; i ++){
            modeItems[i] = new JRadioButtonMenuItem(modes[i]);
            modeItems[i].setFont(f2);
            change_mode.add(modeItems[i]);
            modeGroup.add(modeItems[i]);
            modeItems[i].addActionListener(e -> {
                for(int j = 0; j < modeItems.length; j++){
                    if(modeItems[j].isSelected()){
                        if(j == gameMode){
                            return;
                        }else{
                            if(j == 2) return;
                            changeGameMode(gameMode, j);
                        }
                    }
                }
            });
        }
        modeItems[gameMode].setSelected(true);
        modeItems[2].setEnabled(false);
//        modeItems[2].setEnabled(false);

        //设置速度菜单
        String[] speed = {"龟速","行走","奔跑","疯狂"};
        speedItems = new JRadioButtonMenuItem[speed.length];
        ButtonGroup speedGroup = new ButtonGroup();
        for(int i = 0;i < speed.length;i++) {
            speedItems[i] = new JRadioButtonMenuItem(speed[i]);
            speedItems[i].setFont(f2);
            set_speed.add(speedItems[i]);
            speedGroup.add(speedItems[i]);
            speedItems[i].addActionListener(e -> {
                for(int i1 = 0; i1 < speedItems.length; i1++) {
                    if(speedItems[i1].isSelected()) {
                        if(i1 == 0) {
                            snake.setDefaultSpeed(600);
                            snake.resetSpeed();
                        } else if(i1 == 1) {
                            snake.setDefaultSpeed(400);
                            snake.setDefaultSpeed(500);
                            snake.resetSpeed();
                        } else if(i1 == 2) {
                            snake.setDefaultSpeed(300);
                            snake.setDefaultSpeed(200);
                            snake.resetSpeed();
                        } else if(i1 == 3) {
                            snake.setDefaultSpeed(150);
                            snake.setDefaultSpeed(100);
                            snake.resetSpeed();
                        }
                    }
                }
            });
        }
        speedItems[2].setSelected(true);
        speedItems[1].setSelected(true);

        //设置头部图片
        String[] head = {"doge","二哈","经典","憧憬"};
        headItems = new JRadioButtonMenuItem[head.length];
        ButtonGroup headGroup = new ButtonGroup();
        ImageIcon[] headIcon = new ImageIcon[head.length];
        for(int i = 0; i < head.length; i++) {
            headIcon[i] = new ImageIcon("head//head" + i + ".png");
            headIcon[i].setImage(headIcon[i].getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH));
        }
        for(int i = 0;i < head.length;i++) {
            headItems[i] = new JRadioButtonMenuItem(head[i]);
            headItems[i].setFont(f2);
            headItems[i].setIcon(headIcon[i]);
            set_head.add(headItems[i]);
            headGroup.add(headItems[i]);
            headItems[i].addActionListener(e -> {
                for(int i12 = 0; i12 < headItems.length; i12++) {
                    if(headItems[i12].isSelected()) snake.setHeadIcon(i12);
                }
            });
        }
        headItems[0].setSelected(true);
        //设置身体图片
        String[] body = {"乖巧","笑眼","滑稽","阴险"};
        bodyItems = new JRadioButtonMenuItem[body.length];
        ButtonGroup bodyGroup = new ButtonGroup();
        ImageIcon[] bodyIcon = new ImageIcon[body.length];
        for(int i = 0; i < body.length; i++) {
            bodyIcon[i] = new ImageIcon("body//body" + i + ".png");
            bodyIcon[i].setImage(bodyIcon[i].getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH));
        }
        for(int i = 0;i < body.length;i++) {
            bodyItems[i] = new JRadioButtonMenuItem(body[i]);
            bodyItems[i].setFont(f2);
            bodyItems[i].setIcon(bodyIcon[i]);
            set_body.add(bodyItems[i]);
            bodyGroup.add(bodyItems[i]);
            bodyItems[i].addActionListener(e -> {
                for(int i13 = 0; i13 < bodyItems.length; i13++) {
                    if(bodyItems[i13].isSelected()) snake.setBodyIcon(i13);
                }
            });
        }
        bodyItems[0].setSelected(true);
        backGroundPanel = new BGPanel();
        set_background.addActionListener(e -> backGroundPanel.setVisible(true));
        about.addActionListener(e -> new About());
        help.addActionListener(e -> new Help());
    }

    public void initRightBar(){
        remove(label);remove(label2);remove(label3);remove(label4);
        remove(label5);remove(label6);remove(label7);remove(label8);
        remove(FoodCoor);remove(NextStepCoor);remove(AILength);remove(Length);
        remove(label5);remove(label6);remove(label7);
        remove(FoodCoor);remove(AILength);remove(Length);
        remove(Score);remove(Time);remove(Amount);remove(Weapon);remove(p);

        //布局
        int info_x = padding*3 + gameMap[0].length*pixel_per_unit;
        if(gameMode == 0){//player snake
            add(label);label.setBounds(info_x, 10, 80, 20);label.setFont(f);
            add(Length);Length.setBounds(info_x, 35, 80, 20);Length.setFont(f);
            add(label2);label2.setBounds(info_x, 70, 80, 20);label2.setFont(f);
            add(Time);Time.setBounds(info_x, 95, 80, 20);Time.setFont(f);
            add(label3);label3.setBounds(info_x, 130, 80, 20);label3.setFont(f);
            add(Score);Score.setBounds(info_x, 155, 80, 20);Score.setFont(f);
            add(label4);label4.setBounds(info_x, 190, 80, 20);label4.setFont(f);
            add(Amount);Amount.setBounds(info_x, 215, 80, 20);Amount.setFont(f);
            add(label5);label5.setBounds(info_x, 250, 80, 20);label5.setFont(f);
            add(Weapon);Weapon.setBounds(info_x, 275, 80, 20);Weapon.setFont(f);
        }else if(gameMode == 1){//ai snake
            add(label6);label6.setBounds(info_x, 10, 80, 20);label6.setFont(f);
            add(AILength);AILength.setBounds(info_x, 35, 80, 20);AILength.setFont(f);
            add(label2);label2.setBounds(info_x, 70, 80, 20);label2.setFont(f);
            add(Time);Time.setBounds(info_x, 95, 80, 20);Time.setFont(f);
            add(label7);label7.setBounds(info_x, 130, 80, 20);label7.setFont(f);
            add(FoodCoor);FoodCoor.setBounds(info_x, 155, 80, 20);FoodCoor.setFont(f);
        }else if(gameMode == 2){
            add(label);label.setBounds(info_x, 10, 80, 20);label.setFont(f);
            add(Length);Length.setBounds(info_x, 35, 80, 20);Length.setFont(f);
            add(label2);label2.setBounds(info_x, 70, 80, 20);label2.setFont(f);
            add(Time);Time.setBounds(info_x, 95, 80, 20);Time.setFont(f);
            add(label3);label3.setBounds(info_x, 130, 80, 20);label3.setFont(f);
            add(Score);Score.setBounds(info_x, 155, 80, 20);Score.setFont(f);
            add(label4);label4.setBounds(info_x, 190, 80, 20);label4.setFont(f);
            add(Amount);Amount.setBounds(info_x, 215, 80, 20);Amount.setFont(f);
            add(label5);label5.setBounds(info_x, 250, 80, 20);label5.setFont(f);
            add(Weapon);Weapon.setBounds(info_x, 275, 80, 20);Weapon.setFont(f);
            add(p);p.setBounds(info_x, 300, 70, 1);p.setBorder(BorderFactory.createLineBorder(Color.white));
            add(label6);label6.setBounds(info_x, 315, 80, 20);label6.setFont(f);
            add(AILength);AILength.setBounds(info_x, 340, 80, 20);AILength.setFont(f);
            add(label7);label7.setBounds(info_x, 365, 80, 20);label7.setFont(f);
            add(FoodCoor);FoodCoor.setBounds(info_x, 390, 80, 20);FoodCoor.setFont(f);
            add(label8);label8.setBounds(info_x, 415, 80, 20);label8.setFont(f);
            add(NextStepCoor);NextStepCoor.setBounds(info_x, 440, 80, 20);NextStepCoor.setFont(f);
        }
        //初始化这些Label组成的Hashmap
        infos.put("FoodCoor", FoodCoor);            //食物坐标
        infos.put("NextStepCoor", NextStepCoor);    //下一步
        infos.put("AILength", AILength);            //AI长度
        infos.put("Length", Length);                //当前长度
        infos.put("Score", Score);                  //当前得分
        infos.put("Time", Time);                    //所花时间
        infos.put("Amount", Amount);                //食物个数
        infos.put("Weapon", Weapon);                //剩余子弹
        //字体颜色，为了便于分辨，设为白色
        label.setForeground(Color.white);
        label2.setForeground(Color.white);
        label3.setForeground(Color.white);
        label4.setForeground(Color.white);
        label5.setForeground(Color.white);
        label6.setForeground(Color.white);
        label7.setForeground(Color.white);
        label8.setForeground(Color.white);
        FoodCoor.setForeground(Color.white);
        NextStepCoor.setForeground(Color.white);
        AILength.setForeground(Color.white);
        Length.setForeground(Color.white);
        Score.setForeground(Color.white);
        Time.setForeground(Color.white);
        Amount.setForeground(Color.white);
        Weapon.setForeground(Color.white);
    }
    public void initUI(){
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        Image img = Toolkit.getDefaultToolkit().getImage("image//title.png");//窗口图标
        setIconImage(img);
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int rows = gameMap.length, cols = gameMap[0].length;
        setSize(cols*pixel_per_unit+pixel_rightBar, rows * pixel_per_unit + 75);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        //添加背景图片
        ImageIcon backgroundImage = new ImageIcon("background//background1.png");
        backgroundImage.setImage(backgroundImage.getImage().getScaledInstance(cols*pixel_per_unit+pixel_rightBar,rows * pixel_per_unit + 75,Image.SCALE_SMOOTH));
        background_label = new JLabel(backgroundImage);
        background_label.setBounds(0,0, this.getWidth(), this.getHeight());
        this.getLayeredPane().add(background_label, Integer.valueOf(Integer.MIN_VALUE));
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        paintPanel = new JPanel(){
            //绘制界面的函数
            public void paint(Graphics g1){
                super.paint(g1);
                Graphics2D g = (Graphics2D) g1;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
                //边框线
                if(show_padding){
                    g.setPaint(new GradientPaint(115,135,Color.CYAN,230,135,Color.MAGENTA,true));
                    g.setStroke( new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
                    g.drawRect(2, 2, padding*2-4+cols*pixel_per_unit, rows*pixel_per_unit+6);
                }
                //网格线
                if(show_grid) {
                    for(int i = 0; i <= cols; i++) {
                        g.setStroke( new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 3.5f, new float[] { 15, 10, }, 0f));//虚线
                        g.setColor(Color.black);
                        g.drawLine(padding+i*pixel_per_unit,padding,padding+i*pixel_per_unit,padding+rows*pixel_per_unit);//画竖线
                    }
                    for(int i = 0;i <= rows; i++){
                        g.drawLine(padding,padding+i*pixel_per_unit,padding+cols*pixel_per_unit,padding+i*22);//画横线
                    }
                }

                //显示死亡信息
                if(die) {
                    g.setFont(new Font("微软雅黑",Font.BOLD | Font.ITALIC,50));
                if(die || ai_die) {
                    g.setFont(new Font("微软雅黑",Font.BOLD | Font.ITALIC,30));
                    g.setColor(Color.white);
                    g.setStroke( new BasicStroke(10,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));

                    int x = this.getWidth()/2, y = this.getHeight()/2;
                    g.drawString("Sorry, you die", x-150, y-50);
                    g.drawString("Press ESC to restart", x-150, y+50);
                    //文字的位置没有根据界面大小进行适配，有可能显示出来看不到
                    if(die){
                        g.drawString("Sorry, you die", x-130, y-30);
                        g.drawString("Press Esc to restart", x-180, y+30);
                    }
                    if(ai_die){
                        g.drawString("oops, the stupid AI can not find a way out", x-250, y-30);
                        g.drawString("Press Esc to restart", x-180, y+30);
                    }
                }
            }
        };
        paintPanel.setOpaque(false);
        paintPanel.setBounds(0, 0, 900, 480);
        add(paintPanel);
        initRightBar();
        initMenuBar();
    }
    public void updateInfos(String key, String value){
        infos.get(key).setText(value);
    }
    public Vector<Coordinate> getFoodCoor(){
        return food.getFoodCoors();
    }
    public void removeFood(Coordinate coor){
        food.removeFoodCoor(coor);
    }
    public int getFoodPoint(Coordinate coor){
        return food.getFoodPoint(coor);
    }

    public Coordinate getAITarget(){
        return ai.getTarget();
    }

    public void FindNewPath(){
        ai.FindNewPath();
    }

    public int coor_trans(Coordinate coor){
        //把gamemap里面的二维坐标(x,y)转化成一维坐标
        int x = coor.x, y = coor.y;
        int cols = gameMap[0].length;
        return x * cols + y;
    }
    public void run(){
        food = new Foodset(this);
        if(gameMode == 0) snake = new PlayerSnake(this);
        else if(gameMode == 1) ai = new AISnake(this);
        else if(gameMode == 2) {
            snake = new PlayerSnake(this);
            ai = new AISnake(this);
        }
        setFocusable(true);
        setVisible(true);
        timer = new Timer();
        pause = false;
    }

    //主函数入口
    public static void main(String[] args) {
        System.out.println("Application starting...\t" + getSysTime());
        Scene game = new Scene();
        game.gameMode = 0;
        game.loadGameMap("map//map.txt");//加载游戏地图
        game.gameMode = 2;
        game.loadGameMap("map//trap.txt");//加载游戏地图
        PrintMap(game.getMap(),"debug//map.txt");
        game.initUI();//初始化游戏界面
        game.run();//开始游戏
        System.out.println("\nGame start...\t" + getSysTime());
    }
    /*
     * 计时器类,负责计时
     * 调用方法，直接new一个此类，然后主界面就开始显示计时
     * new Timer();
     */
    private class Timer{
        private int hour = 0;
        private int min = 0;
        private int sec = 0;
        public Timer(){
            this.run();
        }
        public void run() {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                if (!quit && !pause) {
                    sec +=1 ;
                    if(sec >= 60){
                        sec = 0;
                        min +=1 ;
                    }
                    if(min>=60){
                        min=0;
                        hour+=1;
                    }
                    showTime();
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
        public void reset() {
            hour = 0;
            min = 0;
            sec = 0;
        }
        private void showTime(){
            String strTime;
            if(hour < 10) strTime = "0"+hour+":";
            else strTime = ""+hour+":";
            if(min < 10) strTime = strTime+"0"+min+":";
            else strTime =strTime+ ""+min+":";
            if(sec < 10) strTime = strTime+"0"+sec;
            else strTime = strTime+""+sec;
            //在窗体上设置显示时间
            Time.setText(strTime);
        }
    }
    private class MyKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Direction direction = null;
            if(gameMode != 1) direction = snake.getDirection();
            if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {          //向右
                if(!quit && direction != Direction.LEFT && gameMode != 1) {
                    snake.setDirection(Direction.RIGHT);
                }
            } else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {    //向左
                if(!quit && direction != Direction.RIGHT && gameMode != 1) {
                    snake.setDirection(Direction.LEFT);
                }
            } else if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {      //向上
                if(!quit && direction != Direction.DOWN && gameMode != 1) {
                    snake.setDirection(Direction.UP);
                }
            } else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {    //向下
                if(!quit && direction != Direction.UP && gameMode != 1) {
                    snake.setDirection(Direction.DOWN);
                }
            } else if(key == KeyEvent.VK_ESCAPE) {  //重新开始
                restart();
            } else if(key == KeyEvent.VK_SPACE) {
                if(!pause) {//暂停
                    pause = true;
                    System.out.println("暂停...");
                } else {//开始
                    pause = false;
                    System.out.println("开始...");
                }
            }
    		//发射子弹
            if (gameMode != 1 && e.isShiftDown() && !pause) {//gameMode = 1表示界面上只有AI蛇
                snake.fire();
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
    }
    private class BGPanel extends JDialog{
        public BGPanel(){
            setTitle("更换游戏背景");//设置窗体标题
            Image img=Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
            setIconImage(img);
//            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setModal(true);//设置为模态窗口
            setSize(650,390);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(false);
            //添加背景图片
            int back_kind = 6;
            ImageIcon[] background = new ImageIcon[back_kind];
            for(int i = 0; i < back_kind; i++) {
                background[i] = new ImageIcon("background//background" + i + ".png");
                background[i].setImage(background[i].getImage().getScaledInstance(200,110,Image.SCALE_FAST));
            }
            JLabel[] Back_label = new JLabel[back_kind];
            JButton[] choose = new JButton[back_kind];
            JPanel p = new JPanel();
            for(int i = 0; i < back_kind; i++) {
                Back_label[i] = new JLabel(background[i],SwingConstants.LEFT);
                Font f = new Font("微软雅黑", Font.PLAIN, 15);
                Back_label[i].setFont(f);
                Back_label[i].setHorizontalTextPosition(SwingConstants.CENTER);
                Back_label[i].setVerticalTextPosition(SwingConstants.BOTTOM);
                choose[i] = new JButton("选择");
                choose[i].setFont(f);
                p.add(choose[i]);
                p.add(Back_label[i]);
            }
            add(p,BorderLayout.CENTER);
            p.setBackground(Color.white);
            p.setLayout(null);
            Back_label[0].setBounds(10, 0, 200, 120);
            choose[0].setBounds(70, 140, 80, 25);
            Back_label[1].setBounds(220, 0, 200, 120);
            choose[1].setBounds(280, 140, 80, 25);
            Back_label[2].setBounds(430, 0, 200, 120);
            choose[2].setBounds(490, 140, 80, 25);
            Back_label[3].setBounds(10, 180, 200, 120);
            choose[3].setBounds(70, 320, 80, 25);
            Back_label[4].setBounds(220, 180, 200, 120);
            choose[4].setBounds(280, 320, 80, 25);
            Back_label[5].setBounds(430, 180, 200, 120);
            choose[5].setBounds(490, 320, 80, 25);
            for(int i = 0; i < back_kind; i++) {
                choose[i].addActionListener(e -> {
                    int rows = gameMap.length, cols = gameMap[0].length;
                    for(int j = 0; j < back_kind; j++) {
                        if(e.getSource() == choose[j]) {
                            background[j] = new ImageIcon("background//background" + j + ".png");
                            background[j].setImage(background[j].getImage().getScaledInstance(cols*pixel_per_unit+pixel_rightBar,rows * pixel_per_unit + 75,Image.SCALE_SMOOTH));
                            background_label.setIcon(background[j]);
                        }
                    }
                });
            }
        }
    }
}
public class Foodset {
    private Scene GameUI;//母窗体,即游戏主界面
    private List<Food> food = new LinkedList<>();//用于描述食物的数组
    private Vector<Coordinate> food_coors = new Vector<>();
    private Vector<JLabel> food_labels = new Vector<>();
    private static final int FOODKIND = 6;
    private int[] point = new int[]{50, 40, 30, 20, 10, 0};//6中食物各自对应的得分
    private ImageIcon[] foodIcon = new ImageIcon[FOODKIND];//6种食物各自对应的图标
    public Foodset(Scene GameUI){
        this.GameUI = GameUI;
        //加载6张食物的图片
        for(int i = 0;i < FOODKIND;i++) {
            foodIcon[i] = new ImageIcon("food//food" + i + ".png");
            foodIcon[i].setImage(foodIcon[i].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        }

        produceFood();
//        produceFood_test();
        show();
    }

    public int getFoodPoint(Coordinate coor){
        /*给定界面上的一个点，判断该点是否有食物存在，若有，则返回对应食物的得分，否则返回-1
         * 注意coor.x代表横向的序号，从左到右依次为[0,WIDTH-1]
         * coor.y代表纵向的序号，从上到下依次为[0,HEIGHT-1]
         */
        for (Iterator<Food> iter = food.iterator(); iter.hasNext();) {
            Food node = iter.next();
            if(node.coor.x == coor.x && node.coor.y == coor.y) {
                node.label.setVisible(false);//从界面上移除食物
                GameUI.remove(node.label);
                iter.remove();//从food数组中移除被吃掉的食物
                return point[node.kind];//返回该食物对应的分数
            }
        }
        return -1;
    }

    public void produceFood_test(){
        int amount = 2, foodtag = 0;
        Food newfood;

        for(int i = 0; i < amount; i++) {
            Coordinate coor;
            if (i == 0) coor = new Coordinate(8, 4);
            else coor = new Coordinate(8, 0);
            food_coors.add(coor);
            Coordinate _coor = new Coordinate(coor.y,coor.x);//置换行和列
            GameUI.setMap(coor.x, coor.y, 2);
            newfood = new Food(foodtag, _coor, foodIcon[foodtag]);
            food.add(newfood);
            GameUI.add(newfood.label);
            food_labels.add(newfood.label);
        }

        GameUI.updateInfos("Amount", food.size() + "");//刷新GameUI界面上显示食物数量的Label
        show();
        System.out.println("产生" + amount + "个食物\t" + GameUI.getSysTime());
        for (Coordinate node : food_coors) {
            System.out.print("(" + node.x + "," + node.y + ") ");
        }
        System.out.println();
        GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
    }

    public void produceFood(){
        Random rand = new Random();
        int amount = rand.nextInt(3) + 2;
//        int amount = 3;
//        int amount = 1;
        double prob;
        int foodtag = 2;
        Food newfood;

        for(int i = 0; i < amount; i++) {
            Coordinate coor = GameUI.randomCoor();//注意，coor.x是数组的行，coor.y是数组的列，和界面上的行序号和列序号正好相反

            food_coors.add(coor);

            Coordinate _coor = new Coordinate(coor.y,coor.x);//置换行和列
            prob = rand.nextDouble();
            if(prob >= 0 && prob <0.1) 		    foodtag = 0;//10%
            else if(prob >= 0.1  && prob <0.25) foodtag = 4;//15%
            else if(prob >= 0.25 && prob <0.5)  foodtag = 3;//25%
            else if(prob >= 0.5  && prob <0.8)  foodtag = 2;//30%
            else if(prob >= 0.8 && prob <0.95)  foodtag = 1;//15%
            else if(prob >= 0.95 && prob <1) 	foodtag = 5;//5%
            GameUI.setMap(coor.x, coor.y, 2);
            newfood = new Food(foodtag, _coor, foodIcon[foodtag]);
            food.add(newfood);
            GameUI.add(newfood.label);
            food_labels.add(newfood.label);
        }
        GameUI.updateInfos("Amount", food.size() + "");//刷新GameUI界面上显示食物数量的Label
        show();
        System.out.println("产生" + amount + "个食物\t" + GameUI.getSysTime());
        for (Coordinate node : food_coors) {
            System.out.print("(" + node.x + "," + node.y + ") ");
        }
        System.out.println();
        GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
    }
    public Vector<Coordinate> getFoodCoors(){
        return food_coors;
    }
    public void removeFoodCoor(Coordinate coor){
        for(int i = 0; i < food_coors.size(); i++) {
            if(food_coors.get(i).x == coor.x && food_coors.get(i).y == coor.y){
                food_coors.remove(i);
                food_labels.get(i).setVisible(false);
                GameUI.remove(food_labels.get(i));
                food_labels.remove(i);
                if(food_coors.isEmpty()){
                    food_labels.clear();
                    produceFood();
                }
                return;
            }
        }
    }
    public void show(){
        for (Food node : food) {
            node.label.setBounds(GameUI.getPixel(node.coor.x, 5, 22),
                    GameUI.getPixel(node.coor.y, 5, 22), 20, 20);
            node.label.setVisible(true);
        }
    }
    public void removeAll(){//移除界面上的所有食物图片
        for (Food node : food) {
            GameUI.setMap(node.coor.y, node.coor.x, 0);//地图上的该点重新标记为0
            node.label.setVisible(false);
            GameUI.remove(node.label);
        }
        food.clear();
    }
    //食物的数据结构
    public class Food {
        int kind;//食物种类，0-5对应5种不同的食物，见文档说明
        JLabel label;
        Coordinate coor;//坐标
        public Food(int kind,Coordinate coor,ImageIcon icon){
            this.kind = kind;
            label = new JLabel(icon);
            this.coor = coor;
        }
    }
}