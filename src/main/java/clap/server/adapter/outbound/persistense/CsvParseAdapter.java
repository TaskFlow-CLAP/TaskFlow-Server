    package clap.server.adapter.outbound.persistense;

    import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
    import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
    import clap.server.common.annotation.architecture.WebAdapter;
    import clap.server.domain.model.member.Member;
    import org.springframework.stereotype.Component;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.util.ArrayList;
    import java.util.List;


    @Component
    public class CsvParseAdapter {

        public List<RegisterMemberRequest> parse(MultipartFile file) throws IOException {
            List<RegisterMemberRequest> memberRequests = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    String[] fields = line.split(",");

                    // 필드 검증
                    if (fields.length != 7) {
                        throw new IllegalArgumentException("CSV 데이터가 잘못되었습니다. " + lineNumber + "번째 줄");
                    }
                    try {
                        // DTO 생성
                        memberRequests.add(new RegisterMemberRequest(
                                fields[0].trim(), // name
                                fields[4].trim(), // email
                                fields[1].trim(), // nickname
                                Boolean.valueOf(fields[6].trim()), // isReviewer (Boolean 객체)
                                Long.valueOf(fields[2].trim()), // departmentId (Long 객체)
                                MemberRole.valueOf(fields[5].trim()), // role (enum)
                                fields[3].trim() // departmentRole
                        ));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("CSV 데이터 파싱 오류: " + lineNumber + "번째 줄, 내용: " + line, e);
                    }
                }
            }
            return memberRequests;
        }
    }
