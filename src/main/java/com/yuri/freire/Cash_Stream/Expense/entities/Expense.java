package com.yuri.freire.Cash_Stream.Expense.entities;

import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_expense")
@SQLDelete(sql = "UPDATE tb_expense SET deleted_at = CURRENT_TIMESTAMP WHERE expense_id = ?")
public class Expense extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expense_sequence"
    )
    @SequenceGenerator(
            name = "expense_sequence",
            sequenceName =  "expense_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "expense_id")
    private Integer expenseId;

    @Column(name = "expense_description", nullable = false, length = 50)
    private String expenseDescription;

    @Column(name = "expense_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal expenseAmount;

    @Column(name = "is_essential", nullable = false)
    private boolean isEssential;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_method_id")
    private ExpenseMethod expenseMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurrence_id")
    private Recurrence recurrence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_category_id")
    private ExpenseCategory expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_subcategory_id")
    private ExpenseSubcategory expenseSubcategory;

    @Column(name = "deleted_at", updatable = true)
    private LocalDateTime deletedAt;
}
