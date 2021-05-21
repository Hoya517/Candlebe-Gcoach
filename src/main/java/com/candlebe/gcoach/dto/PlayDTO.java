package com.candlebe.gcoach.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayDTO {

    // Member
    private Long mid;
    private String nickname;

    // Content
    private Long cid;
    private String contentName;
    private String contentType;
    private String imgOriginalName;

    int likeCount;
    boolean likeCheck;
}
