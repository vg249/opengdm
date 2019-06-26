package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.utils.email.MailMessage;

public interface Messenger {

	void send(MailMessage message) throws Exception;

}
