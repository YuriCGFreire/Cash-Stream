package com.yuri.freire.Cash_Stream.Incoming.controllers;

import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingRequest;
import com.yuri.freire.Cash_Stream.Incoming.controllers.model.IncomingResponse;
import com.yuri.freire.Cash_Stream.Incoming.services.IncomingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/incoming")
@RequiredArgsConstructor
public class IncomingController {
    private final IncomingService incomingService;

    @PostMapping("/create")
    public ResponseEntity<IncomingResponse> createIncomingCategory(@RequestBody IncomingRequest incomingRequest){
        System.out.println("Teste");
        return ResponseEntity.ok(incomingService.createIncoming(incomingRequest));
    }

    @GetMapping("/all-incomings")
    public ResponseEntity<ArrayList<IncomingResponse>> findAllIncomings(){
        return ResponseEntity.ok(incomingService.findAllIncomings());
    }
}
