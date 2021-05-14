package com.kristujayanticollege.researchpaperverificationsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.kristujayanticollege.researchpaperverificationsystem.model.Upload;
import com.kristujayanticollege.researchpaperverificationsystem.repository.UploadRepository;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UploadRepositoryService {
    @Autowired
    private UploadRepository uploadRepository;

    public Upload save(JSONObject uploadDetails) throws ParseException {
        Upload upload = new Upload();

        upload.setFileName((String) uploadDetails.get("filename"));

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse((String) uploadDetails.get("date"));

        upload.setDate(date);

        upload.setDescription((String) uploadDetails.get("description"));

        return uploadRepository.save(upload);
    }

    public List<Upload> getAllUploads(){
        return uploadRepository.findAll();
    }

    public void deleteUploadById(Long id) {
        uploadRepository.deleteById(id);
    }
}
