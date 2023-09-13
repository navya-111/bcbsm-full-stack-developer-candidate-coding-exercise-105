package com.messaging.emailmanagement.service;

import com.messaging.emailmanagement.config.EncryptionUtil;
import com.messaging.emailmanagement.data.entity.EmailData;
import com.messaging.emailmanagement.data.entity.FileData;
import com.messaging.emailmanagement.data.entity.User;
import com.messaging.emailmanagement.repository.EmailDataRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmailDataService {

    private static Logger logger = LoggerFactory.getLogger(EmailDataService.class);
    @Autowired
    private EmailDataRepository emailDataRepository;

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private UserService userService;

    public String saveEmail(EmailData emailData, MultipartFile file) {
        try {
            emailData.setId(UUID.randomUUID().toString());
            if (file != null) {
                DBObject metadata = new BasicDBObject();
                metadata.put("fileSize", file.getSize());
                ObjectId objectId = template.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
                FileData fileData = new FileData();
                fileData.setFilename(file.getOriginalFilename());
                fileData.setObjectId(objectId.toString());
                emailData.setFileData(fileData);
            } else {
                emailData.setFileData(null);
            }

            emailData.setUploadDate(LocalDateTime.now());
            sendEmail(emailData);
            emailDataRepository.save(emailData);
        } catch (Exception ex) {
            logger.error("Unable to send eamil {}", ex);
            return "Unable to send email";
        }

        return "Email Send Successfully";
    }

    public List<EmailData> getAll(String email) {
        return emailDataRepository.findByFromEmail(email);
    }

    public ResponseEntity<ByteArrayResource> downloadFile(FileData fileData) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(fileData.getObjectId())));

        if (gridFSFile == null)
            return ResponseEntity.notFound().build();

        Document metaData = gridFSFile.getMetadata();
        String fileName = gridFSFile.getFilename();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metaData.get("_contentType").toString()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(
                        IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream())
                ));

    }

    private void sendEmail(EmailData emailData) throws Exception {
        User user = userService.findById(emailData.getFromEmail());
        MimeMessage message = javaMailSender.createMimeMessage();
        javaMailSender.setUsername(emailData.getFromEmail());
        javaMailSender.setPassword(EncryptionUtil.decrypt(user.getPassword()));
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(emailData.getFromEmail());
        helper.setTo(emailData.getToEmail());
        helper.setSubject(emailData.getSubject());
        helper.setText(emailData.getMessage());
        if (emailData.getFileData() != null) {
            ResponseEntity<ByteArrayResource> responseEntity = downloadFile(emailData.getFileData());
            if (responseEntity.hasBody())
                helper.addAttachment(emailData.getFileData().getFilename(), responseEntity.getBody());
        }

        javaMailSender.send(message);
    }
}
