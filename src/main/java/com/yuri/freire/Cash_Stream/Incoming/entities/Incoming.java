package com.yuri.freire.Cash_Stream.Incoming.entities;

import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_incoming")
@SQLDelete(sql = "UPDATE tb_incoming SET deleted_at = CURRENT_TIMESTAMP WHERE incoming_id = ?")
public class Incoming extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "incoming_sequence"
    )
    @SequenceGenerator(
            name = "incoming_sequence",
            sequenceName =  "incoming_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "incoming_id")
    private Integer incomingId;

    @Column(name = "incoming_description", nullable = false, length = 50)
    private String incomingDescription;

    @Column(name = "incoming_source", nullable = true, length = 50)
    private String incomingSource;

    @Column(name = "gross_incoming", nullable = false, precision = 10, scale = 2)
    private BigDecimal grossIncoming;

    @Column(name = "net_incoming", nullable = false, precision = 10, scale = 2)
    private BigDecimal netIncoming;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incoming_category_id")
    private IncomingCategory incomingCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incoming_subcategory_id")
    private IncomingSubcategory incomingSubcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurrence_id")
    private Recurrence recurrence;
}
