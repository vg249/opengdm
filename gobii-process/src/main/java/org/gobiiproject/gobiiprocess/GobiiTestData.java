package org.gobiiproject.gobiiprocess;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by VCalaminos on 2/21/2017.
 */
public class GobiiTestData {

    private static void validateKeys(NodeList nodeList, XPath xPath, Document document) throws Exception {

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


                System.out.println("\nChecking for foreign keys...\n");

                Element keys = (Element) childElement.getElementsByTagName("Keys").item(0);
                NodeList fkeys = keys.getElementsByTagName("Fkey");

                for(int k = 0; k < fkeys.getLength(); k++) {

                    Element fkey = (Element) fkeys.item(k);

                    String entity = fkey.getAttribute("entity");

                    if(entity.isEmpty()) {

                        String message = "Entity attribute for Fkey of "+ childName+" ("+dbPkeysurrogateValue+") cannot be empty.";

                        throw new Exception(message);

                    }

                    NodeList fkeyDbPkey = fkey.getElementsByTagName("DbPKeySurrogate");

                    if(fkeyDbPkey.getLength() < 1) {

                        String message = "FKey property for "+ childName +" ("+ dbPkeysurrogateValue+ ") should have <DbPKeySurrogate> tag.";

                        throw new Exception(message);
                    }

                    String fkeyDbPkeyValue = fkeyDbPkey.item(0).getTextContent();

                    if(fkeyDbPkeyValue.isEmpty()) {

                        String message = "DbPKeySurrogate property for " + entity + " FKey of " + childName + " (" + dbPkeysurrogateValue + ") cannot be empty.";

                        throw new Exception(message);

                    }

                    // get parent node of fkey entity
                    XPathExpression exprParentFkey = xPath.compile("//"+entity+"/parent::*");
                    Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);

                    String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");

                    String exprCheckIfFKeyExists = "count(//Entities/"+ancestor.getNodeName()+"/"+entity+"/Properties["+fkeyPKey+"='"+fkeyDbPkeyValue+"'])";

                    XPathExpression xPathExpressionCountFkey = xPath.compile(exprCheckIfFKeyExists);
                    Double countIfExists = (Double) xPathExpressionCountFkey.evaluate(document, XPathConstants.NUMBER);

                    if(countIfExists < 1) {

                        String message = entity + " (" + fkeyDbPkeyValue+ ") fkey value for "
                                + childName + "(" + dbPkeysurrogateValue + ")" +
                                " doesn't exist in the file.";

                        throw new Exception(message);
                    }
                }
            }
        }

    }

    private static void writePkValues(XPath xPath, Document document, File fXmlFile) throws Exception{

        //get nodes with no FKey dependencies to update DbPKey

        String expr = "//*[local-name() = 'Keys' and not(descendant::*[local-name() = 'Fkey'])]";

        XPathExpression xPathExpression = xPath.compile(expr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        for(int i=0; i<nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            Element parentElement = (Element) element.getParentNode();

            String parentLocalName = parentElement.getLocalName();

            Element rootElement = (Element) parentElement.getParentNode();

            String DBPKeysurrogateName = rootElement.getAttribute("DbPKeysurrogate");
            Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);

            String dbPkeysurrogateValue = props.getElementsByTagName(DBPKeysurrogateName).item(0).getTextContent();


            System.out.println("\nWriting DbPKey for "+parentLocalName+" ("+dbPkeysurrogateValue+") " +
                    " to file...\n");

            // write test pk values;
            Random rn = new Random();
            Integer testPk = rn.nextInt();

            Element dbPKey = (Element) element.getElementsByTagName("DbPKey").item(0);

            dbPKey.setTextContent(testPk.toString());

        }

        writeToFile(document, fXmlFile);

        //update DbPKey of elements with FKey dependencies

        expr = "//*[local-name() = 'Keys' and descendant::*[local-name() = 'Fkey']]";

        xPathExpression = xPath.compile(expr);

        nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        for(int i=0; i<nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            Element parentElement = (Element) element.getParentNode();

            Element rootElement = (Element) parentElement.getParentNode();

            String DBPKeysurrogateName = rootElement.getAttribute("DbPKeysurrogate");
            Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);

            String dbPkeysurrogateValue = props.getElementsByTagName(DBPKeysurrogateName).item(0).getTextContent();

            NodeList fKdbPkeys = element.getElementsByTagName("Fkey");

            for(int j=0; j<fKdbPkeys.getLength(); j++) {


                Element currentFkeyElement = (Element) fKdbPkeys.item(j);

                String entity  = currentFkeyElement.getAttribute("entity");

                String fKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();


                System.out.println("\nWriting "+ entity+ " (" + fKeyDbPkeyValue+ ") FkeyDbPkey for " + parentElement.getLocalName()+
                        "  (" +dbPkeysurrogateValue+ " ) to file...\n");



                XPathExpression exprParentFkey = xPath.compile("//"+entity+"/parent::*");
                Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);

                String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");

                String exprCheckIfFKeyExists = "//Entities/"+ancestor.getNodeName()+"/"+entity+"/Properties["+fkeyPKey+"='"+fKeyDbPkeyValue+"']";

                XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfFKeyExists);
                Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);

                Element parentNode = (Element) nodeFKey.getParentNode();

                String dbPkeyValue =  ((Element) parentNode.getElementsByTagName("Keys").item(0)).getElementsByTagName("DbPKey").item(0).getTextContent();

                // set to <FKey><DbPkey></DbPkey></Fkey>

                Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);

                currentFkeydbPKeyElement.setTextContent(dbPkeyValue);

            }

            System.out.println("\nWriting DbPKey for "+parentElement.getLocalName()+" ("+dbPkeysurrogateValue+") " +
                    " to file...\n");
            // set  <DbPKey/> of current entity

            Element currentDbPkey = (Element) element.getElementsByTagName("DbPKey").item(0);

            Random rn = new Random();
            Integer testPk = rn.nextInt();

            currentDbPkey.setTextContent(testPk.toString());
        }


        writeToFile(document, fXmlFile);

    }

    private static void writeToFile(Document document, File fXmlFile) throws Exception{

        // Get file ready to write
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new FileWriter(fXmlFile));
        transformer.transform(new DOMSource(document), result);

        // Write file out
        result.getWriter().flush();

    }

    public static void main(String[] args) throws Exception{


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        File fXmlFile = new File("src/main/resources/test_data_example2.xml");
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fXmlFile);

        XPath xPath = XPathFactory.newInstance().newXPath();

        //check if all DbPKeysurrogate value is unique for each entity
        String getAllNotesExpr = "//Entities/*";

        XPathExpression xPathExpression = xPath.compile(getAllNotesExpr);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        validateKeys(nodeList, xPath, document);

        System.out.println("\nFile passed key checks...\n");

        writePkValues(xPath, document, fXmlFile);

        System.out.println("\n\n\nSuccessfully saved DbPKeys to file\n");
    }

}
