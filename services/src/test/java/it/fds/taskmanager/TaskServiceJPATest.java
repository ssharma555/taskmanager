package it.fds.taskmanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.sun.javaws.exceptions.InvalidArgumentException;
import it.fds.taskmanager.dto.TaskDTO;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Basic test suite to test the service layer, it uses an in-memory H2 database. 
 * 
 * TODO Add more and meaningful tests! :)
 *
 * @author <a href="mailto:damiano@searchink.com">Damiano Giampaoli</a>
 * @since 10 Jan. 2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration
public class TaskServiceJPATest {

    @Autowired
    TaskService taskService;

    private static final String NEW = TaskState.NEW.toString().toUpperCase();
    private static final String POSTPONED = TaskState.POSTPONED.toString().toUpperCase();
    private static final String RESTORED = TaskState.RESTORED.toString().toUpperCase();
    private static final String RESOLVED = TaskState.RESOLVED.toString().toUpperCase();

    @Test
    public void writeAndReadOnDB() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task1");
        t.setStatus(NEW);
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertThat(tOut.getTitle()).isEqualTo("Test task1");
        List<TaskDTO> list = taskService.showList();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void showList_shouldShowTasksInNewState(){

        TaskDTO task = new TaskDTO();
        task.setStatus(NEW);
        taskService.saveTask(task);
        assertThat(taskService.showList().size()).
            as(NEW+" task should be shown").
            isEqualTo(1);
    }

    @Test
    public void showList_shouldShowTasksInRestoredState(){

        TaskDTO task = new TaskDTO();
        task.setStatus(RESTORED);
        taskService.saveTask(task);
        assertThat(taskService.showList().size()).
            as(RESTORED+" task should be shown").
            isEqualTo(1);
    }

    @Test
    public void showList_shouldShowTasksInResolvedState(){

        TaskDTO task = new TaskDTO();
        task.setStatus(RESOLVED);
        taskService.saveTask(task);
        assertThat(taskService.showList().size()).
            as(RESOLVED+" task should be shown").
            isEqualTo(1);
    }

    @Test
    public void showList_shouldNotShowTasksInPostponedState(){

        TaskDTO task = new TaskDTO();
        task.setStatus(POSTPONED);
        taskService.saveTask(task);
        assertThat(taskService.showList().size()).
            as(POSTPONED+" task should not be shown").
            isEqualTo(0);
    }

    @Test
    public void listShouldBeEmptyWhenNoTasks(){

        assertThat(taskService.showList().size()).
            as("Task List should be empty when there are no tasks").
            isEqualTo(0);
    }

    @Test
    public void listShouldBeEmptyWhenOnlyPostponedTasks(){

        TaskDTO task = new TaskDTO();
        task.setStatus(POSTPONED);
        taskService.saveTask(task);
        assertThat(taskService.showList().size()).
            as("Task List should be empty when there are only postponed tasks").
            isEqualTo(0);
    }

    @Test
    public void findOne_shouldShowTaskForGivenId(){

        TaskDTO taskToSave = new TaskDTO();
        taskToSave.setStatus(NEW);
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        TaskDTO taskToValidate = taskService.findOne(savedTask.getUuid());
        assertThat(taskToValidate).
            as("Task should be fetched for given Id").
            isEqualToComparingFieldByField(savedTask);
    }

    @Test
    public void findOne_shouldShowTaskInPostponedState(){

        TaskDTO taskToSave = new TaskDTO();
        taskToSave.setStatus(POSTPONED);
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        TaskDTO taskToValidate = taskService.findOne(savedTask.getUuid());
        assertThat(taskToValidate).
            as("Postponed task should be fetched").
            isEqualToComparingFieldByField(savedTask);
    }

