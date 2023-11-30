package org.example.game;

import java.util.TimerTask;

public class TimerEvent extends TimerTask {

  private int time = 0;

  private Words words = Words.getInstance();

  public TimerEvent(int time) {
    this.time = time;
  }

  @Override
  public void run() {
    if (time > 0) {
      time--;
    } else {
      words.setIsEnd(true);
      this.cancel();
    }
  }

  public void getTime() {
    System.out.println(time);
  }

}
