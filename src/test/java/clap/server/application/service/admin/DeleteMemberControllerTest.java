package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.admin.DeleteMemberController;
import clap.server.adapter.inbound.web.dto.admin.request.DeleteMemberRequest;
import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteMemberController.class)
class DeleteMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeleteMemberUsecase deleteMemberUsecase;

    @Test
    void deleteMember_ShouldReturn200_WhenRequestIsValid() throws Exception {
        // Given: 유효한 DeleteMemberRequest
        DeleteMemberRequest request = new DeleteMemberRequest(1L);

        // When: Mock된 DeleteMemberUsecase의 호출 설정
        Mockito.doNothing().when(deleteMemberUsecase).deleteMember(1L);

        // Then: API 호출 및 결과 검증
        mockMvc.perform(patch("/api/managements/members/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify: deleteMemberUsecase의 호출 확인
        Mockito.verify(deleteMemberUsecase, Mockito.times(1)).deleteMember(1L);
    }

    @Test
    void deleteMember_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        // Given: 유효하지 않은 DeleteMemberRequest (memberId가 null)
        DeleteMemberRequest invalidRequest = new DeleteMemberRequest(null);

        // Then: API 호출 및 결과 검증
        mockMvc.perform(patch("/api/managements/members/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify: deleteMemberUsecase가 호출되지 않음
        Mockito.verify(deleteMemberUsecase, Mockito.times(0)).deleteMember(Mockito.anyLong());
    }
}