    @Test
    public void findOne_shouldReturnTaskWithCorrectProperties(){

        TaskDTO newTask = getNewTask();
        TaskDTO savedTask = taskService.saveTask(newTask);
        assertThat(taskService.findOne(savedTask.getUuid())).
            as("Each task should match field by field").
            isEqualToComparingFieldByField(savedTask);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_shouldFailForInvalidId(){

        taskService.findOne(UUID.randomUUID());
    }

    @Test
    public void saveTask_ShouldBeSavedWithProvidedProperties(){

        TaskDTO expectedTask = getNewTask();
        TaskDTO savedTask = taskService.saveTask(expectedTask);
        expectedTask.setUuid(savedTask.getUuid());
        assertThat(savedTask).
            as("Each task should match field by field").
            isEqualToComparingFieldByField(expectedTask);
    }

    @Test
    public void saveTask_shouldGenerateNewId(){

        TaskDTO taskToSave = new TaskDTO();
        taskToSave.setUuid(UUID.randomUUID());
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        assertThat(savedTask.getUuid()).
            as("New UUID should be generated").
            isNotEqualTo(taskToSave.getUuid());
    }

    @Test
    public void updateTask_shouldUpdateGivenProperties(){

        TaskDTO taskToSave = new TaskDTO();
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        TaskDTO taskToUpdate = getNewTask();
        taskToUpdate.setUuid(savedTask.getUuid());
        TaskDTO updatedTask = taskService.updateTask(taskToUpdate);
        taskToUpdate.setUpdatedat(updatedTask.getUpdatedat());
        assertThat(updatedTask).
            as("Each task should match field by field").
            isEqualToComparingFieldByField(taskToUpdate);
    }

    @Test
    public void updateTask_shouldAddUpdatedTime() {
        TaskDTO taskToSave = new TaskDTO();
        taskToSave.setUpdatedat(null);
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        Calendar currentDate = Calendar.getInstance();
        TaskDTO updatedTask = taskService.updateTask(savedTask);

        assertThat(updatedTask.getUpdatedat().getTimeInMillis()).
            as("Update task should add 'Updated time' to task").
            isCloseTo(currentDate.getTimeInMillis(), within(Long.valueOf(1000)));
    }

    /*
    Right now updateTask with non existent task id is creating a new Task.
    I think this causes ambiguity and should be avoided.
    Ideally in this case updateTask action should throw appropriate exception
    And REST end points can catch this exception and return 404(Resource not found) to client.
     */
    @Test(expected = Exception.class)
    public void updateTask_shouldFailForNonExistentTask(){

        TaskDTO taskToUpdate = new TaskDTO();
        taskToUpdate.setUuid(UUID.randomUUID());
        taskService.updateTask(taskToUpdate);
    }

    @Test
    public void resolveTask_shouldResolveGivenTask(){

        TaskDTO taskToSave = new TaskDTO();
        taskToSave.setStatus(NEW);
        TaskDTO savedTask = taskService.saveTask(taskToSave);
        Calendar currentDate = Calendar.getInstance();

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(taskService.resolveTask(savedTask.getUuid())).
            as("Task should be resolved successfully").
            isEqualTo(true);

        TaskDTO resolvedTask = taskService.findOne(savedTask.getUuid());
        softAssert.assertThat(resolvedTask.getStatus()).
            as("Task state should be "+RESOLVED).
            isEqualTo(RESOLVED);
        softAssert.assertThat(resolvedTask.getResolvedat().getTimeInMillis()).
            as("Resolve Date should be set").
            isCloseTo(currentDate.getTimeInMillis(), within(Long.valueOf(1000)));
        softAssert.assertAll();
    }

    /*
    Right now its failing with Null pointer exception when TaskId is not found.
    Ideally program should never fail with NPE and it should be handled appropriately
    Its better to throw custom exceptions or InvalidArgumentException
     */
    @Test(expected = InvalidArgumentException.class)
    public void resolveTask_shouldThrowInvalidArgumentExceptionForNonExistentTask(){

        taskService.resolveTask(UUID.randomUUID());
    }

    @Test
    public void postponeTask_shouldPostponeTheTask(){

        TaskDTO savedTask = taskService.saveTask(new TaskDTO());
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(taskService.postponeTask(savedTask.getUuid(), 5)).
            as("Task should be postponed").
            isEqualTo(true);
        TaskDTO postponedTask = taskService.findOne(savedTask.getUuid());
        softAssert.assertThat(postponedTask.getStatus()).
            as("State should be "+POSTPONED).
            isEqualTo(POSTPONED);
        softAssert.assertAll();
    }

    @Test
    public void postponeTask_shouldUpdatePostponeDate(){

        TaskDTO savedTask = taskService.saveTask(new TaskDTO());
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        int postponeTimeInMin = 5;
        int bufferTimeInMillis = 1000;

        taskService.postponeTask(savedTask.getUuid(), postponeTimeInMin);
        TaskDTO postponedTask = taskService.findOne(savedTask.getUuid());
        Long actualDate =  postponedTask.getPostponedat().getTimeInMillis();
        Long expectedDate = Long.valueOf(currentTime + postponeTimeInMin*60*1000);
        assertThat(actualDate).isCloseTo(expectedDate, within(Long.valueOf(bufferTimeInMillis)));
    }

    /*
    Right now its failing with Null pointer exception when TaskId is not found.
    Ideally program should never fail with NPE and it should be handled appropriately
    Its better to throw custom exceptions or InvalidArgumentException
     */
    @Test(expected = InvalidArgumentException.class)
    public void postponeTask_shouldThrowInvalidArgumentExceptionForExistentTask(){

        taskService.postponeTask(UUID.randomUUID(), 5);
    }

    @Test
    public void unmarkPostponed_shouldRestoreTasksWithPostponeDateInPast(){

        TaskDTO postponedTask = new TaskDTO();
        postponedTask.setStatus(POSTPONED);
        postponedTask.setPostponedat(Calendar.getInstance());
        TaskDTO savedTask = taskService.saveTask(postponedTask);
        taskService.unmarkPostoned();
        TaskDTO restoredTask = taskService.findOne(savedTask.getUuid());

        SoftAssertions softassert = new SoftAssertions();
        softassert.assertThat(restoredTask.getStatus()).
            as("Status should be "+RESTORED).
            isEqualTo(RESTORED);

        softassert.assertThat(restoredTask.getPostponedat()).
            as("PostponedDate should be set to null").
            isEqualTo(null);
        softassert.assertAll();
    }

    @Test
    public void unmarkpostponed_shouldNotRestoreTasksWithFuturePostponeDate(){

        TaskDTO postponedTask = new TaskDTO();
        postponedTask.setStatus(POSTPONED);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        postponedTask.setPostponedat(calendar);
        TaskDTO savedTask = taskService.saveTask(postponedTask);
        taskService.unmarkPostoned();
        TaskDTO originalTask = taskService.findOne(savedTask.getUuid());
        assertThat(originalTask.getStatus()).
            as("Status should be "+POSTPONED).
            isEqualTo(POSTPONED);
    }

    @Test
    public void unparkPostponed_shouldNotImpactNewTask(){
        TaskDTO newTask = new TaskDTO();
        newTask.setStatus(NEW);
        TaskDTO savedTask = taskService.saveTask(newTask);
        taskService.unmarkPostoned();
        assertThat(taskService.findOne(savedTask.getUuid()).getStatus()).
            as("Status should be "+NEW).
            isEqualTo(NEW);
    }

    @Test
    public void unparkPostponed_shouldNotImpactRestoredTask(){
        TaskDTO newTask = new TaskDTO();
        newTask.setStatus(RESTORED);
        TaskDTO savedTask = taskService.saveTask(newTask);
        taskService.unmarkPostoned();
        assertThat(taskService.findOne(savedTask.getUuid()).getStatus()).
            as("Status should be "+RESTORED).
            isEqualTo(RESTORED);
    }

    @Test
    public void unparkPostponed_shouldNotImpactResolvedTask(){
        TaskDTO newTask = new TaskDTO();
        newTask.setStatus(RESOLVED);
        TaskDTO savedTask = taskService.saveTask(newTask);
        taskService.unmarkPostoned();
        assertThat(taskService.findOne(savedTask.getUuid()).getStatus()).
            as("Status should be "+RESOLVED).
            isEqualTo(RESOLVED);
    }


    private TaskDTO getNewTask(){
        TaskDTO taskDTO = new TaskDTO();
        Calendar calendar = Calendar.getInstance();
        taskDTO.setUuid(UUID.randomUUID());
        taskDTO.setCreatedat(calendar);
        taskDTO.setUpdatedat(calendar);
        calendar.add(Calendar.HOUR, 1);
        taskDTO.setDuedate(calendar);
        taskDTO.setPostponedat(calendar);
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Creating new Task");
        taskDTO.setPriority("5");
        taskDTO.setStatus(TaskState.NEW.toString().toUpperCase());
        taskDTO.setPostponedtime(null);
        taskDTO.setResolvedat(null);

        return taskDTO;
    }

    @EnableJpaRepositories
    @Configuration
    @SpringBootApplication
    public static class EndpointsMain{}
}
