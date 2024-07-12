package com.kayukin.reportviewer.dto;

import lombok.Builder;

@Builder
public record Report(
        String name,
        String id
) {
}
