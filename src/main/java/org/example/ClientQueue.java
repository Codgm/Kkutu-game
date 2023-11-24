package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class ClientQueue {
    private static ClientQueue instance = null;
    private Queue<String> queue = new LinkedList<>();
    private String currentClientName = null;

    private ClientQueue() {}

    public static synchronized ClientQueue getInstance() {
        if (instance == null) {
            instance = new ClientQueue();
        }
        return instance;
    }

    public synchronized void addClient(String name) {
        queue.add(name);
        if (queue.size() == 1) {
            currentClientName = queue.peek();
        }
    }

    public synchronized void getNextClient() {
        queue.add(currentClientName);
        queue.poll();
        currentClientName = queue.peek();
    }

    public synchronized String getCurrentClientName() {
        return currentClientName;
    }
}