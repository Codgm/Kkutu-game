package org.example;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

public class IOFrame extends JFrame {

  final JLabel inputLabel = new JLabel("input>> ");
  private final Map<String, Integer> clientScore = new HashMap<>();
  private final String[] turnState = {" | Waiting...", " | Your turn", " | Opponent turn"};
  final Font font = new Font("Malgun Gothic", Font.PLAIN, 10);
  final JPanel mainPage = new JPanel();
  final JPanel gamePage = new JPanel();
  final JPanel scorePage = new JPanel();
  final JPanel inputLayout = new JPanel();
  final JPanel stateLayout = new JPanel();
  final JPanel eachClientScore = new JPanel();
  final JTextArea recordTextArea = new JTextArea();
  final JScrollPane scrollRecord = new JScrollPane(recordTextArea);
  final JTextField inputTextField = new JTextField();
  final JLabel scoreLabel = new JLabel("    Score    ");
  private boolean isBeforeFirstRound = true;//처음 한 번만 true고 그 이후로는 항상 false
  private String lastTurn = null;//마지막으로 누구의 턴이었는지, 라운드 종료됀 상태에서 필요함
  private String userName = null;
  private Socket socket = null;
  private boolean isRoundEnd = false;
  private int personalLeftTime = 0;
  final JLabel personalTimeLabel = new JLabel("personal: " + personalLeftTime + "sec left");
  private int roundLeftTime = 0;
  final JLabel roundTimeLabel = new JLabel(" | round: " + roundLeftTime + "sec left");
  private int turnStateValue = 0;
  final JLabel turnLabel = new JLabel(turnState[turnStateValue]);
  private String recordData = "";
  volatile private boolean isInputTextValid = false;
  private String inputText = "";

  public IOFrame(String userName) {
    recordTextArea.setFont(font);
    inputTextField.setFont(font);
    inputLabel.setFont(new Font("Malgun Gothic Bold", Font.PLAIN, 15));

    this.setTitle(userName);
    mainPage.setLayout(new BorderLayout());
    gamePage.setLayout(new BorderLayout());
    scorePage.setLayout(new BorderLayout());
    inputLayout.setLayout(new BorderLayout());
    stateLayout.setLayout(new BorderLayout());
    eachClientScore.setLayout(new BoxLayout(eachClientScore, BoxLayout.Y_AXIS));

    recordTextArea.setEditable(false);
    recordTextArea.setText(recordData);
    inputTextField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
          inputText = inputTextField.getText();
          if (!isRoundEnd) {
            isInputTextValid = true;
          }
          inputTextField.setText("");
          isInputTextValid = false;
        }
      }
    });
    inputTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        // 텍스트가 삽입될 때 호출
        try {
          typedOnTextField();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }

      @Override//오버라이드를 해야해서 구현만 해둠
      public void removeUpdate(DocumentEvent e) {
      }

      @Override//오버라이드를 해야해서 구현만 해둠
      public void changedUpdate(DocumentEvent e) {
      }

      private void typedOnTextField() throws IOException {
        // 텍스트 필드의 내용이 변경될 때 실행할 작업
        if (isBeforeFirstRound) {
          return;
        }
        if (isRoundEnd && lastTurn.equals(getUserName())) {
          isRoundEnd = false;
          OutputStream out = socket.getOutputStream();
          OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out,
              StandardCharsets.UTF_8);
          PrintWriter writer = new PrintWriter(outputStreamWriter, true);
          writer.println("Start");
        }
      }
    });

    stateLayout.add(turnLabel, BorderLayout.EAST);
    stateLayout.add(roundTimeLabel, BorderLayout.CENTER);
    stateLayout.add(personalTimeLabel, BorderLayout.WEST);

    inputLayout.add(inputLabel, BorderLayout.WEST);
    inputLayout.add(inputTextField, BorderLayout.CENTER);
    inputLayout.add(stateLayout, BorderLayout.EAST);

    gamePage.add(inputLayout, BorderLayout.SOUTH);
    gamePage.add(scrollRecord, BorderLayout.CENTER);

    scorePage.add(scoreLabel, BorderLayout.NORTH);
    scorePage.add(eachClientScore);

    mainPage.add(gamePage, BorderLayout.CENTER);
    mainPage.add(scorePage, BorderLayout.WEST);

    this.add(mainPage);

    setSize(700, 340);
    setResizable(true);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    DefaultCaret caret = (DefaultCaret) recordTextArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    setVisible(true);
  }

  boolean getIsRoundEnd() {
    return this.isRoundEnd;
  }

  void setIsRoundEnd(boolean isRoundEnd) {
    this.isRoundEnd = isRoundEnd;
  }

  void setPersonalLeftTime(int personalLeftTime) {
    this.personalLeftTime = personalLeftTime;
    personalTimeLabel.setText("personal: " + personalLeftTime + "sec left");
  }

  void setRoundLeftTime(int roundLeftTime) {
    this.roundLeftTime = roundLeftTime;
    roundTimeLabel.setText(" | round: " + roundLeftTime + "sec left");
  }

  void setTurnStateValue(int turnStateValue) {
    this.turnStateValue = turnStateValue;
    turnLabel.setText(turnState[turnStateValue]);
  }

  void clearRecordDate() {
    this.recordData = "";
    recordTextArea.setText(recordData);
  }

  //개행은 입력에 이미 포함됀 걸로 가정
  void pushRecordData(String extra) {
    this.recordData = this.recordData + extra;
    recordTextArea.setText(recordData);
  }

  void setFrameTitle(String title) {
    this.setTitle(title);
    this.userName = title;
  }

  String getInputText() {
    int debugCount = 0;
    while (!isInputTextValid) {
      Thread.onSpinWait();
    }
    isInputTextValid = false;
    return inputText;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setLastTurn(String lastTurn) {
    this.lastTurn = lastTurn;
  }

  public void setIsBeforeFirstRound(boolean isBeforeFirstRound) {
    this.isBeforeFirstRound = isBeforeFirstRound;
  }

  public void addClientList(String name) {
    clientScore.put(name, 0);
    updateScore(name, 0);
  }

  public void updateScore(String newName, int newScore) {
    eachClientScore.removeAll();
    eachClientScore.setLayout(new BoxLayout(eachClientScore, BoxLayout.Y_AXIS));
    for (String name : clientScore.keySet()) {
      if (name.equals(newName)) {
        clientScore.put(name, newScore);
      }
      JLabel label = new JLabel(name + ": " + clientScore.get(name));
      eachClientScore.add(label);
    }
    eachClientScore.revalidate();
    eachClientScore.repaint();
  }
}
