package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingSubcategoryResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.incoming.IncomingSubcategoryCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Incoming subcategory repository")
class IncomingSubcategoryRepositoryTest {

    @Autowired
    private IncomingSubcategoryRepository subcategoryRepository;
    @Autowired
    private IncomingCategoryRepository categoryRepository;

    private IncomingCategory savedIncomingCategory;

    @BeforeEach
    void setUp(){
        IncomingCategory category = IncomingCategoryCreator.createCategoryToBeSaved();
        savedIncomingCategory = categoryRepository.save(category);
    }

    @Test
    @DisplayName("Save persists Incoming Subcategory when successful")
    void save_PersistsIncomingSubcategory_WhenSuccessful(){
        IncomingSubcategory subcategory = IncomingSubcategoryCreator.createSubcategoryToBeSaved();
        IncomingCategory expectedCategory = subcategory.getIncomingCategory();
        IncomingSubcategory savedSubcategory = subcategoryRepository.save(subcategory);
        IncomingCategory category = savedSubcategory.getIncomingCategory();

        Assertions.assertThat(savedSubcategory)
                .isNotNull().isInstanceOf(IncomingSubcategory.class);
        Assertions.assertThat(savedSubcategory.getSubCategoryName())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(subcategory.getSubCategoryName());
        Assertions.assertThat(savedSubcategory.getIncomingSubcategoryId())
                .isNotNull()
                .isInstanceOf(Integer.class);
        Assertions.assertThat(category).isNotNull().isEqualTo(expectedCategory);
    }

    @Test
    @DisplayName("Save updates Incoming Subcategory when successful")
    void save_UpdatesIncomingSubcategory_WhenSuccessful(){ //Passou
        IncomingSubcategory subcategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();

        IncomingSubcategory subcategorySaved = this.subcategoryRepository.save(subcategoryToBeSaved);

        subcategorySaved.setSubCategoryName("Nvidia");

        IncomingSubcategory subCategoryUpdated = this.subcategoryRepository.save(subcategorySaved);

        Assertions.assertThat(subCategoryUpdated).isNotNull();

        Assertions.assertThat(subCategoryUpdated.getIncomingSubcategoryId()).isNotNull();

        Assertions.assertThat(subCategoryUpdated.getIncomingSubcategoryId()).isEqualTo(subcategorySaved.getIncomingSubcategoryId());
    }

    @Test
    @DisplayName("Delete removes Incoming Subcategory when successfull")
    void delete_RemovesIncomingSubcategory_WhenSuccessfull(){ //Passou
        IncomingSubcategory subcategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();

        IncomingSubcategory subcategorySaved = this.subcategoryRepository.save(subcategoryToBeSaved);

        this.subcategoryRepository.deleteById(subcategorySaved.getIncomingSubcategoryId());

        Optional<IncomingSubcategory> incomingSubcategory = this.subcategoryRepository.findById(subcategorySaved.getIncomingSubcategoryId());

        Assertions.assertThat(incomingSubcategory).isEmpty();
    }

    @Test
    @DisplayName("findAllSubcategory returns a list of IncomingSubcategory inside of Page Object when successfull")
    void findAllSubcategory_ReturnsListOfIncomingSubcategoryInsideOfPageObject_WhenSuccessfull(){
        IncomingSubcategory subCategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();
        subCategoryToBeSaved.setIncomingCategory(savedIncomingCategory);
        this.subcategoryRepository.save(subCategoryToBeSaved);

        Pageable pageable = PageRequest.of(0, 1);
        Page<IncomingSubcategoryResponse> allSubcategories = this.subcategoryRepository.findAllSubcategory(pageable);

        Assertions.assertThat(allSubcategories).isNotNull();
        Assertions.assertThat(allSubcategories.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findAllByCategory returns a list of IncomingSubcategory by Category name when Successfull")
    void findAllByCategory_ReturnsListOfIncomingSubcategoryByCategoryName_WhenSuccessfull(){
        IncomingSubcategory subCategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();
        subCategoryToBeSaved.setIncomingCategory(savedIncomingCategory);
        this.subcategoryRepository.save(subCategoryToBeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<IncomingSubcategoryResponse> allSubcategories = this.subcategoryRepository.findAllByCategory(savedIncomingCategory.getCategoryName(), pageable);

        Assertions.assertThat(allSubcategories).isNotNull();
        Assertions.assertThat(allSubcategories.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findBySubCategoryName returns SubcaTegory when SuccessFull")
    void findBySubCategoryName_ReturnsIncomingSubcategory_WhenSuccessfull(){
        IncomingSubcategory subCategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();
        subCategoryToBeSaved.setIncomingCategory(savedIncomingCategory);
        IncomingSubcategory subcategorySaved = this.subcategoryRepository.save(subCategoryToBeSaved);

        Optional<IncomingSubcategory> fetchedSubcategory = this.subcategoryRepository.findBySubCategoryName(subcategorySaved.getSubCategoryName());

        Assertions.assertThat(fetchedSubcategory)
                .isNotEmpty()
                .contains(subcategorySaved);
    }

    @Test
    @DisplayName("findBySubCategoryName returns empty when no IncomingSubcategory is not found")
    void findBySubcategoryName_ReturnsEmpty_WhenIncomingSubcategoryIsNotFound(){

        Optional<IncomingSubcategory> fetchedSubcategory = this.subcategoryRepository.findBySubCategoryName("Subcategoria qualquer");
        Assertions.assertThat(fetchedSubcategory).isEmpty();
    }

    @Test
    @DisplayName("Save throws exception when field name is empty")
    void save_ThrowsException_WhenFieldNameIsEmpty(){
        IncomingSubcategory subcategoryToBeSaved = new IncomingSubcategory();
        Assertions.assertThatThrownBy(() -> this.subcategoryRepository.saveAndFlush(subcategoryToBeSaved))
                .isInstanceOf(ConstraintViolationException.class);
    }


}