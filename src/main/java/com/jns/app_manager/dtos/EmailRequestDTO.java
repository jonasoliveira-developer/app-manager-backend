package com.jns.app_manager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDTO {
    private String to;
    private String subject;
    private String body;
    private String userName;
    private String password;

}