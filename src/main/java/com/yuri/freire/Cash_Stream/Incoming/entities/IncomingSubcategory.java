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
@Table(name = "tb_incoming_subcategory")
public class IncomingSubcategory extends BaseEntity {
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
    private Integer incomingSubcategoryId;

    @Column(name = "subcategory_name", nullable = false, length = 50)
    private String subCategoryName;

    @OneToMany(mappedBy = "incomingSubcategory")
    private List<Incoming> incomings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private IncomingCategory incomingCategory;
}
