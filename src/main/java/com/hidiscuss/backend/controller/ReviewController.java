package com.hidiscuss.backend.controller;

import com.hidiscuss.backend.config.SecurityConfig;
import com.hidiscuss.backend.controller.dto.*;
import com.hidiscuss.backend.entity.*;
import com.hidiscuss.backend.service.ReviewService;
import com.hidiscuss.backend.utils.PageRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(SecurityConfig.DEFAULT_ROLE)
    @ApiOperation(value="comment review 생성", notes="이 api는 comment review를 생성합니다. requestBody로 온 comment review diff도 개수만큼 테이블에 저장합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "새로운 comment review 생성 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public CommentReviewResponseDto saveCommentReview(@RequestParam("type") ReviewType reviewType, @RequestBody @Valid CreateCommentReviewRequestDto requestDto) {
        // TODO : Inject the Authenticated User
        User user = User.builder().id(7000L).build();
        Review review = reviewService.createCommentReview(user, requestDto, reviewType);
        return CommentReviewResponseDto.fromEntity(review);
    }

    @PostMapping("{reviewId}/thread")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(SecurityConfig.DEFAULT_ROLE)
    @ApiOperation(value="thread 작성", notes="이 api는 comment review 하위에 thread를 저장합니다. thread는 discussion이 완료되어도 누구나 작성할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "새로운 thread 생성 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ThreadResponseDto saveThread(@PathVariable("reviewId") Long reviewId, @RequestBody @Valid CreateThreadRequestDto requestDto) {
        // TODO : Inject the Authenticated User
        User user = User.builder().id(7000L).build();
        Review review = reviewService.findByIdFetchOrNull(reviewId);
        ReviewThread reviewThread = reviewService.createThread(user, requestDto, review);
        return ThreadResponseDto.fromEntity(reviewThread);
    }

    @GetMapping("")
    @ApiOperation(value="review page 가져오기", notes="이 api는 page 단위로 review를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "review page 조회 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public Page<ReviewDto> getReviews(@RequestParam("discussionId") Long discussionId, @RequestParam("page") int page) {
        PageRequest pageRequest = new PageRequest(page, Sort.by("createdAt").descending());
        Page<Review> entityPage = reviewService.findAllByDiscussionIdFetch(discussionId, pageRequest.of());
        Page<ReviewDto> dtoPage = entityPage.map(i -> ReviewDto.fromEntity(i));
        return dtoPage;
    }


}
