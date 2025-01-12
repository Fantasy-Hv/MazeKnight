package com.BugFirstJava.View;

import com.BugFirstJava.Dao.Entity.Player;
import com.BugFirstJava.Dao.Entity.Soldier;
import com.BugFirstJava.Dao.Entity.Tower;
import com.BugFirstJava.Dao.Items.Potions;
import com.BugFirstJava.Dao.Record.Record;
import com.BugFirstJava.Service.Game;
import com.BugFirstJava.Service.ResourceManager;

import javax.swing.text.DateFormatter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

import static com.BugFirstJava.Service.ResourceManager.getRecord;

public class Show {
    public static final String BOLD = "\u001B[1m";// 加粗
    public static final String ITALIC = "\u001B[3m"; // 斜体
    public static final String RED = "\u001B[31m"; // 红色
    public static final String GREEN = "\u001B[32m";// 绿色
    public static final String YELLOW = "\u001B[33m";// 黄色
    public static final String BLUE = "\u001B[34m";// 蓝色
    public static final String LIGHTBLUE = "\u001B[36m";// 青色
    public static final String PURPLE = "\u001B[35m";// 紫色
    public static final String RESET = "\u001B[0m";// 重置属性
    static Scanner sc = new Scanner(System.in);
    public static final String CHOICETAB = "\t\t\t\t\t\t";
    public static final String ITEMTAB = "\t\t\t";
    public static final String RELINE = "\r";

//-----------------------打印画面----------------------------
    /*
       编号：0
       难度：简单
       负责人:梅鑫宇
       功能：打印游戏菜单
       内容：
            1. 开始游戏
            2. 游戏设置
            3. 历史记录
            4. 退出游戏
            请输入你的选择：
     */
    public static void MenuView(){
        clear();
        System.out.println(BLUE+BOLD+ITALIC+"\t\t\t\t==================MazeKnight================="+RESET);
        System.out.println(GREEN+"\t\t\t\t\t\t1. 新的游戏"+RESET);
        System.out.println(LIGHTBLUE+"\t\t\t\t\t\t2. 继续游戏"+RESET);
        System.out.println(YELLOW+"\t\t\t\t\t\t3. 音量设置"+RESET);
        System.out.println(PURPLE+"\t\t\t\t\t\t4. 历史记录"+RESET);
        System.out.println(RED+"\t\t\t\t\t\t5. 退出游戏"+RESET);
        System.out.print("\t\t\t\t\t    请输入你的选择(1-4)：\n\t\t\t\t\t\t ");
    }


