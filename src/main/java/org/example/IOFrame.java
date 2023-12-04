package org.example;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class IOFrame extends JFrame {
  Font font = new Font("Malgun Gothic", Font.PLAIN, 10);
  private int leftTime = 0;
  private int turnStateValue = 0;
  private String recordData = "";
  private String[] turnState = {
      "Waiting... | ", "Your turn | ", "Opponent turn | "
  };
  volatile private boolean isInputTextValid = false;
  private String inputText = "";

  JPanel page = new JPanel();
  JPanel inputLayout = new JPanel();
  JPanel stateLayout = new JPanel();

  JTextArea recordTextArea = new JTextArea();
  JScrollPane scrollRecord = new JScrollPane(recordTextArea);
  final JLabel inputLabel = new JLabel("input>> ");
  JTextField inputTextField = new JTextField();
  JLabel turnLabel = new JLabel(turnState[turnStateValue]);
  JLabel timeLabel = new JLabel(leftTime + "sec left");

  public IOFrame(String userName) {
    recordTextArea.setFont(font);
    inputTextField.setFont(font);

    this.setTitle(userName);
    page.setLayout(new BorderLayout());
    inputLayout.setLayout(new BorderLayout());
    stateLayout.setLayout(new BorderLayout());

    recordTextArea.setEditable(false);
    recordTextArea.setText(recordData);
    inputTextField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
          inputText = inputTextField.getText();
          isInputTextValid = true;
          inputTextField.setText("");
        }
      }
    });

    stateLayout.add(turnLabel, BorderLayout.WEST);
    stateLayout.add(timeLabel, BorderLayout.EAST);

    inputLayout.add(inputLabel, BorderLayout.WEST);
    inputLayout.add(inputTextField, BorderLayout.CENTER);
    inputLayout.add(stateLayout, BorderLayout.EAST);

    page.add(inputLayout, BorderLayout.SOUTH);
    page.add(scrollRecord, BorderLayout.CENTER);

    this.add(page);

    setVisible(true);
    setSize(500, 140);
    setResizable(true);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  void reRenderIOFrame() {
    //inputTextField manage states itself
    recordTextArea.setText(recordData);
    turnLabel.setText(turnState[turnStateValue]);
    timeLabel.setText(leftTime + "sec left");
  }

  void setLeftTime(int leftTime) {
    this.leftTime = leftTime;
    timeLabel.setText(leftTime + "sec left");
  }

  void setTurnStateValue(int turnStateValue) {
    this.turnStateValue = turnStateValue;
    turnLabel.setText(turnState[turnStateValue]);
  }

  void clearRecordDate() {
    this.recordData = "";
  }

  //개행은 입력에 이미 포함됀 걸로 가정
  void pushRecordData(String extra) {
    this.recordData = this.recordData + extra;
    recordTextArea.setText(recordData);
  }

  void setFrameTitle(String title) {
    this.setTitle(title);
  }

  boolean isInputTextValid() {
    return isInputTextValid;
  }

  String getInputText() {
    int debugCount = 0;
    while (!isInputTextValid) {
    }
    isInputTextValid = false;
    return inputText;
  }
}
