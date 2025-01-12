package com.BugFirstJava.Service;

import com.BugFirstJava.Dao.Record.Record;
import com.BugFirstJava.Dao.Record.Save;

import java.io.*;
import java.util.ArrayList;

public class InitRecordFile {
    static File unitRecordFile = new File("D:\\BugFirstJava\\Records\\unitRecord.re");
    static File soldierRecordFile = new File("D:\\BugFirstJava\\Records\\soldierRecord.re");
    static File playerRecordFile = new File("D:\\BugFirstJava\\Records\\playerRecord.re");
    static File gameSaveFile = new File("D:\\BugFirstJava\\Records\\latestSave.sl");

   public static boolean verifyRecords(){
       return unitRecordFile.exists() && soldierRecordFile.exists() && playerRecordFile.exists();
   }
    /*
    将Record记录清零
     */
    public static void init() {
        try {
            createRecordFile();
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(unitRecordFile, false));
            ObjectOutputStream o3 = new ObjectOutputStream(new FileOutputStream(playerRecordFile, false));
            ObjectOutputStream o2 = new ObjectOutputStream(new FileOutputStream(soldierRecordFile, false));
            ObjectOutputStream o4 = new ObjectOutputStream(new FileOutputStream(gameSaveFile, false));
            o.writeObject(new ArrayList<Record>());
            o3.writeObject(new ArrayList<Record>());
            o2.writeObject(new ArrayList<Record>());
            o4.writeObject(new ArrayList<Save>());
            o.close();
            o3.close();
            o2.close();
        } catch (IOException e) {
            System.err.println("记录文件初始化失败TAT");
            e.printStackTrace();
        }
    }
    public static void initSave(){
        try {
            gameSaveFile.delete();
            boolean success = gameSaveFile.createNewFile();
           if(success) {
               ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(gameSaveFile, false));
               o.writeObject(new ArrayList<Save>());
               o.close();
           }
           else {
               System.err.println("存档文件初始化失败TAT");
           }
        } catch (IOException e) {
            System.err.println("存档文件初始化失败TAT");
            e.printStackTrace();
        }
    }

    public static void createRecordFile() {
        boolean success = false;
        try {

            File dir = unitRecordFile.getParentFile();
            if (!dir.exists())dir.mkdirs();
            success =unitRecordFile.delete();
            success=unitRecordFile.createNewFile();
            success=soldierRecordFile.delete();
            success=soldierRecordFile.createNewFile();
            success=playerRecordFile.delete();
            success=playerRecordFile.createNewFile();
            success=gameSaveFile.delete();
            success=gameSaveFile.createNewFile();
            if(!success) throw new IOException();
        } catch (IOException e) {
            System.err.println("记录文件创建失败");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void write(File file, ArrayList<Record> s, boolean isAppend) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file, isAppend));
            o.writeObject(s);
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static ArrayList<Record> readRecord(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            try {
                ArrayList<Record> s = (ArrayList<Record>) in.readObject();
                System.out.println(s);
                in.close();
                return s;
            } catch (ClassNotFoundException e) {
                in.close();
                System.out.println("errer");
                return null;
            }
        } catch (IOException e) {

            System.out.println("errer");
            return null;
        }
    }
}
