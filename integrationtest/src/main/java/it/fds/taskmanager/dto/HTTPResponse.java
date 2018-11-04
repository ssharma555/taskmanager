package it.fds.taskmanager.dto;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import java.lang.reflect.Type;


public class HTTPResponse {

  private int StatusCode;
  private String body;

  public int getStatusCode() {
    return StatusCode;
  }

  public String getBody() {
    return body;
  }

  public void setStatusCode(int statusCode) {
    StatusCode = statusCode;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public <T> T getObject(Class<T> classOfT) {
    Object object = new Gson().fromJson(this.body, (Type) classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }

}
