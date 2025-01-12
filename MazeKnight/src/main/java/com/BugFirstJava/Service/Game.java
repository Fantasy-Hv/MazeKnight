package com.BugFirstJava.Service;

import com.BugFirstJava.Dao.Entity.Player;
import com.BugFirstJava.Dao.Items.Potions;
import com.BugFirstJava.Dao.Entity.Soldier;
import com.BugFirstJava.Dao.Entity.Tower;
import com.BugFirstJava.Dao.Level.Level;
import com.BugFirstJava.Dao.Position.Position;
import com.BugFirstJava.Dao.Record.Record;
import com.BugFirstJava.Dao.Record.Save;

import java.time.LocalDateTime;
import java.util.*;

public class Game {
    //游戏过程中的全局变量
    //玩家对象
     static Player player;
    //关卡对象
     static Level level;
    //是否胜利
    static boolean isWin;
    //玩家要移动的目标位置
    static Position tarPos;
    static Position reachable;



    /*
        编号：18
        难度：中等
        负责人：唐百叶
//        功能：根据玩家输入的移动指令(包括方向和步数，如"W 5")，和玩家位置
            把目标位置的position对象赋值给全局变量tarPos：
            检查路径上每个位置（不包括玩家当前位置，包括终点），
            *若发现移动路径上有非空地（墙、塔、出口/药水/金币），直接把第一个非空地的position对象赋值给tarPos并结束方法
            *因为规定地图最外围是墙或出口，索引越界前循环必结束，不必考虑越界情况。
        参数 :移动指令
        返回值：无
     */
   static void getTarPos(String Command){
       final int[][] DIR = {
               {-1, 0}, // 上 (W)
               {1, 0},  // 下 (S)
               {0, -1}, // 左 (A)
               {0, 1}   // 右 (D)
       };
       final Map<Character,Integer> DIR_MAP=new HashMap<>();
       {
           DIR_MAP.put('W',0);
           DIR_MAP.put('S',1);
           DIR_MAP.put('A',2);
           DIR_MAP.put('D',3);
       }
       char dir=Command.charAt(0);//方向
       int step=Integer.parseInt(Command.substring(2));//步数

       int x=player.position.x;
       int y=player.position.y;
       int dirIndex=DIR_MAP.get(dir);
       int dx=DIR[dirIndex][0];
       int dy=DIR[dirIndex][1];

       int i=1;
       for(;i<=step;i++){
           int checkX=x+dx*i;
           int checkY=y+dy*i;
           if(Math.abs(level.maze[checkX][checkY])!=3){
               break;
               //返回第一个非空地
           }
       }
       if(i<=step){//移动到了非空地
           x+=dx*i;
           y+=dy*i;
           reachable=Position.getPos(x-dx,y-dy);
       }else{
           x+=dx*step;
           y+=dy*step;
           reachable=Position.getPos(x,y);
       }

       tarPos=Position.getPos(x,y);
   }


    /*
        编号：19
        难度：简单
        负责人：唐百叶
        功能：供view层判断目标位置的类型
        描述：调用getTarPos()方法获取目标位置的position对象
        根据全局变量tarPos和level.maze判断并返回目标位置对应数字
        返回值：
		 1 城墙
         2 防御塔
         3 道路(空地或出口)
         6 金币
         7 药水
     */
 public static int tarPosType(String Command){
     getTarPos(Command);
     int x=tarPos.x;
     int y=tarPos.y;
     int n = Math.abs(level.maze[x][y]);
     if(n==3 || n==4)return 3;
     else return n;
 }


    /*
        编号：20
        难度：中等
        负责人：仁雁
        功能：移动玩家到目标位置
        参数：无，
        描述：:
            使用全局变量tarPos
            在level的maze中：
            若目标位置为出口，修改isWin为true;
            否则
                将玩家原来所在坐标处的值改为空地（正）
                目标位置的值改为玩家
                *路径：玩家当前位置与目标位置之间的位置（不包括当前位置，包括目标位置）
                路径上每个位置及其相邻位置的maze值改为原来的绝对值（解锁视野）
                修改player的position为目标位置
        返回值：无
     */
    //*重写
  public static void move(){
         // 判断是否胜利
            if (tarPos.x == level.exit.x && tarPos.y == level.exit.y) {
                isWin = true;
                reachable = tarPos;
            }
        // 移动玩家
            Position temp = tarPos;
            tarPos = reachable;
            // 将玩家原来所在坐标处的值改为空地（正）
            level.maze[player.position.x][player.position.y] = 3;
            // 目标位置的值改为玩家
            level.maze[tarPos.x][tarPos.y] = 0;
            // 解锁路径及路径上每个位置的相邻位置的视野
            setVisible(player.position, tarPos);
            // 修改玩家的position为目标位置
            player.position = tarPos;
            tarPos = temp;

  }


