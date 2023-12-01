package org.example;

import java.io.BufferedReader;
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
import org.example.game.Names;
import org.example.game.Passwd;
import org.example.game.TimerEvent;
import org.example.game.Words;

public class MySocketServer extends Thread {

  private static final ArrayList<Socket> list = new ArrayList<Socket>();
  private final ClientQueue queue = ClientQueue.getInstance();

  private final Words wordSetting = Words.getInstance();
  private Socket socket = null;

  private final Names names = Names.getInstance();

  private ArrayList<String> words = new ArrayList<String>();

  private String startWord = null;

  private final DataBase db = new DataBase("jdbc:postgresql://localhost:5432/kkutudb", "postgres",
      Passwd.getPasswd());


  public MySocketServer(Socket socket) {
    this.socket = socket;
    list.add(socket);
  }


  public void run() {
    try {
      db.connect();
      System.out.println("Server : " + socket.getInetAddress() + " Ip Connected");
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
      names.add(name);
      queue.addClient(name);

      writer.println(name + " Connected");

      while (true) {
        Timer timer = new Timer();
        if(wordSetting.getIsEnd()) {
          if(wordSetting.getIsTimeOver()){
            System.out.println("Time Out");
            queue.pollTimerEvent();
          }
          for(int i = 0; i  < list.size(); i++) {
            PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer2.println("Round Ended");
            writer2.println("Loser : " + queue.getCurrentClientName());
          }
          Game game = Game.getInstance();
          if(game.getRound() == wordSetting.getFinalRound()) {
            for(int i = 0; i  < list.size(); i++) {
              PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
              writer2.println("Game Ended");
              writer2.println("Loser : " + queue.getCurrentClientName());
            }
            break;
          }
          else {
            game.updateRound();
            PrintWriter writer2 = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Current Round : " + game.getRound());
            writer2.println("Start");
            //타이머 초기화
            timer.cancel();
            wordSetting.setIsEnd(false);
            continue;
          }
        }
        readValue = reader.readLine();
        if(readValue == null) break;
        System.out.println("Current Client : " + queue.getCurrentClientName());
        //처음 세팅이 아닐때,
        if(readValue.equals("Start") && name.equals(queue.getCurrentClientName()) && wordSetting.getRoundFlag()) {
          Game game = Game.getInstance();
          timer = new Timer();
          TimerEvent timerEvent = new TimerEvent(120,false);
          for(int i = 0; i < list.size(); i++) {
            PrintWriter writer3 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer3.println("Current Round : " + game.getRound());
            writer3.println("Last Char : " + game.getLastChar());
            writer3.println("Game Started");
          }
          writer.println("Server Ok");
          timer.scheduleAtFixedRate(timerEvent, 0, 1000);
        }
        //처음 세팅일때,
        else if(readValue.equals("Start") && name.equals(queue.getCurrentClientName())) {
          PrintWriter writer2 =  new PrintWriter(socket.getOutputStream(), true);
          timer = new Timer();
          Game game = Game.getInstance();
          TimerEvent timerEvent = new TimerEvent(120,false);
          writer2.println("Write a Language");
          readValue = reader.readLine();
          while (!readValue.equals("ko") && !readValue.equals("en")) {
            writer2.println("Error. Write a Language");
            readValue = reader.readLine();
          }
          String language = readValue;
          game.setLanguage(language);
          writer2.println("Write a Option(1. injeong(Only Korean), 2. manner)");
          String option = reader.readLine();
          if(language.equals("en") && option.split(" ")[0].equals("1")) {
            writer2.println("Injeong only support Korean");
          }
          else game.setInjeong(option.split(" ")[0].equals("1"));
          game.setManner(option.split(" ")[1].equals("1"));
          writer2.println("Write a Round");
          String round = reader.readLine();
          wordSetting.setFinalRound(Integer.parseInt(round));
          wordSetting.setRoundFlag(true);
          Random random = new Random();
          words = db.selectWords(wordSetting.getFinalRound(), game.getLanguage(), game.getInjeong());
          startWord = words.get(random.nextInt(words.size()));
          game.setStartWord(startWord);
          game.setCurrentWord(startWord);
          game.setRound(1);
          game.setLastChar(startWord.charAt(0));
          for(int i = 0; i < list.size(); i++) {
            PrintWriter writer3 = new PrintWriter(list.get(i).getOutputStream(), true);
            writer3.println("Set Round : " + wordSetting.getFinalRound());
            writer3.println("Current Round : " + game.getRound());
            writer3.println("Round Word : " + game.getStartWord());
            writer3.println("Last Char : " + game.getLastChar());
            writer3.println("Game Started");
          }
          writer.println("Server Ok");
          timer.scheduleAtFixedRate(timerEvent, 0, 1000);
        }
        else if(wordSetting.getRoundFlag() && name.equals(queue.getCurrentClientName())) {
          if(readValue.equals("Client Ok")) {
            Timer timer2 = new Timer();
            //TimerEvent personalTimerEvent = new TimerEvent(10,true);
            timer2.scheduleAtFixedRate(queue.getTimerEvent(), 0, 1000);
          }
          readValue = reader.readLine();
          if(wordSetting.getIsEnd()) {
            continue;
          }
          Game game = Game.getInstance();
          if(game.check(readValue)) {
            queue.pollTimerEvent();
            for(int i = 0; i  < list.size(); i++) {
              PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
              writer2.println("Correct Word : " + readValue + "\nLast Char : " + game.getLastChar());
              writer2.println("Current Client : " + queue.getCurrentClientName());
            }
          }
          else {
            //쓰레기값을 보내야 while문에서 readline을 받은후에 해당문으로 다시 돌아올 수 있음.
            writer.println("Wrong Word");
            for(int i = 0; i  < list.size(); i++) {
              PrintWriter writer2 = new PrintWriter(list.get(i).getOutputStream(), true);
              writer2.println(name + " : " + readValue);
            }
          }

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
