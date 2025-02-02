package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.domain.model.task.Label;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LoadLabelPort loadLabelPort;

    public Label findById(Long labelId) {
        return loadLabelPort.findById(labelId).orElseThrow(
                ()-> new ApplicationException(TaskErrorCode.LABEL_NOT_FOUND));
    }
}
