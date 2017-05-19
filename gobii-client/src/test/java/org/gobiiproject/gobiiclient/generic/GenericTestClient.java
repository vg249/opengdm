package org.gobiiproject.gobiiclient.generic;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 5/19/2017.
 */
public class GenericTestClient {

    private static Server server = new Server(8788);

    @BeforeClass
    public static void serverSetup() throws Exception {


        /*
        // This setup is if you are using jetty without jersey
        // Please keep this example around in case we need to revert to that
        // However, for now it seems that jersey makes it easier to set up the
        // test server resources
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[] { connector });

        ContextHandler contextGet = new ContextHandler();
        contextGet.setHandler(new TestServerHandlerGet());
        contextGet.setContextPath("/generic/getresource");

        ContextHandler contextPost = new ContextHandler();
        contextPost.setHandler(new TestServerHandlerPost());
        contextPost.setContextPath("/generic/postresource");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { contextGet, contextPost });
        server.setHandler(contexts);

        server.start();
        server.join();


        System.out.print(server.dump());

        */

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(GenericTestServer.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        server = new Server(8099);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);

        server.start();
        server.join();

    }

    @AfterClass
    public static void serverTearDown() throws Exception {

        server.stop();
    }

    @Test
    public void testGetMethod() throws Exception {

    }
}
