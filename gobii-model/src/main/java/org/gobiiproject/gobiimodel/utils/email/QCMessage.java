package org.gobiiproject.gobiimodel.utils.email;

/**
 * Created by VCalaminos on 1/31/2017.
 * Modified by Angel Villahoz-Baleta on 4/3/2017.
 */
public class QCMessage extends MailMessage {

    public QCMessage qcStart(String user, int datasetId, Long qcJobId) {
        super.setUser(user);

        StringBuilder subject = new StringBuilder("The quality control job ")
                .append(qcJobId);
        super.setSubject(subject.toString());

        StringBuilder body = new StringBuilder("Quality Control job ")
                .append(qcJobId)
                .append(" has started: processing data set ")
                .append(datasetId)
                .append(".<br/>");
        super.setBody(body.toString());

        return this;
    }

    public QCMessage qcStatus(String user, Long qcJobId, boolean qcSuccess, String qcDataDirectory) {
        super.setUser(user);

        StringBuilder subject = new StringBuilder("The status of the quality control job ")
                .append(qcJobId);
        super.setSubject(subject.toString());

        StringBuilder body = new StringBuilder("The quality control job ")
                .append(qcJobId);
        if (qcSuccess) {
            body.append(" was successful.");
        }
        else {
            body.append("failed.");
        }
        body.append("<br>The results of the quality control job ")
                .append(qcJobId)
                .append(" are available at ")
                .append(qcDataDirectory);
        super.setBody(body.toString());

        return this;
    }
}
