package clap.server.adapter.outbound.infrastructure.elastic.document;

import clap.server.domain.model.task.Task;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.time.LocalDateTime;

@Document(indexName = "task")
@Mapping(mappingPath = "elastic/task-mapping.json")
@Getter
public class TaskDocument {
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
    private LocalDateTime createdAt;

    public TaskDocument(Task taskEntity) {
        this.id = taskEntity.getTaskId();
        this.taskCode = taskEntity.getTaskCode();
        this.mainCategory = taskEntity.getCategory().getMainCategory().getName();
        this.subCategory = taskEntity.getCategory().getName();
        this.status = taskEntity.getTaskStatus().name().toLowerCase();
        this.processor = taskEntity.getProcessor().getMemberInfo().getNickname();
        this.createdAt = taskEntity.getCreatedAt();
    }
}
