package clap.server.adapter.inbound.web.task;

import clap.server.application.port.inbound.task.FindManagersUsecase;
import clap.server.adapter.inbound.web.dto.task.response.FindManagersResponse;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@WebAdapter
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {
    private final FindManagersUsecase findManagersUsecase;

    @GetMapping
    public ResponseEntity<List<FindManagersResponse>> findManagers() {
        return ResponseEntity.ok(findManagersUsecase.findManagers());
    }
}
