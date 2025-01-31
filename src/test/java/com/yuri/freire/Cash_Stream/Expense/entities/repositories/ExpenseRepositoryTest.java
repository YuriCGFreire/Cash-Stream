package com.yuri.freire.Cash_Stream.Expense.entities.repositories;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.repositories.RecurrenceRepository;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for Expense repository")
class ExpenseRepositoryTest {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private ExpenseSubcategoryRepository expenseSubcategoryRepository;
    @Autowired
    private ExpenseMethodRespository expenseMethodRespository;
    @Autowired
    private RecurrenceRepository recurrenceRepository;
    @Autowired
    private UserRepository userRepositoryMock;
    private ExpenseCategory savedCategory;
    private ExpenseSubcategory savedSubcategory;
    private ExpenseMethod expenseMethod;
    private Recurrence savedRecurrence;
    private Expense savedExpense;

    private User savedUser;

    @BeforeEach
    void setUp(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceFrequency(RecurrenceType.NONRECURRING)
                .build();
        this.savedUser = userRepositoryMock.save(UserCreator.createValidUser());
        ExpenseMethod expenseMethodToBeSaved = ExpenseMethod.builder().expenseMethodName(ExpenseMethodType.VA).build();
        ExpenseCategory expenseCategoryToBeSaved = ExpenseCategoryCreator.createExpenseCategoryToBeSaved();
        expenseCategoryToBeSaved.setUser(savedUser);
        this.savedCategory = this.expenseCategoryRepository.save(expenseCategoryToBeSaved);
        ExpenseSubcategory subcategoryToBeSaved = ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved();
        subcategoryToBeSaved.setExpenseCategory(savedCategory);
        subcategoryToBeSaved.setUser(savedUser);
        this.savedSubcategory = this.expenseSubcategoryRepository.save(subcategoryToBeSaved);
        this.expenseMethod = this.expenseMethodRespository.save(expenseMethodToBeSaved);
        this.savedRecurrence = this.recurrenceRepository.save(recurrence);
        Expense expense = ExpenseCreator.createExpenseToBeSaved();
        expense.setExpenseCategory(savedCategory);
        expense.setExpenseSubcategory(savedSubcategory);
        expense.setExpenseMethod(expenseMethod);
        expense.setRecurrence(savedRecurrence);
        expense.setUser(savedUser);
        this.savedExpense = this.expenseRepository.save(expense);
    }

    @Test
    @DisplayName("findAllExpenses returns List of Expense inside of Page Object when successful")
    void findAllExpenses_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseResponse> pageExpenseResponse = this.expenseRepository.findAllExpenses(pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseResponse).isNotNull();
        Assertions.assertThat(pageExpenseResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findAllByCategory returns List of Expense inside of Page Object by category when successful")
    void findAllByCategory_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseResponse> pageExpenseResponse = this.expenseRepository.findAllByCategory(savedCategory.getCategoryName(), pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseResponse).isNotNull();
        Assertions.assertThat(pageExpenseResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageExpenseResponse.getContent())
                .extracting(ExpenseResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(savedCategory.getCategoryName()));
    }

    @Test
    @DisplayName("findAllBySubcategory returns List of Expense inside of Page Object by subcategory when successful")
    void findAllBySubcategory_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseResponse> pageExpenseResponse = this.expenseRepository.findAllBySubcategory(savedSubcategory.getSubCategoryName(), pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseResponse).isNotNull();
        Assertions.assertThat(pageExpenseResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageExpenseResponse.getContent())
                .extracting(ExpenseResponse::getSubCategoryName)
                .allMatch(subCategoryName -> subCategoryName.equals(savedSubcategory.getSubCategoryName()));
    }

    @Test
    @DisplayName("findAllBYPaymentMethod returns List of Expense inside of Page Object by payment method when successful")
    void findAllBYPaymentMethod_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseResponse> pageExpenseResponse = this.expenseRepository.findAllBYPaymentMethod( expenseMethod.getExpenseMethodName(), pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseResponse).isNotNull();
        Assertions.assertThat(pageExpenseResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageExpenseResponse.getContent())
                .extracting(ExpenseResponse::getExpenseMethod)
                .allMatch(expenseMethodType -> expenseMethodType.equals(expenseMethod.getExpenseMethodName()));
    }

    @Test
    @DisplayName("findAllByEssentiality returns List of Expense inside of Page Object by essentiality when successful")
    void findAllByEssentiality_ReturnsListOfExpenseSubcategoryInsidePageObject_WhenSuccessful(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<ExpenseResponse> pageExpenseResponse = this.expenseRepository.findAllByEssentiality(savedExpense.isEssential(), pageable, savedUser.getUsername());

        Assertions.assertThat(pageExpenseResponse).isNotNull();
        Assertions.assertThat(pageExpenseResponse.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageExpenseResponse.getContent())
                .extracting(ExpenseResponse::isEssential)
                .allMatch(isEssential -> isEssential.equals(savedExpense.isEssential()));
    }

}