package org.example.game;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TimerTask;

public class TimerEvent extends TimerTask {

  private final int time;
  private final boolean isPersonalTimer;
  private final Words words = Words.getInstance();
  private int curTime;
  private final ArrayList<Socket> list;

  public TimerEvent(int time, boolean isPersonalTimer, ArrayList<Socket> list) {
    this.isPersonalTimer = isPersonalTimer;
    this.curTime = time;
    this.time = time;
    this.list = list;
  }

  @Override
  public void run() {
    try {
      sendLeftTime(list, isPersonalTimer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (!isPersonalTimer) {
      if (words.getIsEnd()) {
        return;
      }
    }
    if (curTime > 0) {
      curTime--;
      if (!isPersonalTimer) {
        words.setRoundTime(curTime);
      } else {
        words.setPersonalTime();
        System.out.println(curTime + "sec left");


      }
    } else {
      words.setIsEnd(true);
      words.setIsTimeOver(true);
      this.cancel();
      //this.curTime = this.time;
    }
  }

  public int getRemainTime() {
    return time - curTime;
  }

  private void sendLeftTime(ArrayList<Socket> list, boolean isPersonalTimer) throws IOException {
    for (Socket socket : list) {
      OutputStream outputStream3 = socket.getOutputStream();
      OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream3,
          StandardCharsets.UTF_8);
      PrintWriter writer3 = new PrintWriter(outputStreamWriter3, true);
      System.out.println(curTime);//for Debugging
      if (isPersonalTimer) {
        writer3.println("Personal Timer: " + curTime);
      } else {
        writer3.println("Round Timer: " + curTime);
      }
    }
  }
}
