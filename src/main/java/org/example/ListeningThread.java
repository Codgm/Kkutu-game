package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ListeningThread extends Thread {
	Socket socket = null;
	CurrentClient currentClient;

	public ListeningThread(Socket socket, CurrentClient currentClient) {
		this.socket = socket;
		this.currentClient = currentClient;
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			while(true) {
				String tmp = reader.readLine();
				if(tmp.contains("Current Client : ")) {
					String name = tmp.substring(17);
					if(!name.equals(currentClient.getName())) currentClient.setName(name);
				}
				else if(tmp.contains("Game Started")) {
					//확인했다는 메시지를 보냄
					OutputStream out = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(out, true);
					writer.println("Game Started");
				}
				else System.out.println(tmp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
