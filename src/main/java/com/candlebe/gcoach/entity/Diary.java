package com.candlebe.gcoach.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Diary extends BaseEntity {

    // Pk
    // date
    // userid( 연관관계 )
    // emotion
    // review
    // todayContents

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emotion;  // 오늘의 감정

    private String todaysReview;  // 후기

    private String year;
    private String month;
    private String date;


    @ManyToOne
    @JoinColumn(name = "mid")
    private Member member;

    public Diary(String emotion, String todaysReview, Member member, String year, String month, String date) {
        this.emotion = emotion;
        this.todaysReview = todaysReview;
        this.member = member;
        this.year = year;
        this.month = month;
        this.date = date;
    }
}

