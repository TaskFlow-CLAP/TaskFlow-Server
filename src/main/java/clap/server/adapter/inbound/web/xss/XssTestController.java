package clap.server.adapter.inbound.web.xss;

import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.swagger.DevelopOnlyApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@WebAdapter
@RequestMapping("/api/xss-test")
@Tag(name = "xss 공격 테스트 API", description = "아래와 같은 페이로드들에 대해 테스트합니다.\n" +
        "1. 기본적인 스크립트 삽입: `<script>alert('xss')</script>`\n" +
        "2. 이미지 태그를 이용한 XSS: `<img src=x onerror=alert('xss')>`\n" +
        "3. JavaScript 프로토콜: `javascript:alert('xss')`\n" +
        "4. HTML 이벤트 핸들러:` <div onmouseover=\"alert('xss')\">hover me</div>`\n" +
        "5. SVG를 이용한 XSS: `<svg><script>alert('xss')</script></svg>`\n" +
        "6. HTML5 태그를 이용한 XSS: `<video><source onerror=\"alert('xss')\">`")
public class XssTestController {

    @GetMapping
    @DevelopOnlyApi
    @Operation(summary = "단일 파라미터 test")
    public ResponseEntity<String> testGetXss(@RequestParam String input) {
        log.info("Received GET input: {}", input);
        return ResponseEntity.ok("Processed GET input: " + input);
    }

    @PostMapping
    @DevelopOnlyApi
    @Operation(summary = "dto test")
    public ResponseEntity<XssTestResponse> testPostXss(@RequestBody XssTestRequest request) {
        log.info("Received POST input: {}", request);
        return ResponseEntity.ok(new XssTestResponse(request.content()));
    }

    @GetMapping("/multi-params")
    @Operation(summary = "다중 파라미터 테스트")
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
