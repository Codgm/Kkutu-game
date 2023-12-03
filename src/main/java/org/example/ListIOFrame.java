package org.example;

import java.util.ArrayList;

public class ListIOFrame {
  ArrayList<IOFrame> IOframeList=new ArrayList<IOFrame>();
  private static ListIOFrame instance = null;
  public static ListIOFrame getInstance() {
    if (instance == null) {
      instance = new ListIOFrame();
    }
    return instance;
  }
  private ListIOFrame() {}
  public void add(IOFrame frame) {
    IOframeList.add(frame);
  }
//  public void remove(IOFrame frame) {
//    IOfRameList.remove(frame);
//  }
}
