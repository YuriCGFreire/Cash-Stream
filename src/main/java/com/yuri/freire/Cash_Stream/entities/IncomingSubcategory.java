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
@Table(name = "tb_incoming_subcategory")
public class IncomingSubcategory {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "incoming_subcategory_sequence"
    )
    @SequenceGenerator(
            name = "incoming_subcategory_sequence",
            sequenceName =  "incoming_subcategory_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "incoming_subcategory_id")
    private Integer incomingSubCategoryId;

    @Column(name = "subcategory_name", nullable = false, length = 50)
    private String subCategoryName;

    @OneToMany(mappedBy = "incomingSubcategory")
    private List<Incoming> incomings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private IncomingCategory incomingCategory;
}
