package org.gobiiproject.gobiiprocess.spring;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GobiiProcessContextSingleton {
    private static final GobiiProcessContextSingleton INSTANCE = new GobiiProcessContextSingleton();

    private ApplicationContext context;
    private static String cropType;
    private static ConfigSettings configSettings;


    private GobiiProcessContextSingleton() {}

    public static GobiiProcessContextSingleton getInstance() {
        return INSTANCE;
    }

    public static void init(String cropType, ConfigSettings configSettings) {
        GobiiProcessContextSingleton.cropType = cropType;
        GobiiProcessContextSingleton.configSettings = configSettings;
        GobiiProcessContextSingleton.INSTANCE.context = new ClassPathXmlApplicationContext(
                "classpath:/spring/application-config.xml");
    }

    public ApplicationContext getContext() {
        if(GobiiProcessContextSingleton.cropType == null
            || GobiiProcessContextSingleton.configSettings == null) {
            throw new GobiiException(
                "Initialize Context with crop type and config location");
        }
        if(context == null) {
            context = new ClassPathXmlApplicationContext(
                "classpath:/spring/application-config.xml");
        }
        return context;
    }

    public <T> T getBean(Class<T> requiredType) {
        return this.context.getBean(requiredType);
    }

    public static String getCropType() {
        return cropType;
    }

    public static ConfigSettings getConfigSettings() {
        return configSettings;
    }
}
