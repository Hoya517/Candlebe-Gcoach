package com.candlebe.gcoach.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {

    private Long cid;

    private String creator;

    private String title;

    private String path;

    private String type;
}
