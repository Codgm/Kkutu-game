package org.example;

public class CurrentClient {

  private String name = null;


  public synchronized String getName() {
    return this.name;
  }

  public synchronized void setName(String name) {
    this.name = name;
  }


}
