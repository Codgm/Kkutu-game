package org.example.game;

import java.io.Serializable;

public class TimerEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private String currentWord;
  private int remainingTimeInSeconds;

  public TimerEvent(String currentWord, int remainingTimeInSeconds) {
    this.currentWord = currentWord;
    this.remainingTimeInSeconds = remainingTimeInSeconds;
  }

  public String getCurrentWord() {
    return currentWord;
  }

  public int getRemainingTimeInSeconds() {
    return remainingTimeInSeconds;
  }
}