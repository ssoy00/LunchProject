package com.ksy1028.ksylunchproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 유효성 체크 하기 위한 도구.
public class LunchDTO {
    private Long mno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