    /*
        编号：21
        难度：简单
        负责人：仁雁
        功能：玩家攻击塔一次
             根据tarPos到level的towers中获取塔对象
             调用player.attack(tower)方法，获取玩家造成的伤害
             若塔死亡：
               把level的maze中对应位置改为空地
               返回玩家造成的伤害的负值;
             否则返回玩家造成的伤害的正值;
        参数：无
        返回值：伤害值，正值表示塔受到伤害但没死，负数表示塔死亡
   */
  public static int roundOne(){
      //根据坐标从防御塔集合中获取塔对象
      Tower t = level.towers.get(tarPos);
      int damage = player.attack(t);//对塔造成的伤害
      if(t.health <= 0){
          level.maze[tarPos.x][tarPos.y] = 3;
          //
          player.gold+=20;
          player.record.score+=100;
          player.record.killCount++;
          return -damage;
      }
      return damage;
  }


    /*
      编号：22
      难度：简单
      负责人：仁雁
      功能：塔攻击玩家一次
          根据tarPos到level的towers中获取塔对象
          调用tower.attack()方法，获取塔造成的伤害
           若玩家死亡，返回伤害的负值
           否则返回伤害的正值
       返回值：伤害值，正值表示玩家受到伤害但没死，负数表示玩家死亡
     */
  public static int roundTwo(){
      Tower t = level.towers.get(tarPos);//获取塔对象
      int damage = t.attack(player);//塔对玩家造成的伤害
      if(player.health <= 0){
          return -damage;
      }
      return damage;
  }


    /*
        编号：23
        难度：简单
        负责人：刘昊
        功能：判断字符对应兵种是否存在
             根据Soldier类的get()返回值判断
        参数：兵种符号
        返回值：是否存在
     */
  public static boolean isExist(char soldierSyb){
      return Soldier.get(soldierSyb) != null;
  }


    /*
        编号：24
        难度：1
        负责人：黄严
        功能：判断玩家是否胜利
        参数：无
        返回值：全局变量isWin
     */
    public static boolean isWin(){return isWin;}


    /*
        编号：25
        难度：1
        负责人：仁雁
        功能：初始化关卡和玩家数据
            参数调用ResourceManager.getLevel()来初始化全局变量level
            调用initPlayer()，参数reSetRecord为真，初始化Player对象；
            令isWin = false;
        参数：关卡编号，玩家选择的兵种
        返回值：是否有存档
     */
    public static boolean initGame(int levelId,char soldierSyb,boolean load) {
        isWin = false;//初始化是否胜利
        if (load) {
            Save save = ResourceManager.getSave();
            if (save != null) {
                level = save.level;
                player = save.player;
                if (player.position == level.exit){
                    nextLevel(level.id < 7);
                }
                return true;
            }
        }
        level = ResourceManager.getLevel(levelId, true);//初始化关卡
        initPlayer(soldierSyb, true);//初始化玩家
        return false;
    }

    /*
        编号：26
        难度：简单
        负责人：tby
        功能：进入下一关
        参数：无
        描述：调用ResourceManager.getLevel()来更新Level对象
             调用initPlayer()，参数reSetRecord为假，以实现恢复玩家血量；
            令isWin = false;
        返回值：是否完全通关
     */
    public static boolean nextLevel(boolean fromFile){
        if (level.id == 7&&fromFile){
            return true;
        }
        level = ResourceManager.getLevel(level.id + 1,fromFile);
        initPlayer(player.soldier.symbol,false);
        player.position = level.entrance;
        isWin = false;
        return false;
    }


    /*
    编号：27
    难度：简单
    负责人：刘昊
    功能：获取初始化的玩家对象，（使用在getLevel之后）
    参数：soldier符号
    返回值：已初始化的玩家对象
    描述：（根据玩家选择的兵种，可选）初始化玩家战斗相关属性；
        把Game类的level对象的entrance对象作为玩家的位置
        若reSetRecord为真，则初始化玩家Record属性；
        返回玩家对象
     */
    static void initPlayer(char soldierSyb,boolean isReset){
         if(isReset) {
             //第一次，创建玩家对象
             player = new Player();
             player.gold = 100;
             player.soldier =Soldier.get(soldierSyb);
             player.position=Game.level.entrance;
             player.health= player.soldier.health;
             player.maxHealth = player.health;
             player.attackStrength=player.soldier.attackStrength;
             player.defence = player.soldier.defence;
             player.escape = player.soldier.escape;
             player.critRate =player.soldier.critRate;
             player.luck = player.soldier.luck;
             player.record = new Record();
             player.record.soldierSyb = soldierSyb;
             player.record.highestLevel=level.id;
         }
         //不是第一次，那就是回血
         else {
             player.health = player.maxHealth;
             player.record.highestLevel=level.id;

         }
    }

