package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for Expense Category Respository")
class ExpenseCategoryRepositoryTest {
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Test
    @DisplayName("findByCategoryName returns ExpenseCategory when successful")
    void findByCategoryName_ReturnsExpenseCategory_WhenSuccessful(){
        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        Optional<ExpenseCategory> fetchedExpenseCategory = expenseCategoryRepository.findByCategoryName("username", savedExpenseCategory.getCategoryName());
        Assertions.assertThat(fetchedExpenseCategory)
                .isNotEmpty()
                .contains(savedExpenseCategory);
    }

    @Test
    @DisplayName("findAllCategoryExpenses returns list of ExpenseCategory inside Page Object when successful")
    void findAllCategoryExpenses_ReturnsListOfExpenseCategoryInsidePageObject_WhenSuccessful(){
        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseCategoryResponse> pageExpenseCategoryResponse = this.expenseCategoryRepository.findAllCategoryExpenses("username", pageable);

        Assertions.assertThat(pageExpenseCategoryResponse).isNotNull();
        Assertions.assertThat(pageExpenseCategoryResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
    }
}