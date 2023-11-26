package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class ClientQueue {
    private static ClientQueue instance = null;
    private volatile Queue<String> queue = new LinkedList<>();
    private volatile String currentClientName = null;

    private ClientQueue() {}

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

    public void getNextClient() {
        queue.add(currentClientName);
        queue.poll();
        currentClientName = queue.peek();
    }

    public String getCurrentClientName() {
        return currentClientName;
    }
}