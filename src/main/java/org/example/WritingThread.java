package org.example;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WritingThread extends Thread {

  private Socket socket = null;
  private IOFrame frame = null;


  public WritingThread(Socket socket, CurrentClient currentClient, IOFrame frame) {
    this.socket = socket;
    this.frame = frame;
  }


  public void run() {
    try {

      OutputStream out = socket.getOutputStream();
      // Use OutputStreamWriter to send UTF-8 encoded string
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      PrintWriter writer = new PrintWriter(outputStreamWriter, true);
      while (true) {
        //String tmp = scanner.nextLine();
        String tmp = frame.getInputText();
				/* -> Client단 말고 Server단에서 처리하기로 변경..
				if(name.equals(currentClient.getName())) {
					if(tmp.contains(" ")) {
						System.out.println("No Space");
						continue;
					}
					writer.println(tmp);
				}
				else {
					System.out.println("Not Your Turn");
				}
				 */
        writer.println(tmp);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


  }


}
