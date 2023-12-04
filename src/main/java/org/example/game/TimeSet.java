package org.example.game;

public class TimeSet {

  public static int timeSet(int leftTime) {
    if(leftTime >= 95) {
      return 15;
    }
    else if(leftTime >= 81) {
      return 13;
    }
    else if(leftTime >= 68) {
      return 12;
    }
    else if(leftTime >= 56) {
      return 10;
    }
    else if(leftTime >= 45) {
      return 9;
    }
    else if(leftTime >= 35) {
      return 8;
    }
    else if(leftTime >= 26) {
      return 6;
    }
    else if(leftTime >= 18) {
      return 5;
    }
    else if(leftTime >= 11) {
      return 3;
    }
    else if(leftTime >= 5) {
      return 2;
    }
    else {
      return 1;
    }
  }

}
