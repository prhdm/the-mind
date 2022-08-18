package server;

import config.ConfigPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private int port;
    public Config() {
        try (InputStream input = new FileInputStream(ConfigPath.Config)) {
            Properties props = new Properties();
            props.load(input);
            port =  Integer.parseInt(props.getProperty("port"));
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    public int getPort() {
        return port;
    }
}