    /*
       编号：1
       难度：简单
       负责人：梅鑫宇
       功能：打印游戏场景
       调用Game.getMaze()方法获取地图二维数组
          遍历打印map[x][y]的值对应的字符：
		负数：不可见
		非负：可见
		绝对值：
		 0玩家（打印用对应兵种符号）
		 1 城墙（“#”）
         2 防御塔（“&”）
         3 空地（“.”）
         4 出口 （“]”）
         5 虚空 （" "）
         6 金币 （“$”）
         7 药水 （“?”）
       参数：
       返回值：无
    */
    public static void MazeView(){
        clear();
         int[][]map = Game.getMaze();
        for (int i = 0; i < map.length; i++) {
            System.out.print(ITEMTAB);
            for (int j = 0; j < map[i].length; j++) {
                if(map[i][j]>=0){
                    if(map[i][j]==0){
                        System.out.print(LIGHTBLUE+Game.getPlayerSyb()+" "+RESET);
                    }else if(Math.abs(map[i][j]) == 1){
                        System.out.print("# ");
                    }else if(Math.abs(map[i][j])==2){
                        System.out.print(RED+"& "+RESET);
                    } else if(Math.abs(map[i][j])==3){
                        System.out.print(". ");
                    }else if(Math.abs(map[i][j])==4){
                        System.out.print("] ");
                    } else if (Math.abs(map[i][j])==6) {
                        System.out.print(YELLOW+"$ "+RESET);
                    }else if (Math.abs(map[i][j])==7){
                        System.out.print("? ");
                    }
                }else if (map[i][j]<0){
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }


    /*
       编号：2
       难度：简单
       负责人：唐百叶
       功能：
            打印失败提示；
            调用currentRecordView()方法打印本次游戏记录
            接受任意输入后结束
       参数：无，
       返回值：无
     */
    public static void loseView(){
        System.out.println("很遗憾，游戏失败！");
        currentRecordView();
        System.out.println("输入任意键返回菜单");
        sc.nextLine();
    }


    /*
       编号：3
       难度：简单
       负责人：梅鑫宇
       功能：打印胜利信息
         打印本关通过提示；
         调用currentRecordView()方法打印本次游戏记录
         接受任意输入后方法结束（提示玩家；输入任意键后进入下一关）
       参数：无
       返回值：无
     */
    public static void WinView(){
        Random random = new Random(System.currentTimeMillis());
        winEmoji(random.nextInt(3)+1);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("当前战绩:");
        currentRecordView();
        System.out.println("输入任意键后进入下一关....");
        sc.nextLine();
    }





    /*
       编号：5
       难度：简单
       负责人：拓展功能，待定
       功能：打印本次游戏记录
       描述：使用Game.getRecord()方法获取Record对象
            分行打印历史战绩条目
       参数：无
       返回值：无
     */
    public static void currentRecordView(){
        Record re =Game.getRecord();
        System.out.println(LIGHTBLUE+"角色：" + Game.getPlayer().soldier + " 得分：" + re.score + " 击杀数：" + re.killCount+RESET);
    }


    /*
        编号：6
        难度：简单
        负责人：梅鑫宇
        功能：展示士兵信息：
        参数：无
        返回值：无
            调用Soldier类的静态方法values()获取所有Soldier对象
            打印Soldier类所有士兵对象的信息；
            格式：
    （这是表头）士兵名称（符号） 属性名1, 属性名2....
  （这是数据项） WARRIOR(w)     500     70   ...

     */
    public static void SoldiersView(){
        clear();
        System.out.println(GREEN+"角色名称(符号)\t\t生命值\t\t攻击力\t\t防御力\t\t闪避\t\t暴击率\t\t暴击伤害+"+RESET);
        Soldier[] soldier = Soldier.values();
        for (int i = 0; i < soldier.length; i++) {
            System.out.println(soldier[i]+"\t"+soldier[i].symbol+"\t\t"+soldier[i].health+"\t\t"+soldier[i].attackStrength+
                    "\t\t"+soldier[i].defence+"\t\t"+soldier[i].escape+"\t\t"+soldier[i].critRate+"\t\t"+soldier[i].luck);
        }
    }




    public static void shopView(){
//        展示商店面板
//        调用  Potion.values()获取药水数组
//        打印药水信息-包括 索引 增益（只打印buff数组非空值） 价格
//        提示玩家选择:Q/q退出,索引值表示购买
        clear();
        Player player = Game.getPlayer();
             System.out.println(YELLOW+BOLD+ITALIC+"====================商店=====================");
        System.out.println("以下是商店中的药水列表：");
        // 调用 Potion.values() 获取药水数组
        Potions[] potions = Potions.values();
        // 打印药水信息 - 包括索引、增益（只打印非空值）、价格
        for (int i = 0; i < potions.length; i++) {
            System.out.print((i + 1) + ". " + potions[i].name() + " - ");
            // 打印增益信息
            if (potions[i].health > 0) {
                System.out.print("生命值 +" + potions[i].health + " ");
            }
            if (potions[i].mHealth > 0) {
                System.out.print("最大生命值 +" + potions[i].mHealth + " ");
            }
            if (potions[i].attackStrength > 0) {
                System.out.print("攻击力 +" + potions[i].attackStrength + " ");
            }
            if (potions[i].defence > 0) {
                System.out.print("防御力 +" + potions[i].defence + " ");
            }
            if (potions[i].luck*10 > 0) {
                System.out.print("暴击伤害加成 +" + (int)(potions[i].luck * 100) + "% ");
            }
            if (potions[i].escape*10 > 0) {
                System.out.print("躲闪率 +" + (int)(potions[i].escape * 100) + "% ");
            }
            if (potions[i].critRate*10 > 0) {
                System.out.print("暴击率 +" + (int)(potions[i].critRate * 100) + "% ");
            }
            // 打印价格
            System.out.println("价格：" + potions[i].price + "金币");
        }
        // 提示玩家选择
        System.out.println("您当前的金币数为："+player.gold+RESET);
    }
    public static void playerRecordView(){
//        调用getRecord(3)方法获取玩家历史记录
//        打印 玩家历史最高记录
        clear();
        Record[] re = getRecord(3);
        if (re == null) System.out.println(CHOICETAB+"尚无记录");
        else {
            System.out.println(YELLOW+ITEMTAB+"\t\t------------玩家历史最高记录-----------"+RESET);
            System.out.println(CHOICETAB+"最高得分：" + re[0].score);
            System.out.println(CHOICETAB+"最高关卡：" + re[0].highestLevel);
            System.out.println(CHOICETAB+"最高击杀：" + re[0].killCount);
            System.out.println(CHOICETAB+"时间：" + re[0].time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            System.out.println(ITEMTAB+"\t\t----------------------------------------");
        }
        System.out.println("输入任意键返回");
        sc.nextLine();
    }
    public static void soldierRecordView(){
//        调用getRecord(2)方法获取角色历史记录
//        打印 所有角色的 角色历史最高记录
        clear();
        Record[] record = getRecord(2);
        if (record == null) System.out.println(CHOICETAB+"尚无记录");
        else {
            System.out.println(YELLOW+BOLD+ITEMTAB+"\t-------------角色历史最高记录-----------\n"+RESET);
            for (int i = 0; i < record.length; i++)
                System.out.println("\t\t角色：" + Soldier.get(record[i].soldierSyb) + " 最高得分：" + record[i].score + " 最高击杀：" + record[i].killCount + " 时间：" + record[i].time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        }
        System.out.println("输入任意键返回");
        sc.nextLine();
    }
    public static void unitRecordsView(){
//        调用getRecord(1)方法获取每次游戏记录
//        打印 所有 每次游戏记录
        clear();
        Record[] items = getRecord(1);
        if (items == null) System.out.println(CHOICETAB+"尚无记录");
        else {
            System.out.println(YELLOW+"使用角色\t最高通关\t最高积分\t击杀数\t\t时间"+RESET);
            for (Record r : items) {
                String exp ;
                exp = String.format("%s\t\t%d\t\t%d\t\t%d\t%s\n", Soldier.get(r.soldierSyb).name(), r.highestLevel, r.score, r.killCount, r.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                System.out.printf(exp);
            }
        }
        System.out.println("输入任意键返回");
        sc.nextLine();

    }
    public static void potionConsumeView(Potions p){
        System.out.print(YELLOW);
        if(p.health!=0) System.out.printf("当前生命+%.0f ",p.health);
        if(p.mHealth!=0) System.out.printf("最大生命+%.0f ",p.mHealth);
        if(p.attackStrength!=0) System.out.printf("攻击力+%.0f ",p.attackStrength);
        if(p.defence!=0) System.out.printf("防御力+%.0f ",p.defence);
        if(p.escape!=0) System.out.printf("闪避率+%.2f ",p.escape);
        if(p.critRate!=0) System.out.printf("暴击率+%.2f  ",p.critRate);
        if(p.luck!=0) System.out.printf("暴击伤害+%.2f ",p.luck);
        System.out.println(RESET);
    }

    public static void towerView(){
       Tower tower = Game.getTower();
       System.out.println(RED+BOLD+"===================敌人的属性==================");
        System.out.printf("血量:%.1f 攻击:%.1f 防御:%.1f \n",tower.health,tower.attackStrength,tower.defence);
    }

     public static void playerView(){
        Player p = Game.getPlayer();
        System.out.printf(BLUE + BOLD + "===================你的属性====================\n" +GREEN+
                  "血量: %.0f "+LIGHTBLUE+"攻击: %.0f 防御: %.0f 闪避: %.2f 暴击率: %.2f 暴击伤害: %.0f%% "+YELLOW+"金币: %d" + RESET + "\n",
                  p.health, p.attackStrength, p.defence, p.escape, p.critRate, (1 + p.luck) * 100, p.gold);

//       System.out.println(BLUE+BOLD+"===================你的属性====================\n"+"血量:"+p.health+" 攻击:"+p.attackStrength+" 防御:"+p.defence+" 闪避:"+p.escape+" 暴击率:"+p.critRate+" 暴击伤害:"+(int)((1+p.luck)*100)+"% 金币:"+p.gold+RESET);
    }
    //-----------------------打印画面-----------------------------------------
/*

 */
    public static void clear(){
        if (System.getProperty("os.name").contains("Windows")) {
                try {
                 new ProcessBuilder("cmd", "/c", "cls")
                         .inheritIO()
                         .start().waitFor();
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
             }catch (IOException e) {
                 throw new RuntimeException(e);
             }
        }
    }
     public static void endView()  {
        //让程序休眠1秒
         clear();
         try {
             Thread.sleep(1000);
             System.out.println();
             System.out.println(RELINE+YELLOW + BOLD + ITALIC + "\t\t\t\t========================恭喜==============================\n" + RESET);
             Thread.sleep(1500);
             System.out.println(GREEN+"""
                     \t\t\t\t\t\t♪ 你通过了全部关卡! ♪
                     \t\t\t\t\t\tミ ゛ミ ∧＿∧ ミ゛ミ
                     \t\t\t\t\t\tミ ミ ( ・∀・ )ミ゛ミ
                     \t\t\t\t\t\t゛゛ ＼　　　／゛゛
                     \t\t\t\t\t\t　　 　i⌒ヽ ｜
                    　\t\t\t\t\t\t      (＿) ノ
                    """+RESET);
//             System.out.println(RELINE+YELLOW + BOLD + ITALIC + "\t\t\t\t====================你已经通过了全部关卡!===================\n" + RESET);
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t====================制作：BugFirstJava====================\n" + RESET);
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t唐百叶\n"  );
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t黄严\n" );
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t黄仁雁\n");
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t刘昊\n");
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t曾鹏\n");
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t梅鑫宇\n");
             Thread.sleep(1500);
             System.out.println(RELINE+LIGHTBLUE + "\t\t\t\t\t\t\t吴昊霖\n" + RESET);
             Thread.sleep(1500);
             System.out.println(RELINE+YELLOW+"按任意键进入探索模式，尽情享受随机数生成器带来的欢愉吧(^_^)"+RESET);
             sc.nextLine();
         }catch (InterruptedException e){}
    }
    public static void volumeSetView(){
         System.out.println(YELLOW+"\t\t\t\t\t--------------音量设置--------------"+RESET);
         System.out.println("\t\t\t\t\t\t按1.进入菜单音量设置");
         System.out.println("\t\t\t\t\t\t按2.进入游戏音音量设置");
         System.out.println("\t\t\t\t\t\t按3.进入音效音量设置");
    }
    public static void winEmoji(int id) {
        switch (id) {
           case 1-> System.out.println("""
                     ┌────────────┐
                   　│通知：　      │
                   　│你通过了本关!  |
                   　│好厉害呀.     │
                   　 (ﾖ─-∧＿∧─-E)
                   　 ＼（* ´∀｀）／
                   　 　 Y 　　　 Y
                   """);
           case 2-> System.out.println("""
                   ／￣￣￣￣￣￣￣￣￣
                   |　　<哎呀，出口被你找到了!>
                   ＼
                   　￣￣∨￣￣￣￣￣￣
                   　 ∧＿∧
                   　(　・∀・)　
                   　(　 つつヾ
                   　 | ｜ |　进入下一关 0.00001%~
                   　(＿_)＿)
                   """);
           case 3-> System.out.println("""
                   巴拉巴拉巴拉，game win！
                   　 ∧＿∧
                   （｡･ω･｡)つ━☆・*。
                   ⊂　　 ノ 　　　・゜+.
                   　しーＪ　　　°。+ *´¨)
                   　　　 　　.· ´¸.·*´¨) ¸.·*¨)
                   　(¸.·´ (¸.·’*
                   """);
           case 4-> System.out.println("（｡･ω･｡)つ。不愧是你！");
        }
    }
}

