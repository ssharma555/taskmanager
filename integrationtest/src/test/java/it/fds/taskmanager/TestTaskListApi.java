package it.fds.taskmanager;

import static org.assertj.core.api.Assertions.assertThat;

import it.fds.taskmanager.dto.HTTPResponse;
import it.fds.taskmanager.dto.Task;
import it.fds.taskmanager.util.DBUtil;
import it.fds.taskmanager.util.TaskApiUtil;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestTaskListApi {

  private static final Logger logger = Logger.getLogger(TestTaskListApi.class);

  @BeforeClass
  public void checkPreRequisiteToRunTest(){
    Map<String, Task> taskIdMap = TaskApiUtil.getTasksCreatedBeforeTime(
        Calendar.getInstance().getTimeInMillis());

    if (taskIdMap.size() == 0){
      logger.error("Skipping the test as Task list is Empty");
      throw new SkipException("This test cannot be executed as task list is empty. "
          + "Please run scheduler before starting the test");
    }
  }

  /**
   * Test to check that /task/list api is showing all the valid Tasks as expected.
   * Test steps :
   * 1) Get list of tasks from DB which are not in postponed state
   * 2) Get list of tasks from /task/list api
   * 3) Compare both the list for equality and do field by field comparison for each
   *    Task object in the lists.
   */
  @Test
  public void testTaskListApiShowsAllTasksAsExpected(){

    //here 2 mins is used as a buffer to filter out the new tasks which could have been created by
    // scheduler
    Calendar currentTime = Calendar.getInstance();
    currentTime.add(Calendar.MINUTE, -2);

    List<Task> taskListFromDB = DBUtil.getTasksCreatedBeforeTime(currentTime);
    logger.info("DB list size = "+taskListFromDB.size());

    Map<String, Task> taskIdMap = TaskApiUtil.getTasksCreatedBeforeTime(
        currentTime.getTimeInMillis());
    logger.info("Api List Size = "+taskIdMap.size());

    SoftAssertions softassert = new SoftAssertions();
    softassert.assertThat(taskIdMap.size()).
        as("Number of Tasks from API should be equal to that from DB").
        isEqualTo(taskListFromDB.size());

    for(Task task : taskListFromDB){

      Task taskFromApi = taskIdMap.getOrDefault(task.getUuid(), new Task());
      softassert.assertThat(task).
          as("All the fields should match exactly").
          isEqualToComparingFieldByField(taskFromApi);  //Compare all the fields in Task object
    }
    softassert.assertAll();
  }

  /**
   * Test to check that /task/list api is not listing postponed tasks
   * Test steps :
   * 1) Get list of tasks from /task/list api
   * 2) Postpone a randomly selected task from task list
   * 3) Check that postponed tasks are not listed by /task/list api
   */
  @Test
  public void testTaskListApiDoesntShowPostponedTasks(){
    Map<String, Task> taskIdMap = TaskApiUtil.getTasksCreatedBeforeTime(
        Calendar.getInstance().getTimeInMillis());

    Task taskToPostpone = taskIdMap.get(taskIdMap.keySet().toArray()[0]);
    logger.info("Postponing task with Id : "+taskToPostpone.getUuid());
    HTTPResponse response = TaskApiUtil.postponeTask(taskToPostpone, 10);

    assertThat(response.getStatusCode()).as("Failed to Postpone task").isEqualTo(200);

    taskIdMap = TaskApiUtil.getTasksCreatedBeforeTime(Calendar.getInstance().getTimeInMillis());
    assertThat(taskIdMap.getOrDefault(taskToPostpone.getUuid(), null)).
        as("Postponed task should not be listed").
        isEqualTo(null);
  }
}

