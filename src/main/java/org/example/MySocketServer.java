package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import org.example.game.TimerEvent;
import org.example.game.Words;

public class MySocketServer extends Thread {

  private static ArrayList<Socket> list = new ArrayList<Socket>();
  private ClientQueue queue = ClientQueue.getInstance();

  private Words wordSetting = Words.getInstance();
  private Socket socket = null;


  private ArrayList<String> words = new ArrayList<String>();

  private String startWord = null;

  private DataBase db = new DataBase("jdbc:postgresql://localhost:5432/kkutudb", "postgres",
      Passwd.getPasswd());


  public MySocketServer(Socket socket) {
    this.socket = socket;
    list.add(socket);
  }


  public void run() {
    try {
      System.out.println("Server : " + socket.getInetAddress() + " Ip Connected");
      db.connect();
      wordSetting.setRoundFlag(false);
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      OutputStream out = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(out, true);

      writer.println("Enter The ID");

      String readValue;
      String name = null;
      boolean identify = false;

      readValue = reader.readLine();
      name = readValue;
      queue.addClient(name);

      writer.println(name + " Connected");

      while ((readValue = reader.readLine()) != null) {
        Timer timer = new Timer();
        TimerEvent timerEvent = new TimerEvent(60);
        if(wordSetting.getIsEnd()) {
          for(int i = 0; i  < list.size(); i++) {
            PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer2.println("Game Ended");
            writer2.println("Loser : " + queue.getCurrentClientName());
          }
          break;
        }
        System.out.println("Current Client : " + queue.getCurrentClientName());
        if(wordSetting.getRoundFlag() && name.equals(queue.getCurrentClientName())) {
          for(int i = 0; i  < list.size(); i++) {
            PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer2.println("Current Client : " + queue.getCurrentClientName());
          }
          Game game = Game.getInstance();
          if(game.check(readValue)) {
            for(int i = 0; i  < list.size(); i++) {
              PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
              writer2.println("Correct Word : " + readValue + "\nLast Word : " + game.getLastChar());
            }
          }
          else {
            for(int i = 0; i  < list.size(); i++) {
              PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
              writer2.println("Wrong Word");
            }
          }
        } else if(readValue.equals("Start") && name.equals(queue.getCurrentClientName())) {
          PrintWriter writer2 =  new PrintWriter(socket.getOutputStream(), true);
          writer2.println("Write a Round");
          String round = reader.readLine();
          wordSetting.setFinalRound(Integer.parseInt(round));
          wordSetting.setRoundFlag(true);
          Random random = new Random();
          words = db.selectWords(wordSetting.getFinalRound());
          startWord = words.get(random.nextInt(words.size()));
          wordSetting.setStartWord(startWord);
          Game game = Game.getInstance();
          game.setCurrentWord(startWord);
          game.setLastChar(startWord.charAt(0));
          for(int i = 0; i < list.size(); i++) {
            PrintWriter writer3 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer3.println("Round : " + wordSetting.getFinalRound());
            writer3.println("Start Word : " + wordSetting.getStartWord());
            writer3.println("Game Started");
          }
          timer.scheduleAtFixedRate(timerEvent, 0, 1000);
        }
        else {
          //내 차례가 아닐때에는 채팅 모드로..
          for(int i = 0; i  < list.size(); i++) {
            PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer2.println(name + " : " + readValue);
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    try {
      int socketPort = 1234;
      ServerSocket serverSocket = new ServerSocket(socketPort);
      System.out.println("socket : " + socketPort + " open Server");

      while (true) {
        Socket socketUser = serverSocket.accept();
        Thread thd = new MySocketServer(socketUser);
        thd.start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
