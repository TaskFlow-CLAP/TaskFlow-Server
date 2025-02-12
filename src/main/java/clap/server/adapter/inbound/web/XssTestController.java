package clap.server.adapter.inbound.web;

import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.swagger.DevelopOnlyApi;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@WebAdapter
@RequestMapping("/api/xss-test")
public class XssTestController {

    @GetMapping
    @DevelopOnlyApi
    @Operation(summary = "파라미터 xss test")
    public ResponseEntity<String> testGetXss(@RequestParam String input) {
        log.info("Received GET input: {}", input);
        return ResponseEntity.ok("Processed GET input: " + input);
    }

    @PostMapping
    @DevelopOnlyApi
    @Operation(summary = "dto xss test")
    public ResponseEntity<XssTestResponse> testPostXss(@RequestBody XssTestRequest request) {
        log.info("Received POST input: {}", request);
        return ResponseEntity.ok(new XssTestResponse(request.content()));
    }
}
