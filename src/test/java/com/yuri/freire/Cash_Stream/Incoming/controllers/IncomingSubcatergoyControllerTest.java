package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryRequestCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryRequestCreator;
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
@DisplayName("Tests for Incoming Subcategory Controller")
class IncomingSubcatergoyControllerTest {
//
//    @InjectMocks
//    private IncomingSubcatergoyController subcatergoyController;
//    @Mock
//    private IncomingSubcategoryService subcategoryServiceMock;
//    @Mock
//    IncomingCategoryService categoryServiceMock;
//    @Mock
//    private HttpServletRequest requestMock;
//
//    @BeforeEach
//    void setUp(){
//
//        IncomingSubcategoryResponse subcategory = IncomingSubcategoryCreator.createValidSubcategoryResponse();
//        categoryServiceMock.createIncomingCategory(IncomingCategoryRequestCreator.createIncomingCategoryRequest());
//
//        PageImpl<IncomingSubcategoryResponse> subcategoryPage = new PageImpl<>(List.of(subcategory));
//
//        BDDMockito.when(subcategoryServiceMock.findAllSubcategory(ArgumentMatchers.any()))
//                .thenReturn(subcategoryPage);
//
//        BDDMockito.when(subcategoryServiceMock.findAllByCategoryName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
//                        .thenReturn(subcategoryPage);
//
//            BDDMockito.when(requestMock.getRequestURI())
//                    .thenReturn("/subcategory-test-path");
//
//        BDDMockito.when(subcategoryServiceMock.createIncomingSubcategory(ArgumentMatchers.any()))
//                .thenReturn(subcategory);
//
//        BDDMockito.when(subcategoryServiceMock.deleteBySubcategoryId(ArgumentMatchers.anyInt()))
//                .thenReturn(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName());
//    }
//
//    @Test
//    @DisplayName("findAll returns list of Incoming Subcategory inside of Page Object When successfull")
//    void findAll_ReturnsListOfIncomingSubcategoryInsidePageObject_WhenSuccessfull(){
//        Page<IncomingSubcategoryResponse> subcategoryPage = subcatergoyController.findAllIncomingSubcategory(null, requestMock).getBody().getData();
//
//        Assertions.assertThat(subcategoryPage).isNotNull();
//        Assertions.assertThat(subcategoryPage.toList())
//                .isNotEmpty()
//                .hasSize(1);
//    }
//
//    @Test
//    @DisplayName("findAllByCategoryName returns a list os Incoming Subcategory by category name inside of a page object when successfull")
//    void findAllByCategoryName_ReturnsAListOfSubcategoryByCategoryNameInsideOfPageObject_WhenSucessfull(){
//        String expectedCategoryName = IncomingCategoryCreator.createValidCategory().getCategoryName();
//        Page<IncomingSubcategoryResponse> subcategoryPage = subcatergoyController.findAllByCategoryName(expectedCategoryName, null, requestMock).getBody().getData();
//        Assertions.assertThat(subcategoryPage.toList())
//                .isNotEmpty()
//                .hasSize(1);
//        Assertions.assertThat(subcategoryPage.toList().get(0).getCategoryName()).isEqualTo(expectedCategoryName);
//    }
//
//
//    @Test
//    @DisplayName("createIncomingSubcategory returns Incoming Subcategory when successfull")
//    void createIncomingSubcategory_ReturnsIncomingSubcategory_WhenSuccessfull(){
//        IncomingSubcategoryResponse savedSubcategory = subcatergoyController.createIncomingSubcategory(IncomingSubcategoryRequestCreator.createIncomingSubcategoryRequest(), requestMock)
//                .getBody().getData();
//        Assertions.assertThat(savedSubcategory).isNotNull();
//        Assertions.assertThat(savedSubcategory.getIncomingSubcategoryId()).isNotNull();
//        Assertions.assertThat(savedSubcategory.getSubCategoryName())
//                .isNotNull()
//                .isEqualTo(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName());
//        Assertions.assertThat(savedSubcategory.getCategoryName())
//                .isNotNull()
//                .isEqualTo(IncomingSubcategoryRequestCreator.createIncomingSubcategoryRequest().getIncomingCategoryName());
//    }
//
//    @Test
//    @DisplayName("deleteByCategoryId removes category whem successfull")
//    void deleteByCategoryId_RemovesCategory_WhenSuccessfull(){
//        ResponseEntity<ApiResponse<String>> deletedSubcategory = subcatergoyController.deleteByIncomingSubcategoryId(1, requestMock);
//
//        Assertions.assertThat(deletedSubcategory.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(deletedSubcategory.getBody().getData()).isEqualTo(IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName());
//        Assertions.assertThat(deletedSubcategory.getBody().getMessage()).isEqualTo("Subcategory deleted successfully");
//    }
//
}