package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryRequestCreator;
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
@DisplayName("Tests for Incoming Category Controller")
class IncomingCategoryControllerTest {
    @InjectMocks
    private IncomingCategoryController categoryController;
    @Mock
    private IncomingCategoryService categoryServiceMock;
    @Mock
    private HttpServletRequest requestMock;

    @BeforeEach
    void setUp(){
        PageImpl<IncomingCategoryResponse> categoryPage = new PageImpl<>(List.of(IncomingCategoryCreator.createValidCategory()));

        BDDMockito.when(categoryServiceMock.findAll(ArgumentMatchers.any()))
                .thenReturn(categoryPage);

        BDDMockito.when(requestMock.getRequestURI())
                .thenReturn("/category-test-path");

        BDDMockito.when(categoryServiceMock.createIncomingCategory(ArgumentMatchers.any(IncomingCategoryRequest.class)))
                .thenReturn(IncomingCategoryCreator.createValidCategory());

        BDDMockito.when(categoryServiceMock.deleteByCategoryId(ArgumentMatchers.anyInt()))
                .thenReturn(IncomingCategoryCreator.createValidCategory().getCategoryName());
    }

    @Test
    @DisplayName("findAll returns list of Incoming Category inside of Page Object When successfull")
    void findAll_ReturnsListOfIncomingCategoryInsidePageObject_WhenSuccessfull(){
        String expectedName = IncomingCategoryCreator.createValidCategory().getCategoryName();
        Page<IncomingCategoryResponse> categoryPage = categoryController.findAllIncomingCategory(null, requestMock).getBody().getData();
        Assertions.assertThat(categoryPage).isNotNull();
        Assertions.assertThat(categoryPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(categoryPage.toList().get(0).getCategoryName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("createIncomingCategory returns Incoming Category when successfull")
    void createIncomingCategory_ReturnsIncomingCategoryResponse_WhenSuccessfull(){
        IncomingCategoryResponse category = categoryController.createIncomingCategory(IncomingCategoryRequestCreator.createIncomingCategoryRequest(),requestMock)
                .getBody().getData();

        Assertions.assertThat(category).isNotNull().isEqualTo(IncomingCategoryCreator.createValidCategory());
    }

    @Test
    @DisplayName("deleteByCategoryId removes category whem successfull")
    void deleteByCategoryId_RemovesCategory_WhenSuccessfull(){
        ResponseEntity<ApiResponse<String>> deletedCategory = categoryController.deleteByCategoryId(1, requestMock);

        Assertions.assertThat(deletedCategory.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deletedCategory.getBody().getData()).isEqualTo(IncomingCategoryCreator.createValidCategory().getCategoryName());
        Assertions.assertThat(deletedCategory.getBody().getMessage()).isEqualTo("Category deleted successfully");
    }
}