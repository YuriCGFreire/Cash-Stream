package com.yuri.freire.Cash_Stream.Expense.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseSubcategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseSubcategoryRequestCreator;
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
public class ExpenseSubcategoryIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ExpenseSubcategoryRepository expenseSubcategoryRepository;
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
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
    @DisplayName("createExpenseCategory persist ExpenseSubcategory when successful")
    void createExpenseCategory_PersistExpenseSubcategory_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        userRepository.save(userTest);
        ExpenseSubcategoryRequest expenseSubcategoryRequest = ExpenseSubcategoryRequestCreator.createExpenseSubcategoryRequest();
        ExpenseSubcategoryResponse expectedSubcategory = ExpenseSubcategoryCreator.createValidExpenseSubcategoryResponse();
        ResponseEntity<ApiResponse<ExpenseSubcategoryResponse>> expenseSubcategoryResponse = testRestTemplate.exchange(
                "/expense-subcategory/create",
                HttpMethod.POST,
                new HttpEntity<>(expenseSubcategoryRequest),
                new ParameterizedTypeReference<ApiResponse<ExpenseSubcategoryResponse>>() {
                }
        );

        Assertions.assertThat(expenseSubcategoryResponse).isNotNull();
        Assertions.assertThat(expenseSubcategoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getMessage()).isEqualTo("Subcategory created successfully");
        Assertions.assertThat(expenseSubcategoryResponse.getBody().isSuccess()).isTrue();
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getErrors()).isNull();
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getData()).isNotNull().isEqualTo(expectedSubcategory);
    }

    @Test
    @DisplayName("createExpenseCategory return 400 bad request when some form field is incorrect")
    void createExpenseCategory_Return400BadRequest_WhenSomeFormFieldIsIncorrect() {
        userRepository.save(userTest);
        ExpenseSubcategoryRequest invalidSubcategoryRequest = ExpenseSubcategoryRequestCreator.createInvalidSubcategoryRequest();
        ResponseEntity<ApiResponse<ExpenseSubcategoryResponse>> expenseSubcategoryResponse = testRestTemplate.exchange(
                "/expense-subcategory/create",
                HttpMethod.POST,
                new HttpEntity<>(invalidSubcategoryRequest),
                new ParameterizedTypeReference<ApiResponse<ExpenseSubcategoryResponse>>() {
                }
        );

        Assertions.assertThat(expenseSubcategoryResponse).isNotNull();
        Assertions.assertThat(expenseSubcategoryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getMessage()).isEqualTo("Argument not valid exception. Check form fields");
        Assertions.assertThat(expenseSubcategoryResponse.getBody().isSuccess()).isFalse();
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getErrors()).isNotEmpty().containsAnyOf(
                "subcategoryName: Subcategory name length must be between 3 and 50 characters",
                "categoryName: Category name length must be between 3 and 50 characters",
                "categoryName: Category name cannot be null",
                "subcategoryName: Category name cannot be null"
        );
        Assertions.assertThat(expenseSubcategoryResponse.getBody().getErrorCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("findAllExpenses return List of ExpenseSubcategory inside page object when successful")
    void findAllExpenses_ReturnListOfExpenseSubcategoryInsidePageObject_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>> allExpenses = testRestTemplate.exchange(
                "/expense-subcategory/find-all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>>() {
                }
        );

        Assertions.assertThat(allExpenses).isNotNull();
        Assertions.assertThat(allExpenses.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpenses.getBody().getMessage()).isEqualTo("Subcategories fetched successfully");
        Assertions.assertThat(allExpenses.getBody().isSuccess()).isTrue();
        Assertions.assertThat(allExpenses.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("findAllExpenses return List of ExpenseSubcategory inside page object by Category when successful")
    void findAllExpenses_ReturnListOfExpenseSubcategoryInsidePageObjectByCategory_WhenSuccessful() {
        ExpenseCategory expectedCategoryName = expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        ExpenseSubcategory savedSubcategory = expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>> allExpenses = testRestTemplate.exchange(
                "/expense-subcategory/all-by-category?categoryName=" + savedSubcategory.getExpenseCategory().getCategoryName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>>() {
                }
        );

        Assertions.assertThat(allExpenses).isNotNull();
        Assertions.assertThat(allExpenses.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(allExpenses.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(allExpenses.getBody().getMessage()).isEqualTo("Subcategories fetched by category successfully");
        Assertions.assertThat(allExpenses.getBody().isSuccess()).isTrue();
        Assertions.assertThat(allExpenses.getBody().getErrors()).isNull();
        Assertions.assertThat(allExpenses.getBody().getData().toList())
                .extracting(ExpenseSubcategoryResponse::getCategoryName)
                .allMatch(categoryName -> categoryName.equals(expectedCategoryName.getCategoryName()));
    }

    @Test
    @DisplayName("findAllExpenses return 404 NotFound when category does not exist")
    void findAllExpenses_Return404NotFound_WhenCategoryDoesNotExist() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>> allExpenses = testRestTemplate.exchange(
                "/expense-subcategory/all-by-category?categoryName=anyRandomName",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseSubcategoryResponse>>>() {
                }
        );
        System.out.println(allExpenses);

        Assertions.assertThat(allExpenses).isNotNull();
        Assertions.assertThat(allExpenses.getBody().getData()).isNull();
        Assertions.assertThat(allExpenses.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(allExpenses.getBody().getMessage()).isEqualTo( "Resource not found");
        Assertions.assertThat(allExpenses.getBody().isSuccess()).isFalse();
        Assertions.assertThat(allExpenses.getBody().getErrors()).contains(
                "Category not found: anyRandomName"
        );
        Assertions.assertThat(allExpenses.getBody().getErrorCode()).isEqualTo(404);

    }

    @Test
    @DisplayName("deleteSubcategoryById delete ExpenseSubcategory when successful")
    void deleteSubcategoryById_DeleteExpenseSubcategory_WhenSuccessful() {
        expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        ExpenseSubcategory savedSubcategory = expenseSubcategoryRepository.save(ExpenseSubcategoryCreator.createValidExpenseSubcategoryTobeSaved());
        userRepository.save(userTest);
        String expectedSubCategoryName = ExpenseSubcategoryCreator.createValidExpenseSubcategory().getSubCategoryName();

        ResponseEntity<ApiResponse<String>> deletedSubcategory = testRestTemplate.exchange(
                "/expense-subcategory/delete/" + savedSubcategory.getExpenseSubcategoryId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<String>>() {
                }
        );

        Assertions.assertThat(deletedSubcategory).isNotNull();
        Assertions.assertThat(deletedSubcategory.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Subcategory deleted successfully");
        Assertions.assertThat(deletedSubcategory.getBody().getData()).isEqualTo(expectedSubCategoryName);
    }

    @Test
    @DisplayName("deleteSubcategoryById return 404 NotFound when subcategory does not exist")
    void deleteSubcategoryById_Return404NotFound_WhenSubcategoryDoesNotExist() {
        userRepository.save(userTest);
        ResponseEntity<ApiResponse<String>> deletedSubcategory = testRestTemplate.exchange(
                "/expense-subcategory/delete/12908",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ApiResponse<String>>() {
                }
        );

        Assertions.assertThat(deletedSubcategory).isNotNull();
        Assertions.assertThat(deletedSubcategory.getBody().isSuccess()).isFalse();
        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(deletedSubcategory.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(deletedSubcategory.getBody().getErrors()).contains(
                "Subcategory not found: 12908"
        );
    }
}
