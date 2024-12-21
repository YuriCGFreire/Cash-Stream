package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingSubcategoryRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.IncomingSubcategoryCreator;
import com.yuri.freire.Cash_Stream.util.IncomingSubcategoryRequestCreator;
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
@DisplayName("Tests for Incoming Subcategory Service")
class IncomingSubcategoryServiceTest {
    @InjectMocks
    private IncomingSubcategoryService subcategoryService;
    @Mock
    private IncomingSubcategoryRepository subcategoryRepositoryMock;
    @Mock
    private IncomingCategoryService categoryServiceMock;
    @Mock
    private IncomingFactory incomingFactory;

    @BeforeEach
    void setUp() {
        IncomingSubcategory subcategory = IncomingSubcategoryCreator.createValidSubcategoryRepository();
        IncomingSubcategoryResponse subcategory1 = IncomingSubcategoryCreator.createValidSubcategoryResponse();
        IncomingSubcategoryResponse subcategory2 = IncomingSubcategoryCreator.createValidSubcategoryResponse();

        PageImpl<IncomingSubcategoryResponse>  subcategoryPage = new PageImpl<>(List.of(
                subcategory1,
                subcategory2));

        BDDMockito.when(
                subcategoryRepositoryMock.save(ArgumentMatchers.any(IncomingSubcategory.class))
        ).thenReturn(subcategory);

        BDDMockito.when(
                subcategoryRepositoryMock.findBySubCategoryName(ArgumentMatchers.anyString())
        ).thenReturn(Optional.of(subcategory));

        BDDMockito.when(
                subcategoryRepositoryMock.findBySubCategoryName("Some random subcategoryname")
        ).thenReturn(Optional.empty());

        BDDMockito.when(subcategoryRepositoryMock.findAllSubcategory(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(subcategoryPage);

        BDDMockito.when(subcategoryRepositoryMock.findAllByCategory(IncomingCategoryCreator.createValidCategory().getCategoryName(), PageRequest.of(0, 2)))
                .thenReturn(subcategoryPage);

        BDDMockito.when(categoryServiceMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(IncomingCategoryCreator.createValidCategoryForRepository());

        BDDMockito.when(categoryServiceMock.findByCategoryName("Some random categoryname"))
                .thenThrow(new EntityNotFoundException("Category not found: Some random categoryname"));

        BDDMockito.when(subcategoryRepositoryMock.findById(1))
                        .thenReturn(Optional.of(subcategory));

        BDDMockito.when(subcategoryRepositoryMock.findById(999))
                        .thenReturn(Optional.empty());

        BDDMockito.doNothing().when(subcategoryRepositoryMock).deleteById(ArgumentMatchers.anyInt());

        BDDMockito.when(incomingFactory.createIncomingSubcategory(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(subcategory);

        BDDMockito.when(incomingFactory.createIncomingSubcategoryResponse(ArgumentMatchers.any()))
                .thenReturn(subcategory1);
    }

    @Test
    @DisplayName("createIncomingSubcategory returns IncomingSubcategoryResponse when successfull")
    void createIncomingSubcategory_ReturnsIncomingSubcategoryResponse_WhenSuccessfull(){
        IncomingSubcategoryResponse validSubcategoryResponse = IncomingSubcategoryCreator.createValidSubcategoryResponse();
        IncomingSubcategoryResponse incomingSubcategory = subcategoryService.createIncomingSubcategory(IncomingSubcategoryRequestCreator.createIncomingSubcategoryRequest());

        Assertions.assertThat(incomingSubcategory)
                .isNotNull()
                .isEqualTo(validSubcategoryResponse)
                .isInstanceOf(IncomingSubcategoryResponse.class);

        Assertions.assertThat(incomingSubcategory.getCategoryName())
                .isNotNull()
                .isEqualTo(validSubcategoryResponse.getCategoryName());
    }

    @Test
    @DisplayName("findBySubcategoryName returns IncomingSubcategory by name when successfull")
    void findBySubcategoryName_ReturnsIncomingSubcategoryByName_WhenSuccessfull(){
        IncomingSubcategory expcetedSubcategory = IncomingSubcategoryCreator.createValidSubcategoryRepository();
        IncomingSubcategory fetchedSubcategory = subcategoryService.findBySubcategoryName(IncomingSubcategoryCreator.createValidSubcategoryRepository().getSubCategoryName());
        Assertions.assertThat(fetchedSubcategory).isNotNull().isInstanceOf(IncomingSubcategory.class);
        Assertions.assertThat(fetchedSubcategory.getIncomingSubcategoryId()).isNotNull();
        Assertions.assertThat(fetchedSubcategory.getSubCategoryName()).isNotNull().isEqualTo(expcetedSubcategory.getSubCategoryName());
        Assertions.assertThat(fetchedSubcategory.getIncomingCategory().getCategoryName()).isNotNull().isEqualTo(expcetedSubcategory.getIncomingCategory().getCategoryName());
    }

    @Test
    @DisplayName("findBySubcategoryName returns entityNotFoundException when subcategory was not found")
    void findBySubcategoryName_ReturnsEntityNotFoundException_WhenSubcategoryWastNotFound(){
        String fakeSubcategoryName = "Some random subcategoryname";
        Assertions.assertThatThrownBy(() -> subcategoryService.findBySubcategoryName(fakeSubcategoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Subcategory not found: " + fakeSubcategoryName);
    }

    @Test
    @DisplayName("findAllSubcategory returns list of Incoming Subcategory inside of Page Object When successfull")
    void findAllSubcategory_ReturnsListOfIncomingSubcategoryInsideOfPageObject_WhenSuccessfull(){
        String expectedSubcategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName();
        Integer expctedSubcategoryId = IncomingSubcategoryCreator.createValidSubcategoryResponse().getIncomingSubcategoryId();
        String expectedCategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getCategoryName();
        Page<IncomingSubcategoryResponse> subcategoryPage = subcategoryService.findAllSubcategory(PageRequest.of(0, 2));

        Assertions.assertThat(subcategoryPage).isNotNull();
        Assertions.assertThat(subcategoryPage.toList())
                .isNotEmpty()
                .hasSize(2);
        Assertions.assertThat(subcategoryPage.toList().get(0).getIncomingSubcategoryId())
                .isNotNull()
                .isEqualTo(expctedSubcategoryId);
        Assertions.assertThat(subcategoryPage.toList().get(0).getSubCategoryName())
                .isNotNull()
                .isEqualTo(expectedSubcategoryName);
        Assertions.assertThat(subcategoryPage.toList().get(0).getCategoryName())
                .isNotNull()
                .isEqualTo(expectedCategoryName);
    }

    @Test
    @DisplayName("findAllByCategoryName returns list of Incoming Subcategory inside of Page Object by category name When successfull")
    void findAllByCategoryName_ReturnsListOfIncomingSubcategoryInsidePageObjectByCategoryName_WhenSuccessfull(){
        String expectedCategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getCategoryName();
        Page<IncomingSubcategoryResponse> subcategoryPage = subcategoryService.findAllByCategoryName(expectedCategoryName, PageRequest.of(0, 2));
        Assertions.assertThat(subcategoryPage).isNotNull();
        Assertions.assertThat(subcategoryPage.toList())
                .isNotEmpty()
                .hasSize(2);
        Assertions.assertThat(subcategoryPage.toList())
                .allMatch(subcategory -> subcategory.getCategoryName().equals(expectedCategoryName));
    }

    @Test
    @DisplayName("findAllByCategoryName throw EntityNotFoundException when Category does not exist")
    void findAllByCategoryName_ThrowEntityNotFoundException_WhenCategoryDoesNotExist(){
        String categoryName = "Some random categoryname";
        Assertions.assertThatThrownBy(() -> subcategoryService.findAllByCategoryName(categoryName, PageRequest.of(0, 2)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryName);
    }

    @Test
    @DisplayName("deleteBySubcategoryId removes subcategory when successfull")
    void deleteBySubcategoryId_RemovesSubcategory_WhenSuccessfull(){
        String expcetedSubcategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName();
        String subcategoryName = subcategoryService.deleteBySubcategoryId(1);
        Assertions.assertThat(subcategoryName).isNotNull().isEqualTo(expcetedSubcategoryName);
    }

    @Test
    @DisplayName("deleteBySubcategoryId throws EntityNotFoundException when subcategory does not exist")
    void expcetedSubcategoryName_ThrowsEntityNotFoundException_WhenSubcategoryDoesNotExist(){
        Integer subcategoryId = 999;
        Assertions.assertThatThrownBy(() -> subcategoryService.deleteBySubcategoryId(subcategoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Subcategory not found: " + subcategoryId);
    }
}