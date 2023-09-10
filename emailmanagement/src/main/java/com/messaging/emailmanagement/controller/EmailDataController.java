package com.messaging.emailmanagement.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.emailmanagement.data.entity.EmailData;
import com.messaging.emailmanagement.data.entity.FileData;
import com.messaging.emailmanagement.service.EmailDataService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailDataController {

    @Autowired
    private EmailDataService emailDataService;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping(consumes = {"multipart/form-data"}, value = "/send")
    @CrossOrigin(origins = "http://localhost:4200")
    public String sendEmail(@RequestPart("emaildata") String emailData, @RequestPart(name = "file", required = false) MultipartFile file) throws JsonProcessingException {
        return emailDataService.saveEmail(mapper.readValue(emailData, EmailData.class), file);
    }

    @GetMapping("/getmails")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<EmailData> getEmails(@RequestHeader(name = "userid") String email) {

        return emailDataService.getAll(email);
    }

    @PostMapping("/download")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ByteArrayResource> download(@RequestBody FileData id) throws IOException {
        return emailDataService.downloadFile(id);
    }
}

