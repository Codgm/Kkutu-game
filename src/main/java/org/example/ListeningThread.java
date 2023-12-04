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
        } else if (tmp.contains("turn : ")) {
          System.out.println("Client-listening:" + tmp.substring(7));
          System.out.println("frame name:"+frame.getUserName());
          if (tmp.substring(7).equals(frame.getUserName())) {
            frame.setTurnStateValue(1);
          } else {
            frame.setTurnStateValue(2);
          }
        }
        //쓰레기값을 보내야해서..
        else if (tmp.equals("Wrong Word")) {
          System.out.println("Wrong Word");
          frame.pushRecordData("Wrong Word\n");
          writer.println("Wrong Word");
        } else if (tmp.equals("Start")) {
          writer.println("Start");
        } else if (tmp.equals("Server Ok")) {
          writer.println("Client Ok");
        } else if (tmp.contains("Personal Timer: ")) {
          if (tmp.substring(16).equals("0")) {
            frame.setPersonalLeftTime(0);//남은 시간이 1로 뜨는 경우가 있음
            frame.clearRecordDate();
            frame.pushRecordData("Round time Over\n");
            frame.pushRecordData("if you wanna continue, type anything\n");
            frame.setIsRoundEnd(true);
          }
          if (!(frame.getIsRoundEnd())) {
            frame.setPersonalLeftTime(Integer.parseInt(tmp.substring(16)));
          }
        } else if (tmp.contains("Round Timer: ")) {
          if (tmp.substring(13).equals("0")) {
            frame.setRoundLeftTime(0);//남은 시간이 1로 뜨는 경우가 있음
            frame.clearRecordDate();
            frame.pushRecordData("Round time Over\n");
            frame.pushRecordData("if you wanna continue, type anything\n");
            frame.setIsRoundEnd(true);
          }
          if (!(frame.getIsRoundEnd())) {
            frame.setRoundLeftTime(Integer.parseInt(tmp.substring(13)));
          }
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
          System.out.println(tmp);//for Debugging
          frame.pushRecordData(tmp + "\n");
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
