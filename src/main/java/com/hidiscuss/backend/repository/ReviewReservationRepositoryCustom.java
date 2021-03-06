package com.hidiscuss.backend.repository;

import com.hidiscuss.backend.entity.ReviewReservation;

import java.util.List;

public interface ReviewReservationRepositoryCustom {
    List<ReviewReservation> findByDiscussionId(Long discussionId);

    List<ReviewReservation> findByUserId(Long userId);

    ReviewReservation findByReservationId(Long resrvationId);
}
