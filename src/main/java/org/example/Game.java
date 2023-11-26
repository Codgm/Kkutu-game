package org.example;

import java.util.ArrayList;

public class Game {

  private static Game instance = null;

  private String currentWord = null;

  private char lastChar = ' ';

  private final ArrayList<String> words = new ArrayList<>();

  private final DataBase db = new DataBase("jdbc:postgresql://localhost:5432/kkutudb", "postgres",
      Passwd.getPasswd());

  private final ClientQueue queue = ClientQueue.getInstance();

  public static synchronized Game getInstance() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }

  private Game() {
    db.connect();
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

  public synchronized boolean check(String tmp) {
    if(tmp.charAt(0) == getLastChar() && db.select(tmp) && !words.contains(tmp)) {
      words.add(tmp);
      setCurrentWord(tmp);
      setLastChar(tmp.charAt(tmp.length() - 1));
      queue.getNextClient();
      return true;
    }
    else {
      return false;
    }
  }

}
