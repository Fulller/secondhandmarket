package com.secondhandmarket.dto.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int totalPages;
    private long totalElements;
}
