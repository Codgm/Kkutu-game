package org.example.game;

public class Words {

  private static Words instance = null;

  private boolean roundFlag = true;

  private int finalRound = 0;

  private boolean isEnd = false;
  private boolean isTimeOver = false;

  private int roundTime;
  private int initialRoundTime;

  private Words() {
  }

  public static synchronized Words getInstance() {
    if (instance == null) {
      instance = new Words();
    }
    return instance;
  }

  public synchronized boolean getIsTimeOver() {
    return isTimeOver;
  }//for Debugging

  public synchronized void setIsTimeOver(boolean flag) {
    this.isTimeOver = flag;
  }//for Debugging

  public synchronized boolean getRoundFlag() {
    return this.roundFlag;
  }

  public synchronized void setRoundFlag(boolean roundFlag) {
    this.roundFlag = roundFlag;
  }

  public synchronized int getFinalRound() {
    return this.finalRound;
  }

  public synchronized void setFinalRound(int finalRound) {
    this.finalRound = finalRound;
  }

  public synchronized boolean getIsEnd() {
    return this.isEnd;
  }

  public synchronized void setIsEnd(boolean isEnd) {
    this.isEnd = isEnd;
  }

  public synchronized int getRoundTime() {
    return this.roundTime;
  }

  public synchronized void setRoundTime(int roundTime) {
    this.roundTime = roundTime;
  }

  public synchronized int getInitialRoundTime() {
    return this.initialRoundTime;
  }

  public synchronized void setInitialRoundTime(int initialRoundTime) {
    this.initialRoundTime = initialRoundTime;
  }

  public synchronized void setPersonalTime(int PersonalTime) {
  }

  public void reset() {
    instance = null;
  }
}
