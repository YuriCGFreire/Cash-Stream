package com.yuri.freire.Cash_Stream.Incoming.services;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.repositories.IncomingRepository;
import com.yuri.freire.Cash_Stream.Incoming.services.facade.IncomingFacade;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import com.yuri.freire.Cash_Stream.util.IncomingCategoryCreator;
import com.yuri.freire.Cash_Stream.util.IncomingCreator;
import com.yuri.freire.Cash_Stream.util.IncomingRequestCreator;
import com.yuri.freire.Cash_Stream.util.IncomingSubcategoryCreator;
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
@DisplayName("Tests for Incoming Service")
class IncomingServiceTest {

    @InjectMocks
    private IncomingService incomingService;
    @Mock
    private IncomingRepository incomingRepositoryMock;
    @Mock
    private IncomingFacade incomingFacadeMock;


    @BeforeEach
    void setUp(){
        Incoming incoming = IncomingCreator.createValidIncoming();
        IncomingResponse incoming1 = IncomingCreator.createValidIncomingResponse();
        IncomingResponse incoming2 = IncomingCreator.createValidIncomingResponse();

        PageImpl<IncomingResponse> incomingPage = new PageImpl<>(List.of(incoming1, incoming2));

        BDDMockito.when(
                this.incomingRepositoryMock.findAllIncomings(ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(incomingPage);

        BDDMockito.when(
                this.incomingRepositoryMock.findAllByCategory(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(incomingPage);

        BDDMockito.when(
                this.incomingRepositoryMock.findAllByCategory(ArgumentMatchers.contains("Some random category name"), ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(Page.empty());

        BDDMockito.when(
                this.incomingRepositoryMock.findAllBySubcategory(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(incomingPage);

        BDDMockito.when(
                this.incomingRepositoryMock.findAllBySubcategory(ArgumentMatchers.contains("Some random subcategory name"), ArgumentMatchers.any(PageRequest.class))
        ).thenReturn(Page.empty());

        BDDMockito.when(
                this.incomingRepositoryMock.save(ArgumentMatchers.any(Incoming.class))
        ).thenReturn(incoming);

        BDDMockito.when(
                this.incomingRepositoryMock.findById(ArgumentMatchers.anyInt())
        ).thenReturn(Optional.of(incoming));

        BDDMockito.doNothing().when(this.incomingRepositoryMock).deleteById(ArgumentMatchers.anyInt());

        BDDMockito.when(
                this.incomingRepositoryMock.findById(999)
        ).thenThrow(new EntityNotFoundException("Incoming not found: 999"));

        BDDMockito.when(incomingFacadeMock.createIncoming(ArgumentMatchers.any()))
                .thenReturn(incoming);

        BDDMockito.when(incomingFacadeMock.createIncomingResponse(ArgumentMatchers.any()))
                .thenReturn(incoming1);
    }

    @Test
    @DisplayName("createIncoming return IncomingResponse when successful")
    void createIncoming_ReturnIncomingResponse_WhenSuccessful(){
        IncomingResponse expctedIncomingResponse = IncomingCreator.createValidIncomingResponse();
        IncomingResponse savedIncoming = this.incomingService.createIncoming(IncomingRequestCreator.createIncomningRequest());

        Assertions.assertThat(savedIncoming).isNotNull().isInstanceOf(IncomingResponse.class);

        Assertions.assertThat(savedIncoming)
                .usingRecursiveComparison()
                .ignoringFields("incomingId")
                .isEqualTo(expctedIncomingResponse);

        Assertions.assertThat(savedIncoming.getIncomingId()).isNotNull();
    }

    @Test
    @DisplayName("findAllIncomings return list of Incomings inside of Page Object when successful")
    void findAllIncomings_ReturnListOfIncomingInsidePageObject_WhenSuccessful(){
        IncomingResponse expectedIncoming = IncomingCreator.createValidIncomingResponse();
        Page<IncomingResponse> incomingPage = this.incomingService.findAllIncomings(PageRequest.of(0, 2));
        Assertions.assertThat(incomingPage).isNotNull();
        Assertions.assertThat(incomingPage.toList())
                .isNotEmpty()
                .hasSize(2);

        incomingPage.forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("incomingId")
                    .isEqualTo(expectedIncoming);

            Assertions.assertThat(incomingResponse.getIncomingId())
                    .isNotNull();
        });
    }

    @Test
    @DisplayName("findAllIncomingsByCategory return list of Incomings inside of Page Object selected by category name when successful")
    void findAllIncomingsByCategory_ReturnListOfIncomingsInsideOfPageObjectSelectedByCategoryName_WhenSuccessful(){
        String expctedCategoryName = IncomingCategoryCreator.createValidCategory().getCategoryName();
        Page<IncomingResponse> incomingByCategoryNameResponse = this.incomingService.findAllIncomingsByCategory(expctedCategoryName, PageRequest.of(0, 2));

        Assertions.assertThat(incomingByCategoryNameResponse).isNotNull();
        Assertions.assertThat(incomingByCategoryNameResponse.toList())
                .isNotEmpty()
                .hasSize(2);

        incomingByCategoryNameResponse.forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getCategoryName())
                    .isNotNull()
                    .isEqualTo(expctedCategoryName);

            Assertions.assertThat(incomingResponse.getIncomingId())
                    .isNotNull();
        });
    }

    @Test
    @DisplayName("findAllIncomingsByCategory return empty list of Incomings when category does not exists")
    void findAllIncomingsByCategory_ReturnEmptyListOfIncomings_WhenCategoryDoesNotExists(){
        Page<IncomingResponse> incomingByCategoryNameResponse = this.incomingService.findAllIncomingsByCategory("Some random category name", PageRequest.of(0, 2));

        Assertions.assertThat(incomingByCategoryNameResponse).isNotNull();
        Assertions.assertThat(incomingByCategoryNameResponse.toList())
                .isEmpty();
    }

    @Test
    @DisplayName("findAllIncomingsBySubcategory return list of Incomings inside of Page Object selected by subcategory name when successful")
    void findAllIncomingsBySubcategory_ReturnListOfIncomingsInsideOfPageObjectSelectedBySuncategoryName_WhenSuccessful(){
        String expctedSubcategoryName = IncomingSubcategoryCreator.createValidSubcategoryResponse().getSubCategoryName();
        Page<IncomingResponse> incomingByCategoryNameResponse = this.incomingService.findAllIncomingsBySubcategory(expctedSubcategoryName, PageRequest.of(0, 2));

        Assertions.assertThat(incomingByCategoryNameResponse).isNotNull();
        Assertions.assertThat(incomingByCategoryNameResponse.toList())
                .isNotEmpty()
                .hasSize(2);

        incomingByCategoryNameResponse.forEach(incomingResponse -> {
            Assertions.assertThat(incomingResponse.getSubCategoryName())
                    .isNotNull()
                    .isEqualTo(expctedSubcategoryName);

            Assertions.assertThat(incomingResponse.getIncomingId())
                    .isNotNull();
        });
    }

    @Test
    @DisplayName("findAllIncomingsBySubcategory return empty list of Incomings when Subcategory does not exists")
    void findAllIncomingsBySubcategory_ReturnsEmptyList_WhenSubcategoryDoesNotExists(){
        Page<IncomingResponse> incomingByCategoryNameResponse = this.incomingService.findAllIncomingsBySubcategory("Some random subcategory name", PageRequest.of(0, 2));

        Assertions.assertThat(incomingByCategoryNameResponse).isNotNull();
        Assertions.assertThat(incomingByCategoryNameResponse.toList())
                .isEmpty();
    }

    @Test
    @DisplayName("deleteByIncomingId removes incoming when successful")
    void deleteByIncomingId_RemovesIncoming_WhenSuccessful(){
        String expectedIncomingDescription = IncomingCreator.createValidIncoming().getIncomingDescription();
        String incomingDescription = this.incomingService.deleteByIncomingId(1);
        Assertions.assertThat(incomingDescription).isNotNull().isEqualTo(expectedIncomingDescription);
    }

    @Test
    @DisplayName("deleteByIncomingId throws entitynoffoundexception when incomingId does not exists")
    void deleteByIncomingId_ThrowsEntityNotFoundException_WhenIncomingIdDoesNotExists(){
        Assertions.assertThatThrownBy(() -> this.incomingService.deleteByIncomingId(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Incoming not found: 999");
    }
}