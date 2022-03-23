package com.project.apiproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class LineItems {
    @NotBlank(message = "Product name cannot be blank")
    @NotNull(message = "Product name cannot be null")
    private String product_name;
    //@Min(value = 1, message = "Minimum quantity should be 1")
    @Positive(message="Quantity cannot be less than 0")
    private int quantity;
    @Positive(message = "Product price cannot be negative")
    private Double price;
}
