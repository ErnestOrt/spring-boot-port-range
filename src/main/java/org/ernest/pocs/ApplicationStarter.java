package org.ernest.pocs;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public abstract class ApplicationStarter extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

    @Value("${port.range.max:-1}")
    private int maxPort;

    @Value("${port.range.min:-1}")
    private int minPort;

    @Value("${server.port:-1}")
    private int port;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }
        };
        factory.setPort(choosePort(minPort, maxPort, port));
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        return factory;
    }

    private int choosePort(int minPort, int maxPort, int defaultPort) {
        if (minPort == -1 && maxPort == -1) {
            if (defaultPort >= 0) {
                return defaultPort;
            }
            return 8080;
        }

        for (int port = minPort; port < maxPort; port++) {
            if (available(port)) {
                return port;
            }
        }
        throw new IllegalArgumentException("Application can not start for port range " + minPort + " - " + maxPort);
    }

    private boolean available(int port) {
        logger.info("TRY PORT " + port);

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) { ds.close(); }
            if (ss != null) { try { ss.close(); } catch (IOException e) {}}
        }
        return false;
    }
}