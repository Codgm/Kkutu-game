package org.example.game;

import java.util.TimerTask;

public class TimerEvent extends TimerTask {

  private final int time;
  private int curTime = 0;
  private final boolean isPersonalTimer;
  private final Words words = Words.getInstance();

  public TimerEvent(int time, boolean isPersonalTimer) {
    this.isPersonalTimer = isPersonalTimer;
    this.curTime = time;
    this.time = time;
  }

  @Override
  public void run() {
    if (curTime > 0) {
      curTime--;
      if(!isPersonalTimer) words.setRoundTime(curTime);
      if(isPersonalTimer){
        System.out.println(curTime+"sec left");
      }
    } else {
      words.setIsEnd(true);
      words.setIsTimeOver(true);
      //this.cancel();
      //this.curTime = this.time;
    }
  }

  public void getTime() {
    System.out.println(time);
  }

}
