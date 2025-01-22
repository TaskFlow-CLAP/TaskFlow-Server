package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.domain.model.task.Category;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final LoadCategoryPort loadCategoryPort;

    public Category findById(Long categoryId) {
        return loadCategoryPort.findById(categoryId).orElseThrow(
                ()-> new ApplicationException(TaskErrorCode.CATEGORY_NOT_FOUND));
    }
}
