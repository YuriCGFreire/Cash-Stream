package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for Expense Subcategory Respository")
class ExpenseSubcategoryRepositoryTest {

    @Autowired
    private ExpenseSubcategoryRepository expenseSubcategoryRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    private ExpenseCategory savedExpenseCategory;

    @BeforeEach
    void setUp(){
        ExpenseCategory category = ExpenseCategoryCreator.createExpenseCategoryToBeSaved();
        savedExpenseCategory = expenseCategoryRepository.save(category);
    }

    @Test
    @DisplayName("findBySubCategoryName returns expenseSubcategory when successful")
    void findBySubCategoryName_ReturnsExpenseSubcategory_WhenSuccessful(){
        ExpenseSubcategory tobeSaved = ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved();
        tobeSaved.setExpenseCategory(savedExpenseCategory);
        ExpenseSubcategory savedExpenseSubcategory = expenseSubcategoryRepository.save(tobeSaved);
        Optional<ExpenseSubcategory> fetchedExpenseSubcategory = expenseSubcategoryRepository.findBySubCategoryName("username", savedExpenseSubcategory.getSubCategoryName());
        Assertions.assertThat(fetchedExpenseSubcategory)
                .isNotEmpty()
                .contains(savedExpenseSubcategory);
    }

    @Test
    @DisplayName("findAllSubcategoryExpenses returns List of ExpenseSubcategory inside of Page Object when successful")
    void findAllSubcategoryExpenses_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        ExpenseSubcategory tobeSaved = ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved();
        tobeSaved.setExpenseCategory(savedExpenseCategory);
        ExpenseSubcategory savedExpenseSubcategory = expenseSubcategoryRepository.save(tobeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseSubcategoryResponse> pageExpenseSubcategoryResponse = this.expenseSubcategoryRepository.findAllSubcategoryExpenses("username", pageable);

        Assertions.assertThat(pageExpenseSubcategoryResponse).isNotNull();
        Assertions.assertThat(pageExpenseSubcategoryResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findAllSubcategoryExpensesByCategory returns List of ExpenseSubcategory inside of Page Object when successful")
    void findAllSubcategoryExpensesByCategory_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        ExpenseSubcategory tobeSaved = ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved();
        tobeSaved.setExpenseCategory(savedExpenseCategory);
        ExpenseSubcategory savedExpenseSubcategory = expenseSubcategoryRepository.save(tobeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseSubcategoryResponse> pageExpenseSubcategoryResponse = this.expenseSubcategoryRepository
                .findAllSubcategoryExpensesByCategory("username", savedExpenseCategory.getCategoryName(), pageable);

        Assertions.assertThat(pageExpenseSubcategoryResponse).isNotNull();
        Assertions.assertThat(pageExpenseSubcategoryResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

}