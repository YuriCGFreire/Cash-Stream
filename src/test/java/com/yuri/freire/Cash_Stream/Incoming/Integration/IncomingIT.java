package com.yuri.freire.Cash_Stream.Incoming.Integration;

import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.PageableResponse;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingRequestCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;
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
public class IncomingIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IncomingRepository incomingRepository;

    @Autowired
    private IncomingSubcategoryRepository subcategoryRepository;

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
    @DisplayName("createIncoming returns Incoming Response when successful")
    void createIncoming_ReturnsIncomingResponse_WhenSuccessfull(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        IncomingResponse expcetedIncomingResponse = IncomingCreator.createValidIncomingResponse();
        IncomingRequest incomingRequest = IncomingRequestCreator.createIncomningRequest();

        ResponseEntity<ApiResponse<IncomingResponse>> incomingResponse = testRestTemplate.exchange(
                "/incoming/create",
                HttpMethod.POST,
                new HttpEntity<>(incomingRequest),
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertThat(incomingResponse.getBody().getData()).isNotNull();
        Assertions.assertThat(incomingResponse.getBody().getData())
                .usingRecursiveComparison()
                .isEqualTo(expcetedIncomingResponse);
        Assertions.assertThat(incomingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingResponse.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingResponse.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingResponse.getBody().getMessage()).isEqualTo("Incoming created successfully");
    }

    @Test
    @DisplayName("findAllIncomings returns Incoming list inside of page object when Successful")
    void findAllIncomings_ReturnsIncomingListInsideOfPageObject_WhenSuccessful(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());
        IncomingResponse expectedIncoming = IncomingCreator.createValidIncomingResponse();

        ResponseEntity<ApiResponse<PageableResponse<IncomingResponse>>> incomingPage = testRestTemplate.exchange(
                "/incoming/all-incomings", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});


        Assertions.assertThat(incomingPage.getBody().getData()).isNotNull();
        Assertions.assertThat(incomingPage.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings fetched successfully");
        incomingPage.getBody().getData().forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedIncoming);
        });
    }

    @Test
    @DisplayName("findAllIncomingsByCategory return list of Incomings inside of Page Object selected by category name when successful")
    void findAllIncomingsByCategory_ReturnListOfIncomingsInsideOfPageObjectSelectedByCategoryName_WhenSuccessful(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());

        ResponseEntity<ApiResponse<PageableResponse<IncomingResponse>>> incomingPage = testRestTemplate.exchange(
                "/incoming/all-by-category?categoryName="+savedIncoming.getIncomingCategory().getCategoryName(), HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        incomingPage.getBody().getData().forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getCategoryName())
                    .isNotNull()
                    .isEqualTo(savedIncoming.getIncomingCategory().getCategoryName());
        });
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by category fetched successfully");
    }

    @Test
    @DisplayName("findAllIncomingsByCategory return empty list of Incomings when category does not exists")
    void findAllIncomingsByCategory_ReturnEmptyListOfIncomings_WhenCategoryDoesNotExists(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());

        ResponseEntity<ApiResponse<PageableResponse<IncomingResponse>>> incomingPage = testRestTemplate.exchange(
                "/incoming/all-by-category?categoryName=nomequalquer", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        Assertions.assertThat(incomingPage.getBody().getData()).isEmpty();
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by category fetched successfully");
    }

    @Test
    @DisplayName("findAllIncomingsBySubcategory return list of Incomings inside of Page Object selected by subcategory name when successful")
    void findAllIncomingsBySubcategory_ReturnListOfIncomingsInsideOfPageObjectSelectedBySubcategoryName_WhenSuccessful(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());

        ResponseEntity<ApiResponse<PageableResponse<IncomingResponse>>> incomingPage = testRestTemplate.exchange(
                "/incoming/all-by-subcategory?subcategoryName="+savedIncoming.getIncomingSubcategory().getSubCategoryName(), HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        incomingPage.getBody().getData().forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getSubCategoryName())
                    .isNotNull()
                    .isEqualTo(savedIncoming.getIncomingSubcategory().getSubCategoryName());
        });
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by subcategory fetched successfully");
    }

    @Test
    @DisplayName("findAllIncomingsBySubcategory return empty list of Incomings when Subcategory does not exists")
    void findAllIncomingsBySubcategory_ReturnsEmptyList_WhenSubcategoryDoesNotExists(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());

        ResponseEntity<ApiResponse<PageableResponse<IncomingResponse>>> incomingPage = testRestTemplate.exchange(
                "/incoming/all-by-subcategory?subcategoryName=nomequalquer", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        System.out.println(incomingPage.getBody().getData());

        Assertions.assertThat(incomingPage.getBody().getData()).isEmpty();
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by subcategory fetched successfully");
    }

    @Test
    @DisplayName("deleteByIncomingId removes incoming when successful")
    void deleteByIncomingId_RemovesIncoming_WhenSuccessful(){
        String expctedIncomingDescription = IncomingCreator.createValidIncoming().getIncomingDescription();
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());

        ResponseEntity<ApiResponse<String>> deletedIncoming = testRestTemplate.exchange("/incoming/delete/" + savedIncoming.getIncomingId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                });


        Assertions.assertThat(deletedIncoming.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedIncoming.getBody().getData()).isEqualTo(expctedIncomingDescription);
        Assertions.assertThat(deletedIncoming.getBody().getMessage()).isEqualTo("Incoming deleted successfully");
        Assertions.assertThat(deletedIncoming.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedIncoming.getBody().getErrors()).isNull();
    }

    @Test
    @DisplayName("deleteByIncomingId throws entity nof found exception when incomingId does not exists")
    void deleteByIncomingId_ThrowsEntityNotFoundException_WhenIncomingIdDoesNotExists(){
        this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        this.subcategoryRepository.save(IncomingSubcategoryCreator.createSubcategoryToBeSaved());
        this.userRepository.save(userTest);
        Incoming savedIncoming = this.incomingRepository.save(IncomingCreator.createValidIncomingToBeSaved());
        ResponseEntity<ApiResponse<String>> deletedIncoming = testRestTemplate.exchange("/incoming/delete/1235",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>() {
                });


        Assertions.assertThat(deletedIncoming.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(deletedIncoming.getBody().getData()).isEqualTo(null);
        Assertions.assertThat(deletedIncoming.getBody().getMessage()).isEqualTo("Resource not found");
        Assertions.assertThat(deletedIncoming.getBody().isSuccess()).isFalse();
        Assertions.assertThat(deletedIncoming.getBody().getErrors().get(0)).isEqualTo("Incoming not found: 1235");
    }

}
