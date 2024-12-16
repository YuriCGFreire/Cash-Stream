package com.yuri.freire.Cash_Stream.Incoming.entities.repositories;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.entities.repositories.RecurrenceRepository;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.IncomingCreator;
import com.yuri.freire.Cash_Stream.util.IncomingSubcategoryCreator;
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
@DisplayName("Tests for Incomings repository")
class IncomingRepositoryTest {
    @Autowired
    private IncomingRepository incomingRepository;
    @Autowired
    private IncomingCategoryRepository categoryRepository;
    @Autowired
    private IncomingSubcategoryRepository subcategoryRepository;
    @Autowired
    private RecurrenceRepository recurrenceRepository;
    private IncomingCategory savedCategory;
    private IncomingSubcategory savedSubcategory;
    private Recurrence savedRecurrence;

    @BeforeEach
    void setUp(){
        Recurrence recurrence = Recurrence.builder()
                .recurrenceFrequency(RecurrenceType.ANNUAL)
                .build();
        this.savedCategory = this.categoryRepository.save(IncomingCategoryCreator.createCategoryToBeSaved());
        IncomingSubcategory subcategoryToBeSaved = IncomingSubcategoryCreator.createSubcategoryToBeSaved();
        subcategoryToBeSaved.setIncomingCategory(savedCategory);
        this.savedSubcategory = this.subcategoryRepository.save(subcategoryToBeSaved);
        this.savedRecurrence = this.recurrenceRepository.save(recurrence);
    }

    @Test
    @DisplayName("Save persists Incoming when successful")
    void Save_PersistsIncoming_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        Incoming savedIncoming = this.incomingRepository.save(incomingToBeSaved);

        Assertions.assertThat(savedIncoming).isNotNull().isInstanceOf(Incoming.class);

        Assertions.assertThat(savedIncoming)
                .usingRecursiveComparison()
                .ignoringFields("incomingId")
                .isEqualTo(incomingToBeSaved);

        Assertions.assertThat(savedIncoming.getIncomingId()).isNotNull();
    }

    @Test
    @DisplayName("Save updates Incoming when successful")
    void save_UpdatesIncoming_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        Incoming savedIncoming = this.incomingRepository.save(incomingToBeSaved);
        savedIncoming.setIncomingDescription("Dividendos Amazon atualizado");
        Incoming updatedIncoming = this.incomingRepository.save(savedIncoming);

        Assertions.assertThat(updatedIncoming).isNotNull();
        Assertions.assertThat(updatedIncoming.getIncomingId()).isNotNull().isEqualTo(savedIncoming.getIncomingId());
    }

    @Test
    @DisplayName("delete removes Incoming when successful")
    void delete_RemovesInocming_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        Incoming savedIncoming = this.incomingRepository.save(incomingToBeSaved);
        this.incomingRepository.deleteById(savedIncoming.getIncomingId());
        Optional<Incoming> fetchedIncoming = this.incomingRepository.findById(savedIncoming.getIncomingId());

        Assertions.assertThat(fetchedIncoming).isEmpty();
    }

    @Test
    @DisplayName("findAllIncomings returns list of Incomings inside a Page Object when successful")
    void findAllIncomings_ReturnsListOfIncomingsInsideOfPageObject_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        incomingToBeSaved.setIncomingCategory(savedCategory);
        incomingToBeSaved.setIncomingSubcategory(savedSubcategory);
        incomingToBeSaved.setRecurrence(savedRecurrence);
        this.incomingRepository.save(incomingToBeSaved);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Incoming> incomingPage = this.incomingRepository.findAllIncomings(pageable);


        Assertions.assertThat(incomingPage).isNotNull();
        Assertions.assertThat(incomingPage.getContent())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("findAllBYCategory returns a list of Incoming inside of Page Object selected by Category when successful")
    void findAllByCategory_ReturnsListOfIncomingInsidePageObjectSelectedByCategory_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        incomingToBeSaved.setIncomingCategory(savedCategory);
        incomingToBeSaved.setIncomingSubcategory(savedSubcategory);
        incomingToBeSaved.setRecurrence(savedRecurrence);

        this.incomingRepository.save(incomingToBeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Incoming> incomingPage = this.incomingRepository.findAllByCategory(savedCategory.getCategoryName(), pageable);

        Assertions.assertThat(incomingPage).isNotNull();
        Assertions.assertThat(incomingPage.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(incomingPage.getContent().get(0).getIncomingCategory().getCategoryName())
                .isNotNull()
                .isEqualTo(savedCategory.getCategoryName());
        Assertions.assertThat(incomingPage.getContent().get(0).getIncomingCategory().getIncomingCategoryId())
                .isNotNull()
                .isEqualTo(savedCategory.getIncomingCategoryId());
    }

    @Test
    @DisplayName("findAllBySubcategory returns a list of Incoming inside of Page Object selected by Subcategory when successful")
    void findAllBySubcategory_ReturnsListOfIncomingInsidePageObjectSelectedBySubcategory_WhenSuccessful(){
        Incoming incomingToBeSaved = IncomingCreator.createValidIncomingToBeSaved();
        incomingToBeSaved.setIncomingCategory(savedCategory);
        incomingToBeSaved.setIncomingSubcategory(savedSubcategory);
        incomingToBeSaved.setRecurrence(savedRecurrence);
        this.incomingRepository.save(incomingToBeSaved);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Incoming> incomingPage = this.incomingRepository.findAllBySubcategory(savedSubcategory.getSubCategoryName(), pageable);

        Assertions.assertThat(incomingPage).isNotNull();
        Assertions.assertThat(incomingPage.getContent())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(incomingPage.getContent().get(0).getIncomingSubcategory().getSubCategoryName())
                .isNotNull()
                .isEqualTo(savedSubcategory.getSubCategoryName());
        Assertions.assertThat(incomingPage.getContent().get(0).getIncomingSubcategory().getIncomingSubcategoryId())
                .isNotNull()
                .isEqualTo(savedSubcategory.getIncomingSubcategoryId());
    }
}