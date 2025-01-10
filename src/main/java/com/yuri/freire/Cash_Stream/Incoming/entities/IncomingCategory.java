package com.yuri.freire.Cash_Stream.Incoming.entities;


import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_incoming_category")
@SQLDelete(sql = "UPDATE tb_incoming_category SET deleted_at = CURRENT_TIMESTAMP WHERE incoming_category_id = ?")
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

    @NotNull(message = "Incoming category name cannot be null")
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @OneToMany(mappedBy = "incomingCategory", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Incoming> incomings;

    @OneToMany(mappedBy = "incomingCategory", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<IncomingSubcategory> subcategories;
}
