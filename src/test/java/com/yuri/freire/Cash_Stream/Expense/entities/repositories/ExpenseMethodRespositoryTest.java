package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Expense Method Respository")
class ExpenseMethodRespositoryTest {

    @Autowired
    private ExpenseMethodRespository expenseMethodRespository;

    @Test
    @DisplayName("findByExpenseMethodName return an expense method when successful")
    void findByExpenseMethodName_ReturnAnExpenseMethod_whenSuccessful(){
        ExpenseMethod expectedExpenseMethod = expenseMethodRespository.save(ExpenseMethod.builder().expenseMethodName(ExpenseMethodType.VA).build());

        Optional<ExpenseMethod> fetchedExpenseMethod = expenseMethodRespository.findByExpenseMethodName(expectedExpenseMethod.getExpenseMethodName());
        Assertions.assertThat(fetchedExpenseMethod)
                .isNotEmpty()
                .contains(expectedExpenseMethod);
    }

}