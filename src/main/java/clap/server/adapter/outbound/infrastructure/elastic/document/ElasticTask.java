package clap.server.adapter.outbound.infrastructure.elastic.document;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

@Document(indexName = "task")
@Mapping(mappingPath = "elastic/task-mapping.json")
@Setting(settingPath = "elastic/task-setting.json")
@Getter
public class ElasticTask {
    private Long taskId;
    private String taskCode;
    private String mainCategory;
    private String subCategory;
    private String status;
    private String processor;
    private LocalDateTime createdAt;

    public ElasticTask(TaskEntity taskEntity) {
        this.taskId = taskEntity.getTaskId();
        this.taskCode = taskEntity.getTaskCode();
        this.mainCategory = taskEntity.getCategory().getMainCategory().getName();
        this.subCategory = taskEntity.getCategory().getName();
        this.status = taskEntity.getStatus().getName();
        this.processor = taskEntity.getProcessor().getMemberInfo().getName();
        this.createdAt = taskEntity.getCreatedAt();
    }
}
