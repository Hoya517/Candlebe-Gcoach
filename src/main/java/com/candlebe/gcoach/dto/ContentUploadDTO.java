package com.candlebe.gcoach.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentUploadDTO {

    private String title;

    private String category1;
    private String category2;
    private String category3;

    private String path;
    private String originalName;

    private String imgPath;
    private String imgOriginalName;
}
