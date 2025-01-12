package com.BugFirstJava.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.BugFirstJava.Dao.Position.Position;

class Unit {
    Position pos;
    char c;
    boolean vis;
    Unit par;
    Unit pre;
    boolean isEmpty;
    public Unit(int x, int y, char c) {
        pos = Position.getPos(x,y);
        this.c = c;
        isEmpty = true;
        par = this;
        pre = this;
        vis = false;
    }
    
    @Override
    public String toString() {
        return "" + c;
    }
}
 class Edge extends Unit{
    public Edge(int x, int y, char c) {
        super(x,y,c);
    }

    @Override
    public String toString() {
        return "" + c;
    }
}

public class MazeGenerator {
   private static final int width = 55;//ood
   private static final int height = 43;//ood
   private static char[][] map = new char[height][width];
   private static final HashMap<Position, Unit> units = new HashMap<>();
   private static final ArrayList<Edge> linerEdges = new ArrayList<>();
   private static final ArrayList<Edge> edges = new ArrayList<>();
   private static final Random random = new Random(System.currentTimeMillis());
    private static Unit s;
    private static Unit e;
    private static  int level;
    public static void main(String[] args) {
        generate(12);
        print();
        for (int i = 0; i < 5; i++) {
            examine(2);
        }
    }

    static char[][] generate(int level) {
        //initArray
        MazeGenerator.level = level;
        units.clear();
        edges.clear();
        linerEdges.clear();
        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) stuffWall(i);
            else stuffUnits(i);
        }
        //set start and end
        Random random1 = new Random(System.currentTimeMillis());
        //ood
        int sx = ((random1.nextInt(height) + 1) * 2 - 1) % (height - 1);
        //ood
        int sy = ((random1.nextInt(width) + 1) * 2 - 1) % (width - 1);
        //ood
        int ex = ((random1.nextInt(height) + 1) * 2 - 1) % (height - 1);
        //ood
        int ey = ((random1.nextInt(width) + 1) * 2 - 1) % (width - 1);
        Position sP = Position.getPos(sx, sy);
        Position eP = Position.getPos(ex, ey);
         s = units.get(sP);
         e = units.get(eP);
        s.isEmpty = false;
        e.isEmpty = false;
        e.c = ']';
        s.c = 'S';
        map[sx][sy] = 'S';
        map[ex][ey] = ']';
        //span
        while (true) {
            //
            if (con(units.get(Position.getPos(sx, sy)),units.get(Position.getPos(ex, ey)))) break;
            //get one edge
            int i = random.nextInt(linerEdges.size()*50000)% linerEdges.size();
            Edge e1 = linerEdges.get(i);
            linerEdges.remove(i);
            Unit u1;
            Unit u2;
            if (e1.pos.x % 2 == 0) {
                u1 = units.get(Position.getPos(e1.pos.x - 1, e1.pos.y));
                u2 = units.get(Position.getPos(e1.pos.x + 1, e1.pos.y));
            }
            else {
                u1 = units.get(Position.getPos(e1.pos.x, e1.pos.y - 1));
                u2 = units.get(Position.getPos(e1.pos.x, e1.pos.y + 1));
            }
            //union
            if (!con(u1 , u2)) {
                union(u1, u2);
                e1.par = u1;
                units.put(e1.pos,e1);
                e1.c = '.';
                map[e1.pos.x][e1.pos.y] = '.';
            }
        }
        //放置实体
        spreadEntity(6*level,'&');
        spreadEntity(2*level,'$');
        spreadEntity(4*level,'?');
        return map;
    }
    public static void print() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    public static Unit find(Unit u) {
        if (u.par == u) return u;
        else return u.par = find(u.par);
    }

    public static boolean con(Unit u1, Unit u2) {
        return find(u1) == find(u2);
    }

    public static void union(Unit u1, Unit u2) {
        u1.par = u2;
    }

    public static void stuffWall(int line) {
        for (int i = 0; i < width; i++) {
            map[line][i] = '#';
            if (line != 0 && line != height - 1) {
                if (i % 2 != 0&& i!= width - 1) {
                    linerEdges.add(new Edge(line, i, '#'));
                }
            }
        }
    }
    public static void stuffUnits(int line) {
        for (int i = 0; i <= width - 1; i++) {
            if (i % 2 == 0) {
                map[line][i] = '#';
                if (i != 0 && i != width - 1) linerEdges.add(new Edge(line, i, '#'));
            } else {
                map[line][i] = '.';
                units.put(Position.getPos(line, i), new Unit(line, i, '.'));
            }
        }
    }
    public static void spreadEntity(int n, char c){
        n = Math.max(n,n%spare());
        List<Unit> cp0 = units.values().stream().filter(o->o.isEmpty).toList();
        ArrayList<Unit> cp = new ArrayList<>(cp0);
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            int randIn = r.nextInt(cp.size());
            Unit u = cp.get(randIn);
            Position p =u.pos;
            if (u.isEmpty&&con(u,s)){
                map[p.x][p.y] = c;
                u.c = c;
                u.isEmpty = false;
                cp.remove(u);
            }
        }
    }
    private static int spare(){
        return (int)units.values().stream().filter(u->u.isEmpty).count();
    }
    private static boolean examine(int minimal){
        units.values().forEach(u->{u.pre = u;u.vis =false;});
        Queue<Unit> unitQueue = new ArrayDeque<>();
        unitQueue.add(s);
        int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
        while(!unitQueue.isEmpty()){
            Unit pop = unitQueue.remove();
            Position p = pop.pos;
            for (int i = 0; i < 4; i++) {
                Position uP = Position.getPos(p.x+dir[i][0],p.y+dir[i][1]);
                if (map[uP.x][uP.y] != '#'){
                    Unit u = units.get(uP);
                    if (!u.vis){
                        u.pre = pop;
                        unitQueue.add(u);
                    }
                }
            }
            pop.vis = true;
        }
        Unit cursor = e;
        int count =0;
        while(cursor!=s){
            cursor = cursor.pre;
            if (cursor.c =='&')count++;
        }
        System.out.println(count);
        return count >= minimal;
    }
}


