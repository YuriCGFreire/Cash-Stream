package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingService;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.repositories.RecurrenceRepository;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Incoming Controller")
class IncomingControllerTest {

    @InjectMocks
    private IncomingController incomingController;

    @Mock
    private IncomingService incomingService;

    @Mock
    private IncomingCategoryService categoryServiceMock;

    @Mock
    private IncomingSubcategoryService subcatergoyServiceMock;

    @Mock
    private RecurrenceService recurrenceServiceMock;

    @Mock
    private RecurrenceRepository recurrenceRepositoryMock;

    @Mock
    private HttpServletRequest requestMock;

    @BeforeEach
    void setUp(){
        categoryServiceMock.createIncomingCategory(IncomingCategoryRequestCreator.createIncomingCategoryRequest());
        subcatergoyServiceMock.createIncomingSubcategory(IncomingSubcategoryRequestCreator.createIncomingSubcategoryRequest());
        recurrenceRepositoryMock.save(Recurrence
                .builder()
                .recurrenceFrequency(RecurrenceType.ANNUAL)
                .build());
        IncomingResponse incomingResponse = IncomingCreator.createValidIncomingResponse();
        PageImpl<IncomingResponse> incomingPage = new PageImpl<>(List.of(incomingResponse));

        BDDMockito.when(requestMock.getRequestURI())
                .thenReturn("/incoming-test-path");

        BDDMockito.when(incomingService.createIncoming(ArgumentMatchers.any()))
                .thenReturn(incomingResponse);

        BDDMockito.when(incomingService.findAllIncomings(ArgumentMatchers.any()))
                .thenReturn(incomingPage);

        BDDMockito.when(incomingService.findAllIncomingsByCategory(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(incomingPage);

        BDDMockito.when(incomingService.findAllIncomingsBySubcategory(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(incomingPage);

        BDDMockito.when(incomingService.deleteByIncomingId(ArgumentMatchers.anyInt()))
                .thenReturn(incomingResponse.getIncomingDescription());
    }

    @Test
    @DisplayName("createIncoming returns Incoming Response when successful")
    void createIncoming_ReturnsIncomingResponse_WhenSuccessfull(){
        IncomingResponse expcetedIncomingResponse = IncomingCreator.createValidIncomingResponse();
        ResponseEntity<ApiResponse<IncomingResponse>> incomingResponse = incomingController.createIncoming(IncomingRequestCreator.createIncomningRequest(), requestMock);

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
        ResponseEntity<ApiResponse<Page<IncomingResponse>>> incomingPage = incomingController.findAllIncomings(requestMock, null);

        Assertions.assertThat(incomingPage.getBody().getData()).isNotNull();
        Assertions.assertThat(incomingPage.getBody().getData().toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings fetched successfully");
    }

    @Test
    @DisplayName("findAllIncomingsByCategory return list of Incomings inside of Page Object selected by category name when successful")
    void findAllIncomingsByCategory_ReturnListOfIncomingsInsideOfPageObjectSelectedByCategoryName_WhenSuccessful(){
        String expctedCategoryName = IncomingCategoryCreator.createValidCategory().getIncomingCategoryName();
        ResponseEntity<ApiResponse<Page<IncomingResponse>>> incomingPage = incomingController.findAllIncomingsByCategory(expctedCategoryName, requestMock, null);

        incomingPage.getBody().getData().forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getIncomingCategory())
                    .isNotNull()
                    .isEqualTo(expctedCategoryName);

            Assertions.assertThat(incomingResponse.getIncomingId())
                    .isNotNull();
        });
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by category fetched successfully");
    }

    @Test
    @DisplayName("findAllIncomingsBySubcategory return list of Incomings inside of Page Object selected by subcategory name when successful")
    void findAllIncomingsBySubcategory_ReturnListOfIncomingsInsideOfPageObjectSelectedBySuncategoryName_WhenSuccessful(){
        String expctedSubcategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getIncomingSubcategoryName();
        ResponseEntity<ApiResponse<Page<IncomingResponse>>> incomingPage = incomingController.findAllIncomingsBySubcategory(expctedSubcategoryName, requestMock, null);

        incomingPage.getBody().getData().forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getIncomingSubcategory())
                    .isNotNull()
                    .isEqualTo(expctedSubcategoryName);

            Assertions.assertThat(incomingResponse.getIncomingId())
                    .isNotNull();
        });
        Assertions.assertThat(incomingPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(incomingPage.getBody().isSuccess()).isTrue();
        Assertions.assertThat(incomingPage.getBody().getErrors()).isNull();
        Assertions.assertThat(incomingPage.getBody().getMessage()).isEqualTo("List of incomings by subcategory fetched successfully");
    }

    @Test
    @DisplayName("deleteByIncomingId removes incoming when successful")
    void deleteByIncomingId_RemovesIncoming_WhenSuccessful(){
        String expctedIncomingDescription = IncomingCreator.createValidIncoming().getIncomingDescription();
        ResponseEntity<ApiResponse<String>> deletedIncoming = incomingController.deleteByIncomingId(IncomingCreator.createValidIncoming().getIncomingId(), requestMock);

        Assertions.assertThat(deletedIncoming.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedIncoming.getBody().getData()).isEqualTo(expctedIncomingDescription);
        Assertions.assertThat(deletedIncoming.getBody().getMessage()).isEqualTo("Incoming deleted successfully");
        Assertions.assertThat(deletedIncoming.getBody().isSuccess()).isTrue();
        Assertions.assertThat(deletedIncoming.getBody().getErrors()).isNull();
    }
}