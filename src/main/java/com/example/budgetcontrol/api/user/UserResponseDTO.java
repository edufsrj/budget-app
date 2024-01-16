package com.example.budgetcontrol.api.user;

import com.example.budgetcontrol.infra.base.Status;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDTO(
        UUID id,
        String nickName,
        String email,
        String password,
        Status status
) {}
