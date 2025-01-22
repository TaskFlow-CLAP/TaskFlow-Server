package clap.server.adapter.outbound.infrastructure.elastic.entity;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.time.LocalDate;

@Document(indexName = "task")
@Mapping(mappingPath = "elastic/task-mapping.json")
@Getter
public class ElasticTask {
    @Id
    private Long id;
    @Field(name="task_code")
    private String taskCode;
    @Field(name="main_category")
    private String mainCategory;
    @Field(name="sub_category")
    private String subCategory;
    @Field(name="status")
    private String status;
    @Field(name="processor")
    private String processor;
    @Field(name="created_at")
    private LocalDate createdAt;

    public ElasticTask(TaskEntity taskEntity) {
        this.id = taskEntity.getTaskId();
        this.taskCode = taskEntity.getTaskCode();
        this.mainCategory = taskEntity.getCategory().getMainCategory().getName();
        this.subCategory = taskEntity.getCategory().getName();
        this.status = taskEntity.getStatus().getStatusName().name().toLowerCase();
        this.processor = taskEntity.getProcessor().getNickname();
        this.createdAt = taskEntity.getCreatedAt().toLocalDate();
    }
}
