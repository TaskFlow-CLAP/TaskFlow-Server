package clap.server.application.mapper;


import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Status;
import clap.server.domain.model.task.Task;

public class TaskMapper {
    private TaskMapper() {
        throw new IllegalArgumentException();
    }

    public static Task toTask(Member member, Category category, String title, String description, Status status) {
        return Task.builder()
                .title(title)
                .description(description)
                .category(category)
                .requester(member)
                .status(status)
                .taskCode("1234") //TODO: 하드코딩 제거, reviewer_id 명시 필요
                .build();
    }
}
