package com.candlebe.gcoach.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    // Member
    private Long mid;
    private String nickname;

    // content
    private Long cid;

    // Reply
    private Long rid;
    private String text;
    private LocalDateTime regDate, modDate;
}
