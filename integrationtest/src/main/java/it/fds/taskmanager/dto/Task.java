package it.fds.taskmanager.dto;

public class Task {

  private String uuid;
  private Long createdat;
  private Long updatedat;
  private Long duedate;
  private Long resolvedat;
  private Long postponedat;
  private Long postponedtime;
  private String title;
  private String description;
  private String priority;
  private String status;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Long getCreatedat() {
    return createdat;
  }

  public void setCreatedat(Long createdat) {
    this.createdat = createdat;
  }

  public Long getUpdatedat() {
    return updatedat;
  }

  public void setUpdatedat(Long updatedat) {
    this.updatedat = updatedat;
  }

  public Long getDuedate() {
    return duedate;
  }

  public void setDuedate(Long duedate) {
    this.duedate = duedate;
  }

  public Long getResolvedat() {
    return resolvedat;
  }

  public void setResolvedat(Long resolvedat) {
    this.resolvedat = resolvedat;
  }

  public Long getPostponedat() {
    return postponedat;
  }

  public void setPostponedat(Long postponedat) {
    this.postponedat = postponedat;
  }

  public Long getPostponedtime() {
    return postponedtime;
  }

  public void setPostponedtime(Long postponedtime) {
    this.postponedtime = postponedtime;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
