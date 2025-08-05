package com.jns.app_manager.dtos;

import lombok.Builder;

@Builder
public record AuthenticateResponse(String token, String type){

}
