package com.example.budgetcontrol.infra.user;

import com.example.budgetcontrol.infra.base.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@Table("users")
public class UserEntity {

    @Id
    @Column("id")
    private UUID id;
    private String nickName;
    private String email;
    private String password;
    private Status status;
    @Column("creation_time")
    protected LocalDateTime creationTime;
    @Column("update_time")
    protected LocalDateTime updateTime;
}
