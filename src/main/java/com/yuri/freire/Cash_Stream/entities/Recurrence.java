package com.yuri.freire.Cash_Stream.entities;

import com.yuri.freire.Cash_Stream.entities.entity_enum.RecurrenceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_recurrence")
public class Recurrence extends BaseEntity{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recurrence_sequence"
    )
    @SequenceGenerator(
            name = "recurrence_sequence",
            sequenceName =  "recurrence_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "recurrence_id")
    private Integer recurrenceId;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceFrequency;

    @OneToMany(mappedBy = "recurrence")
    private List<Incoming> incomings;

    @OneToMany(mappedBy = "recurrence")
    private List<Expense> expenses;
}
