package it.fds.taskmanager.clientlib;

import it.fds.taskmanager.config.ConfigCache;

/**
 * Client Factory to get the desired Rest Client
 * Note : Right now only RestAssured Client is implemented.
 */
public class ClientFactory {

  private static ConfigCache configCache = ConfigCache.getInstance();
  public static RestClient getRestClient(){

    String clientType = configCache.getRestclient().toUpperCase();
    if(clientType.equals(ClientTypes.RESTASSURED.toString())){
      return new RestAssuredClient();
    }

    return new RestAssuredClient();
  }
}
