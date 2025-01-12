package com.BugFirstJava.Dao.Items;

public enum Potions {
    LIFEPOTIOND(50,50,0,0,0,0,0,20),
    LIFEPOTIONH(100,0,0,0,0,0,0,20),
    LIFEPOTIONM(0,100,0,0,0,0,0,20),
    STRENGTHPOTION(0,0,5,0,0,0,0,30),
    DEFENCEPOTION(0,0,0,5,0,0,0,40),
    ESCAPEPOTION(0,0,0,0,0,0.05,0,60),
    CRITICPOTION(0,0,0,0,0,0,0.1,60),
    LUCKPOTION(0,0,0,0,0.07,0,0,40);
    public final double health;
    public final double mHealth;
    public final double attackStrength;
    public final double defence;
    public final double luck;
    public final double escape;
    public final double critRate;
    public final int price;
    Potions(double health, double mHealth, double attackStrength, double defence, double luck, double escape, double critRate, int price) {
        this.health = health;
        this.attackStrength = attackStrength;
        this.defence = defence;
        this.luck = luck;
        this.escape = escape;
        this.critRate = critRate;
        this.mHealth = mHealth;
        this.price = price;
    }
}
