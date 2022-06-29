import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public int port;
    public Config() {
        try (InputStream input = new FileInputStream(new File("").getAbsolutePath() + "/src/main/resources/config/config.properties")) {
            Properties props = new Properties();
            props.load(input);
            port =  Integer.parseInt(props.getProperty("port"));
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
