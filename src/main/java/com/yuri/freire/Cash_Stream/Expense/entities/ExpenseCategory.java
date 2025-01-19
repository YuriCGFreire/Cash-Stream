package com.yuri.freire.Cash_Stream.Expense.entities;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "tb_expense_category")
@SQLDelete(sql = "UPDATE tb_expense_category SET deleted_at = CURRENT_TIMESTAMP WHERE expense_category_id = ?")
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

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "expenseCategory", orphanRemoval = true)
    private List<ExpenseSubcategory> expenseSubcategories;

    @OneToMany(mappedBy = "expenseCategory", orphanRemoval = true)
    private List<Expense> expenses;
}
