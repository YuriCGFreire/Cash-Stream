package com.yuri.freire.Cash_Stream.Expense.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryRequest;
import com.yuri.freire.Cash_Stream.Expense.controllers.model.ExpenseCategoryResponse;
import com.yuri.freire.Cash_Stream.Expense.entities.ExpenseCategory;
import com.yuri.freire.Cash_Stream.Expense.entities.repositories.ExpenseCategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryCreator;
import com.yuri.freire.Cash_Stream.util.expense.ExpenseCategoryRequestCreator;
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
public class ExpenseCategoryIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

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
    @DisplayName("createExpenseCategory persist ExpenseCategory when successful")
    void createExpenseCategory_PersistExpenseCategory_WhenSuccessful() {
        userRepository.save(userTest);
        ExpenseCategoryRequest expenseCategoryRequest = ExpenseCategoryRequestCreator.createExpenseCategoryRequest();
        ExpenseCategoryResponse expectedResponse = ExpenseCategoryCreator.createValidExpenseCategoryResponse();
        expectedResponse.setCategoryName("Magic");
        ResponseEntity<ApiResponse<ExpenseCategoryResponse>> categoryResponse = testRestTemplate.exchange(
                "/expense-category/create",
                HttpMethod.POST,
                new HttpEntity<>(expenseCategoryRequest),
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(categoryResponse).isNotNull();
        Assertions.assertThat(categoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(categoryResponse.getBody()).isNotNull();
        Assertions.assertThat(categoryResponse.getBody().getData().getExpenseCategoryId()).isNotNull();
        Assertions.assertThat(categoryResponse.getBody().getData()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("createExpenseCategory return 400 bad request when some form field is incorrect")
    void createExpenseCategory_400BadRequest_WhenSomeFormFieldIsIncorrect() {
        userRepository.save(userTest);
        ExpenseCategoryRequest expenseCategoryRequest = ExpenseCategoryRequestCreator.createInvalidCategoryRequest();
        ExpenseCategoryResponse expectedCategoryResponse = ExpenseCategoryCreator.createValidExpenseCategoryResponse();
        ResponseEntity<ApiResponse<ExpenseCategoryResponse>> categoryResponse = testRestTemplate.exchange(
                "/expense-category/create",
                HttpMethod.POST,
                new HttpEntity<>(expenseCategoryRequest),
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(categoryResponse).isNotNull();
        Assertions.assertThat(categoryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(categoryResponse.getBody().isSuccess()).isFalse();
        Assertions.assertThat(categoryResponse.getBody().getMessage()).isEqualTo("Argument not valid exception. Check form fields");
        Assertions.assertThat(categoryResponse.getBody().getErrorCode()).isEqualTo(400);
        Assertions.assertThat(categoryResponse.getBody().getErrors().get(0)).isEqualTo("categoryName: Category name length must be between 3 and 50 characters");
    }

    @Test
    @DisplayName("findAllExpenses return List of ExpenseCategory inside page object when successful")
    void findAllExpenses_ReturnListOfExpenseCategoryInsidePageObject_WhenSuccessful() {
        ExpenseCategory savedCategory = expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        userRepository.save(userTest);
        String expectedCategoryName = savedCategory.getCategoryName();
        ResponseEntity<ApiResponse<PageableResponse<ExpenseCategoryResponse>>> allExpenses = testRestTemplate.exchange(
                "/expense-category/find-all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<ExpenseCategoryResponse>>>() {
                }
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
        this.expenseCategoryRepository.save(ExpenseCategoryCreator.createExpenseCategoryToBeSaved());
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<String>> deletedExpenseCategoryResponse = testRestTemplate.exchange(
                "/expense-category/delete/1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(deletedExpenseCategoryResponse).isNotNull();
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedExpenseCategoryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getMessage()).isEqualTo("Category deleted successfully");
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getData()).isEqualTo(expectedCategoryName);
    }

    @Test
    @DisplayName("deleteByCategoryId throws EntityNotFoundException when category does not exist")
    void deleteByCategoryId_ThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        userRepository.save(userTest);

        ResponseEntity<ApiResponse<String>> deletedExpenseCategoryResponse = testRestTemplate.exchange(
                "/expense-category/delete/432432",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Assertions.assertThat(deletedExpenseCategoryResponse).isNotNull();
        Assertions.assertThat(deletedExpenseCategoryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().isSuccess()).isFalse();
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(deletedExpenseCategoryResponse.getBody().getErrors().get(0)).isEqualTo("Category not found: 432432");
    }
}
