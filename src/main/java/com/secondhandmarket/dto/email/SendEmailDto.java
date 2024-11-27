package com.secondhandmarket.dto.email;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendEmailDto {
    String to;
    String subject;
    String text;
}
