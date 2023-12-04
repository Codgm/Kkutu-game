package org.example;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class MySocketClient {

  private static final IOFrame frame = new IOFrame("Client");

  public static void main(String[] args) {
    try {
      Socket socket = null;
      socket = new Socket("localhost", 1234);
      frame.setSocket(socket);
      System.out.println("Connected to Server");
      OutputStream out = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(out, true);
      BufferedReader reader = new BufferedReader(
          new java.io.InputStreamReader(socket.getInputStream()));
      Scanner scanner = new Scanner(System.in);
      CurrentClient currentClient = new CurrentClient();
      String message;
      String name = null;
      while ((message = reader.readLine()) != null) {
        //System.out.println(message);
        frame.pushRecordData(message+"\n");
        if (message.equals("Enter The ID")) {
          name = frame.getInputText();
          frame.setFrameTitle(name);
          writer.println(name);
          break;
        }
      }

      ListeningThread t1 = new ListeningThread(socket, currentClient, name, frame);
      WritingThread t2 = new WritingThread(socket, currentClient, frame);
      t1.start();
      t2.start(); // WritingThread Start

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

