package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseCategoryService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryRequestCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Category Controller")
class ExpenseCategoryControllerTest {

    @InjectMocks
    private ExpenseCategoryController expenseCategoryController;
    @Mock
    private ExpenseCategoryService expenseCategoryServiceMock;
    @Mock
    private HttpServletRequest servletRequestMock;

    @BeforeEach
    void setUp(){
        ExpenseCategoryResponse expenseCategoryResponse = ExpenseCategoryCreator.createValidExpenseCategoryResponse();
        PageImpl<ExpenseCategoryResponse> categoryPage = new PageImpl<>(List.of(expenseCategoryResponse));

        BDDMockito.when(expenseCategoryServiceMock.createExpenseCategory(ArgumentMatchers.any()))
                .thenReturn(expenseCategoryResponse);

        BDDMockito.when(expenseCategoryServiceMock.findAllCategoryExpenses(ArgumentMatchers.any()))
                .thenReturn(categoryPage);

        BDDMockito.when(expenseCategoryServiceMock.deleteByCategoryId(ArgumentMatchers.anyInt()))
                .thenReturn(expenseCategoryResponse.getCategoryName());

        BDDMockito.when(expenseCategoryServiceMock.deleteByCategoryId(1298))
                .thenThrow(new EntityNotFoundException("Category not found: 1298"));

        BDDMockito.when(servletRequestMock.getRequestURI())
                .thenReturn("/category-test-path");
    }

    @Test
    @DisplayName("createExpenseCategory persist ExpenseCategory when successful")
    void createExpenseCategory_PersistExpenseCategory_WhenSuccessful() {
        ExpenseCategoryResponse expectedCategory = ExpenseCategoryCreator.createValidExpenseCategoryResponse();
        ResponseEntity<ApiResponse<ExpenseCategoryResponse>> expenseCategoryResponse = expenseCategoryController.createExpenseCategory(
                ExpenseCategoryRequestCreator.createExpenseCategoryRequest(), servletRequestMock
        );

        Assertions.assertThat(expenseCategoryResponse).isNotNull();
        Assertions.assertThat(expenseCategoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(expenseCategoryResponse.getBody().getMessage()).isEqualTo("Category created successfully");
        Assertions.assertThat(expenseCategoryResponse.getBody().isSuccess()).isTrue();
        Assertions.assertThat(expenseCategoryResponse.getBody().getErrors()).isNull();
        Assertions.assertThat(expenseCategoryResponse.getBody().getData()).isNotNull().isEqualTo(expectedCategory);
    }

    @Test
    @DisplayName("findAllExpenses return List of ExpenseCategory inside page object when successful")
    void findAllExpenses_ReturnListOfExpenseCategoryInsidePageObject_WhenSuccessful() {
        ExpenseCategoryResponse expectedCategory = ExpenseCategoryCreator.createValidExpenseCategoryResponse();
        ResponseEntity<ApiResponse<Page<ExpenseCategoryResponse>>> allExpenses = expenseCategoryController.findAllExpenses(
                PageRequest.of(0, 1), servletRequestMock
        );

        Assertions.assertThat(allExpenses).isNotNull();
        Assertions.assertThat(allExpenses.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpenses.getBody().getMessage()).isEqualTo("List of categories fetched successfully");
        Assertions.assertThat(allExpenses.getBody().isSuccess()).isTrue();
        Assertions.assertThat(allExpenses.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("deleteByCategoryId removes Expense Category when successful")
    void deleteByCategoryId_RemovesCategory_WhenSuccessfull() {
        String expectedCategoryName = ExpenseCategoryCreator.createValidExpenseCategoryResponse().getCategoryName();
        ResponseEntity<ApiResponse<String>> deletedExpenseCategoryResponse = expenseCategoryController.deleteByCategoryId(
                ExpenseCategoryCreator.createValidExpenseCategory().getExpenseCategoryId(),
                servletRequestMock
        );

        Assertions.assertThat(deletedExpenseCategoryResponse).isNotNull();
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedExpenseCategoryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getMessage()).isEqualTo("Category deleted successfully");
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getData()).isEqualTo(expectedCategoryName);
    }

    @Test
    @DisplayName("deleteByCategoryId throws EntityNotFoundException when category does not exist")
    void deleteByCategoryId_ThrowsEntitytNotFoundException_WhenCategoryDoesNotExists(){
        Integer someRandomId = 1298;

        Assertions.assertThatThrownBy(() ->
                        expenseCategoryController.deleteByCategoryId(someRandomId, servletRequestMock)
                )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category not found: 1298");
    }
}