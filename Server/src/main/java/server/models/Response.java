package server.models;

public class Response {
    private int status;
    private String body;

    public Response(int status , String body) {
        this.status = status;
        this.body = body;
    }
}
