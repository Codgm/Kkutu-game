package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  public boolean select(String string, String lang, boolean injeong) {
    try {
      String query;
      if (injeong) {
        query = "SELECT _id FROM public.kkutu_" + lang + " WHERE _id = '" + string
            + "' AND type LIKE '%INJEONG%'";
      } else {
        query = "SELECT _id, type FROM public.kkutu_" + lang + " WHERE _id = '" + string + "'";
      }
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      //type에 INJEONG이 포함되어있으면 false 아니면 true
      if (injeong) {
        return resultSet.next();
      } else {
        if (resultSet.next()) {
          String type = resultSet.getString("type");
          return !type.contains("INJEONG");
        } else {
          return false;
        }
      }
    } catch (SQLException e) {
      return false;
    }
  }

  public String selectType(String string, String lang) {
    try {
      String query = "SELECT type FROM public.kkutu_" + lang + " WHERE _id = '" + string + "'";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString("type");
      } else {
        return null;
      }
    } catch (SQLException e) {
      return null;
    }
  }


  public ArrayList<String> selectWords(int round, String lang, boolean injeong) {
    //우선 en으로 설정. 추후 Parameter로 받아서 언어 설정할 수 있게 변경.
    String query = "SELECT _id FROM public.kkutu_" + lang + " WHERE CHAR_LENGTH(_id) = " + round;
    ArrayList<String> words = new ArrayList<String>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        words.add(resultSet.getString("_id"));
      }
      return words;
    } catch (SQLException e) {
      return null;
    }
  }

  public String getMeanByWord(String word, String lang) {
    String query = "SELECT mean FROM public.kkutu_" + lang + " WHERE _id = '" + word + "'";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString("mean");
      } else {
        return null;
      }
    } catch (SQLException e) {
      return null;
    }
  }

}
