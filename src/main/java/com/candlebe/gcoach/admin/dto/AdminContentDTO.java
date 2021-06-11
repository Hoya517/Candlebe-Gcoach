package com.candlebe.gcoach.admin.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminContentDTO {
    private Long cid;
    private String title;
    private String content;
    private String category1;
    private String category2;
    private String category3;
}
