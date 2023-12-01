package org.example;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class IOFrame extends JFrame {

  String tempRecordData = "YOU- car\n"
      + "OPPONENT- race\n"
      + "YOU- edit\n"
      + "OPPONENT- tree\n"
      + "YOU- europe\n"
      + "OPPONENT- earth\n"
      + "YOU- hell";
  String[] tempTurn = {
      "Waiting... | ", "Your turn | ", "Opponent turn | "
  };
  String[] tempTime = {
      "16sec left ", "3sec left ", "120sec left "
  };

  public IOFrame() {
    this.setTitle("${userName}");

    JPanel page = new JPanel();
    JPanel inputLayout = new JPanel();
    JPanel stateLayout = new JPanel();
    page.setLayout(new BorderLayout());
    inputLayout.setLayout(new BorderLayout());
    stateLayout.setLayout(new BorderLayout());

    JTextArea recordTextArea = new JTextArea();
    JScrollPane scrollRecord = new JScrollPane(recordTextArea);
    JLabel inputLabel = new JLabel("input>> ");
    JTextField inputTextField = new JTextField();
    JLabel turnLabel = new JLabel(tempTurn[2]);
    JLabel timeLabel = new JLabel(tempTime[2]);

    recordTextArea.setEditable(false);
    recordTextArea.setText(tempRecordData);//temp
    inputTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        print();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        print();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        print();
      }

      private void print() {
        String text = inputTextField.getText();
        System.out.println("호출됌" + text);
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
}
