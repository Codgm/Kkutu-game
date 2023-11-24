package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class MySocketClient {


  public static void main(String[] args) {
    try {
      Socket socket = null;
      socket = new Socket("localhost", 1234);
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
				System.out.println(message);
				if(message.equals("Enter The ID")) {
					name = scanner.nextLine();
					writer.println(name);
					break;
				}
      }

      ListeningThread t1 = new ListeningThread(socket, currentClient);
			WritingThread t2 = new WritingThread(socket, name, currentClient);
			t1.start();
			t2.start(); // WritingThread Start

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

