package com.hidiscuss.backend.controller.dto;

import com.hidiscuss.backend.entity.LiveReviewDiff;
import lombok.Getter;

@Getter
public class LiveReviewDiffResponseDto extends ReviewDiffResponseDto {
    public static LiveReviewDiffResponseDto fromEntity(LiveReviewDiff entity) {
        LiveReviewDiffResponseDto dto = new LiveReviewDiffResponseDto();
        dto.id = entity.getId();
        dto.discussionCode = DiscussionCodeDto.fromEntity(entity.getDiscussionCode());
        dto.codeAfter = entity.getCodeAfter();
        return dto;
    }
}