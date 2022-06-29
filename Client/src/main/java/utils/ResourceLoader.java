package utils;

public class ResourceLoader {
    public String resourceLoader(String path) {
        return  getClass().getResource(path).toString().substring(5);
    }
}
