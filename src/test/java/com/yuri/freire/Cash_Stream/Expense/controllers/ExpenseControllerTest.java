package com.yuri.freire.Cash_Stream.Expense.controllers;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.services.ExpenseService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseRequestCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Controller")
class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;
    @Mock
    private ExpenseService expenseServiceMock;
    @Mock
    private HttpServletRequest requestMock;

    @BeforeEach
    void setUp(){
        ExpenseResponse validExpenseResponse = ExpenseCreator.createValidExpenseResponse();
        PageImpl<ExpenseResponse> pageExpenses = new PageImpl<>(List.of(validExpenseResponse));

        BDDMockito.when(expenseServiceMock.createExpense(ArgumentMatchers.any()))
                .thenReturn(validExpenseResponse);

        BDDMockito.when(expenseServiceMock.findAllExpenses(ArgumentMatchers.any()))
                .thenReturn(pageExpenses);

        BDDMockito.when(expenseServiceMock.findAllExpensesByCategoryName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(pageExpenses);

        BDDMockito.when(expenseServiceMock.findAllExpensesByCategoryName(ArgumentMatchers.eq("someRandomcategoryName"), ArgumentMatchers.any()))
                .thenThrow(new EntityNotFoundException("Category not found: someRandomcategoryName"));

        BDDMockito.when(expenseServiceMock.findAllBySubcategoryName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(pageExpenses);

        BDDMockito.when(expenseServiceMock.findAllBySubcategoryName(ArgumentMatchers.eq("someRandomSubcategoryName"), ArgumentMatchers.any()))
                .thenThrow(new EntityNotFoundException("Category not found: someRandomSubcategoryName"));

        BDDMockito.when(expenseServiceMock.findAllExpensesByPaymentMethod(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpenses);

        BDDMockito.when(expenseServiceMock.findAllExpensesByIsEssential(ArgumentMatchers.anyBoolean(), ArgumentMatchers.any()))
                .thenReturn(pageExpenses);
    }

    @Test
    @DisplayName("createExpense persist Expense when successful")
    void createExpense_PersistExpense_WhenSuccessful() {
        ExpenseResponse expectedExpenseResponse = ExpenseCreator.createValidExpenseResponse();
        ResponseEntity<ApiResponse<ExpenseResponse>> createdExpense = expenseController.createExpense(
                ExpenseRequestCreator.createExpenseRequest(),
                requestMock
        );

        Assertions.assertThat(createdExpense.getBody().getErrors()).isNull();
        Assertions.assertThat(createdExpense.getBody().isSuccess()).isTrue();
        Assertions.assertThat(createdExpense.getBody().getMessage()).isEqualTo( "Expense created successfully");
        Assertions.assertThat(createdExpense.getBody().getData()).isNotNull().isInstanceOf(ExpenseResponse.class)
                .isEqualTo(expectedExpenseResponse);
    }

    @Test
    @DisplayName("findAllExpenses return list of Expenses inside page object when successful")
    void findAllExpenses_ReturnListOfExpensesInsidePageObject_WhenSuccessful() {
        ResponseEntity<ApiResponse<Page<ExpenseResponse>>> allExpenses = expenseController.findAllExpenses(PageRequest.of(0, 1), requestMock);
        Assertions.assertThat(allExpenses).isNotNull();

        Assertions.assertThat(allExpenses.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpenses.getBody().getMessage()).isEqualTo("Expenses fetched successfully");
        Assertions.assertThat(allExpenses.getBody().isSuccess()).isTrue();
        Assertions.assertThat(allExpenses.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("indAllExpensesByCategoryName return list of Expenses inside page object by categoryName when successful")
    void findAllExpensesByCategoryName_ReturnListOfExpensesInsidePageObjectByCategoryName_WhenSuccessful() {
        String expectedCategoryName = ExpenseCategoryCreator.createValidExpenseCategory().getCategoryName();
        ResponseEntity<ApiResponse<Page<ExpenseResponse>>> allExpensesByCategoryName = expenseController.findAllExpensesByCategoryName(
                expectedCategoryName,
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allExpensesByCategoryName.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesByCategoryName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesByCategoryName.getBody().getMessage()).isEqualTo("Expenses fetched by category successfully");
        Assertions.assertThat(allExpensesByCategoryName.getBody().getData().toList())
                .extracting(ExpenseResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(expectedCategoryName));
    }

    @Test
    @DisplayName("indAllExpensesByCategoryName throw EntityNotFoundException when category does not exist")
    void findAllExpensesByCategoryName_ThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        String randomCategoryName = "someRandomcategoryName";
        Assertions.assertThatThrownBy(() ->
                        expenseController.findAllExpensesByCategoryName(randomCategoryName, PageRequest.of(0, 1), requestMock)
                )
                .hasMessage("Category not found: someRandomcategoryName")
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findAllExpensesBySubcategoryName return list of Expenses inside Page Object by subcategoryName when successful")
    void findAllExpensesBySubcategoryName_ReturnListOfExpensesInsidePageObjectBySubcategoryName_WhenSuccessful() {
        String expectedsubcategoryName = ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName();
        ResponseEntity<ApiResponse<Page<ExpenseResponse>>> allExpensesBySubcategoryName = expenseController.findAllExpensesBySubcategoryName(
                expectedsubcategoryName,
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesBySubcategoryName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getMessage()).isEqualTo( "Expenses fetched by subcategory successfuly");
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getData().toList())
                .extracting(ExpenseResponse::getSubCategoryName)
                .allMatch(subCategoryName -> subCategoryName.equals(expectedsubcategoryName));
    }

    @Test
    @DisplayName("findAllExpensesBySubcategoryName throw EntityNotFoundException when category does not exist")
    void findAllExpensesBySubcategoryName_ThrowEntityNotFoundException_WhenSubcategoryDoesNotExist() {
        String randomSubcategoryName = "someRandomSubcategoryName";
        Assertions.assertThatThrownBy(() ->
                        expenseController.findAllExpensesBySubcategoryName(randomSubcategoryName, PageRequest.of(0, 1), requestMock)
                )
                .hasMessage("Category not found: someRandomSubcategoryName")
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findAllExpensesByPaymentMethod return list of Expenses inside Page object by paymentMethod when successful")
    void findAllExpensesByPaymentMethod_ReturnListOfExpensesInsidePageObjectByPaymentMethod_WhenSuccessful() {
        ExpenseMethodType expenseMethodName = ExpenseCreator.createValidExpense().getExpenseMethod().getExpenseMethodName();
        ResponseEntity<ApiResponse<Page<ExpenseResponse>>> allExpensesByPaymentMethod = expenseController.findAllExpensesByPaymentMethod(
                expenseMethodName,
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesByPaymentMethod.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getMessage()).isEqualTo( "Expenses fetched by payment method successfuly");
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getData().toList())
                .extracting(ExpenseResponse::getExpenseMethod)
                .allMatch(expenseMethodType -> expenseMethodType.equals(expenseMethodName));
    }

    @Test
    @DisplayName("findAllByIsEssential return list of Expenses inside Page object by it essentiality when successful")
    void findAllByIsEssential_ReturnListOfExpensesInsidePageObjectByItEssentiality_WhenSuccessful() {
        boolean expectedEssentiality = ExpenseCreator.createValidExpense().isEssential();
        ResponseEntity<ApiResponse<Page<ExpenseResponse>>> allByIsEssential = expenseController.findAllByIsEssential(
                expectedEssentiality,
                PageRequest.of(0, 1),
                requestMock
        );
        Assertions.assertThat(allByIsEssential.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allByIsEssential.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allByIsEssential.getBody().getMessage()).isEqualTo( "Expenses fetched by essentiality successfully");
        Assertions.assertThat(allByIsEssential.getBody().getData().toList())
                .extracting(ExpenseResponse::isEssential)
                .allMatch(isEssential -> isEssential.equals(expectedEssentiality));
    }
}