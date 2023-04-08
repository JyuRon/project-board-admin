package com.example.projectboardadmin.controller;

import com.example.projectboardadmin.dto.response.UserAccountResponse;
import com.example.projectboardadmin.service.UserAccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/management/user-accounts")
public class UserAccountManagementController {

    private final UserAccountManagementService userAccountManagementService;

    @GetMapping
    public String userAccounts(Model model) {

        model.addAttribute("userAccounts", userAccountManagementService
                .getUserAccounts().stream()
                .map(UserAccountResponse::from)
                .toList()
        );
        return "management/user-accounts";
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public UserAccountResponse userAccount(@PathVariable String userId){
        return UserAccountResponse.from(
                userAccountManagementService.getUserAccount(userId)
        );

    }
    @PostMapping("/{userId}")
    public String deleteUserAccount(@PathVariable String userId) {
        userAccountManagementService.deleteUserAccount(userId);

        return "redirect:/management/user-accounts";
    }

}
