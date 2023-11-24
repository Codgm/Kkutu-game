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
	private String name;

	private CurrentClient currentClient = null;


	
	public WritingThread(Socket socket, String name, CurrentClient currentClient) {
		this.socket = socket;
		this.name = name;
		this.currentClient = currentClient;
	}


	public void run() {
		try {

			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			while(true) {
				String tmp = scanner.nextLine();
				if(name.equals(currentClient.getName())) {
					writer.println(tmp);
				}
				else {
					System.out.println("Not Your Turn");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


}
