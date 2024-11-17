package com.secondhandmarket.dto.option;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class OptionRequest {
    @NotBlank(message = "Bạn chưa nhập tên option")
    private String name;
}
