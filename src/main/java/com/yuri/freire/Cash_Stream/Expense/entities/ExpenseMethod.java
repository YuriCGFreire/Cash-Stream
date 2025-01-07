package com.yuri.freire.Cash_Stream.Expense.entities;


import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
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
@Table(name = "tb_expense_method")
public class ExpenseMethod extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expense_method_sequence"
    )
    @SequenceGenerator(
            name = "expense_method_sequence",
            sequenceName =  "expense_method_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "expense_method_id")
    private Integer expenseMethodId;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true)
    private ExpenseMethodType expenseMethodName;

    @OneToMany(mappedBy = "expenseMethod")
    private List<Expense> expenses;

}
