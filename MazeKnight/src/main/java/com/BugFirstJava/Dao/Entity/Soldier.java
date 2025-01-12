package com.BugFirstJava.Dao.Entity;

import java.io.Serial;
import java.io.Serializable;

public enum Soldier  implements Serializable {
    // 士兵类
    WARRIOR('W',600,55,35,0,0,0.25),
    ARCHER('A',440,85,20,0.15,0.2,0.35),
    MAGE('M',380,80,25,0.1,0.25,0.25),
    TiMO('T',240,85,25,0.25,0.3,0.3),
    丁丁('D',150,90,32,0.25,0.3,0.5);
    @Serial
    private static final long serialVersionUID = 9L;
    public final double health;
    public final double attackStrength;
    public final double defence;
    public final double escape;
    public final double luck;
    public final char symbol;
    public final double critRate;
    Soldier(char symbol, int health, int attackStrength, int defence,double escape,double critRate ,double luck) {
        this.attackStrength = attackStrength;
        this.symbol = symbol;
        this.luck = luck;
        this.health = health;
        this.defence = defence;
        this.escape = escape;
        this.critRate = critRate;
    }
    /*
        编号：35
        难度：简单
        负责人：黄严
        功能：根据symbol获取对应的Soldier对象
        参数：兵种符号
        返回值：Soldier对象，若不存在返回null
     */
    public static Soldier get(char symbol){
        for (Soldier soldier : Soldier.values()) {
            if (soldier.symbol == symbol) {
                return soldier;
            }
        }
        return null;
    }
    @Override // 重写toString方法，返回symbol
    public String toString() {
        return this.name();
    }
}
