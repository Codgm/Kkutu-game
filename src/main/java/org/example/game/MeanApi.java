package org.example.game;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MeanApi {

  private static final String key = "EFC3B08AB04BBE176E587372B31F44E6";

  public static ArrayList<String> getMeans(String word) {
    ArrayList<String> means = new ArrayList<>();
    try {
      String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
      String url = "https://stdict.korean.go.kr/api/search.do?key=" + key + "&type_search=search&q="
          + encodedWord;
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(url);
      NodeList nList = doc.getElementsByTagName("item");
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          means.add(getValue("definition", eElement));
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
    return means;
  }

  public static String getValue(String tag, Element element) {
    NodeList nl = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node value = nl.item(0);
    return value.getNodeValue();
  }

}
