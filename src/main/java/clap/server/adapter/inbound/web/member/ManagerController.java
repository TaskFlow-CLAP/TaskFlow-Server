package clap.server.adapter.inbound.web.member;

import clap.server.application.port.inbound.domain.FindManagersUsecase;
import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final FindManagersUsecase findManagersUsecase;

    @GetMapping
    public ResponseEntity<List<FindManagersResponse>> findManagers() {

        List<FindManagersResponse> managers = findManagersUsecase.execute();

        if (managers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(FindManagersResponse.emptyListResponse());
        }

        return ResponseEntity.ok(managers);
    }
}
