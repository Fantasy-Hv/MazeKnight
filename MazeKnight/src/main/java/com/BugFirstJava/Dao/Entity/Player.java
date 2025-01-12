package com.BugFirstJava.Dao.Entity;

import com.BugFirstJava.Dao.Position.Position;
import com.BugFirstJava.Dao.Record.Record;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Player implements Serializable {
    //玩家类
    @Serial
    private static final long serialVersionUID = 4L;
   public Position position;
   public double health;//血量，最小为0！！
   public double maxHealth;
   public double attackStrength;
   public double defence;
   public double luck;
   public double critRate;
   public double escape;
   public int gold;
   public Record record;
   public Soldier soldier;

     /*
     编号：36
     难度：简单
     负责人：曾鹏
     功能：对塔进行一次攻击
     描述：内含伤害计算函数，计算塔受到的伤害，并修改塔血量(最小为0)
     参数：塔对象
     返回值：塔受到的伤害
     */
    public int attack(Tower t){
        /*
         1.伤害计算  damage = A * [A/(2d + A) + H/3maxH]
         2.用随机数与暴击率判断是否暴击
           若暴击 damage * = 1 + luck
	     3.塔扣血,返回伤害值（转为int）
         */
          int damage=(int)(attackStrength*(attackStrength/(2*t.defence+attackStrength)+health/(3*maxHealth)));
        Random r=new Random();
        double random=r.nextDouble(1);
        if (random<=critRate){
            damage=damage*(int)(1+luck);
            t.health=Math.max(t.health-damage,0);//塔扣血
            return damage;
        }else {
            t.health=Math.max(t.health-damage,0);
            return damage;
        }
    }
}
