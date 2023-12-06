package org.example;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.example.game.Names;
import org.example.game.TimeSet;
import org.example.game.TimerEvent;
import org.example.game.Words;

public class ClientQueue {

  private static ClientQueue instance = null;
  private final Words words = Words.getInstance();
  private final Names names = Names.getInstance();
  private final Queue<String> queue = new LinkedList<>();
  private final Queue<TimerEvent> personalTimerEvent = new LinkedList<>();
  private volatile String currentClientName = null;


  private ClientQueue() {
  }

  public synchronized static ClientQueue getInstance() {
    if (instance == null) {
      instance = new ClientQueue();
    }
    return instance;
  }

  public void addClient(String name) {
    queue.add(name);
    if (queue.size() == 1) {
      currentClientName = queue.peek();
    }
  }

  public void addTimer(ArrayList<Socket> list) {
    for (String name : names.getNames()) {
      personalTimerEvent.add(new TimerEvent(TimeSet.timeSet(words.getRoundTime()), true, list));
    }
  }

  public void getNextClient() {
    queue.add(currentClientName);
    queue.poll();
    currentClientName = queue.peek();
    System.out.println("timerAdded-" + personalTimerEvent.size() + "left");//Debugging

  }

  public TimerEvent getTimerEvent() {
    return personalTimerEvent.peek();
  }

  public synchronized void pollTimerEvent(ArrayList<Socket> list) {
    personalTimerEvent.add(new TimerEvent(TimeSet.timeSet(words.getRoundTime()), true, list));
    if (personalTimerEvent.peek() != null) {
      personalTimerEvent.peek().cancel();
    }
    personalTimerEvent.poll();
  }

  public String getCurrentClientName() {
    return currentClientName;
  }
}
