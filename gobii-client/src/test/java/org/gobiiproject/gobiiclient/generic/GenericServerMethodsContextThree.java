
package org.gobiiproject.gobiiclient.generic;

import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Path(GenericTestPaths.GENERIC_TEST_ROOT + "/" + GenericTestPaths.GENERIC_CONTEXT_THREE)
public class GenericServerMethodsContextThree {


    Logger LOGGER = LoggerFactory.getLogger(GenericServerMethodsContextThree.class);

//    @GET
//    @Path(GenericTestPaths.FILES_MARKERS)
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response markers() {
//
//
//        File fileToSend = new File(GenericTestValues.FILE_MARKERS);
//        return Response
//                .ok(fileToSend, MediaType.APPLICATION_OCTET_STREAM)
//                //         .header(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM)
//                .build();
//
//    }


    @GET
    @Path(GenericTestPaths.FILES_MARKERS)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile() throws Exception {

//        Resource resource = new ClassPathResource("/xls/template.xlsx");
//
//        final InputStream inp = resource.getInputStream();
//        final Workbook wb = WorkbookFactory.create(inp);
//        Sheet sheet = wb.getSheetAt(0);
//
//        Row row = CellUtil.getRow(7, sheet);
//        Cell cell = CellUtil.getCell(row, 0);
//        cell.setCellValue("TITRE TEST");


        StreamingOutput stream ;

        try {

            // File fileToSend = new File(GenericTestValues.FILE_MARKERS);


            stream = new StreamingOutput() {

                public void write(OutputStream output) throws IOException, WebApplicationException {
                    try {

                        File fileToSend = new ClassPathResource(GenericTestValues.FILE_MARKERS).getFile();
                        FileInputStream fileInputStream = new FileInputStream(fileToSend);

                        byte[] buf = new byte[8192];
                        int c;
                        while ((c = fileInputStream.read(buf, 0, buf.length)) > 0) {
                            output.write(buf, 0, c);
                            output.flush();
                        }
                    } catch (Exception e) {
                        throw new WebApplicationException(e);
                    }
                }
            };


        } catch (Exception e) {
            LOGGER.error("errror in " + GenericTestPaths.FILES_MARKERS, e);
            throw new WebApplicationException(e);
        }


        return Response
                .ok(stream)
                .header("content-disposition", "attachment; filename = " + GenericTestValues.FILE_MARKERS)
                .build();
    }
}
