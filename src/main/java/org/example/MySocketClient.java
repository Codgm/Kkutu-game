package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocketClient {

  public static void make() throws IOException {
    IOFrame frame = new IOFrame("Client");
    Socket socket;
    socket = new Socket("localhost", 1234);
    frame.setSocket(socket);
    System.out.println("Connected to Server");
    OutputStream out = socket.getOutputStream();
    PrintWriter writer = new PrintWriter(out, true);
    BufferedReader reader = new BufferedReader(
        new java.io.InputStreamReader(socket.getInputStream()));
    String message;
    String name = null;
    while ((message = reader.readLine()) != null) {
      //System.out.println(message);
      frame.pushRecordData(message + "\n");
      if (message.equals("Enter The ID")) {
        name = frame.getInputText();
        frame.setFrameTitle(name);
        writer.println(name);
        break;
      }
    }

    ListeningThread t1 = new ListeningThread(socket, name, frame);
    WritingThread t2 = new WritingThread(socket, frame);
    t1.start();
    t2.start(); // WritingThread Start
  }
  public static void main(String[] args) {
    try {
      make();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
