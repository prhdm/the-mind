package client.request;

public  class Request  {
    public String authToken;
    public String url;
    public String body;

    public Request(String authToken,String url,String body) {
        this.authToken = authToken;
        this.url = url;
        this.body = body;
    }
}