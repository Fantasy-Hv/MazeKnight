package com.BugFirstJava.Dao.Level;

import com.BugFirstJava.Dao.Entity.Tower;
import com.BugFirstJava.Dao.Position.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;


public class Level  implements Serializable {
    /*
     关卡类
     包含关卡编号
     迷宫地图
     起点和出口位置
     防御塔
     */
    //迷宫规格
    public static int maxWidth = 100 ;
    public static int maxHeight = 100 ;
    @Serial
    private static final long serialVersionUID = 2L;
    // 关卡编号，从1开始，与地图文件名对应
    public int id;
    /*
    迷宫地图
		map[x][y]的值：
		负数：不可见
		非负：可见
		绝对值：
		 0玩家（打印用对应兵种符号）
		 1城墙（打印用“#”）
         2 防御塔（打印用“&”）
         3 空地（打印用“.”）
         4 出口 （“]”）
         5 非迷宫区域（地图未利用的位置，打印用空格）
         6 药水（打印用“?”）
         7 金币（打印用“$”）
     */
    public int[][] maze;
    // 防御塔集合
    public  HashMap<Position,Tower> towers;
    // 迷宫起点和出口
    public Position entrance;
    public Position exit;

    public Level(int level, int[][] maze, Position entrance, Position exit, HashMap<Position, Tower> towers) {
        this.id = level;
        this.maze = maze;
        this.entrance = entrance;
        this.exit = exit;
        this.towers = towers;
    }
}
