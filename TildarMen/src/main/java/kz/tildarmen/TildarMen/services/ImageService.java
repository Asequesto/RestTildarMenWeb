package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.ImageDto;
import kz.tildarmen.TildarMen.enums.ImageUsageType;
import kz.tildarmen.TildarMen.mapper.ImageMapper;
import kz.tildarmen.TildarMen.model.Image;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final TranslatorService translatorService;
    private final ImageMapper imageMapper;

    public ImageDto uploadFile(Long translatorId, MultipartFile file, ImageUsageType usageType)
            throws IOException, SQLException {
        Translator translator = translatorService.getTranslatorById(translatorId);

        if(imageRepository.existsByTranslatorAndUsageType(translator, usageType)){
            Image image = imageRepository.findByTranslator(translator);
            image.setUsageType(usageType);
            image.setImage(new SerialBlob(file.getBytes()));
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            String patternUrl = "/translator/file/download/";
            imageRepository.save(image);

            image.setDownloadUrl(patternUrl + image.getId());
            imageRepository.save(image);
            return imageMapper.toDto(image);
        }


        Image profileImage = new Image();
        profileImage.setTranslator(translator);
        profileImage.setImage(new SerialBlob(file.getBytes()));
        profileImage.setUsageType(usageType);
        profileImage.setFileName(file.getOriginalFilename());
        profileImage.setFileType(file.getContentType());

        String patternUrl = "/translator/file/download/";
        imageRepository.save(profileImage);

        profileImage.setDownloadUrl(patternUrl + profileImage.getId());
        imageRepository.save(profileImage);

        return imageMapper.toDto(profileImage);
    }

    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }
}
