package com.jns.app_manager.dtos.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    String oldPassword;
    String newPassword;
}
