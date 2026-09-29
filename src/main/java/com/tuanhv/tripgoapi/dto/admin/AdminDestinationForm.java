package com.tuanhv.tripgoapi.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDestinationForm {

    @NotBlank(message = "Tên điểm đến là bắt buộc")
    @Size(
            max = 255,
            message = "Tên điểm đến tối đa 255 ký tự"
    )
    private String name;

    @NotBlank(message = "Slug là bắt buộc")
    @Size(
            max = 255,
            message = "Slug tối đa 255 ký tự"
    )
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Slug chỉ gồm chữ thường, số và dấu -"
    )
    private String slug;

    @Size(
            max = 500,
            message = "URL ảnh tối đa 500 ký tự"
    )
    private String imageUrl;
}
