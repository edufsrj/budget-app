package com.example.budgetcontrol.domain.user;

import com.example.budgetcontrol.infra.base.Status;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDTO(
        UUID id,
        String nickName,
        String email,
        String password,
        Status status
) {}
