package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import javax.lang.model.type.ArrayType;
import org.example.game.Mission;
import org.example.game.Names;
import org.example.game.Passwd;
import org.example.game.TimerEvent;
import org.example.game.Words;

public class MySocketServer extends Thread {
  private static TimerEvent timerEvent;

  private static final ArrayList<Socket> list = new ArrayList<>();
  private final ClientQueue queue = ClientQueue.getInstance();

  private final Words wordSetting = Words.getInstance();
  private final Names names = Names.getInstance();
  private final DataBase db = new DataBase(
      Passwd.getPasswd());
  private final Socket socket;


  public MySocketServer(Socket socket) {
    this.socket = socket;
    list.add(socket);
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

  public void run() {
    try {
      db.connect();
      System.out.println("Server : " + socket.getInetAddress() + " Ip Connected");
      wordSetting.setRoundFlag(false);
      InputStream inputStream = socket.getInputStream();
      // Use InputStreamReader to receive UTF-8 encoded string
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
          StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      OutputStream outputStream = socket.getOutputStream();
      // Use OutputStreamWriter to send UTF-8 encoded string
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,
          StandardCharsets.UTF_8);
      PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
      printWriter.println("Enter The ID");

      String readValue;
      String name;

      readValue = bufferedReader.readLine();
      name = readValue;
      names.add(name);
      queue.addClient(name);
      for (Socket element : list) {//name보다 list가 먼저 add되기 때문에 ㄱㅊ
        OutputStream outputStream2 = element.getOutputStream();
        OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
            StandardCharsets.UTF_8);
        PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
        for (int j = 0; j < names.getNames().size(); j++) {//IOFrame이 저장하는 client 정보는 Map이라 중복 삭제해줌
          printWriter2.println("New Client " + names.getNames().get(j));
        }
      }

      printWriter.println(name + " Connected");

      while (true) {
        Timer timer = new Timer();
        Mission mission = new Mission();
        if (wordSetting.getIsEnd()) {
          if (wordSetting.getIsTimeOver()) {
            System.out.println("Time Out");
            queue.pollTimerEvent(list);
          }
          for (Socket item : list) {
            OutputStream outputStream2 = item.getOutputStream();
            // Use OutputStreamWriter to send UTF-8 encoded string
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                StandardCharsets.UTF_8);
            PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
            printWriter2.println("Round Ended");
            printWriter2.println("Loser : " + queue.getCurrentClientName());
          }
          Game game = Game.getInstance();
          if (game.getRound() == wordSetting.getFinalRound()) {
            for (Socket value : list) {
              OutputStream outputStream2 = value.getOutputStream();
              // Use OutputStreamWriter to send UTF-8 encoded string
              OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                  StandardCharsets.UTF_8);
              PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
              printWriter2.println("Game Ended");
              printWriter2.println("Loser : " + queue.getCurrentClientName());
              if(timerEvent!=null)timerEvent.cancel();
            }
            wordSetting.setIsTimeOver(false);
            wordSetting.setIsEnd(false);
            break;
          } else {
            game.updateRound();
            OutputStream outputStream2 = socket.getOutputStream();
            // Use OutputStreamWriter to send UTF-8 encoded string
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                StandardCharsets.UTF_8);
            PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
            System.out.println("Current Round : " + game.getRound());
            printWriter2.println("Start");
            //타이머 초기화
            timer.cancel();
            wordSetting.setIsEnd(false);
            continue;
          }
        }
        readValue = bufferedReader.readLine();
        if (readValue == null) {
          break;
        }
        System.out.println("Current Client : " + queue.getCurrentClientName());
        for (Socket item : list) {
          OutputStream outputStream3 = item.getOutputStream();
          OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream3,
              StandardCharsets.UTF_8);
          PrintWriter printWriter3 = new PrintWriter(outputStreamWriter3, true);
          printWriter3.println("turn : " + queue.getCurrentClientName());
        }
        if(readValue.equals("Reset")) {
          Game game = Game.getInstance();
          ArrayList<Socket> temp=new ArrayList<>(list);
          game.reset();
          queue.reset();
          wordSetting.reset();
          if(timerEvent != null) timerEvent.cancel();
          timerEvent=null;
          timer.cancel();
          names.reset();
          list.clear();
          for (Socket value : temp) {
            OutputStream outputStream2 = value.getOutputStream();
            // Use OutputStreamWriter to send UTF-8 encoded string
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                StandardCharsets.UTF_8);
            PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
            printWriter2.println("Reset");
          }
        }
        //처음 세팅이 아닐때,
        else if (readValue.equals("Start") && name.equals(queue.getCurrentClientName())
            && wordSetting.getRoundFlag()) {
          Game game = Game.getInstance();
          timer = new Timer();
          wordSetting.setRoundTime(wordSetting.getInitialRoundTime());
          if(timerEvent != null) timerEvent.cancel();
          timerEvent = new TimerEvent(wordSetting.getInitialRoundTime(), false, list);
          for (Socket value : list) {
            OutputStream outputStream2 = value.getOutputStream();
            // Use OutputStreamWriter to send UTF-8 encoded string
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                StandardCharsets.UTF_8);
            PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
            printWriter2.println("Current Round : " + game.getRound());
            printWriter2.println("Last Char : " + game.getLastChar());
            printWriter2.println("Game Started");
          }
          printWriter.println("Server Ok");
          timer.scheduleAtFixedRate(timerEvent, 0, 1000);
        }
        //처음 세팅일때,
        else if (readValue.equals("Start") && name.equals(queue.getCurrentClientName())) {
          OutputStream outputStream2 = socket.getOutputStream();
          // Use OutputStreamWriter to send UTF-8 encoded string
          OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
              StandardCharsets.UTF_8);
          PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2, true);
          timer = new Timer();
          Game game = Game.getInstance();
          printWriter2.println("Write a Language");
          readValue = bufferedReader.readLine();
          while (!readValue.equals("ko") && !readValue.equals("en")) {
            printWriter2.println("Error. Write a Language");
            readValue = bufferedReader.readLine();
          }
          String language = readValue;
          game.setLanguage(language);
          printWriter2.println("Write a Option(Injeong) : 1. Injeong 0. Not Injeong");
          String option = bufferedReader.readLine();
          game.setInjeong(option.equals("1"));
          printWriter2.println("Write a RoundTime");
          int roundTime = Integer.parseInt(bufferedReader.readLine());
          wordSetting.setRoundTime(roundTime);
          wordSetting.setInitialRoundTime(roundTime);
          timerEvent = new TimerEvent(roundTime, false, list);
          printWriter2.println("Write a Round");
          String round = bufferedReader.readLine();
          wordSetting.setFinalRound(Integer.parseInt(round));
          wordSetting.setRoundFlag(true);
          Random random = new Random();
          ArrayList<String> words= db.selectWords(wordSetting.getFinalRound(), game.getLanguage()
          );
          String startWord= words.get(random.nextInt(words.size()));
          while(startWord.matches(".*\\d+.*")){
            startWord = words.get(random.nextInt(words.size()));
          }
          game.setStartWord(startWord);
          game.setCurrentWord();
          game.setRound(1);
          game.setLastChar(startWord.charAt(0));
          queue.addTimer(list);
          for (Socket value : list) {
            OutputStream outputStream3 = value.getOutputStream();
            // Use OutputStreamWriter to send UTF-8 encoded string
            OutputStreamWriter outputStreamWriter3 = new OutputStreamWriter(outputStream3,
                StandardCharsets.UTF_8);
            PrintWriter writer3 = new PrintWriter(outputStreamWriter3, true);
            writer3.println("Set Round : " + wordSetting.getFinalRound());
            writer3.println("Current Round : " + game.getRound());
            writer3.println("Round Word : " + game.getStartWord());
            writer3.println("Last Char : " + game.getLastChar());
            writer3.println("Game Started");
          }
          printWriter.println("Server Ok");
          timer.scheduleAtFixedRate(timerEvent, 0, 1000);
        }
        //게임 자기차례일떄..
        else if (wordSetting.getRoundFlag() && name.equals(queue.getCurrentClientName())) {
          if (readValue.equals("Client Ok")) {
            Timer timer2 = new Timer();
            //TimerEvent personalTimerEvent = new TimerEvent(10,true);
            timer2.scheduleAtFixedRate(queue.getTimerEvent(), 0, 1000);
          }
          Game game = Game.getInstance();
          if (game.getLanguage().equals("ko")) {
            mission.makeRandomMissionChar();
            printWriter.println("Mission word: " + mission.getMissionWord());
          }
          readValue = bufferedReader.readLine();
          if (wordSetting.getIsEnd()) {
            continue;
          }
          if (game.check(readValue)) {
            queue.pollTimerEvent(list);
            game.setChain(game.getChain() + 1);
            game.updateScore(name, readValue.length());
            ArrayList<String> means = game.getMean(readValue, game.getLanguage());
            for (Socket value : list) {
              OutputStream outputStream2 = value.getOutputStream();
              OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                  StandardCharsets.UTF_8);
              PrintWriter writer2 = new PrintWriter(outputStreamWriter2, true);
              writer2.println("Correct Word : " + readValue);
              if (game.getLanguage().equals("ko") && readValue.contains(mission.getMissionWord())) {
                game.setMission(true);
                writer2.println("Mission accomplished! You will get a bonus point.");
              }
              System.out.println(
                  "Score: " + game.getScore(name) + " Name: " + name);//Maybe here is error
              writer2.println(
                  "Score: " + game.getScore(name) + " Name: " + name);//Maybe here is error
              game.setMission(false);
              writer2.print("Mean :");
              if(means.isEmpty()) writer2.println("Api can't find the mean");
              for(String mean : means){
                writer2.println(mean);
              }
              writer2.println("Last Char : " + game.getLastChar());
              writer2.println("Current Client : " + queue.getCurrentClientName());
            }
          } else {
            //쓰레기값을 보내야 while문에서 readline을 받은후에 해당문으로 다시 돌아올 수 있음.
            printWriter.println("Wrong Word");
            for (Socket value : list) {
              OutputStream outputStream2 = value.getOutputStream();
              OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                  StandardCharsets.UTF_8);
              PrintWriter writer2 = new PrintWriter(outputStreamWriter2, true);
              writer2.println(name + " : " + readValue);
            }
          }

        } else {
          //내 차례가 아닐때에는 채팅 모드로..
          for (Socket value : list) {
            OutputStream outputStream2 = value.getOutputStream();
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2,
                StandardCharsets.UTF_8);
            PrintWriter writer2 = new PrintWriter(outputStreamWriter2, true);
            writer2.println(name + " : " + readValue);
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
