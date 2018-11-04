package it.fds.taskmanager.dto;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import org.dalesbred.result.RowMapper;

/**
 * Custom Rowmapper for mapping Task Table to Java Object.
 */
public class TaskRowMapper implements RowMapper<Task> {

  @Override
  public Task mapRow(ResultSet rs) throws SQLException {

    Task task = new Task();
    ByteBuffer bb = ByteBuffer.wrap(rs.getBytes("uuid"));
    long high = bb.getLong();
    long low = bb.getLong();
    UUID uuid = new UUID(high, low);

    task.setUuid(uuid.toString());
    task.setTitle(rs.getString("title"));
    task.setDescription(rs.getString("description"));
    task.setPriority(rs.getString("priority"));
    task.setStatus(rs.getString("status"));
    task.setCreatedat(getTime(rs.getTimestamp("createdat")));
    task.setDuedate(getTime(rs.getTimestamp("duedate")));
    task.setPostponedat(getTime(rs.getTimestamp("postponedat")));
    task.setPostponedtime(getTime(rs.getTimestamp("postponedtime")));
    task.setResolvedat(getTime(rs.getTimestamp("resolvedat")));
    task.setUpdatedat(getTime(rs.getTimestamp("updatedat")));
    return task;
  }

  private Long getTime(Timestamp timestamp){
    return timestamp != null ? timestamp.getTime() : null;

  }

}
