package com.BugFirstJava.Service;

import com.BugFirstJava.Dao.Record.Record;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

class AudioManager {
    // 音频控制类
    //bgm的路径
    Clip music;
    /*
    编号：30
    难度：简单
    负责人：黄严
    功能：构造函数，加载音频文件

     */
    public AudioManager(String soundFilePath) {
        try {
            InputStream inputStream = AudioManager.class.getClassLoader().getResourceAsStream(soundFilePath);
            if (inputStream == null) {
                throw new RuntimeException("音频文件未找到: " + soundFilePath);
            }
            BufferedInputStream inputStream1 = new BufferedInputStream(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream1);
            music = AudioSystem.getClip();
            music.open(audioInputStream);

            //加载菜单音乐
        }
            catch (UnsupportedAudioFileException e) {
            System.err.println("音频加载出错!-" + soundFilePath);
             try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
            System.err.println("音频加载出错!-" + soundFilePath);
            try {
                 Thread.sleep(1500);
             } catch (InterruptedException ex) {
                 throw new RuntimeException(ex);
             }
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("音频加载出错!-" + soundFilePath);
             try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
    /*
    编号：31
    难度：简单
    负责人：拓展，待定
     功能：设置音量
     参数：音量值，范围为0-100
     */
    public boolean setVolume(int volume) {
        if (music != null && volume >= 0 && volume <= 100) {
            FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (volume == 0) ? -80.0f : (20 * (float) Math.log10(volume / 100.0));
            gainControl.setValue(dB);
            return true;
        }
        return false;
    }
    /*
    编号：32
    难度：简单
    负责人：拓展，待定
     功能：播放音频
     */
    public void play(boolean isLoop) {
        if (music != null) {
            // 如果音频没有运行，则开始播放
            if (!music.isRunning()) {
                // 选择是否重置到起始位置
                music.setFramePosition(0); // 重置到起始位置
                music.start();// 开始播放
                if (isLoop)
                    music.loop(Clip.LOOP_CONTINUOUSLY);// 循环播放
            }
        }
    }
    /*
    编号：33
    难度：拓展，待定
    负责人：
     功能：停止播放音频
     */
    public void stopPlay(){
           if (music != null&&music.isRunning()) {
                   music.stop(); // 停止播放
                   music.setFramePosition(0); // 重置到起始位置
           }
    }
    public boolean setLoop(boolean isLoop){
        if (music!= null) {
            if (music.isRunning()) {
                if (isLoop)
                    music.loop(Clip.LOOP_CONTINUOUSLY);// 循环播放
                else
                    music.loop(0);
                return true;
            }
        }
        return false;
    }
     /*
    难度：简单
    功能：播放菜单音乐
     */

    /*
    难度：简单
    功能：停止播放菜单音乐
     */
}
