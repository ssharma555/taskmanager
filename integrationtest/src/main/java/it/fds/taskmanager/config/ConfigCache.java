package it.fds.taskmanager.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Config Object which holds the integrationtest properties
 * Its a Singleton class and use ConfigCache.getInstance() to get the instance of this class.
 */
public class ConfigCache {

  private String dbUrl;
  private String dbUser;
  private String dbPassword;
  private String baseurl;
  private String restclient;

  private static final String PROPERTY_FILE_PATH = "application.properties";
  private static ConfigCache configCache = null;

  private ConfigCache(){

  }

  public static ConfigCache getInstance(){

    if(configCache == null){
      synchronized (ConfigCache.class) {
        if(configCache == null){
          configCache = new ConfigCache();
          configCache.loadConfigs();
        }
      }
    }
    return configCache;
  }

  private void loadConfigs(){

    try {
      Properties properties = new Properties();
      InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_PATH);
      properties.load(in);
      this.setDbUrl(properties.getProperty("db.url"));
      this.setDbUser(properties.getProperty("db.username"));
      this.setDbPassword(properties.getProperty("db.password"));
      this.setBaseurl(properties.getProperty("baseurl"));
      this.setRestclient(properties.getProperty("restclient"));

    } catch (Exception e) {
      throw new RuntimeException("Exception while loading properties:" + e.getMessage(), e);

    }

  }

  public String getDbUrl() {
    return dbUrl;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public String getDbUser() {
    return dbUser;
  }

  public void setDbUser(String dbUser) {
    this.dbUser = dbUser;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }

  public String getBaseurl() {
    return baseurl;
  }

  public void setBaseurl(String baseurl) {
    this.baseurl = baseurl;
  }

  public String getRestclient() {
    return restclient;
  }

  public void setRestclient(String restclient) {
    this.restclient = restclient;
  }
}
