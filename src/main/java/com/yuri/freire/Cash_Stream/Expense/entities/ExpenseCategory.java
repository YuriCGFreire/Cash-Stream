package com.yuri.freire.Cash_Stream.Expense.entities;

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
@Table(name = "tb_expense_category")
public class ExpenseCategory extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expense_category_sequence"
    )
    @SequenceGenerator(
            name = "expense_category_sequence",
            sequenceName =  "expense_category_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "expense_category_id")
    private Integer expenseCategoryId;

    @Column(name = "category_name", nullable = false, length = 50, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "expenseCategory", orphanRemoval = true)
    private List<ExpenseSubcategory> expenseSubcategories;

    @OneToMany(mappedBy = "expenseCategory", orphanRemoval = true)
    private List<Expense> expenses;
}
