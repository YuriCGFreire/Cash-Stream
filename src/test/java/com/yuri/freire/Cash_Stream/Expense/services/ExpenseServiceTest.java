package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseMethod;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseRepository;
import com.yuri.freire.Cash_Stream.Expense.services.facade.ExpenseFacade;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.util.expense.*;
import com.yuri.freire.Cash_Stream.util.user.UserCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Expense Service")
class ExpenseServiceTest {

    @InjectMocks
    ExpenseService expenseService;
    @Mock
    ExpenseRepository expenseRepositoryMock;
    @Mock
    ExpenseFacade expenseFacadeMock;


    @BeforeEach
    void setUp(){
        Expense validExpense = ExpenseCreator.createValidExpense();
        ExpenseResponse validExpenseResponse = ExpenseCreator.createValidExpenseResponse();
        PageImpl<ExpenseResponse> pageExpense = new PageImpl<>(List.of(validExpenseResponse));

        BDDMockito.when(expenseFacadeMock.createExpense(ArgumentMatchers.any()))
                .thenReturn(validExpense);

        BDDMockito.when(expenseFacadeMock.getCurrentUsername())
                        .thenReturn(UserCreator.createValidUser().getUsername());

        BDDMockito.when(expenseRepositoryMock.save(ArgumentMatchers.any()))
                .thenReturn(validExpense);

        BDDMockito.when(expenseFacadeMock.createExpenseResponse(ArgumentMatchers.any()))
                .thenReturn(validExpenseResponse);

        BDDMockito.when(expenseRepositoryMock.findAllExpenses(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpense);

        BDDMockito.when(expenseRepositoryMock.findAllByCategory(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpense);

        BDDMockito.when(expenseFacadeMock.findExpenseCategoryByName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(ExpenseCategoryCreator.createValidExpenseCategory());

        BDDMockito.when(expenseFacadeMock.findExpenseCategoryByName(ArgumentMatchers.eq("Some random categoryname"), ArgumentMatchers.any()))
                .thenThrow(new EntityNotFoundException("Category not found: Some random categoryname"));

        BDDMockito.when(expenseRepositoryMock.findAllBySubcategory(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpense);

        BDDMockito.when(expenseFacadeMock.findExpenseSubcategoryByName(ArgumentMatchers.anyString()))
                .thenReturn(ExpenseSubcategoryCreator.createValidExpenseSubcategory());

        BDDMockito.when(expenseFacadeMock.findExpenseSubcategoryByName(ArgumentMatchers.eq("Some random subcategoryname")))
                .thenThrow(new EntityNotFoundException("Subcategory not found: Some random subcategoryname"));

        BDDMockito.when(expenseRepositoryMock.findAllBYPaymentMethod(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpense);

        BDDMockito.when(expenseFacadeMock.findExpenseMethod(ArgumentMatchers.any()))
                .thenReturn(ExpenseMethodCreator.createValidExpenseMethod());

        BDDMockito.when(expenseRepositoryMock.findAllByEssentiality( ArgumentMatchers.anyBoolean(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(pageExpense);

        BDDMockito.when(expenseRepositoryMock.findExpenseById(ArgumentMatchers.eq(1), ArgumentMatchers.any()))
                .thenReturn(Optional.of(validExpense));

        BDDMockito.when(expenseRepositoryMock.findExpenseById(ArgumentMatchers.eq(999), ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

    }

    @Test
    @DisplayName("createExpense oersist expense when successful")
    void createExpense_PersistExpense_WhenSuccessful() {
        ExpenseResponse expecetedExpenseResponse = ExpenseCreator.createValidExpenseResponse();
        ExpenseResponse savedExpense = expenseService.createExpense(ExpenseRequestCreator.createExpenseRequest());

        Assertions.assertThat(savedExpense).isNotNull().isInstanceOf(ExpenseResponse.class);

        Assertions.assertThat(savedExpense)
                .usingRecursiveComparison()
                .ignoringFields("incomingId")
                .isEqualTo(expecetedExpenseResponse);

        Assertions.assertThat(savedExpense.getExpenseId()).isNotNull();
    }

    @Test
    @DisplayName("findAllExpenses return list of Expenses inside page object when successful")
    void findAllExpenses_ReturnListOfExpenseInsidePageObject_WhenSuccessful() {
        Page<ExpenseResponse> pageExpense = expenseService.findAllExpenses(PageRequest.of(0, 1));

        Assertions.assertThat(pageExpense).isNotNull();
        Assertions.assertThat(pageExpense.toList())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findAllExpensesByCategoryName return list of expense by categoryname Inside Page Object when successful")
    void findAllExpensesByCategoryName_ReturnListOfExpenseByCategoryNameInsidePageObject_WhenSuccessful() {
        String expectedCategoryName = ExpenseCreator.createValidExpenseResponse().getCategoryName();
        Page<ExpenseResponse> pageExpense = expenseService.findAllExpensesByCategoryName(expectedCategoryName, PageRequest.of(0, 1));

        Assertions.assertThat(pageExpense).isNotNull();
        Assertions.assertThat(pageExpense.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pageExpense.getContent())
                .extracting(ExpenseResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(expectedCategoryName));
    }

    @Test
    @DisplayName("findAllExpensesByCategoryName throw EntityNotFoundException when category does not exist")
    void findAllExpensesByCategoryName_ThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        String expectedCategoryName = "Some random categoryname";
        Assertions.assertThatThrownBy(() -> expenseService.findAllExpensesByCategoryName(expectedCategoryName, PageRequest.of(0, 1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + expectedCategoryName);
    }

    @Test
    @DisplayName("findAllBySubcategoryName return list of expense by subCategoryName inside page object when successful")
    void findAllBySubcategoryName_ReturnListOfExpenseBySubCategorynameInsidePageObject_WhenSuccessful() {
        String expectedSubcategoryName = ExpenseCreator.createValidExpenseResponse().getSubCategoryName();
        Page<ExpenseResponse> pageExpense = expenseService.findAllBySubcategoryName(expectedSubcategoryName, PageRequest.of(0, 1));

        Assertions.assertThat(pageExpense).isNotNull();
        Assertions.assertThat(pageExpense.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pageExpense.getContent())
                .extracting(ExpenseResponse::getSubCategoryName)
                .allMatch(subCategoryName -> subCategoryName.equals(expectedSubcategoryName));
    }

    @Test
    @DisplayName("findAllBySubcategoryName throw EntityNotFoundException when Subcategory does not exist")
    void findAllBySubcategoryName_ThrowEntityNotFoundException_WhenSubcategoryDoesNotExist() {
        String expectedSubcategoryName = "Some random subcategoryname";
        Assertions.assertThatThrownBy(() -> expenseService.findAllBySubcategoryName(expectedSubcategoryName, PageRequest.of(0, 1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Subcategory not found: " + expectedSubcategoryName);
    }

    @Test
    @DisplayName("findAllExpensesByPaymentMethod return list of expense by PaymentMethod inside page object when successful")
    void findAllExpensesByPaymentMethod_ReturnListOfExpenseByPaymentMethodInsidePageObject_WhenSuccessful() {
        ExpenseMethodType expectedPaymentMethod = ExpenseCreator.createValidExpenseResponse().getExpenseMethod();
        Page<ExpenseResponse> pageExpense = expenseService.findAllExpensesByPaymentMethod(expectedPaymentMethod, PageRequest.of(0, 1));

        Assertions.assertThat(pageExpense).isNotNull();
        Assertions.assertThat(pageExpense.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pageExpense.getContent())
                .extracting(ExpenseResponse::getExpenseMethod)
                .allMatch(expenseMethodType -> expenseMethodType.equals(expectedPaymentMethod));
    }

//    Todo: Corrigir teste
//    @Test
//    @DisplayName("findAllExpensesByIsEssential return list of Expense by Essentiality inside page object when successful")
//    void findAllExpensesByIsEssential_ReturnListOfExpenseByEssentialityInsidePageObject_WhenSuccessful() {
//        boolean expectedEssentiality = ExpenseCreator.createValidExpenseResponse().isEssential();
//        Page<ExpenseResponse> pageExpense = expenseService.find(PageRequest.of(0, 1));
//
//        Assertions.assertThat(pageExpense).isNotNull();
//        Assertions.assertThat(pageExpense.toList())
//                .isNotEmpty()
//                .hasSize(1);
//
//        Assertions.assertThat(pageExpense.getContent())
//                .extracting(ExpenseResponse::isEssential)
//                .allMatch(isEssential -> isEssential.equals(expectedEssentiality));
//    }

    @Test
    @DisplayName("softDeleteExpense sofDelete expense when successful")
    void softDeleteExpense_UpdateDeletedAtField_WhenSuccessful(){
        String deletedExpense = expenseService.softDeleteExpense(ExpenseCreator.createValidExpenseResponse().getExpenseId());

        Assertions.assertThat(deletedExpense)
                .isNotNull()
                .isEqualTo(ExpenseCreator.createValidExpenseResponse().getExpenseDescription());
    }

    @Test
    @DisplayName("softDeleteExpense throw EntityNotFoundException when Expense does not exist(")
    void softDeleteExpense_ThrowsEntityNotFoundException_WhenExpenseDoesNotExist(){
        Integer expenseId = 999;
        Assertions.assertThatThrownBy(() -> expenseService.softDeleteExpense(expenseId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Expense not found with id: " + expenseId);
    }
}