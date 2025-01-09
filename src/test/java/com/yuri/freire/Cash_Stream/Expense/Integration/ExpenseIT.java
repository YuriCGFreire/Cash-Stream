package com.yuri.freire.Cash_Stream.Expense.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.Expense;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.entity_enum.ExpenseMethodType;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseMethodRespository;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseRepository;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseRequestCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExpenseIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSubcategoryRepository expenseSubcategoryRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private ExpenseMethodRespository expenseMethodRespository;

    @Autowired
    private UserRepository userRepository;

    private static final User userTest = User.builder()
            .firstname("Yuri")
            .lastname("Freire")
            .username("Yuri Freire")
            .email("gurgel@gurgel.com")
            .password("$2a$10$PHo.d506nvwf//eBXlflAulA7Kl1.VEOrEw1y00e/DSYv8BNUAQjC")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("Yuri Freire", "Ke6pqw84#");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("createExpense persist Expense when successful")
    void createExpense_PersistExpense_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        userRepository.save(userTest);
        ExpenseResponse expectedExpenseResponse = ExpenseCreator.createValidExpenseResponse();
        ExpenseRequest expenseRequest = ExpenseRequestCreator.createExpenseRequest();

        ResponseEntity<ApiResponse<ExpenseResponse>> createdExpense = testRestTemplate.exchange(
                "/expense/create",
                HttpMethod.POST,
                new HttpEntity<>(expenseRequest),
                new ParameterizedTypeReference<ApiResponse<ExpenseResponse>>() {
                }
        );


        Assertions.assertThat(createdExpense.getBody().getErrors()).isNull();
        Assertions.assertThat(createdExpense.getBody().isSuccess()).isTrue();
        Assertions.assertThat(createdExpense.getBody().getMessage()).isEqualTo( "Expense created successfully");
        Assertions.assertThat(createdExpense.getBody().getData()).isNotNull().isInstanceOf(ExpenseResponse.class)
                .isEqualTo(expectedExpenseResponse);
        Assertions.assertThat(createdExpense.getBody().getData()).isNotNull().isEqualTo(expectedExpenseResponse);
    }

    @Test
    @DisplayName("createExpense return 400 bad request when some form field is incorrect")
    void createExpense_Return400BadRequest_WhenSomeFormFieldIsIncorrect() {
        userRepository.save(userTest);
        ExpenseRequest expenseRequest = ExpenseRequestCreator.createInvalidExpenseRequest();

        ResponseEntity<ApiResponse<ExpenseResponse>> createdExpense = testRestTemplate.exchange(
                "/expense/create",
                HttpMethod.POST,
                new HttpEntity<>(expenseRequest),
                new ParameterizedTypeReference<ApiResponse<ExpenseResponse>>() {
                }
        );

        Assertions.assertThat(createdExpense.getBody().getErrors()).isNotNull();
        Assertions.assertThat(createdExpense.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(createdExpense.getBody().getErrorCode()).isEqualTo(400);
        Assertions.assertThat(createdExpense.getBody().isSuccess()).isFalse();
        Assertions.assertThat(createdExpense.getBody().getMessage()).isEqualTo(  "Argument not valid exception. Check form fields");
        Assertions.assertThat(createdExpense.getBody().getErrors()).isNotEmpty().containsAnyOf(
                "recurrence: Recurrence cannot be null",
                "expenseDescription: Expense description cannot be null",
                "expenseSubcategory: Expense subcategory cannot be null",
                "expenseCategory: Expense category cannot be null",
                "expenseCategory: Expense category name length must be between 3 and 50 characters",
                "expenseMethod: Expense method cannot be null",
                "expenseAmount: Expense amount must be greater than 0",
                "expenseDescription: Expense description length must be between 3 and 50 characters",
                "expenseSubcategory: Expense subcategory name length must be between 3 and 50 characters"
        );
    }

    @Test
    @DisplayName("findAllExpenses return list of Expenses inside page object when successful")
    void findAllExpenses_ReturnListOfExpensesInsidePageObject_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpenses = testRestTemplate.exchange(
                "/expense/find-all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );

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
        ExpenseCategory expectedCategory = expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpensesByCategoryName = testRestTemplate.exchange(
                "/expense/find-by-category?categoryName="+expectedCategory.getCategoryName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );
        Assertions.assertThat(allExpensesByCategoryName.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesByCategoryName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesByCategoryName.getBody().getMessage()).isEqualTo("Expenses fetched by category successfully");
        Assertions.assertThat(allExpensesByCategoryName.getBody().getData().toList())
                .extracting(ExpenseResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(expectedCategory.getCategoryName()));
    }

    @Test
    @DisplayName("indAllExpensesByCategoryName return return 404 NotFound when Expense Category does not exist")
    void findAllExpensesByCategoryName_Return404NotFound_WhenExpenseCategoryDoesNotExist() {
        String expectedCategory = "someRandomCategoryName";
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpensesByCategoryName = testRestTemplate.exchange(
                "/expense/find-by-category?categoryName="+expectedCategory,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );
        Assertions.assertThat(allExpensesByCategoryName.getBody().getData()).isNull();
        Assertions.assertThat(allExpensesByCategoryName.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(allExpensesByCategoryName.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(allExpensesByCategoryName.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(allExpensesByCategoryName.getBody().getErrors()).contains("Category not found: someRandomCategoryName");
    }

    @Test
    @DisplayName("findAllExpensesBySubcategoryName return list of Expenses inside Page Object by subcategoryName when successful")
    void findAllExpensesBySubcategoryName_ReturnListOfExpensesInsidePageObjectBySubcategoryName_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        ExpenseSubcategory expectedSubcategory = expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpensesBySubcategoryName = testRestTemplate.exchange(
                "/expense/find-by-subcategory?subcategoryName="+expectedSubcategory.getSubCategoryName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesBySubcategoryName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getMessage()).isEqualTo( "Expenses fetched by subcategory successfuly");
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getData().toList())
                .extracting(ExpenseResponse::getSubCategoryName)
                .allMatch(subCategoryName -> subCategoryName.equals(expectedSubcategory.getSubCategoryName()));
    }

    @Test
    @DisplayName("findAllExpensesBySubcategoryName return 404 NotFound when Expense subcategory does mot exist")
    void findAllExpensesBySubcategoryName_Return404NotFound_WhenExpensesubcategoryDoesNotExist() {
        String expectedSubcategory = "someRandomSubcategoryName";
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpensesBySubcategoryName = testRestTemplate.exchange(
                "/expense/find-by-subcategory?subcategoryName="+expectedSubcategory,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getData()).isNull();
        Assertions.assertThat(allExpensesBySubcategoryName.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(allExpensesBySubcategoryName.getBody().getErrors()).contains("Subcategory not found: someRandomSubcategoryName");
    }

    @Test
    @DisplayName("findAllExpensesByPaymentMethod return list of Expenses inside Page object by paymentMethod when successful")
    void findAllExpensesByPaymentMethod_ReturnListOfExpensesInsidePageObjectByPaymentMethod_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allExpensesByPaymentMethod = testRestTemplate.exchange(
                "/expense/find-by-method?expenseMethodType=" + ExpenseMethodType.CREDIT,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {}
        );
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpensesByPaymentMethod.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getMessage()).isEqualTo( "Expenses fetched by payment method successfuly");
        Assertions.assertThat(allExpensesByPaymentMethod.getBody().getData().toList())
                .extracting(ExpenseResponse::getExpenseMethod)
                .allMatch(expenseMethodType -> expenseMethodType.equals(ExpenseMethodType.CREDIT));
    }

    @Test
    @DisplayName("findAllByIsEssential return list of Expenses inside Page object by it essentiality when successful")
    void findAllByIsEssential_ReturnListOfExpensesInsidePageObjectByItEssentiality_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        Expense savedExpense = expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<PageableResponse<ExpenseResponse>>> allByIsEssential = testRestTemplate.exchange(
                "/expense/isEssential?isEssential=" + savedExpense.isEssential(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseResponse>>>() {
                }
        );
        Assertions.assertThat(allByIsEssential.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allByIsEssential.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allByIsEssential.getBody().getMessage()).isEqualTo( "Expenses fetched by essentiality successfully");
        Assertions.assertThat(allByIsEssential.getBody().getData().toList())
                .extracting(ExpenseResponse::isEssential)
                .allMatch(isEssential -> isEssential.equals(savedExpense.isEssential()));
    }

    @Test
    @DisplayName("softDeleteExpense update deletedAt field when successful")
    void softDeleteExpense_UpdateDeletedAtField_WhenSuccessful(){
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        Expense savedExpense = expenseRepository.save(ExpenseCreator.createExpenseToBeSaved());
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<String>> deletedExpense = testRestTemplate.exchange(
                "/expense/delete/" + savedExpense.getExpenseId() + "/soft",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<String>>() {}
        );

        Assertions.assertThat(deletedExpense).isNotNull();
        Assertions.assertThat(deletedExpense.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedExpense.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedExpense.getBody().getMessage()).isEqualTo("Expense deleted successfuly");
        Assertions.assertThat(deletedExpense.getBody().getData()).isEqualTo(savedExpense.getExpenseDescription());
    }

    @Test
    @DisplayName("softDeleteExpense throw EntityNotFoundException when Expense does not exist(")
    void softDeleteExpense_ThrowsEntityNotFoundException_WhenExpenseDoesNotExist(){
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<String>> deletedExpense = testRestTemplate.exchange(
                "/expense/delete/" + 999 + "/soft",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<String>>() {}
        );

        Assertions.assertThat(deletedExpense).isNotNull();
        Assertions.assertThat(deletedExpense.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedExpense.getBody().isSuccess()).isFalse();
        Assertions.assertThat(deletedExpense.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(deletedExpense.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(deletedExpense.getBody().getErrors().get(0)).isEqualTo("Expense not found with id: 999");
    }
}
