package com.tuanhv.tripgoapi.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminItineraryForm {

    @NotNull(
            message = "Số ngày là bắt buộc"
    )
    @Min(
            value = 1,
            message = "Ngày phải bắt đầu từ 1"
    )
    private Integer dayNumber;

    @NotBlank(
            message = "Tiêu đề lịch trình là bắt buộc"
    )
    private String title;

    private String description;
}
