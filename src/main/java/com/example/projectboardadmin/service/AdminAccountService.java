package com.example.projectboardadmin.service;

import com.example.projectboardadmin.domain.AdminAccount;
import com.example.projectboardadmin.domain.constant.RoleType;
import com.example.projectboardadmin.dto.AdminAccountDto;
import com.example.projectboardadmin.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;

    @Transactional(readOnly = true)
    public Optional<AdminAccountDto> searchUser(String username){

        return adminAccountRepository.findById(username)
                .map(AdminAccountDto::from);
    }

    public AdminAccountDto saveUser(
            String username,
            String password,
            Set<RoleType> roleTypes,
            String email,
            String nickname,
            String memo
    ){
        return AdminAccountDto.from(
                adminAccountRepository.save(AdminAccount.of(username, password, roleTypes, email, nickname, memo, username))
        );
    }

    @Transactional(readOnly = true)
    public List<AdminAccountDto> users(){
        return adminAccountRepository.findAll().stream()
                .map(AdminAccountDto::from)
                .toList();
    }

    public void deleteUser(String username){
        adminAccountRepository.deleteById(username);
    }
}
