package com.yuri.freire.Cash_Stream.Expense.services;

import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.services.factory.ExpenseFactory;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryRequestCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;
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
@DisplayName("Tests for Expense Subcategory Service")
class ExpenseSubcategoryServiceTest {

    @InjectMocks
    private ExpenseSubcategoryService expenseSubcategoryService;
    @Mock
    private ExpenseSubcategoryRepository expenseSubcategoryRepositoryMock;
    @Mock
    private ExpenseCategoryService expenseCategoryServiceMock;
    @Mock
    private ExpenseFactory expenseFactoryMock;

    @BeforeEach
    void setUp(){
        ExpenseCategory expenseCategory = ExpenseCategoryCreator.createValidExpenseCategory();
        ExpenseSubcategory expenseSubcategory = ExpenseSubcategoryCreator.createValidExpenseSubcategory();
        ExpenseSubcategoryResponse expenseSubcategoryResponse = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        PageImpl<ExpenseSubcategoryResponse> pageSubcategory = new PageImpl<>(List.of(expenseSubcategoryResponse));

        BDDMockito.when(expenseSubcategoryRepositoryMock.save(ArgumentMatchers.any(ExpenseSubcategory.class)))
                .thenReturn(expenseSubcategory);

        BDDMockito.when(expenseCategoryServiceMock.findByCategoryName(ArgumentMatchers.any()))
                        .thenReturn(ExpenseCategoryCreator.createValidExpenseCategory());

        BDDMockito.when(expenseFactoryMock.createExpenseSubcategory(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(expenseSubcategory);

        BDDMockito.when(expenseFactoryMock.createExpenseSubcategoryResponse(ArgumentMatchers.any()))
                .thenReturn(expenseSubcategoryResponse);

        BDDMockito.when(expenseSubcategoryRepositoryMock.findAllSubcategoryExpenses(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pageSubcategory);

        BDDMockito.when(expenseSubcategoryRepositoryMock.findBySubCategoryName(ArgumentMatchers.any()))
                .thenReturn(Optional.ofNullable(expenseSubcategory));

        BDDMockito.when(expenseSubcategoryRepositoryMock.findBySubCategoryName("Some random subcategoryname"))
                .thenReturn(Optional.empty());

        BDDMockito.when(expenseCategoryServiceMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(expenseCategory);

        BDDMockito.when(expenseCategoryServiceMock.findByCategoryName("Some random categoryname"))
                .thenThrow(new EntityNotFoundException("Category not found: Some random categoryname"));

        BDDMockito.when(expenseSubcategoryRepositoryMock.findAllSubcategoryExpensesByCategory(ArgumentMatchers.any(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pageSubcategory);

        BDDMockito.when(expenseSubcategoryRepositoryMock.findById(1))
                .thenReturn(Optional.of(expenseSubcategory));

        BDDMockito.when(expenseSubcategoryRepositoryMock.findById(999))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("createExpenseSubcategory persist data and returns ExpenseSubcategoryResponse when successfull")
    void createExpenseSubcategory_PersistDataAndReturnExpenseSubcategoryResponse_WhenSuccessful(){
        ExpenseSubcategoryResponse validSubcategoryResponse = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        ExpenseSubcategoryResponse savedExpenseSubcategoryResponse = expenseSubcategoryService.createExpenseSubcategory(ExpenseSubcategoryRequestCreator.createExpenseSubcategoryRequest());

        Assertions.assertThat(savedExpenseSubcategoryResponse)
                .isNotNull()
                .isEqualTo(validSubcategoryResponse)
                .isInstanceOf(ExpenseSubcategoryResponse.class);

        Assertions.assertThat(savedExpenseSubcategoryResponse.getCategoryName())
                .isNotNull()
                .isEqualTo(validSubcategoryResponse.getCategoryName());
    }

    @Test
    @DisplayName("findBySubcategoryName returns ExpenseSubcategory by name when successful")
    void findBySubcategoryName_ReturnsExpenseSubcategoryByName_WhenSuccessful(){
        ExpenseSubcategory expectedExpenseSubcategory = ExpenseSubcategoryCreator.createValidExpenseSubcategory();
        ExpenseSubcategory fetchedExpenseSubcategory = expenseSubcategoryService.findBySubcategoryName(expectedExpenseSubcategory.getSubCategoryName());

        Assertions.assertThat(fetchedExpenseSubcategory)
                .isNotNull()
                .isInstanceOf(ExpenseSubcategory.class);

        Assertions.assertThat(fetchedExpenseSubcategory.getExpenseSubcategoryId())
                .isNotNull()
                .isEqualTo(expectedExpenseSubcategory.getExpenseSubcategoryId());
        Assertions.assertThat(fetchedExpenseSubcategory.getSubCategoryName())
                .isNotNull()
                .isEqualTo(expectedExpenseSubcategory.getSubCategoryName());
        Assertions.assertThat(fetchedExpenseSubcategory.getExpenseCategory().getCategoryName())
                .isNotNull()
                .isEqualTo(expectedExpenseSubcategory.getExpenseCategory().getCategoryName());
    }

    @Test
    @DisplayName("findBySubcategoryName throw EntityNotFoundException when subcategory does not exist")
    void findBySubcategoryName_ThrowEntityNotFoundException_WhenSubcategoryDoesNotExist(){
        String subcategoryName = "Some random subcategoryname";
        Assertions.assertThatThrownBy(() -> expenseSubcategoryService.findBySubcategoryName(subcategoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Subcategory not found: " + subcategoryName);
    }

    @Test
    @DisplayName("findAllSubcategoryExpenses returns list of Expense Subcategory inside of Page Object When successful")
    void findAllSubcategoryExpenses_ReturnsListOfExpensesSubcategoryInsideOfPageObject_WhenSuccessful(){
        ExpenseSubcategoryResponse expectedSubcategoryResponse = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        Page<ExpenseSubcategoryResponse> pageSubcategory = expenseSubcategoryService.findAllSubcategoryExpenses(PageRequest.of(0, 1));

        Assertions.assertThat(pageSubcategory).isNotNull();
        Assertions.assertThat(pageSubcategory.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageSubcategory.toList().get(0))
                .usingRecursiveComparison()
                .isEqualTo(expectedSubcategoryResponse)
                .isInstanceOf(ExpenseSubcategoryResponse.class);
    }

    @Test
    @DisplayName("findAllSubcategoryExpensesByCategory returns list of Expense Subcategory by Category name inside of Page Object When successful")
    void findAllSubcategoryExpensesByCategory_ReturnsListOfExpenseSubcategoryByCategoryName_WhenSuccessful(){
        ExpenseSubcategoryResponse expenseSubcategoryResponse = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        Page<ExpenseSubcategoryResponse> pageSubcategory = expenseSubcategoryService.findAllSubcategoryExpensesByCategory(expenseSubcategoryResponse.getCategoryName(), PageRequest.of(0, 1));

        Assertions.assertThat(pageSubcategory).isNotNull();
        Assertions.assertThat(pageSubcategory.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(pageSubcategory.getContent())
                .extracting(ExpenseSubcategoryResponse::getCategoryName)
                .allMatch(expenseMethodType -> expenseMethodType.equals(expenseSubcategoryResponse.getCategoryName()));
    }

    @Test
    @DisplayName("findAllSubcategoryExpensesByCategory throw EntityNotFounException when Category does not exist")
    void findAllSubcategoryExpensesByCategory_ThrowEntityNotFounException_WhenCategoryDoesNotExist(){
        String categoryName = "Some random categoryname";
        Assertions.assertThatThrownBy(() -> expenseSubcategoryService.findAllSubcategoryExpensesByCategory(categoryName, PageRequest.of(0, 1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryName);
    }

    @Test
    @DisplayName("deleteBySubcategoryId delete expense when successful")
    void deleteBySubcategoryId_DeleteExpense_WhenSuccessful(){
        String expcetedSubcategoryName = ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName();
        String subcategoryName = expenseSubcategoryService.deleteBySubcategoryId(1);
        Assertions.assertThat(subcategoryName).isNotNull().isEqualTo(expcetedSubcategoryName);
    }

    @Test
    @DisplayName("deleteBySubcategoryId throw EntityNotFoundException when subcategory does not exist")
    void deleteBySubcategoryId_ThrowEntityNotFoundException_WhenSubcategoryDoesNotExists(){
        Integer anyRandomSubcategoryId = 999;
        Assertions.assertThatThrownBy(() -> expenseSubcategoryService.deleteBySubcategoryId(anyRandomSubcategoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Subcategory not found: " + anyRandomSubcategoryId);
    }
}