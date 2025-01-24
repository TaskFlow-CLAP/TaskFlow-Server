package clap.server.adapter.inbound.web.member;

import clap.server.application.port.inbound.domain.FindManagersUsecase;
import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import lombok.RequiredArgsConstructor;
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
    public List<FindManagersResponse> findManagers() {

        List<FindManagersResponse> managers = findManagersUsecase.execute();

        if (managers.isEmpty()) {
            return FindManagersResponse.emptyListResponse();
        }

        return managers;
    }
}
