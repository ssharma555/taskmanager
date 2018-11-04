package it.fds.taskmanager.util;

import it.fds.taskmanager.clientlib.ClientFactory;
import it.fds.taskmanager.clientlib.RestClient;
import it.fds.taskmanager.config.ConfigCache;
import it.fds.taskmanager.dto.HTTPResponse;
import it.fds.taskmanager.dto.Task;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for /task API endpoint
 */
public class TaskApiUtil {

  private static ConfigCache configCache = ConfigCache.getInstance();
  private static final String TASK_CONTROLLER_BASE_URL = configCache.getBaseurl() + "/task";

  /**
   * Method to get the tasks returned by /task/list api
   * @return
   */
  public static HTTPResponse invokeTaskListApi(){
    RestClient client = ClientFactory.getRestClient();
    return client.get(TASK_CONTROLLER_BASE_URL + "/list");
  }

  /**
   * Method to get TaskId to Task map from /task/list api
   * @param time
   * @return
   */
  public static Map<String, Task> getTasksCreatedBeforeTime(Long time){
    List<Task> taskListFromApi = Arrays.asList(invokeTaskListApi().getObject(Task[].class));
    List<Task> filteredList = taskListFromApi.stream().filter(task -> task.getCreatedat() <= time).
        collect(Collectors.toList());
    Map<String, Task> taskIdMap = new HashMap<>();
    filteredList.forEach(task -> taskIdMap.put(task.getUuid(), task));
    return taskIdMap;
  }

  /**
   * Method to postpone a given task.
   * @param task
   * @param postponeTime
   * @return
   */
  public static HTTPResponse postponeTask(Task task, int postponeTime){
    RestClient client = ClientFactory.getRestClient();
    return client.post(String.format(TASK_CONTROLLER_BASE_URL+"/%s/postpone", task.getUuid()), postponeTime);
  }

}
