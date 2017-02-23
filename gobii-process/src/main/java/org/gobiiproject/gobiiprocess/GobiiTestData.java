package org.gobiiproject.gobiiprocess;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by VCalaminos on 2/21/2017.
 */
public class GobiiTestData {

    private static void checkDbPKey(NodeList nodeList, XPath xPath, Document document) throws Exception {

        for(int i=0; i<nodeList.getLength(); i++) {

            String parentName = nodeList.item(i).getLocalName();

            Element element = (Element) nodeList.item(i);
            String DbPKeysurrogate = element.getAttribute("DbPKeysurrogate");

            System.out.println("\nChecking DbPKeysurrogate ("+DbPKeysurrogate+") for " + parentName +"......\n");
            Node node = nodeList.item(i);

            NodeList childNodes = node.getChildNodes();

            int count = 0;
            for (int j=0; j<childNodes.getLength(); j++) {

                if (childNodes.item(j) instanceof Element == false)
                    continue;

                System.out.println("\nRecord " + (count+1) + "....\n");

                count++;
                String childName = childNodes.item(j).getLocalName();
                Element childElement = (Element) childNodes.item(j);

                Element props = (Element) childElement.getElementsByTagName("Properties").item(0);

                String dbPkeysurrogateValue = props.getElementsByTagName(DbPKeysurrogate).item(0).getTextContent();

                System.out.println("\nChecking for value....\n");

                if(dbPkeysurrogateValue.isEmpty()) {

                    throw new Exception("DbPKeysurrogate ("+DbPKeysurrogate+") attribute for "+
                            childName+" entity cannot be empty.");

                }

                System.out.println("\nChecking for duplicates...\n");

                String expr = "count(//Entities/"+parentName+"/"+childName+"/Properties["+DbPKeysurrogate+"='"+dbPkeysurrogateValue+"'])";

                XPathExpression xPathExpressionCount = xPath.compile(expr);
                Double countDuplicate = (Double) xPathExpressionCount.evaluate(document, XPathConstants.NUMBER);


                if(countDuplicate > 1) {

                    String message = "Duplicate DbPKeysurrogate (" +
                            DbPKeysurrogate + ") value ("+ dbPkeysurrogateValue +") " +
                            "for " + childName + " entity.";


                    throw new Exception(message);

                }
            }
        }

    }

    public static void main(String[] args) throws Exception{


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        File fXmlFile = new File("C:\\Users\\VCalaminos\\Documents\\gobii\\gobiiproject\\gobii-process\\src\\main\\resources\\test_data_example2.xml");
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fXmlFile);

        XPath xPath = XPathFactory.newInstance().newXPath();

        //check if all DbPKeysurrogate value is unique for each entity
        String getAllNotesExpr = "//Entities/*";

        XPathExpression xPathExpression = xPath.compile(getAllNotesExpr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        checkDbPKey(nodeList, xPath, document);

    }

}
