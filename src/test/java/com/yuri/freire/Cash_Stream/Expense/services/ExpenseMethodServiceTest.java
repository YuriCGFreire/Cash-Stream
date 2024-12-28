package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseMethodRespository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Method Service")
class ExpenseMethodServiceTest {
    @InjectMocks
    private ExpenseMethodService expenseMethodService;
    @Mock
    private ExpenseMethodRespository expenseMethodRespositoryMock;
    private ExpenseMethod savedExpenseMethod;

    @BeforeEach
    void setUp(){
        this.savedExpenseMethod = ExpenseMethod.builder()
                .expenseMethodId(1)
                .expenseMethodName(ExpenseMethodType.VA)
                .build();
        BDDMockito.when(expenseMethodRespositoryMock.findByExpenseMethodName(ArgumentMatchers.any()))
                .thenReturn(Optional.ofNullable(savedExpenseMethod));
    }

    @Test
    @DisplayName("findByExpenseMethodName return ExpenseMethod by methodName when successful")
    void findByExpenseMethodName_ReturnExpenseMethodByMethodName_WhenSuccessful(){
        ExpenseMethod fetchedExpenseMethod = expenseMethodService.findByExpenseMethodName(savedExpenseMethod.getExpenseMethodName());

        Assertions.assertThat(fetchedExpenseMethod)
                .isNotNull()
                .isInstanceOf(ExpenseMethod.class);
        Assertions.assertThat(fetchedExpenseMethod.getExpenseMethodId()).isNotNull().isEqualTo(savedExpenseMethod.getExpenseMethodId());
        Assertions.assertThat(fetchedExpenseMethod.getExpenseMethodName()).isNotNull().isEqualTo(savedExpenseMethod.getExpenseMethodName());
    }
}