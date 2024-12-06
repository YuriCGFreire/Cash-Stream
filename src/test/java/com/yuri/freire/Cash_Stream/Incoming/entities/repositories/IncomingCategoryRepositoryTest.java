package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;


import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Incoming Category Respository")
class IncomingCategoryRepositoryTest {

    @Autowired
    private IncomingCategoryRepository categoryRepository;

    @Test
    @DisplayName("Save persists Incoming Category when successfull")
    void save_PersistIncomingCategory_WhenSuccessfull(){
        IncomingCategory categoryToBeSaved = IncomingCategoryCreator.createCategoryToBeSaved();
        IncomingCategory categorySaved = this.categoryRepository.save(categoryToBeSaved);

        Assertions.assertThat(categorySaved).isNotNull();
        Assertions.assertThat(categorySaved.getIncomingCategoryId()).isNotNull();
        Assertions.assertThat(categorySaved.getCategoryName()).isEqualTo(categorySaved.getCategoryName());
    }

    @Test
    @DisplayName("Save updates Incoming Category when successfull")
    void save_UpdatesIncomingCategory_WhenSuccessfull(){
        IncomingCategory categoryToBeSaved = IncomingCategoryCreator.createCategoryToBeSaved();

        IncomingCategory categorySaved = this.categoryRepository.save(categoryToBeSaved);

        categorySaved.setCategoryName("Freelance");

        IncomingCategory categoryUpdated = this.categoryRepository.save(categorySaved);

        Assertions.assertThat(categoryUpdated).isNotNull();

        Assertions.assertThat(categoryUpdated.getIncomingCategoryId()).isNotNull();

        Assertions.assertThat(categoryUpdated.getIncomingCategoryId()).isEqualTo(categorySaved.getIncomingCategoryId());
    }

    @Test
    @DisplayName("Delete removes Incoming Category when successfull")
    void delete_RemovesIncomingCategory_WhenSuccessfull(){
        IncomingCategory categoryToBeSaved = IncomingCategoryCreator.createCategoryToBeSaved();

        IncomingCategory categorySaved = this.categoryRepository.save(categoryToBeSaved);

        this.categoryRepository.deleteById(categorySaved.getIncomingCategoryId());

        Optional<IncomingCategory> incomingCategory = this.categoryRepository.findById(categorySaved.getIncomingCategoryId());

        Assertions.assertThat(incomingCategory).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns Incoming Category list when successfull")
    void find_by_name_ReturnIncomingCategory_WhenSuccessfull(){
        IncomingCategory categoryToBeSaved = IncomingCategoryCreator.createCategoryToBeSaved();

        IncomingCategory categorySaved = this.categoryRepository.save(categoryToBeSaved);

        Optional<IncomingCategory> fetchedCategory = this.categoryRepository.findByCategoryName(categorySaved.getCategoryName());

        Assertions.assertThat(fetchedCategory)
                .isNotEmpty()
                .contains(categorySaved);
    }

    @Test
    @DisplayName("Find by name returns empty list of Incoming Category when no IncomingCategory is not found")
    void find_by_name_ReturnEmptyListofIncominfCategory_WhenIncomingCategoryIsNotFound(){

        Optional<IncomingCategory> fetchedCategory = this.categoryRepository.findByCategoryName("Categoria qualquer");

        Assertions.assertThat(fetchedCategory).isEmpty();
    }

    @Test
    @DisplayName("Save throws exception when field name is empty")
    void save_ThrowsException_WhenFieldNameIsEmpty(){
        IncomingCategory categoryToBeSaved = new IncomingCategory();
        Assertions.assertThatThrownBy(() -> this.categoryRepository.saveAndFlush(categoryToBeSaved))
                    .isInstanceOf(ConstraintViolationException.class);
    }

}