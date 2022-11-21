package com.d108.sduty.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d108.sduty.dto.Image;

public interface ImageService {
	public Image insertImage(Image image);
	public Optional<Image> selectImage(int id);
	public void fileUpload(MultipartFile multipartFile);
	public void deleteFile(String imageFileName);
}
