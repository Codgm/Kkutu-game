package org.example.game;

import java.util.ArrayList;
import java.util.Random;

public class Mission {

  private final ArrayList<String> missionList = new ArrayList<>();
  Random random = new Random();
  private int indexNum;
  private String missionWord;

  public Mission() {
    initMissionList();
  }

  public String getMissionWord() {
    return missionWord;
  }

  public void setMissionWord(String missionWord) {
    this.missionWord = missionWord;
  }

  public int getIndexNum() {
    return indexNum;
  }

  private void initMissionList() {
    this.missionList.add("가");
    this.missionList.add("나");
    this.missionList.add("다");
    this.missionList.add("라");
    this.missionList.add("마");
    this.missionList.add("바");
    this.missionList.add("사");
    this.missionList.add("아");
    this.missionList.add("자");
    this.missionList.add("차");
    this.missionList.add("카");
    this.missionList.add("타");
    this.missionList.add("파");
    this.missionList.add("하");
  }

  public void makeRandomMissionChar() {
    indexNum = random.nextInt(14);
    setMissionWord(missionList.get(getIndexNum()));
  }

}
