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
    @Operation(summary = "단일 파라미터 xss test")
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

    @GetMapping("/multi-params")
    @Operation(summary = "다중 파라미터 XSS 테스트")
    public ResponseEntity<String> testMultiParamXss(@RequestParam(value = "inputs", required = false) String[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return ResponseEntity.badRequest().body("No inputs provided");
        }

        StringBuilder response = new StringBuilder("Processed inputs:\n");
        for (int i = 0; i < inputs.length; i++) {
            log.info("Received input {}: {}", i, inputs[i]);
            response.append("Input ").append(i).append(": ").append(inputs[i]).append("\n");
        }

        return ResponseEntity.ok(response.toString());
    }
}
