package it.fds.taskmanager.clientlib;

import com.jayway.restassured.response.Response;
import it.fds.taskmanager.dto.HTTPResponse;
import org.apache.log4j.Logger;

import static com.jayway.restassured.RestAssured.given;

public class RestAssuredClient implements RestClient {

  private static final Logger logger = Logger.getLogger(RestAssuredClient.class);

  private HTTPResponse parseResponse(Response response){
    HTTPResponse httpResponse = new HTTPResponse();
    httpResponse.setStatusCode(response.getStatusCode());
    httpResponse.setBody(response.body().asString());
    return httpResponse;
  }

  @Override
  public HTTPResponse get(String endPoint) {

    Response response = given().when().get(endPoint);
    logger.debug("Response from endpoint "+endPoint+" is "+response.body().asString());
    return parseResponse(response);
  }

  @Override
  public HTTPResponse post(String endPoint, Object body) {
    Response response = given().when().body(body).contentType("application/json").
        post(endPoint);
    logger.debug("Response from endpoint "+endPoint+" is "+response.body().asString());
    return parseResponse(response);
  }

  @Override
  public HTTPResponse put(String endPoint, Object body) {

    Response response = given().when().body(body).contentType("application/json").
        put(endPoint);
    logger.debug("Response from endpoint "+endPoint+" is "+response.body().asString());
    return parseResponse(response);
  }

  @Override
  public HTTPResponse delete(String endPoint) {

    Response response = given().when().delete(endPoint);
    logger.debug("Response from endpoint "+endPoint+" is "+response.body().asString());
    return parseResponse(response);
  }
}
