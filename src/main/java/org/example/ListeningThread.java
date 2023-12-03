package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ListeningThread extends Thread {

  private Socket socket = null;
  private CurrentClient currentClient;
  private IOFrame frame = null;

  private String name;

  public ListeningThread(Socket socket, CurrentClient currentClient, String name, IOFrame frame) {
    this.socket = socket;
    this.currentClient = currentClient;
    this.name = name;
    this.frame = frame;
  }

  public void run() {
    try {
      InputStream input = socket.getInputStream();
      // Use InputStreamReader to receive UTF-8 encoded string
      InputStreamReader inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
      BufferedReader reader = new BufferedReader(inputStreamReader);

      OutputStream out = socket.getOutputStream();
      // Use OutputStreamWriter to send UTF-8 encoded string
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      PrintWriter writer = new PrintWriter(outputStreamWriter, true);

      while (true) {
        String tmp = reader.readLine();
        if (tmp.contains("Current Client : ")) {
          String name = tmp.substring(17);
          if (this.name.equals(name)) {
            writer.println("Client Ok");
          }
        }
        //쓰레기값을 보내야해서..
        else if (tmp.equals("Wrong Word")) {
          System.out.println("Wrong Word");
          writer.println("Wrong Word");
        } else if (tmp.equals("Start")) {
          writer.println("Start");
        } else if (tmp.equals("Server Ok")) {
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
          //System.out.println(tmp);
          frame.pushRecordData(tmp + "\n");
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
