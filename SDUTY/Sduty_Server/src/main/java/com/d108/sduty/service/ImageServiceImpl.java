package com.d108.sduty.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d108.sduty.dto.Image;
import com.d108.sduty.repo.ImageRepo;

@Service
public class ImageServiceImpl implements ImageService {
	//private final String FILE_URL = "/home/ubuntu/S07P12D108/Sduty_Server/src/main/resources/image/";
	private final String FILE_URL = "/home/files/";
//	private final String FILE_URL = "C:\\SSAFY\\Sduty\\Sduty_Server\\src\\main\\resources\\image\\";
	
	@Autowired
	private ImageRepo imageRepo;

	@Override
	public Image insertImage(Image image) {
		return imageRepo.save(image);
	}

	@Override
	public Optional<Image> selectImage(int id) {
		return imageRepo.findById(id);
	}

	@Override
	public void fileUpload(MultipartFile multipartFile) {
		Path copyOfLocation = Paths.get(FILE_URL + multipartFile.getOriginalFilename());
		try {
			Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not store file : " + multipartFile.getOriginalFilename());
		}
	}

	@Override
	public void deleteFile(String imageFileName) {
		String path = FILE_URL + imageFileName;
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}
