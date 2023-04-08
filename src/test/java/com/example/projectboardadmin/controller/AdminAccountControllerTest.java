package com.example.projectboardadmin.controller;

import com.example.projectboardadmin.config.GlobalControllerConfig;
import com.example.projectboardadmin.config.SecurityConfig;
import com.example.projectboardadmin.config.TestSecurityConfig;
import com.example.projectboardadmin.domain.constant.RoleType;
import com.example.projectboardadmin.dto.AdminAccountDto;
import com.example.projectboardadmin.service.AdminAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민 회원")
@Import({SecurityConfig.class, GlobalControllerConfig.class})
//@Import(TestSecurityConfig.class) // Duplicate mock definition
@WebMvcTest(AdminAccountController.class)
@ActiveProfiles("testdb")
class AdminAccountControllerTest {

    private final MockMvc mvc;
    @MockBean
    private AdminAccountService adminAccountService;

    public AdminAccountControllerTest(
            @Autowired  MockMvc mvc
    ) {
        this.mvc = mvc;
    }

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createAdminAccountDto()));
        given(adminAccountService.saveUser(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
                .willReturn(createAdminAccountDto());
    }

    @WithMockUser(username = "tester", roles = "USER") // 인증정보를 구분할 필요가 없을때, 인증이 되었다고 signal 을 보내는 것과 동일한 효과
    @DisplayName("[view][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 호환되는 타입까지 매칭 : text/html;charset=UTF-8
                .andExpect(view().name("admin/members"))
        ;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 어드민 회원 리스트 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingAdminMembers_thenReturnsAdminMembers() throws Exception {
        // Given
        given(adminAccountService.users()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(adminAccountService).should().users();
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[data][DELETE] 어드민 회원 삭제 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenDeletingAdminMember_thenDeletesAdminMember() throws Exception {
        // Given
        String username = "jyuka";
        willDoNothing().given(adminAccountService).deleteUser(username);

        // When & Then
        mvc.perform(
                        delete("/api/admin/members/" + username)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());
        then(adminAccountService).should().deleteUser(username);
    }


    private AdminAccountDto createAdminAccountDto() {
        return AdminAccountDto.of(
                "jyukaTest",
                "pw",
                Set.of(RoleType.USER),
                "jyuka-test@email.com",
                "jyuka-test",
                "test memo"
        );
    }


}