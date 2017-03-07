package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static <E> E processTypes (Object value, Class<E> type) throws ParseException {

        if (type.equals(Integer.class)) {

            return type.cast(Integer.parseInt(value.toString()));

        } else if (type.equals(Date.class)) {

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            Date date = formatter.parse(value.toString());

            return type.cast(date);

        }


        return type.cast(value);
    }

    private static void checkStatus(PayloadEnvelope payloadEnvelope) throws  Exception{

        Header header = payloadEnvelope.getHeader();
        if (!header.getStatus().isSucceeded() ||
                header
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(headerStatusMessage -> headerStatusMessage.getGobiiStatusLevel().equals(GobiiStatusLevel.VALIDATION))
                        .count() > 0) {

            System.out.println("\n***** Header errors: *****");
            String message = "";

            for (HeaderStatusMessage currentStatusMessage : header.getStatus().getStatusMessages()) {

                message = message +  "\n" + currentStatusMessage.getMessage();
            }

            throw new Exception(message);

        }

    }

    private static Integer createOrganization(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                              XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        OrganizationDTO newOrganizationDTO = new OrganizationDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = OrganizationDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newOrganizationDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        newOrganizationDTO.setCreatedBy(1);
        newOrganizationDTO.setCreatedDate(new Date());
        newOrganizationDTO.setModifiedBy(1);
        newOrganizationDTO.setModifiedDate(new Date());
        newOrganizationDTO.setStatusId(1);

        if(fkeys != null && fkeys.getLength() > 0){

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = OrganizationDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newOrganizationDTO, fKeyDbPkey);

            }

        }


        // create organization

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                payloadEnvelope);

        checkStatus(organizationDTOResponseEnvelope);

        OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = organizationDTOResponse.getOrganizationId();


        return  returnVal;

    }

    private static Integer createContact(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                         XPath xPath, Document document, NodeList propKeyList) throws Exception {


        Integer returnVal = null;


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
                field.set(newContactDTO, processTypes(propKey.getTextContent(), field.getType()));

            }

        }

        newContactDTO.setCreatedBy(1);
        newContactDTO.setCreatedDate(new Date());
        newContactDTO.setModifiedBy(1);
        newContactDTO.setModifiedDate(new Date());

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ContactDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newContactDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ContactDTO> payloadEnvelopeContact = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceContact = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(ServiceRequestId.URL_CONTACTS));
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceContact.post(ContactDTO.class,
                payloadEnvelopeContact);

        checkStatus(contactDTOResponseEnvelope);

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = contactDTOResponse.getContactId();


        return returnVal;


    }

    private static Integer createPlatform(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        PlatformDTO newPlatformDTO = new PlatformDTO();

        Element propertiesElement = null;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if (propKeyLocalName.equals("properties")) {

                propertiesElement = propKey;
                continue;
            }

            if(propKey.getParentNode().equals(propertiesElement)) { // add to properties attribute of platform

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newPlatformDTO.getProperties().add(entityPropertyDTO);
            }
            else {

                Field field = PlatformDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newPlatformDTO, processTypes(propKey.getTextContent(), field.getType()));

            }

        }

        newPlatformDTO.setCreatedDate(new Date());
        newPlatformDTO.setCreatedBy(1);
        newPlatformDTO.setModifiedDate(new Date());
        newPlatformDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = PlatformDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newPlatformDTO, fKeyDbPkey);

            }

        }


        PayloadEnvelope<PlatformDTO> payloadEnvelopePlatform = new PayloadEnvelope<>(newPlatformDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourcePlatform = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_PLATFORM));
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelope = gobiiEnvelopeRestResourcePlatform.post(PlatformDTO.class,
                payloadEnvelopePlatform);


        checkStatus(platformDTOResponseEnvelope);

        PlatformDTO platformDTOResponse = platformDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = platformDTOResponse.getPlatformId();

        return returnVal;

    }

    private static Integer createProtocol(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        ProtocolDTO newProtocolDTO = new ProtocolDTO();

        Element propsElement = null;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("vendorProtocols")) {continue;}

            if(propKeyLocalName.equals("props")) {

                propsElement = propKey;
                continue;
            }

            if(propKey.getParentNode().equals(propsElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newProtocolDTO.getProps().add(entityPropertyDTO);
            }
            else {

                Field field = ProtocolDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newProtocolDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }

        newProtocolDTO.setCreatedDate(new Date());
        newProtocolDTO.setCreatedBy(1);
        newProtocolDTO.setModifiedDate(new Date());
        newProtocolDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ProtocolDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newProtocolDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ProtocolDTO> payloadEnvelopeProtocol = new PayloadEnvelope<>(newProtocolDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ProtocolDTO> gobiiEnvelopeRestResourceProtocol = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_PROTOCOL));
        PayloadEnvelope<ProtocolDTO> protocolDTOResponseEnvelope = gobiiEnvelopeRestResourceProtocol.post(ProtocolDTO.class,
                payloadEnvelopeProtocol);


        checkStatus(protocolDTOResponseEnvelope);

        ProtocolDTO protocolDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = protocolDTOResponse.getProtocolId();

        return returnVal;

    }

    private static Integer createReference(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        ReferenceDTO newReferenceDTO = new ReferenceDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ReferenceDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newReferenceDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newReferenceDTO.setCreatedDate(new Date());
        newReferenceDTO.setCreatedBy(1);
        newReferenceDTO.setModifiedDate(new Date());
        newReferenceDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ReferenceDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newReferenceDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference = new PayloadEnvelope<>(newReferenceDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResourceReference = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelope = gobiiEnvelopeRestResourceReference.post(ReferenceDTO.class,
                payloadEnvelopeReference);


        checkStatus(referenceDTOResponseEnvelope);

        ReferenceDTO referenceDTOResponse = referenceDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = referenceDTOResponse.getReferenceId();

        return  returnVal;

    }

    private static Integer createMapset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                           XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        MapsetDTO newMapsetDTO = new MapsetDTO();

        Element propertiesElement = null;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if (propKeyLocalName.equals("properties")) {

                propertiesElement = propKey;
                continue;

            }

            if (propKey.getParentNode().equals(propertiesElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newMapsetDTO.getProperties().add(entityPropertyDTO);

            }
            else {

                Field field = MapsetDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newMapsetDTO, processTypes(propKey.getTextContent(), field.getType()));

            }
        }

        newMapsetDTO.setCreatedDate(new Date());
        newMapsetDTO.setCreatedBy(1);
        newMapsetDTO.setModifiedDate(new Date());
        newMapsetDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = MapsetDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newMapsetDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<MapsetDTO> payloadEnvelopeMapset = new PayloadEnvelope<>(newMapsetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceMapset = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_MAPSET));
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = gobiiEnvelopeRestResourceMapset.post(MapsetDTO.class,
                payloadEnvelopeMapset);


        checkStatus(mapsetDTOResponseEnvelope);

        MapsetDTO mapsetDTOResponse = mapsetDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = mapsetDTOResponse.getMapsetId();

        return returnVal;

    }

    private static Integer createProject(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        ProjectDTO newProjectDTO = new ProjectDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ProjectDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newProjectDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newProjectDTO.setCreatedDate(new Date());
        newProjectDTO.setCreatedBy(1);
        newProjectDTO.setModifiedDate(new Date());
        newProjectDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ProjectDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newProjectDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ProjectDTO> payloadEnvelopeProject = new PayloadEnvelope<>(newProjectDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceProject = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_PROJECTS));
        PayloadEnvelope<ProjectDTO> projectDTOResponseEnvelope = gobiiEnvelopeRestResourceProject.post(ProjectDTO.class,
                payloadEnvelopeProject);


        checkStatus(projectDTOResponseEnvelope);

        ProjectDTO projectDTOResponse = projectDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = projectDTOResponse.getProjectId();

        return returnVal;

    }

    private static Integer createManifest(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        ManifestDTO newManifestDTO = new ManifestDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ManifestDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newManifestDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newManifestDTO.setCreatedDate(new Date());
        newManifestDTO.setCreatedBy(1);
        newManifestDTO.setModifiedDate(new Date());
        newManifestDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ManifestDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newManifestDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ManifestDTO> payloadEnvelopeManifest = new PayloadEnvelope<>(newManifestDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceManifest = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_MANIFEST));
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelope = gobiiEnvelopeRestResourceManifest.post(ManifestDTO.class,
                payloadEnvelopeManifest);


        checkStatus(manifestDTOResponseEnvelope);

        ManifestDTO manifestDTOResponse = manifestDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = manifestDTOResponse.getManifestId();

        return returnVal;


    }

    private static Integer createExperiment(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        ExperimentDTO newExperimentDTO = new ExperimentDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            Field field = ExperimentDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newExperimentDTO, processTypes(propKey.getTextContent(), field.getType()));

        }
        newExperimentDTO.setCreatedDate(new Date());
        newExperimentDTO.setCreatedBy(1);
        newExperimentDTO.setModifiedDate(new Date());
        newExperimentDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = ExperimentDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newExperimentDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<ExperimentDTO> payloadEnvelopeExperiment = new PayloadEnvelope<>(newExperimentDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_EXPERIMENTS));
        PayloadEnvelope<ExperimentDTO> experimentDTOResponseEnvelope = gobiiEnvelopeRestResourceExperiment.post(ExperimentDTO.class,
                payloadEnvelopeExperiment);


        checkStatus(experimentDTOResponseEnvelope);

        ExperimentDTO experimentDTOResponse = experimentDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = experimentDTOResponse.getExperimentId();

        return returnVal;

    }

    private static Integer createAnalysis(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        AnalysisDTO newAnalysisDTO = new AnalysisDTO();

        Element paramElement = null;

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("parameters")){

                paramElement = propKey;
                continue;
            }

            if (propKey.getParentNode().equals(paramElement)) {

                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();

                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());

                newAnalysisDTO.getParameters().add(entityPropertyDTO);
            }
            else {

                Field field = AnalysisDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newAnalysisDTO, processTypes(propKey.getTextContent(), field.getType()));

            }
        }

        newAnalysisDTO.setCreatedDate(new Date());
        newAnalysisDTO.setCreatedBy(1);
        newAnalysisDTO.setModifiedDate(new Date());
        newAnalysisDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = AnalysisDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newAnalysisDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(newAnalysisDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);


        checkStatus(analysisDTOResponseEnvelope);

        AnalysisDTO analysisDTOResponse = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = analysisDTOResponse.getAnalysisId();

        return returnVal;



    }

    private static Integer createDataset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        Integer returnVal = null;

        DataSetDTO newDataSetDTO = new DataSetDTO();

        for (int j=0; j<propKeyList.getLength(); j++) {

            Element propKey = (Element) propKeyList.item(j);

            String propKeyLocalName = propKey.getLocalName();

            propKeyLocalName = processPropName(propKeyLocalName);

            if(propKeyLocalName.equals("analysesIds")) {continue;}

            if(propKeyLocalName.equals("analysisId")) {

                newDataSetDTO.getAnalysesIds().add(Integer.parseInt(propKey.getTextContent()));
                continue;
            }

            if(propKeyLocalName.equals("scores")) {continue;}

            if(propKeyLocalName.equals("score")) {

                newDataSetDTO.getScores().add(Integer.parseInt(propKey.getTextContent()));
                continue;
            }

            Field field = DataSetDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newDataSetDTO, processTypes(propKey.getTextContent(), field.getType()));

        }

        newDataSetDTO.setCreatedDate(new Date());
        newDataSetDTO.setCreatedBy(1);
        newDataSetDTO.setModifiedDate(new Date());
        newDataSetDTO.setModifiedBy(1);

        if(fkeys != null && fkeys.getLength() > 0) {

            for (int i=0; i<fkeys.getLength(); i++) {

                Element currentFkeyElement = (Element) fkeys.item(i);

                String fkproperty = currentFkeyElement.getAttribute("fkproperty");

                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);

                Field field = DataSetDTO.class.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(newDataSetDTO, fKeyDbPkey);

            }

        }

        PayloadEnvelope<DataSetDTO> payloadEnvelopeDataSet = new PayloadEnvelope<>(newDataSetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(ServiceRequestId.URL_DATASETS));
        PayloadEnvelope<DataSetDTO> dataSetDTOResponseEnvelope = gobiiEnvelopeRestResourceDataSet.post(DataSetDTO.class,
                payloadEnvelopeDataSet);


        checkStatus(dataSetDTOResponseEnvelope);

        DataSetDTO dataSetDTOResponse = dataSetDTOResponseEnvelope.getPayload().getData().get(0);

        returnVal = dataSetDTOResponse.getDataSetId();

        return returnVal;


    }

    private static Integer createEntity(Element parentElement, String entityName, NodeList fKeys, String dbPkeysurrogateValue, XPath xPath, Document document) throws Exception {

        System.out.println("\n Creating " + entityName + " ("+ dbPkeysurrogateValue +") in the database...\n");

        Integer returnVal = null;


        Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);
        NodeList propKeyList = props.getElementsByTagName("*");

        Authenticator.authenticate();

        switch (entityName) {

            case "Organization" :

                returnVal = createOrganization(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Contact":

                returnVal = createContact(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Platform":

                returnVal = createPlatform(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Protocol":

                returnVal = createProtocol(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Reference":

                returnVal = createReference(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Mapset":

                returnVal = createMapset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Project":

                returnVal = createProject(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Manifest":

                returnVal = createManifest(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Experiment":

                returnVal = createExperiment(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Analysis":

                returnVal = createAnalysis(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            case "Dataset":

                returnVal = createDataset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);

                break;

            default:
                break;


        }


        return returnVal;

    }


    private static Integer getFKeyDbPKey(Element currentFkeyElement, Element parentElement, String dbPkeysurrogateValue, Document document, XPath xPath) throws Exception {


        String entity = currentFkeyElement.getAttribute("entity");

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

        System.out.println(dbPkeyValue);

        return Integer.parseInt(dbPkeyValue);


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


            // write test pk values;
            Random rn = new Random();
            Integer testPk = rn.nextInt();

            Element dbPKey = (Element) element.getElementsByTagName("DbPKey").item(0);

            NodeList fkeys = null;

            Integer returnEntityId = createEntity(parentElement, parentLocalName, fkeys, dbPkeysurrogateValue, xPath, document);


            System.out.println("\nWriting DbPKey for "+parentLocalName+" ("+dbPkeysurrogateValue+") " +
                    " to file...\n");

            dbPKey.setTextContent(returnEntityId.toString());

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

            Element currentDbPkey = (Element) element.getElementsByTagName("DbPKey").item(0);

            String parentLocalName = parentElement.getLocalName();

            Integer returnEntityId = createEntity(parentElement, parentLocalName, fKdbPkeys, dbPkeysurrogateValue, xPath, document);

            Random rn = new Random();
            Integer testPk = rn.nextInt();

            System.out.println("\nWriting DbPKey for "+parentElement.getLocalName()+" ("+dbPkeysurrogateValue+") " +
                    " to file...\n");
            // set  <DbPKey/> of current entity

            currentDbPkey.setTextContent(returnEntityId.toString());
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
