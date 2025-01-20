package clap.server.application.mapper;


import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;

public class TaskMapper {
    private TaskMapper() {
        throw new IllegalArgumentException();
    }

    public static Task toTask(Member member, Category category, String title, String description) {
        return Task.builder()
                .title(title)
                .description(description)
                .category(category)
                .requester(member)
                .build();
    }
}
