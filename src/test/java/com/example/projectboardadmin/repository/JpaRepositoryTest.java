package com.example.projectboardadmin.repository;


import com.example.projectboardadmin.domain.UserAccount;
import com.example.projectboardadmin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 * @DataJpaTest 사용시 자동으로 지정된 embedded db 의 데이터베이스를 참조
 * 즉 테스트시 h2 db가 아닌 mysql, oracle등을 사용할때 사용 (직접 테스트 디비를 지정하는 방법)
 * 해당 설정은 yml에서 관리가 가능하기 때문에 여기서는 사용하지 않는다.
 */

/**
 * Auditing 사용 시 SecurityContextHolder에서 사용자 정보를 불러오도록 설정한 이후 문제 발생
 * insert 에서 createdBy 정보를 불러 오지 못함
 * @DataJpaTest 사용한 Jpa 에 대한 슬라이스 테스트로 security context 정보를 불러오지 못하기 때문
 * 기존 @Import(JpaConfig.class) 에서 변경
 */
@DisplayName("JPA 연결 테스트")
@ActiveProfiles("testdb")
@DataJpaTest
@Import(JpaRepositoryTest.TestJpaConfig.class)
class JpaRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenUserAccount_whenSelecting_thenWorksFine(){
        // Given

        // When
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // Then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4)
        ;
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenUserAccount_whenInserting_thenWorksFine() {
        // Given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        // When
        userAccountRepository.save(userAccount);

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() {
        // Given
        UserAccount userAccount = userAccountRepository.getReferenceById("jyuka");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);


        /**
         * saveAndFlush 가 사용된 이유??
         * @DataJpaTest 의 경우 각 메소드 단위로 롤백
         * Jpa 입장에서 결국 롤백되기 때문에 update 실행 하지 않음
         * update 문을 확인하고자 saveAndFlush 사용
         */
        // When
        UserAccount updatedAccount = userAccountRepository.saveAndFlush(userAccount);

        // Then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "jyuka")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenUserAccount_whenDeleting_thenWorksFine() {
        // Given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.getReferenceById("jyuka");

        // When
        userAccountRepository.delete(userAccount);

        // Then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
    }


    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("jyuka");
        }
    }


}