package com.yuri.freire.Cash_Stream.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_incoming_category")
public class IncomingCategory {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "incoming_category_sequence"
    )
    @SequenceGenerator(
            name = "incoming_category_sequence",
            sequenceName =  "incoming_category_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "incoming_category_id")
    private Integer incomingCategoryId;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @OneToMany(mappedBy = "incomingCategory")
    private List<Incoming> incomings;

    @OneToMany(mappedBy = "category")
    private List<IncomingSubcategory> subcategories;
}
