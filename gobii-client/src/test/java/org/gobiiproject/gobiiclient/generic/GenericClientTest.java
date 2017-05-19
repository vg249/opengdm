package org.gobiiproject.gobiiclient.generic;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.reflect.annotation.ExceptionProxy;

/**
 * Created by Phil on 5/19/2017.
 */
public class GenericClientTest {

    private static Server server = new Server(8788);

    @BeforeClass
    public static void serverSetup() throws Exception {


        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[] { connector });

        ContextHandler contextGet = new ContextHandler();
        contextGet.setHandler(new TestServerHandlerGet());
        contextGet.setContextPath("/generic/getresource");

        ContextHandler contextPost = new ContextHandler();
        contextPost.setHandler(new TestServerHandlerPost());
        contextPost.setContextPath("/generic/postresource");
//        File dir1 = MavenTestingUtils.getTestResourceDir("dir1");
//        context1.setBaseResource(Resource.newResource(dir1));
//        context1.setHandler(rh1);

        // Create a ContextHandlerCollection and set the context handlers to it.
        // This will let jetty process urls against the declared contexts in
        // order to match up content.
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { contextGet, contextPost });
        server.setHandler(contexts);

        server.start();
        server.join();


        System.out.print(server.dump());
    }

    @AfterClass
    public static void serverTearDown() throws Exception {

        server.stop();
    }

    @Test
    public void testGetMethod() throws Exception {

    }
}
