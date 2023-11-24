package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
				else System.out.println(tmp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