    /*
        编号：28
        难度：简单
        负责人：黄严
        功能：获取当前关卡的地图
        参数：无
        返回值：当前关卡的地图
        描述：直接返回Game类的level对象的maze属性
     */
    public static int[][] getMaze(){return level.maze;}

    /*
        编号：29
        难度：简单
        负责人：黄严
        功能：获取本次游戏的战绩
        参数：无
        返回值：本次游戏的战绩
     */
    public static Record getRecord(){return player.record;}


static  void setVisibleAround(int[][] maze, Position pos) {
    int startX = pos.x;
    int startY = pos.y;

    // 定义八个方向的偏移量
    int[][] directions = {
        {-1, 0},  // 上
        {1, 0},   // 下
        {0, -1},  // 左
        {0, 1},   // 右
        {-1, -1}, // 左上
        {-1, 1},  // 右上
        {1, -1},  // 左下
        {1, 1}    // 右下
    };

    // 遍历八个方向，设置视野
    for (int[] direction : directions) {
        int x = startX + direction[0];
        int y = startY + direction[1];

        // 检查是否在地图范围内
        if (x >= 0 && x < maze.length && y >= 0 && y < maze[x].length) {
            // 将格子的值设置为其绝对值
            maze[x][y] = Math.abs(maze[x][y]);
        }
    }

    // 也将当前位置设置为绝对值
    maze[startX][startY] = Math.abs(maze[startX][startY]);
}

/*
负责人：仁雁
 */
private static List<Position> getPath(Position startPos, Position endPos) {
        List<Position> path = new ArrayList<>();
        int x = startPos.x;
        int y = startPos.y;

        // 确保我们总是从小到大遍历
        int deltaX = Integer.compare(endPos.x, startPos.x);
        int deltaY = Integer.compare(endPos.y, startPos.y);

        while (x != endPos.x || y != endPos.y) {
            path.add(Position.getPos(x, y));
            if (x != endPos.x) x += deltaX;
            if (y != endPos.y) y += deltaY;
        }
        // 包括目标位置
        path.add(endPos);
        return path;
    }

    /*
        编号：~
        难度：简单
        *负责人：仁雁
        功能：设置玩家视野
        参数：无
        描述：调用setVisibility()，参数为玩家当前位置和出口位置
     */
     private static void setVisible(Position startPos, Position endPos) {
        // 获取从起点到终点的路径
        List<Position> path = getPath(startPos, endPos);
        // 遍历路径上的每个位置及其相邻位置，设置为绝对值
        for (Position pos : path) {
            setVisibleAround(level.maze, pos);
        }
    }
    public static char getPlayerSyb(){return player.soldier.symbol;}

     public static Potions consume(Potions p){

//        用于玩家购买药水或拾取药水时的增益获得
//        参数：从商店处买的药水
         if (p!= null){
//        若参数不为null，
//            就使用参数作为要使用的药水
//            扣除玩家相应金币
             player.gold-=p.price;
         }
         else {
//        否则
//           使用随机数生成随机索引，获取Potion.values()返回值中的药水对象。
             Random random = new Random(System.currentTimeMillis());
             p = Potions.values()[random.nextInt(Potions.values().length)];
//            把地图对应位置改为空地
             level.maze[tarPos.x][tarPos.y]=3;
             player.record.score+=100;
         }
//        把药水的增益值加到玩家对应属性上
         player.health+=p.health;
         player.maxHealth+=p.mHealth;
         player.attackStrength+=p.attackStrength;
         player.defence+=p.defence;
         player.escape = Math.min(p.escape+player.escape,0.7);
         player.critRate = Math.min(p.critRate+player.critRate,0.7);
         player.luck = Math.min(p.luck+player.luck,1.0);
//         返回药水对象
        return p;
    }
    public static void pick(){
        /*此时全局变量tarPos为金币在迷宫中的位置
        玩家货币+20
         把地图对应位置改为空地
         */
         player.gold+= (int) (20+Math.random()*10+level.id*5);
        level.maze[tarPos.x][tarPos.y]=3;
    }
    public static Player getPlayer(){
         return player;}
     public static Tower getTower(){
      return level.towers.get(tarPos);
    }



}
