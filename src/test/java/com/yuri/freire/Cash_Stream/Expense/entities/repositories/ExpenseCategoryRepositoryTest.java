package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepositoryMock;

    private User savedUser;

    @BeforeEach
    void setUp(){
        User user = UserCreator.createValidUser();
        savedUser = userRepositoryMock.save(user);
    }

    @Test
    @DisplayName("findByCategoryName returns ExpenseCategory when successful")
    void findByCategoryName_ReturnsExpenseCategory_WhenSuccessful(){
        ExpenseCategory expenseCategoryToBeSaved = ExpenseCategoryCreator.createExpenseCategoryToBeSaved();
        expenseCategoryToBeSaved.setUser(savedUser);
        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(expenseCategoryToBeSaved);
        Optional<ExpenseCategory> fetchedExpenseCategory = expenseCategoryRepository.findByCategoryName(savedExpenseCategory.getCategoryName(), savedUser.getUsername());
        Assertions.assertThat(fetchedExpenseCategory)
                .isNotEmpty()
                .contains(savedExpenseCategory);
    }

    @Test
    @DisplayName("findAllCategoryExpenses returns list of ExpenseCategory inside Page Object when successful")
    void findAllCategoryExpenses_ReturnsListOfExpenseCategoryInsidePageObject_WhenSuccessful(){
        ExpenseCategory expenseCategoryToBeSaved = ExpenseCategoryCreator.createExpenseCategoryToBeSaved();
        expenseCategoryToBeSaved.setUser(savedUser);
        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(expenseCategoryToBeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseCategoryResponse> pageExpenseCategoryResponse = this.expenseCategoryRepository.findAllCategoryExpenses(pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseCategoryResponse).isNotNull();
        Assertions.assertThat(pageExpenseCategoryResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
    }
}