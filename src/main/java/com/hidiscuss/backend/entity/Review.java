package com.hidiscuss.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "Review",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"reviewer_id", "discussion_id", "review_type"})
)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReviewDiff> commentDiffList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiveReviewDiff> liveDiffList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewThread> threadList = new ArrayList<>();

    @Setter
    @Column(columnDefinition ="boolean default false", name = "accepted", nullable = false)
    private Boolean accepted;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType;

    @Setter
    @Column(columnDefinition ="boolean default false", name = "isdone", nullable = false)
    private Boolean isdone;

    @Builder
    public Review(Long id, User reviewer, Discussion discussion, List<CommentReviewDiff> commentDiffList, List<LiveReviewDiff> liveDiffList, List<ReviewThread> threadList, Boolean accepted, ReviewType reviewType, Boolean isdone) {
        this.id = id;
        this.reviewer = reviewer;
        this.discussion = discussion;
        this.commentDiffList = commentDiffList;
        this.liveDiffList = liveDiffList;
        this.threadList = threadList;
        this.accepted = accepted;
        this.reviewType = reviewType;
        this.isdone = isdone;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewer=" + reviewer +
                ", discussion=" + discussion +
                ", commentDiffList=" + commentDiffList +
                ", liveDiffList=" + liveDiffList +
                ", threadList=" + threadList +
                ", accepted=" + accepted +
                ", reviewType=" + reviewType +
                ", isDone=" + isdone +
                '}';
    }

    public void setCommentDiffList(List<CommentReviewDiff> commentDiffList) {
        this.commentDiffList = commentDiffList;
    }

    public void setLiveDiffList(List<LiveReviewDiff> liveDiffList) {
        this.liveDiffList = liveDiffList;
    }
}
