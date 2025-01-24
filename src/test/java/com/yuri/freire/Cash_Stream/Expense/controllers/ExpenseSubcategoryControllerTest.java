package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseCategoryService;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseSubcategoryService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryRequestCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryRequestCreator;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Subcategory Controller")
class ExpenseSubcategoryControllerTest {
    @InjectMocks
    private ExpenseSubcategoryController expenseSubcategoryController;
    @Mock
    private ExpenseSubcategoryService expenseSubcategoryServiceMock;
    @Mock
    private HttpServletRequest requestMock;

    @BeforeEach
    void setUp(){
        ExpenseSubcategoryResponse expenseSubcategoryResponse = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        PageImpl<ExpenseSubcategoryResponse> expenseSubcategoryResponsePage = new PageImpl<>(List.of(expenseSubcategoryResponse));

        BDDMockito.when(expenseSubcategoryServiceMock.createExpenseSubcategory(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(expenseSubcategoryResponse);

        BDDMockito.when(expenseSubcategoryServiceMock.findAllSubcategoryExpenses(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(expenseSubcategoryResponsePage);

        BDDMockito.when(expenseSubcategoryServiceMock.findAllSubcategoryExpensesByCategory(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(expenseSubcategoryResponsePage);

        BDDMockito.when(expenseSubcategoryServiceMock.findAllSubcategoryExpensesByCategory(ArgumentMatchers.eq("someRandomcategoryName"), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new EntityNotFoundException("Category not found: someRandomcategoryName"));

        BDDMockito.when(expenseSubcategoryServiceMock.deleteBySubcategoryId(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
                .thenReturn(expenseSubcategoryResponse.getSubCategoryName());

        BDDMockito.when(expenseSubcategoryServiceMock.deleteBySubcategoryId(ArgumentMatchers.eq(1298), ArgumentMatchers.any()))
                        .thenThrow(new EntityNotFoundException("Subcategory not found: 1298"));

        BDDMockito.when(requestMock.getRequestURI())
                .thenReturn("/subcategory-test-path");
    }

    @Test
    @DisplayName("createExpenseSubcategory persist expenseSubcategory when successful")
    void createExpenseSubcategory_PersistExpenseSubcategory_WhenSuccessful() {
        ExpenseSubcategoryResponse expectedSubcategory = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        ResponseEntity<ApiResponse<ExpenseSubcategoryResponse>> expenseSubcategory = expenseSubcategoryController.createExpenseSubcategory(
                ExpenseSubcategoryRequestCreator.createExpenseSubcategoryRequest(), requestMock
        );

        Assertions.assertThat(expenseSubcategory.getBody().getErrors()).isNull();
        Assertions.assertThat(expenseSubcategory.getBody().isSuccess()).isTrue();
        Assertions.assertThat(expenseSubcategory.getBody().getMessage()).isEqualTo("Subcategory created successfully");
        Assertions.assertThat(expenseSubcategory.getBody().getData()).isNotNull().isInstanceOf(ExpenseSubcategoryResponse.class)
                .isEqualTo(expectedSubcategory);
    }
    
    @Test
    @DisplayName("findAllSubcategoryExpenses return list of ExpenseSubcategoryResponse inside page object when successful")
    void findAllSubcategoryExpenses_ReturnListOfExpenseSubcategoryResponseInsidePageObject_WhenSuccessful() {
        ResponseEntity<ApiResponse<Page<ExpenseSubcategoryResponse>>> allSubcategoryExpenses = expenseSubcategoryController.findAllSubcategoryExpenses(
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allSubcategoryExpenses).isNotNull();

        Assertions.assertThat(allSubcategoryExpenses.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allSubcategoryExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allSubcategoryExpenses.getBody().getMessage()).isEqualTo("Subcategories fetched successfully");
        Assertions.assertThat(allSubcategoryExpenses.getBody().isSuccess()).isTrue();
        Assertions.assertThat(allSubcategoryExpenses.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("findAllSubcategoryExpensesByCategory return list of ExpenseSubcategoryResponse inside page object by category when successful")
    void findAllSubcategoryExpensesByCategory_ReturnListOfExpenseSubcategoryResponseInsidePageObjectByCategory_WhenSuccessful() {
        String expectedCategoryName = ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName();
        ResponseEntity<ApiResponse<Page<ExpenseSubcategoryResponse>>> allSubcategoryExpensesByCategory = expenseSubcategoryController.findAllSubcategoryExpensesByCategory(
                expectedCategoryName,
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allSubcategoryExpensesByCategory.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allSubcategoryExpensesByCategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allSubcategoryExpensesByCategory.getBody().getMessage()).isEqualTo("Subcategories fetched by category successfully");
        Assertions.assertThat(allSubcategoryExpensesByCategory.getBody().getData().toList())
                .extracting(ExpenseSubcategoryResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(expectedCategoryName));
    }

    @Test
    @DisplayName("findAllSubcategoryExpensesByCategory throw EntityNotFoundException when category does not exist")
    void findAllSubcategoryExpensesByCategory_ThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        String randomCategoryName = "someRandomcategoryName";
        Assertions.assertThatThrownBy(() ->
                expenseSubcategoryController.findAllSubcategoryExpensesByCategory(randomCategoryName, PageRequest.of(0, 1), requestMock)
        )
        .hasMessage("Category not found: someRandomcategoryName")
        .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("deleteSubcategoryById delete ExpenseSubcategory when successful")
    void deleteSubcategoryById_DeleteExpenseSubcategory_WhenSuccessful() {
        String expectedSubCategoryName = ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName();
        ResponseEntity<ApiResponse<String>> deletedSubcategory = expenseSubcategoryController.deleteSubcategoryById(
                ExpenseSubcategoryCreator.createValidExpenseSubcategory().getExpenseSubcategoryId(),
                requestMock
        );

        Assertions.assertThat(deletedSubcategory).isNotNull();
        Assertions.assertThat(deletedSubcategory.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Subcategory deleted successfully");
        Assertions.assertThat(deletedSubcategory.getBody().getData()).isEqualTo(expectedSubCategoryName);
    }

    @Test
    @DisplayName("deleteSubcategoryById throw EntityNotFoundException when subcategory does not exist")
    void deleteSubcategoryById_ThrowEntityNotFoundException_WhenSubcategoryDoesNotExist() {
        Integer someRandomId = 1298;

        Assertions.assertThatThrownBy(() ->
                        expenseSubcategoryController.deleteSubcategoryById(someRandomId, requestMock)
                )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Subcategory not found: 1298");
    }
}