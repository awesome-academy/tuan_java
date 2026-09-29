package com.tuanhv.tripgoapi.dto.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminTourForm {

    @NotBlank(
            message = "Tên tour là bắt buộc"
    )
    @Size(
            max = 255,
            message = "Tên tour tối đa 255 ký tự"
    )
    private String title;

    @NotBlank(
            message = "Slug là bắt buộc"
    )
    @Size(max = 255)
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Slug chỉ gồm chữ thường, số và dấu -"
    )
    private String slug;

    @NotNull(
            message = "Điểm đến là bắt buộc"
    )
    private Long destinationId;

    @NotNull(
            message = "Loại hình là bắt buộc"
    )
    private Long categoryId;

    @NotNull(
            message = "Giá là bắt buộc"
    )
    @DecimalMin(
            value = "0.01",
            message = "Giá phải lớn hơn 0"
    )
    private BigDecimal price;

    @DecimalMin(
            value = "0.01",
            message = "Giá khuyến mại phải lớn hơn 0"
    )
    private BigDecimal discountPrice;

    @NotNull(
            message = "Thời lượng là bắt buộc"
    )
    @Min(
            value = 1,
            message = "Thời lượng phải từ 1 ngày"
    )
    private Integer durationDays;

    @NotNull(
            message = "Số khách tối đa là bắt buộc"
    )
    @Min(
            value = 1,
            message = "Số khách tối đa phải từ 1"
    )
    private Integer maxGroupSize;

    @NotBlank(
            message = "Mô tả là bắt buộc"
    )
    private String description;

    private String thumbnail;

    private Boolean featured = false;

    private List<String> images = new ArrayList<>();

    @NotEmpty(
            message = "Phải có ít nhất một ngày lịch trình"
    )
    @Valid
    private List<AdminItineraryForm> itineraries = new ArrayList<>();
}
