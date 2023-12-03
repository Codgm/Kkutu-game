package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ListeningThread extends Thread {

  private Socket socket = null;
  private CurrentClient currentClient;

  private String name;

  public ListeningThread(Socket socket, CurrentClient currentClient, String name) {
    this.socket = socket;
    this.currentClient = currentClient;
    this.name = name;
  }

  public void run() {
    try {
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));

      while (true) {
        String tmp = reader.readLine();
        if (tmp.contains("Current Client : ")) {
          String name = tmp.substring(17);
          if (this.name.equals(name)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Client Ok");
          }
        }
        //쓰레기값을 보내야해서..
        else if (tmp.equals("Wrong Word")) {
          System.out.println("Wrong Word");
          PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
          writer.println("Wrong Word");
        } else if (tmp.equals("Start")) {
          OutputStream out = socket.getOutputStream();
          PrintWriter writer = new PrintWriter(out, true);
          writer.println("Start");
        } else if (tmp.equals("Server Ok")) {
          OutputStream out = socket.getOutputStream();
          PrintWriter writer = new PrintWriter(out, true);
          writer.println("Client Ok");
        }
				/*
				else if(tmp.contains("Game Started")) {
					//확인했다는 메시지를 보냄
					OutputStream out = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);
					writer.println("Game Started");
				}
				 */
        else {
          System.out.println(tmp);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
