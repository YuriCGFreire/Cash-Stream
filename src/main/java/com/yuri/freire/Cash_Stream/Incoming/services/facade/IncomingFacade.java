package com.yuri.freire.Cash_Stream.Incoming.services.facade;
import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.services.UserService;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.entities.Incoming;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingCategory;
import com.yuri.freire.Cash_Stream.Incoming.entities.IncomingSubcategory;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingCategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingSubcategoryService;
import com.yuri.freire.Cash_Stream.Incoming.services.factory.IncomingFactory;
import com.yuri.freire.Cash_Stream.Recurrence.entities.Recurrence;
import com.yuri.freire.Cash_Stream.Recurrence.entities.entitie_enum.RecurrenceType;
import com.yuri.freire.Cash_Stream.Recurrence.services.RecurrenceService;
import com.yuri.freire.Cash_Stream.Utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingFacade {
    private final IncomingSubcategoryService incomingSubcategoryService;
    private final IncomingCategoryService incomingCategoryService;
    private final RecurrenceService recurrenceService;
    private final IncomingFactory incomingFactory;

    private final UserService userService;

    public Incoming createIncoming(IncomingRequest incomingRequest){
        String currentUsername = SecurityUtils.getCurrentUsername();
        User user = userService.findUserByUsername(currentUsername);
        IncomingCategory incomingCategory = incomingCategoryService.findByCategoryName(incomingRequest.getIncomingCategory(), currentUsername);
        IncomingSubcategory incomingSubcategory = incomingSubcategoryService.findBySubcategoryName(incomingRequest.getIncomingSubcategory());
        Recurrence recurrence = recurrenceService.findByRecurrenFrequency(incomingRequest.getRecurrence());

        return incomingFactory.createIncoming(incomingRequest, incomingCategory, incomingSubcategory, recurrence, user);
    }

    public IncomingResponse createIncomingResponse(Incoming incoming){
        return incomingFactory.createIncomingResponse(incoming);
    }

    public IncomingSubcategory findIncomingSubcategoryByName(String incomingSubcategoryName, String username){
        return incomingSubcategoryService.findBySubcategoryName(incomingSubcategoryName);
    }

    public IncomingCategory findIncomingCategoryByName(String incomingCategoryName, String username){
        return incomingCategoryService.findByCategoryName(incomingCategoryName, username);
    }

    public Recurrence findByRecurrenFrequency(RecurrenceType recurrenceType){
        return recurrenceService.findByRecurrenFrequency(recurrenceType);
    }

    public User findUserByName(String username){
        return userService.findUserByUsername(username);
    }
}
