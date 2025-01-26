package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.application.port.inbound.domain.FindManagersUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Tag(name = "담당자 조회")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerController {

    private final FindManagersUsecase findManagersUsecase;

    @GetMapping("/managers")
    public List<FindManagersResponse> findManagers() {

        List<FindManagersResponse> managers = findManagersUsecase.execute();

        if (managers.isEmpty()) {
            return FindManagersResponse.emptyListResponse();
        }

        return managers;
    }
}
