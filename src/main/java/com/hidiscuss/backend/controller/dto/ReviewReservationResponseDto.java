package com.hidiscuss.backend.controller.dto;

import com.hidiscuss.backend.entity.ReviewReservation;
import com.hidiscuss.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewReservationResponseDto {

    private Long id;
    private UserResponseDto reviewer;
    //private ReviewResponseDto review;
    private DiscussionResponseDto discussion;
    private LocalDateTime reviewStartDateTime;
    private Boolean reviewerParticipated;
    private Boolean revieweeParticipated;
    private Boolean isdone;

    private ReviewReservationResponseDto(Long id, DiscussionResponseDto discussion, LocalDateTime reviewStartDateTime, Boolean reviewerParticipated, Boolean revieweeParticipated, Boolean isdone, User user) {
        this.id = id;
        this.discussion = discussion;
        this.reviewStartDateTime = reviewStartDateTime;
        this.reviewerParticipated = reviewerParticipated;
        this.revieweeParticipated = revieweeParticipated;
        this.isdone = isdone;
        this.reviewer = UserResponseDto.fromEntity(user);
        //this.review = review;
    }

    public static ReviewReservationResponseDto fromEntity(ReviewReservation reviewReservation) {
        return new ReviewReservationResponseDto(
                reviewReservation.getId(),
                DiscussionResponseDto.fromEntity(reviewReservation.getDiscussion()),
                reviewReservation.getReviewStartDateTime(),
                reviewReservation.getReviewerParticipated(),
                reviewReservation.getRevieweeParticipated(),
                reviewReservation.getIsdone(),
                reviewReservation.getReviewer()
        );
    }
}