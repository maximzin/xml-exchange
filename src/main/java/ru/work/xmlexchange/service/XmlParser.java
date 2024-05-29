package ru.work.xmlexchange.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

@Service
public class XmlParser {

    public XmlParser() throws ParserConfigurationException, IOException, SAXException {}

    public String parseResponse(String xml) throws IOException, SAXException, ParserConfigurationException {
        Document readyXml = firstPhase(xml);
        NodeList nodeList = readyXml.getElementsByTagName("Customer");
        Node first = nodeList.item(0);
        return first.getTextContent();
    }

    //Здесь происходит парсинг статусного сообщения от ШОД
    public String parseStatusMessage(String xml) throws IOException, SAXException, ParserConfigurationException {

        //Делаем документ из нашей String xml
        Document readyXml = firstPhase(xml);;

        //Парсим код
        NodeList nodeList = readyXml.getElementsByTagName("Code");
        String code = nodeList.item(0).getTextContent();

        //Парсим результат
        nodeList = readyXml.getElementsByTagName("Result");
        String result = nodeList.item(0).getTextContent();

        //Парсим описание
        nodeList = readyXml.getElementsByTagName("Description");
        String description = nodeList.item(0).getTextContent();

        //Возвращаем всё
        return code + " " + result + " " + description;
    }

    //Нужно превратить нашу строку в документ для дальнейшей обработки
    private Document firstPhase(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();
        return doc;
    }
}
