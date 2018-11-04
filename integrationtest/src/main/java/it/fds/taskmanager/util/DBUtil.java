package it.fds.taskmanager.util;

import it.fds.taskmanager.config.ConfigCache;
import it.fds.taskmanager.dto.Task;
import it.fds.taskmanager.dto.TaskRowMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.dalesbred.Database;

/**
 * Utility class for interacting with Database
 */
public class DBUtil {

  private static ConfigCache configCache = ConfigCache.getInstance();
  private static Database db = Database.forUrlAndCredentials(configCache.getDbUrl(),
      configCache.getDbUser(), configCache.getDbPassword());

  /**
   * Returns all the tasks which are not in POSTPONED state and created before given calendar instance
   * @param calendar
   * @return
   */
  public static List<Task> getTasksCreatedBeforeTime(Calendar calendar){

    TimeZone tz = calendar.getTimeZone();
    ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
    LocalDateTime time = LocalDateTime.ofInstant(calendar.toInstant(), zid);
    String query = "SELECT * from task where status != 'POSTPONED' and createdat < ?";
    List<Task> taskList = db.findAll(new TaskRowMapper(), query, time);
    return taskList;
  }

}
