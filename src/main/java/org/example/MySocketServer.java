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
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.example.game.Words;

public class MySocketServer extends Thread {

  private static ArrayList<Socket> list = new ArrayList<Socket>();
  private ClientQueue queue = ClientQueue.getInstance();

  private Words wordSetting = Words.getInstance();
  private Socket socket = null;


  private int round;

  private ArrayList<String> words = new ArrayList<String>();

  private String startWord = null;


  public MySocketServer(Socket socket) {
    this.socket = socket;
    list.add(socket);
  }


  public void run() {
    try {
      System.out.println("Server : " + socket.getInetAddress() + " Ip Connected");

      DataBase db = new DataBase("jdbc:postgresql://localhost:5432/kkutudb", "postgres",
          Passwd.getPasswd());

      db.connect();
      wordSetting.setRoundFlag(true);
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));

      OutputStream out = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(out, true);

      writer.println("Enter The ID");

      String readValue;
      String name = null;
      boolean identify = false;

      readValue = reader.readLine();

      while (readValue != null && wordSetting.getRoundFlag()) {
        if(readValue.equals("Game Started")) break;
        System.out.println("Current Client : " + queue.getCurrentClientName());
        //클라이언트가 접속했을때 현재 클라이언트를 알려주는 코드
        for (int i = 0; i < list.size(); i++) {
          out = list.get(i).getOutputStream();
          PrintWriter printWriter = new PrintWriter(out, true);
          printWriter.println("Current Client : " + queue.getCurrentClientName());
        }
        if (!identify) {
          name = readValue;
          identify = true;
          writer.println(name + " Connected");
          queue.addClient(name);
        }
        //초기 방장을 위한 코드
        else if (readValue.equals("Start") && name.equals(queue.getCurrentClientName())) {
          readValue = reader.readLine();
          round = Integer.parseInt(readValue);
          wordSetting.setRoundFlag(false);
          words = db.selectWords(round);
          Random random = new Random();
          int index = random.nextInt(words.size());
          startWord = words.get(index);
          wordSetting.setStartWord(startWord);
          wordSetting.setRound(0);
          wordSetting.setFinalRound(round);
          for (int i = 0; i < list.size(); i++) {
            out = list.get(i).getOutputStream();
            PrintWriter printWriter = new PrintWriter(out, true);
            printWriter.println("Round : " + round);
            printWriter.println("Start Word : " + startWord);
            printWriter.println("Game Started");
          }
          break;
        } else {
          //시작안했으면 채팅모드로.
          for (int i = 0; i < list.size(); i++) {
            out = list.get(i).getOutputStream();
            PrintWriter printWriter = new PrintWriter(out, true);
            printWriter.println(name + " : " + readValue);
          }
        }
        if (!wordSetting.getRoundFlag()) {
          System.out.println("Break");
          break;
        }
        else readValue = reader.readLine();
      }
      for (int i = wordSetting.getRound(); i < wordSetting.getFinalRound();) {
        String currentWord = null;
        char lastChar;
        if(wordSetting.getLastChar() == ' ') {
          lastChar = wordSetting.getStartWord().charAt(i);
          wordSetting.setLastChar(lastChar);
        }
        else {
          lastChar = wordSetting.getLastChar();
        }
        PrintWriter printWriter;
        if (!wordSetting.getIsStart()) {
          queue.getNextClient();
          wordSetting.setIsStart(true);
          for (int j = 0; j < list.size(); j++) {
            out = list.get(j).getOutputStream();
            printWriter = new PrintWriter(out, true);
            printWriter.println("Current Char : " + wordSetting.getLastChar());
          }
        }
        while ((readValue = reader.readLine()) != null) {
          if(readValue.equals("Game Started")) continue;
          //클라이언트가 접속했을때 현재 클라이언트를 알려주는 코드
          //System.out.println("Current Client : " + queue.getCurrentClientName());
          for (int j = 0; j < list.size(); j++) {
            out = list.get(j).getOutputStream();
            printWriter = new PrintWriter(out, true);
            printWriter.println("Current Client : " + queue.getCurrentClientName());
          }
          if (db.select(readValue) && readValue.charAt(0) == wordSetting.getLastChar()) {
            currentWord = readValue;
            lastChar = currentWord.charAt(currentWord.length() - 1);
            wordSetting.setLastChar(lastChar);
            for (int j = 0; j < list.size(); j++) {
              out = list.get(j).getOutputStream();
              printWriter = new PrintWriter(out, true);
              printWriter.println("Correct Word : " + currentWord);
              printWriter.println("Current Char : " + lastChar);
            }
            queue.getNextClient();
          } else {
            for (int j = 0; j < list.size(); j++) {
              out = list.get(j).getOutputStream();
              printWriter = new PrintWriter(out, true);
              printWriter.println("Wrong Word");
            }
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
