package org.gobiiproject.gobiiprocess.spring;

import org.glassfish.jersey.jaxb.internal.XmlJaxbElementProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.transaction.Transactional;

public class DataBaseContextSingleton {
    private static final DataBaseContextSingleton INSTANCE = new DataBaseContextSingleton();

    private ApplicationContext context = new ClassPathXmlApplicationContext(
        "classpath:/spring/application-config.xml");

    private DataBaseContextSingleton() {}

    public static DataBaseContextSingleton getInstance() {
        return INSTANCE;
    }

    public ApplicationContext getContext() {
        return context;
    }

}
