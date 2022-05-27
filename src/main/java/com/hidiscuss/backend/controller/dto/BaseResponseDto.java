package com.hidiscuss.backend.controller.dto;

import com.hidiscuss.backend.entity.BaseEntity;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class BaseResponseDto {
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;

    public void setBaseResponse(BaseEntity entity) {
        this.createdAt = entity.getCreatedAt();
        this.lastModifiedAt = entity.getLastModifiedAt();
    }
}
