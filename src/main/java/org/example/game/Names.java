package org.example.game;

import java.util.ArrayList;

public class Names {

  ArrayList<String> names = new ArrayList<>();
  private static Names instance = null;

  public static Names getInstance() {
    if (instance == null) {
      instance = new Names();
    }
    return instance;
  }

  public Names() {}

  public synchronized void add(String name) {
    names.add(name);
  }

  public synchronized void remove(String name) {
    names.remove(name);
  }

  public synchronized ArrayList<String> getNames() {
    return names;
  }

  public synchronized void clear() {
    names.clear();
  }

}
