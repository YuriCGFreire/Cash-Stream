package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingCategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingCategoryRepository;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryRequestCreator;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Incoming Category Service")
class IncomingCategoryServiceTest {

    @InjectMocks
    private IncomingCategoryService categoryService;
    @Mock
    private IncomingCategoryRepository categoryRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<IncomingCategory> categoryPage = new PageImpl<>(List.of(IncomingCategoryCreator.createValidCategoryForRepository()));
        BDDMockito.when(categoryRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(categoryPage);

        BDDMockito.when(categoryRepositoryMock.save(ArgumentMatchers.any(IncomingCategory.class)))
                .thenReturn(IncomingCategoryCreator.createValidCategoryForRepository());

        BDDMockito.when(categoryRepositoryMock.findById(1))
                .thenReturn(Optional.of(IncomingCategoryCreator.createValidCategoryForRepository()));

        BDDMockito.when(categoryRepositoryMock.findById(999))
                        .thenReturn(Optional.empty());

        BDDMockito.doNothing().when(categoryRepositoryMock).deleteById(ArgumentMatchers.anyInt());

        BDDMockito.when(categoryRepositoryMock.findByCategoryName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(IncomingCategoryCreator.createValidCategoryForRepository()));

        BDDMockito.when(categoryRepositoryMock.findByCategoryName("Nome qualquer"))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("createIncomingCategory return IncomingCategoryResponse when successfull")
    void createIncomingCategory_ReturnIncomingCategoryResponse_WhenSuccessfull(){
        IncomingCategoryResponse category = categoryService.createIncomingCategory(IncomingCategoryRequestCreator.createIncomingCategoryRequest());

        Assertions.assertThat(category).isNotNull().isEqualTo(IncomingCategoryCreator.createValidCategory());
        Assertions.assertThat(category.getIncomingCategoryName()).isNotNull().isEqualTo(IncomingCategoryCreator.createValidCategory().getIncomingCategoryName());
        Assertions.assertThat(category.getIncomingCategoryId()).isNotNull().isInstanceOf(Integer.class);
    }

    @Test
    @DisplayName("findAll returns list of Incoming Category inside of Page Object When successfull")
    void findAll_ReturnsListOfIncomingCategoryInsideOfPageObject_WhenSuccessfull(){
        String expectedName = IncomingCategoryCreator.createValidCategoryForRepository().getCategoryName();
        Integer expectedId = IncomingCategoryCreator.createValidCategoryForRepository().getIncomingCategoryId();
        Page<IncomingCategoryResponse> categoryPage = categoryService.findAll(PageRequest.of(1, 10));

        Assertions.assertThat(categoryPage).isNotNull();
        Assertions.assertThat(categoryPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(categoryPage.toList().get(0).getIncomingCategoryName()).isEqualTo(expectedName);
        Assertions.assertThat(categoryPage.toList().get(0).getIncomingCategoryId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("deleteByCategoryId removes category whem successfull")
    void deleteByCategoryId_RemovesCategory_WhenSuccessfull(){
        String deletedCategory = categoryService.deleteByCategoryId(1);

        Assertions.assertThat(deletedCategory)
                .isNotNull()
                .isEqualTo("Stocks");

    }

    @Test
    @DisplayName("deleteByCategoryId throws EntityNotFoundException when category does not exist")
    void deleteByCategoryId_ThrowsEntityNotFoundException_WhenCategoryDoesNotExiste(){
        Integer categoryId = 999;
        Assertions.assertThatThrownBy(() -> categoryService.deleteByCategoryId(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryId);
    }

    @Test
    @DisplayName("findCategoryByName returns Incoming Category when successfull")
    void findCategoryByName_ReturnsIncomingCategory_WhenSuccessfull(){
        IncomingCategory expctedCategory = IncomingCategoryCreator.createValidCategoryForRepository();
        IncomingCategory category = categoryService.findByCategoryName("Stocks");

        Assertions.assertThat(category)
                .isNotNull()
                .isInstanceOf(IncomingCategory.class);
        Assertions.assertThat(category.getIncomingCategoryId()).isNotNull().isEqualTo(expctedCategory.getIncomingCategoryId());
        Assertions.assertThat(category.getCategoryName()).isNotNull().isEqualTo(expctedCategory.getCategoryName());
    }

    @Test
    @DisplayName("findByCategoryName EntityNotFoundException when category does not exist")
    void findByCategoryName_ThrowsEntityNotFoundException_WhenCategoryDoesNotExiste(){
        String categoryName = "Nome qualquer";

        Assertions.assertThatThrownBy(() -> categoryService.findByCategoryName(categoryName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category not found: " + categoryName);
    }
}