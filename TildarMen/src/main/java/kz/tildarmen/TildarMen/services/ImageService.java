package kz.tildarmen.TildarMen.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.ImageDto;
import kz.tildarmen.TildarMen.mapper.ImageMapper;
import kz.tildarmen.TildarMen.model.Image;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.ImageRepository;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final TranslatorService translatorService;
    private final ImageMapper imageMapper;
    private final Storage storage;
    private final TranslatorRepository translatorRepository;

    public ImageDto uploadFile(Long translatorId, MultipartFile file)
            throws IOException, SQLException {
        Translator translator = translatorService.getTranslatorById(translatorId);

        Image profileImage = new Image();
        profileImage.setTranslator(translator);
        profileImage.setImage(new SerialBlob(file.getBytes()));
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

    public String uploadFile(Long id, MultipartFile file, String type) throws IOException {
        BlobId blobId = BlobId.of("tildarmen_bucket", Objects.requireNonNull(file.getOriginalFilename()));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        String url = String.format("https://storage.googleapis.com/%s/%s", "your-bucket",
                "translator-profile-photos/" + file.getOriginalFilename());
        if(type != null) uploadUrl(id, url, type);

        return url;

    }

    public void uploadUrl(Long id, String url, String type) {
        Translator translator = translatorService.getTranslatorById(id);
        if(type.equals("profile")){
            translator.setProfileImageUrl(url);
        }
        if(type.equals("video")){
            translator.setVideoUrl(url);
        }
        if(type.equals("project")){
            translator.getProjectUrls().add(url);
        }
        translatorRepository.save(translator);
    }
}
