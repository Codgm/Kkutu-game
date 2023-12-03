package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import org.example.game.TimerEvent;

public class ClientQueue {
    private static ClientQueue instance = null;
    private volatile Queue<String> queue = new LinkedList<>();
    private volatile String currentClientName = null;
    private Queue<TimerEvent> personalTimerEvent=new LinkedList<>();

    private ClientQueue() {}

    public synchronized static ClientQueue getInstance() {
        if (instance == null) {
            instance = new ClientQueue();
        }
        return instance;
    }

    public void addClient(String name) {
        queue.add(name);
        personalTimerEvent.add(new TimerEvent(10,true));
        System.out.println("timerAdded-"+personalTimerEvent.size()+"left");//Debugging
        if (queue.size() == 1) {
            currentClientName = queue.peek();
        }
    }

    public void getNextClient() {
        queue.add(currentClientName);
        personalTimerEvent.add(new TimerEvent(10,true));
        queue.poll();
        currentClientName = queue.peek();
        System.out.println("timerAdded-"+personalTimerEvent.size()+"left");//Debugging

    }
    public TimerEvent getTimerEvent(){
        return personalTimerEvent.peek();
    }
    public synchronized void pollTimerEvent(){
        personalTimerEvent.add(new TimerEvent(10,true));
        personalTimerEvent.peek().cancel();
        personalTimerEvent.poll();
    }

    public String getCurrentClientName() {
        return currentClientName;
    }
}