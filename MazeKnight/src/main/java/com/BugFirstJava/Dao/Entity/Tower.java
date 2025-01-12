package com.BugFirstJava.Dao.Entity;

import com.BugFirstJava.Dao.Position.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Tower implements Serializable {
    Position pos;
    public double health = 130;//血量，最小为0！
    public double attackStrength = 65;
    public double defence = 35;
    @Serial
    private static final long serialVersionUID = 5;
    /*
     编号：34
     难度：简单
     负责人：曾鹏
     功能：对玩家进行一次攻击
     描述：根据伤害计算函数，计算玩家受到的伤害，并修改玩家血量(最小为0)
     参数：玩家对象
     返回值：玩家受到的伤害
     */
    public int attack(Player p) {
        /*
         伤害计算  damage = A * [A/(2d + A)]
         1.用随机数与玩家闪避率判断是否闪避
         2.若闪避，玩家不扣血，返回0；
	       否则玩家扣血，返回伤害值（转为int）
         */
           /*
         伤害计算  damage = A * [A/(2d + A)]
         1.用随机数与玩家闪避率判断是否闪避
         2.若闪避，玩家不扣血，返回0；
	       否则玩家扣血，返回伤害值（转为int）
         */
           /*
         伤害计算  damage = A * [A/(2d + A)]
         1.用随机数与玩家闪避率判断是否闪避
         2.若闪避，玩家不扣血，返回0；
           否则玩家扣血，返回伤害值（转为int）
         */
           /*
         伤害计算  damage = A * [A/(2d + A)]
         1.用随机数与玩家闪避率判断是否闪避
         2.若闪避，玩家不扣血，返回0；
           否则玩家扣血，返回伤害值（转为int）
         */
        int damage=(int)(attackStrength*(attackStrength/((2*p.defence)+attackStrength)));
        Random r=new Random();
        double random=r.nextDouble(1);
        if (random<=p.escape){
            return 0;
        }else {
            p.health=Math.max(p.health-damage,0);
            return damage;
        }
    }

    public Tower(int x, int y) {
        this.pos = Position.getPos(x,y);
    }
}
