package org.example;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class WritingThread extends Thread {

  private Socket socket = null;
  private Scanner scanner = new Scanner(System.in);

  private CurrentClient currentClient = null;


  public WritingThread(Socket socket, CurrentClient currentClient) {
    this.socket = socket;
    this.currentClient = currentClient;
  }


  public void run() {
    try {

      OutputStream out = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(out, true);
      while (true) {
        String tmp = scanner.nextLine();
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
