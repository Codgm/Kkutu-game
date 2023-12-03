package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DataBase {

  private final String url;
  private final String id;
  private final String password;

  private Connection connection;

  DataBase(String url, String id, String password) {
    this.url = url;
    this.id = id;
    this.password = password;
  }

  public void connect() {
    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(this.url, this.id, this.password);
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean select(String string, String lang, boolean injeong, boolean injeong2, boolean manner) {
    try {
      String query;
      if(injeong2) query = "SELECT _id FROM public.kkutu_" + lang +" WHERE _id = '" + string + "' AND type LIKE '%INJEONG%'";
      else if(manner) query = "SELECT _id FROM public.kkutu_manner_" + lang +" WHERE _id = '" + string + "'";
      else query = "SELECT _id FROM public.kkutu_" + lang +" WHERE _id = '" + string + "'";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    } catch (SQLException e) {
      return false;
    }
  }


  public ArrayList<String> selectWords(int round, String lang, boolean injeong) {
    //우선 en으로 설정. 추후 Parameter로 받아서 언어 설정할 수 있게 변경.
    String query = "SELECT _id FROM public.kkutu_" + lang + " WHERE CHAR_LENGTH(_id) = " + round;
    ArrayList<String> words = new ArrayList<String>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while(resultSet.next()) {
        words.add(resultSet.getString("_id"));
      }
      return words;
    } catch (SQLException e) {
      return null;
    }
  }

}
