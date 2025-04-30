package kz.tildarmen.TildarMen.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {

    private final TranslatorService translatorService;
    private final Storage storage;
    private final TranslatorRepository translatorRepository;
    private final EmployerService employerService;

    public String uploadFile(Long id, MultipartFile file, String type) throws IOException {
        String fileName = LocalDateTime.now() + file.getOriginalFilename();
        BlobId blobId = BlobId.of("tildarmen_bucket", Objects.requireNonNull(fileName));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        String url = String.format("https://storage.googleapis.com/%s/%s", "tildarmen_bucket", file.getOriginalFilename());
        if(type != null) uploadUrl(id, url, type);

        return url;

    }

    public String uploadFileEmployer(Long id, MultipartFile file) throws IOException {
        String fileName = LocalDateTime.now() + file.getOriginalFilename();
        BlobId blobId = BlobId.of("tildarmen_bucket", Objects.requireNonNull(fileName));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        String url = String.format("https://storage.googleapis.com/%s/%s", "tildarmen_bucket", file.getOriginalFilename());
        Employer employer = employerService.getEmployerById(id);
        employer.setProfileImageUrl(url);

        return url;
    }

    public String uploadReportFile(MultipartFile file) throws IOException {
        String fileName = LocalDateTime.now() + file.getOriginalFilename();
        BlobId blobId = BlobId.of("tildarmen_bucket", Objects.requireNonNull(fileName));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", "tildarmen_bucket", file.getOriginalFilename());
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
        if(type.equals("profile-image")){
            translator.setProfileImageUrl(url);
        }
        translatorRepository.save(translator);
    }

    public void deleteImage(String fileName){
        Blob blob = storage.get(BlobId.of("tildarmen_bucket",
                fileName.substring(fileName.lastIndexOf("/") + 1)));
        if(blob != null && blob.exists()){
            blob.delete();
        }else{
            throw new RuntimeException("File not found " + fileName);
        }
    }
}
