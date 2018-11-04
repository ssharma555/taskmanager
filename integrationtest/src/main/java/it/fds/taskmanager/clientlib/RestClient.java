package it.fds.taskmanager.clientlib;

import it.fds.taskmanager.dto.HTTPResponse;

public interface RestClient {

  HTTPResponse get(String endPoint);
  HTTPResponse post(String endPoint, Object body);
  HTTPResponse put(String endPoint, Object body);
  HTTPResponse delete(String endPoint);
}
