package com.yuri.freire.Cash_Stream.Incoming.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryRequestCreator;
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
public class IncomingCategoryIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;
    @Autowired
    private IncomingCategoryRepository categoryRepository;
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
    @DisplayName("findAll returns list of Incoming Category inside of Page Object When successfull")
    void findAll_ReturnsListOfIncomingCategoryInsidePageObject_WhenSuccessfull(){
        userRepository.save(userTest);
        IncomingCategory savedCategory = this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());

        String expectedName = savedCategory.getCategoryName();

        ApiResponse<PageableResponse<IncomingCategoryResponse>> categoryPage = testRestTemplate.exchange("/incoming-category/find-all", HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<IncomingCategoryResponse>>>() {}).getBody();


        Assertions.assertThat(categoryPage).isNotNull();

        Assertions.assertThat(categoryPage.getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(categoryPage.getData().toList().get(0).getCategoryName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("createIncomingCategory returns Incoming Category when successfull")
    void createIncomingCategory_ReturnsIncomingCategoryResponse_WhenSuccessfull(){
        userRepository.save(userTest);
        IncomingCategoryRequest incomingCategoryRequest = IncomingCategoryRequestCreator.createIncomingCategoryRequest();

        ResponseEntity<ApiResponse<IncomingCategoryResponse>> categoryResponse = testRestTemplate.exchange("/incoming-category/create", HttpMethod.POST, new HttpEntity<>(incomingCategoryRequest),
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(categoryResponse).isNotNull();
        Assertions.assertThat(categoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(categoryResponse.getBody()).isNotNull();
        Assertions.assertThat(categoryResponse.getBody().getData().getIncomingCategoryId()).isNotNull();
    }

    @Test
    @DisplayName("deleteByCategoryId removes category whem successfull")
    void deleteByCategoryId_RemovesCategory_WhenSuccessfull(){
        userRepository.save(userTest);
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());

        ResponseEntity<ApiResponse<String>> deletedCategory = testRestTemplate.exchange("/incoming-category/delete/1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(deletedCategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedCategory.getBody().getData()).isEqualTo(IncomingCategoryCreator.createValidCategory().getCategoryName());
        Assertions.assertThat(deletedCategory.getBody().getMessage()).isEqualTo("Category deleted successfully");
    }

    @Test
    @DisplayName("deleteByCategoryId throws EntityNotFoundException if category was not found")
    void deleteByCategoryId_ThrowsEntityNotFounException_IfCategoryWasNotFound(){
        userRepository.save(userTest);
        Integer categoryId = 325;
        ResponseEntity<ApiResponse<String>> deletedCategory = testRestTemplate.exchange("/incoming-category/delete/" + categoryId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {});

        Assertions.assertThat(deletedCategory.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedCategory.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(deletedCategory.getBody().getData()).isEqualTo(null);
        Assertions.assertThat(deletedCategory.getBody().getErrors().get(0)).isEqualTo("Category not found: "+categoryId);
        Assertions.assertThat(deletedCategory.getBody().getMessage()).isEqualTo("Resource not found");
    }
}
