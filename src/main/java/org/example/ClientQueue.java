package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import org.example.game.TimeSet;
import org.example.game.TimerEvent;
import org.example.game.Words;

public class ClientQueue {
    private static ClientQueue instance = null;
    private volatile Queue<String> queue = new LinkedList<>();
    private volatile String currentClientName = null;
    private Queue<TimerEvent> personalTimerEvent = new LinkedList<>();

    private final Words words = Words.getInstance();



    private ClientQueue() {}

    public synchronized static ClientQueue getInstance() {
        if (instance == null) {
            instance = new ClientQueue();
        }
        return instance;
    }

    public void addClient(String name) {
        queue.add(name);
        personalTimerEvent.add(new TimerEvent(TimeSet.timeSet(15),true));
        System.out.println("timerAdded-"+personalTimerEvent.size()+"left");//Debugging
        if (queue.size() == 1) {
            currentClientName = queue.peek();
        }
    }

    public void getNextClient() {
        queue.add(currentClientName);
        queue.poll();
        currentClientName = queue.peek();
        System.out.println("timerAdded-"+personalTimerEvent.size()+"left");//Debugging

    }
    public TimerEvent getTimerEvent(){
        return personalTimerEvent.peek();
    }
    public synchronized void pollTimerEvent(){
        personalTimerEvent.add(new TimerEvent(TimeSet.timeSet(words.getRoundTime()),true));
        personalTimerEvent.peek().cancel();
        personalTimerEvent.poll();
    }

    public String getCurrentClientName() {
        return currentClientName;
    }
}