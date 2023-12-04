package org.example.game;

import java.io.IOException;
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

  public static ArrayList<String> getMean(String word)
      throws ParserConfigurationException, IOException, SAXException {
    String url =
        "https://stdict.korean.go.kr/api/search.do?key=" + key + "&type_search=search&q=" + word;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(url);

    NodeList nl = doc.getElementsByTagName("item");
    ArrayList<String> words = new ArrayList<String>();
    for (int i = 0; i < nl.getLength(); i++) {
      Node node = nl.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        words.add(getValue("word", element));
      }
    }
    return words;
  }

  public static String getValue(String tag, Element element) {
    NodeList nl = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node value = (Node) nl.item(0);
    return value.getNodeValue();
  }
}

