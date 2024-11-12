package com.secondhandmarket.dto.auth;

import lombok.Data;

@Data
public class AuthUserInfoResponse {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private Double rating;
    private boolean isFromOutside;
}
