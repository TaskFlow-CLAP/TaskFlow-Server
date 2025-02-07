package clap.server.application.mapper;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

import static clap.server.domain.model.task.Attachment.createAttachment;

public class AttachmentMapper {
    private AttachmentMapper() {
        throw new IllegalArgumentException();
    }

    public static List<Attachment> toTaskAttachments(Task task, List<MultipartFile> files, List<String> fileUrls) {
        return IntStream.range(0, files.size())
                .mapToObj(i -> createAttachment(
                        task,
                        files.get(i).getOriginalFilename(),
                        fileUrls.get(i),
                        files.get(i).getSize()
                ))
                .toList();
    }

}

