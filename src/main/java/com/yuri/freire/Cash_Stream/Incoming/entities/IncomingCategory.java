package com.yuri.freire.Cash_Stream.Incoming.entities;


import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_incoming_category")
public class IncomingCategory extends BaseEntity {
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

    @OneToMany(mappedBy = "incomingCategory", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Incoming> incomings;

    @OneToMany(mappedBy = "incomingCategory", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<IncomingSubcategory> subcategories;
}
