package com.project.apiproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Cart {
    @Id
    private String cart_id;
    @NotBlank(message="Customer name cannot be blank")
    @Indexed(unique = true)
    private String customername;
    private Double total_value;
    private List<LineItems> line_items;
}
