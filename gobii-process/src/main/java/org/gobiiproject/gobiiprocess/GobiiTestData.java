package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.*;

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

    private static String processPropName(String propertyName) {

        char c[] = propertyName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        propertyName = new String(c);


        return propertyName;

    }


    private static Integer createEntity(Element parentElement, String entityName) throws Exception {

        Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);
        NodeList propKeyList = props.getElementsByTagName("*");

        Integer returnVal = null;

        switch (entityName) {

            case "Organization" :

                OrganizationDTO newOrganizationDTO = new OrganizationDTO();

                for (int j=0; j<propKeyList.getLength(); j++) {

                    Element propKey = (Element) propKeyList.item(j);

                    String propKeyLocalName = propKey.getLocalName();

                    propKeyLocalName = processPropName(propKeyLocalName);

                    Field field = OrganizationDTO.class.getDeclaredField(propKeyLocalName);
                    field.setAccessible(true);
                    field.set(newOrganizationDTO, propKey.getTextContent());
                }

                newOrganizationDTO.setCreatedBy(1);
                newOrganizationDTO.setCreatedDate(new Date());
                newOrganizationDTO.setModifiedBy(1);
                newOrganizationDTO.setModifiedDate(new Date());
                newOrganizationDTO.setStatusId(1);

                PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDTO, GobiiProcessType.CREATE);
                GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_ORGANIZATION));
                PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                        payloadEnvelope);
                OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);


                returnVal = organizationDTOResponse.getOrganizationId();

                break;

            case "Contact":

                ContactDTO newContactDTO = new ContactDTO();

                // get roles
                RestUri rolesUri = ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .nameIdListByQueryParams();

                GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeNameResource = new GobiiEnvelopeRestResource<>(rolesUri);
                rolesUri.setParamValue("entity", GobiiEntityNameType.ROLES.toString().toLowerCase());

                PayloadEnvelope<NameIdDTO> resultEnvelopeRoles = gobiiEnvelopeNameResource.get(NameIdDTO.class);

                List<NameIdDTO> nameIdDTOList = resultEnvelopeRoles.getPayload().getData();

                Map<String, Integer> rolesMap = new HashMap<>();

                for(int i=0; i<nameIdDTOList.size(); i++) {

                    NameIdDTO currentNameIdDTO = nameIdDTOList.get(i);
                    rolesMap.put(currentNameIdDTO.getName(), currentNameIdDTO.getId());

                }

                for (int j=0; j<propKeyList.getLength(); j++) {

                    Element propKey = (Element) propKeyList.item(j);

                    String propKeyLocalName = propKey.getLocalName();

                    if (propKeyLocalName.equals("Roles")) {continue;}

                    if(propKeyLocalName.equals("Role")) {

                        Integer roleId = rolesMap.get(propKey.getTextContent());

                        newContactDTO.getRoles().add(roleId);

                    } else {

                        propKeyLocalName = processPropName(propKeyLocalName);

                        Field field = ContactDTO.class.getDeclaredField(propKeyLocalName);
                        field.setAccessible(true);
                        field.set(newContactDTO, propKey.getTextContent());

                    }

                }

                PayloadEnvelope<ContactDTO> payloadEnvelopeContact = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
                GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceContact = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                            .getUriFactory()
                            .resourceColl(ServiceRequestId.URL_CONTACTS));
                PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceContact.post(ContactDTO.class,
                        payloadEnvelopeContact);
                ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);

                returnVal = contactDTOResponse.getContactId();

                System.out.println(newContactDTO.getLastName() + newContactDTO.getEmail() + newContactDTO.getRoles() + "\n");

                break;

            case "Platform":
                break;

            default:
                break;


        }


        return returnVal;

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

//        writeToFile(document, fXmlFile);

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

            String parentLocalName = parentElement.getLocalName();

            if (parentLocalName.equals("Contact")) {

                Integer returnEntityId = createEntity(parentElement, parentLocalName);

            }

            Random rn = new Random();
            Integer testPk = rn.nextInt();

            currentDbPkey.setTextContent(testPk.toString());
        }


//        writeToFile(document, fXmlFile);

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
