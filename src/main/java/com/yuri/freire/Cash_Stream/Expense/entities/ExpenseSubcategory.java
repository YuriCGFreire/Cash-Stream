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
@Table(name = "tb_expense_subcategory")
@SQLDelete(sql = "UPDATE tb_expense_subcategory SET deleted_at = CURRENT_TIMESTAMP WHERE expense_subcategory_id = ?")
public class ExpenseSubcategory extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expense_subcategory_sequence"
    )
    @SequenceGenerator(
            name = "expense_subcategory_sequence",
            sequenceName =  "expense_subcategory_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "expense_subcategory_id")
    private Integer expenseSubcategoryId;

    @Column(name = "subcategory_name", nullable = false, length = 50)
    private String subCategoryName;

    @OneToMany(mappedBy = "expenseSubcategory", orphanRemoval = true)
    private List<Expense> expenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ExpenseCategory expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
