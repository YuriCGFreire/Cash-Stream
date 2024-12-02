package com.yuri.freire.Cash_Stream.Recurrence.entities;

import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
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
@Table(name = "tb_recurrence")
public class Recurrence extends BaseEntity {
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

    @OneToMany(mappedBy = "recurrence", fetch = FetchType.LAZY)
    private List<Incoming> incomings;

    @OneToMany(mappedBy = "recurrence", fetch = FetchType.LAZY)
    private List<Expense> expenses;
}
