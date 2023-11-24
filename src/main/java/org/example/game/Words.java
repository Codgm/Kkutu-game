package org.example.game;

public class Words {

  private static Words instance = null;
  private String currentWord = null;
  private char lastChar = ' ';

  private boolean roundFlag = true;

  private String startWord = null;

  private boolean isStart = false;

  private int round = 0;

  private int finalRound = 0;


  public static synchronized Words getInstance() {
    if (instance == null) {
      instance = new Words();
    }
    return instance;
  }


  private Words() {
  }

  public synchronized String getCurrentWord() {
    return this.currentWord;
  }

  public synchronized void setCurrentWord(String currentWord) {
    this.currentWord = currentWord;
  }

  public synchronized char getLastChar() {
    return this.lastChar;
  }

  public synchronized void setLastChar(char lastChar) {
    this.lastChar = lastChar;
  }

  public synchronized boolean getRoundFlag() {
    return this.roundFlag;
  }

  public synchronized void setRoundFlag(boolean roundFlag) {
    this.roundFlag = roundFlag;
  }

  public synchronized String getStartWord() {
    return this.startWord;
  }

  public synchronized void setStartWord(String startWord) {
    this.startWord = startWord;
  }

  public synchronized boolean getIsStart() {
    return this.isStart;
  }

  public synchronized void setIsStart(boolean isStart) {
    this.isStart = isStart;
  }

  public synchronized int getRound() {
    return this.round;
  }

  public synchronized void setRound(int round) {
    this.round = round;
  }

  public synchronized int getFinalRound() {
    return this.finalRound;
  }

  public synchronized void setFinalRound(int finalRound) {
    this.finalRound = finalRound;
  }

}
