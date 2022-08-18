package controllers;

import repositories.ServerRepository;
import server.models.Request;
import server.models.Response;

public interface RequestPerformer {

    ServerRepository serverRepository = ServerRepository.getInstance();
    Response performRequest(Request request);
}
