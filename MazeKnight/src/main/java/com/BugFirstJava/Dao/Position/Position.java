package com.BugFirstJava.Dao.Position;

import com.BugFirstJava.Dao.Level.Level;

import java.io.Serial;
import java.io.Serializable;

public class Position implements Serializable {
    // 坐标的封装类,采用单例模式,作为hashmap的key
    /*
        负责人：黄严
     */
    @Serial
    private static final long serialVersionUID = 7L;
    public final int x;
    public final int y;
    private Position(int x,int y){
        this.x = x;
        this.y = y;
    }
    //位置对象仓库
    private static final Position[][] pos = new Position[Level.maxHeight][Level.maxWidth];

    /*
        功能：获取指定坐标的position对象
        参数：x，y
        返回值：position对象
     */
    public static Position getPos(int x,int y){
        if(pos[x][y] == null)
            pos[x][y] = new Position(x,y);
        return pos[x][y];
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Position)
            return this.x == ((Position) obj).x && this.y == ((Position) obj).y;
        return false;
    }
    @Override
    public int hashCode() {
        return x * (Level.maxHeight+Level.maxWidth) + y;
    }
}
