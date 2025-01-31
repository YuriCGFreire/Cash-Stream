package com.yuri.freire.Cash_Stream.Cashflow.entities;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Common.BaseEntity;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cash_flow")
@SQLDelete(sql = "UPDATE tb_cash_flow SET deleted_at = CURRENT_TIMESTAMP WHERE cash_flow_id = ?")
public class Cashflow extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cashflow_sequence"
    )
    @SequenceGenerator(
            name = "cashflow_sequence",
            sequenceName =  "cashflow_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @Column(name = "cash_flow_id")
    private Integer cashflowId;

    @Column(name = "net_cash_flow", nullable = false, precision = 10, scale = 2)
    private BigDecimal netCashflow;

    @Column(name = "total_incoming", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalIncoming = new BigDecimal("0.00");

    @Column(name = "total_expense", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalExpense = new BigDecimal("0.00");

    @Column(name = "top_expense_category", nullable = false, length = 50)
    private String topExpensecategory;

    @Column(name = "top_incoming_category", nullable = false, length = 50)
    private String topIncomingcategory;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
