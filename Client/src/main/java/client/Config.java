package client;

import utils.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public String host;
    public int port;
    public Config() {
        try (InputStream input = new FileInputStream(new ResourceLoader().resourceLoader("/config/config.properties"))) {
            Properties props = new Properties();
            props.load(input);
            host = props.getProperty("host");
            port =  Integer.parseInt(props.getProperty("port"));
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
