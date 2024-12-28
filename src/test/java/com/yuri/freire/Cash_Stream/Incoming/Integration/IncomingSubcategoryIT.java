package com.yuri.freire.Cash_Stream.Incoming.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRespository;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryRequestCreator;
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
public class IncomingSubcategoryIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;
    @Autowired
    private IncomingSubcategoryRepository subcategoryRepository;
    @Autowired
    private IncomingCategoryRepository categoryRepository;
    @Autowired
    private UserRespository userRespository;
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
    @DisplayName("findAll returns list of Incoming Subcategory inside of Page Object When successfull")
    void findAll_ReturnsListOfIncomingSubcategoryInsidePageObject_WhenSuccessfull(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        IncomingSubcategory savedSubcategory = this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        userRespository.save(userTest);
        String expectedCategoryName = savedSubcategory.getIncomingCategory().getCategoryName();
        String expectedSubcategoryName = savedSubcategory.getSubCategoryName();
        Integer expectedSubcategoryId = savedSubcategory.getIncomingSubcategoryId();

        ApiResponse<PageableResponse<IncomingSubcategoryResponse>> subcategoryPage = testRestTemplate.exchange(
                "/incoming-subcategory/find-all", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<IncomingSubcategoryResponse>>>() {}).getBody();

        Assertions.assertThat(subcategoryPage).isNotNull();
        Assertions.assertThat(subcategoryPage.getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(subcategoryPage.getData().toList().get(0).getSubCategoryName())
                .isNotNull()
                .isEqualTo(expectedSubcategoryName);
        Assertions.assertThat(subcategoryPage.getData().toList().get(0).getCategoryName())
                .isNotNull()
                .isEqualTo(expectedCategoryName);
        Assertions.assertThat(subcategoryPage.getData().toList().get(0).getIncomingSubcategoryId())
                .isNotNull()
                .isEqualTo(expectedSubcategoryId);
    }

    @Test
    @DisplayName("findAllByCategoryName returns a list os Incoming Subcategory by category name inside of a page object when successfull")
    void findAllByCategoryName_ReturnsAListOfSubcategoryByCategoryNameInsideOfPageObject_WhenSucessfull(){
        IncomingCategory savedCategory = this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        userRespository.save(userTest);

        ApiResponse<PageableResponse<IncomingSubcategoryResponse>> subcategoryPage = testRestTemplate.exchange("/incoming-subcategory/all-by-category?categoryName="+savedCategory.getCategoryName(), HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<IncomingSubcategoryResponse>>>() {}).getBody();

        Assertions.assertThat(subcategoryPage).isNotNull();
        Assertions.assertThat(subcategoryPage.getData().toList())
                .isNotEmpty()
                .hasSize(2);
        Assertions.assertThat(subcategoryPage.getData().toList())
                .allMatch(subcategory -> subcategory.getCategoryName().equals(savedCategory.getCategoryName()));
    }

    @Test
    @DisplayName("findAllByCategoryName throw EntityNotFoundException when Category does not exist")
    void findAllByCategoryName_ThrowEntityNotFoundException_WhenCategoryDoesNotExist(){
        String incomingCategoryName = "Some rando categoryName";
        userRespository.save(userTest);

        ResponseEntity<ApiResponse<PageableResponse<IncomingSubcategoryResponse>>> subcategoryPageResponse = testRestTemplate.exchange("/incoming-subcategory/all-by-category?categoryName=" + incomingCategoryName, HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiResponse<PageableResponse<IncomingSubcategoryResponse>>>() {
                });

        ApiResponse<PageableResponse<IncomingSubcategoryResponse>> body = subcategoryPageResponse.getBody();

        Assertions.assertThat(subcategoryPageResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.isSuccess()).isFalse();
        Assertions.assertThat(body.getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(body.getErrorCode()).isEqualTo(404);
        Assertions.assertThat(body.getErrors())
                .isNotEmpty()
                .contains("Category not found: " + incomingCategoryName);
    }

    @Test
    @DisplayName("createIncomingSubcategory returns Incoming Subcategory when successfull")
    void createIncomingSubcategory_ReturnsIncomingSubcategory_WhenSuccessfull(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        userRespository.save(userTest);
        IncomingSubcategoryRequest incomingSubcategoryRequest = IncomingSubcategoryRequestCreator.createIncomingSubcategoryRequest();

        ResponseEntity<ApiResponse<IncomingSubcategoryResponse>> savedSubcategory = testRestTemplate.exchange("/incoming-subcategory/create", HttpMethod.POST, new HttpEntity<>(incomingSubcategoryRequest),
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(savedSubcategory).isNotNull();
        Assertions.assertThat(savedSubcategory.getBody().getData().getIncomingSubcategoryId()).isNotNull();
        Assertions.assertThat(savedSubcategory.getBody().getData().getSubCategoryName())
                .isNotNull()
                .isEqualTo(incomingSubcategoryRequest.getSubcategoryName());
        Assertions.assertThat(savedSubcategory.getBody().getData().getCategoryName())
                .isNotNull()
                .isEqualTo(incomingSubcategoryRequest.getIncomingCategoryName());
    }

    @Test
    @DisplayName("deleteByCategoryId removes category whem successfull")
    void deleteByCategoryId_RemovesCategory_WhenSuccessfull(){
        IncomingCategory savedCategory = this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        IncomingSubcategory savedSubcategory = this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        userRespository.save(userTest);

        ResponseEntity<ApiResponse<String>> deletedSubcategory = testRestTemplate.exchange(
                "/incoming-subcategory/delete-by-id?incomingSubcategoryId="+savedSubcategory.getIncomingSubcategoryId(),
                HttpMethod.DELETE, null,
                new ParameterizedTypeReference<>() {
                }
        );

        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedSubcategory.getBody().getData()).isEqualTo(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName());
        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Subcategory deleted successfully");
    }

    @Test
    @DisplayName("deleteBySubccategoryId throws EntityNotFoundException if subcategory was not found")
    void deleteByCategoryId_ThrowsEntityNotFounException_IfCategoryWasNotFound(){
        userRespository.save(userTest);
        Integer subcategoryId = 325;
        ResponseEntity<ApiResponse<String>> deletedSubcategory = testRestTemplate.exchange("/incoming-subcategory/delete-by-id?incomingSubcategoryId=" + subcategoryId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedSubcategory.getBody().getErrorCode()).isEqualTo(404);
        Assertions.assertThat(deletedSubcategory.getBody().getData()).isEqualTo(null);
        Assertions.assertThat(deletedSubcategory.getBody().getErrors().get(0)).isEqualTo("Subcategory not found: "+ subcategoryId);
        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Resource not found");
    }

}
