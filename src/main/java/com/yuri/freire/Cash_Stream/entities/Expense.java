package com.yuri.freire.Cash_Stream.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_expense")
public class Expense extends BaseEntity{
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
    @Column(name = "deleted_at", insertable = false, updatable = true)
    private LocalDateTime deletedAt;

    public void markAsDeleted(){
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted(){
        return this.deletedAt != null;
    }
}
