package ru.work.xmlexchange.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Service
public class XmlResponse {

    public String createXmlResponse(int statusCode) throws ParserConfigurationException, TransformerException {
        if (statusCode == 1000) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElementNS("ИЗ СХЕМЫ НАЙТИ И ПОДСТАВИТЬ","Status");
            doc.appendChild(root);

            Element code = doc.createElement("Code");
            code.appendChild(doc.createTextNode("1000"));
            root.appendChild(code);

            Element result = doc.createElement("Result");
            result.appendChild(doc.createTextNode("OK"));
            root.appendChild(result);

            Element description = doc.createElement("Description");
            description.appendChild(doc.createTextNode("Успешно"));
            root.appendChild(description);

            DOMSource domSource = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            StreamResult resultXml = new StreamResult(stringWriter);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, resultXml);

            return stringWriter.toString();
        }
        return null;
    }


}
