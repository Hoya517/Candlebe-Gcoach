package com.candlebe.gcoach.dto;

import com.candlebe.gcoach.entity.Content;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HistoryDTO {

    private List<Content> contents;
    private LocalDateTime time;
}
