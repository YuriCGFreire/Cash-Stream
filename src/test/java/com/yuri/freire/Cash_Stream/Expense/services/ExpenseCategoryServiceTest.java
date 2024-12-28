package com.yuri.freire.Cash_Stream.Expense.services;


import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryRequestCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Category Service")
class ExpenseCategoryServiceTest {

    @InjectMocks
    private ExpenseCategoryService expenseCategoryService;

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepositoryMock;

    @Mock
    private ExpenseFactory expenseFactoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<ExpenseCategoryResponse> categoryPage = new PageImpl<>(List.of(ExpenseCategoryCreator.createValidExpenseCategoryResponse()));
        ExpenseCategory expenseCategory = ExpenseCategoryCreator.createValidExpenseCategory();
        ExpenseCategoryResponse expenseCategoryResponse = ExpenseCategoryCreator.createValidExpenseCategoryResponse();

        BDDMockito.when(expenseFactoryMock.createExpenseCategory(ArgumentMatchers.any()))
                .thenReturn(expenseCategory);

        BDDMockito.when(expenseFactoryMock.createExpenseCategoryResponse(ArgumentMatchers.any()))
                .thenReturn(expenseCategoryResponse);

        BDDMockito.when(expenseCategoryRepositoryMock.findAllCategoryExpenses(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(categoryPage);

        BDDMockito.when(expenseCategoryRepositoryMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(ExpenseCategoryCreator.createValidExpenseCategory()));

        BDDMockito.when(expenseCategoryRepositoryMock.findByCategoryName("Some random categoryname"))
                .thenReturn(Optional.empty());

        BDDMockito.when(expenseCategoryRepositoryMock.findById(1))
                .thenReturn(Optional.of(ExpenseCategoryCreator.createExpenseCategoryToBeSaved()));

        BDDMockito.when(expenseCategoryRepositoryMock.findById(999))
                .thenReturn(Optional.empty());

        BDDMockito.doNothing().when(expenseCategoryRepositoryMock).deleteById(ArgumentMatchers.anyInt());
    }

    @Test
    @DisplayName("createExpenseCategory persist expensecategory when successful")
    void createExpenseCategory_PersistExpenseCategory_WhenSuccessful(){
        ExpenseCategoryResponse expenseCategory = expenseCategoryService.createExpenseCategory(ExpenseCategoryRequestCreator.createExpenseCategoryRequest());

        Assertions.assertThat(expenseCategory).isNotNull().isEqualTo(ExpenseCategoryCreator.createValidExpenseCategoryResponse());
        Assertions.assertThat(expenseCategory.getCategoryName()).isNotNull().isEqualTo(ExpenseCategoryCreator.createValidExpenseCategoryResponse().getCategoryName());
        Assertions.assertThat(expenseCategory.getExpenseCategoryId()).isNotNull().isInstanceOf(Integer.class);
    }

    @Test
    @DisplayName("findAllCategoryExpenses returns list of ExpenseCategory inside Page Object when successful")
    void findAllCategoryExpenses_ReturnsListOfExpenseCategoryInsidePageObject_WhenSuccessful(){
        Page<ExpenseCategoryResponse> categoryPage = expenseCategoryService.findAllCategoryExpenses(PageRequest.of(0, 1));
        Assertions.assertThat(categoryPage).isNotNull();
        Assertions.assertThat(categoryPage.toList())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findByCategoryName return ExpenseCategory when successful")
    void findByCategoryName_ReturnExpenseCategory_WhenSuccessful(){
        ExpenseCategory expectedExpenseCategory = ExpenseCategoryCreator.createValidExpenseCategory();
        ExpenseCategory fetchedExpenseCategory = expenseCategoryService.findByCategoryName(expectedExpenseCategory.getCategoryName());

        Assertions.assertThat(fetchedExpenseCategory)
                .isNotNull()
                .isInstanceOf(ExpenseCategory.class);
        Assertions.assertThat(fetchedExpenseCategory.getExpenseCategoryId()).isNotNull().isEqualTo(expectedExpenseCategory.getExpenseCategoryId());
        Assertions.assertThat(fetchedExpenseCategory.getCategoryName()).isNotNull().isEqualTo(expectedExpenseCategory.getCategoryName());
    }

    @Test
    @DisplayName("findByCategoryName throw EntityNotFoundException when category does not exist")
    void findByCategoryName_ThrowsEntityNotFoundException_WhenCategoryDoesNotExiste(){
        String categoryName = "Some random categoryname";
        Assertions.assertThatThrownBy(() -> expenseCategoryService.findByCategoryName(categoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryName);
    }

    @Test
    @DisplayName("deleteByCategoryId removes category whem successful")
    void deleteByCategoryId_RemovesExpenseCategroy_WhenSuccessful(){
        String deletedCategory = expenseCategoryService.deleteByCategoryId(1);

        Assertions.assertThat(deletedCategory)
                .isNotNull()
                .isEqualTo("Alimentação");
    }

    @Test
    @DisplayName("deleteByCategoryId throws EntityNotFoundException when category does not exist")
    void deleteByCategoryId_ThrowsEntityNotFoundException_WhenCategoryDoesNotExiste(){
        Integer categoryId = 999;
        Assertions.assertThatThrownBy(() -> expenseCategoryService.deleteByCategoryId(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryId);
    }
}