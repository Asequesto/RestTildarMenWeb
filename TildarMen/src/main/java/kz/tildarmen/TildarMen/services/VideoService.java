package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.VideoDto;
import kz.tildarmen.TildarMen.mapper.VideoMapper;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.Video;
import kz.tildarmen.TildarMen.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
@Service
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;

    private final String folderPath = "C:\\Users\\asarr\\Downloads\\TildarMenVideos\\";
    private final TranslatorService translatorService;
    private final VideoMapper videoMapper;

    public VideoDto uploadVideo(Long translatorId, MultipartFile file) throws IOException {
        Translator translator = translatorService.getTranslatorById(translatorId);
        String filePath = folderPath + file.getOriginalFilename();
        Video video = new Video();
        video.setTranslator(translator);
        video.setFileName(file.getOriginalFilename());
        video.setFileType(file.getContentType());
        video.setFilePath(filePath);

        file.transferTo(new File(filePath));

        return videoMapper.toDto(videoRepository.save(video));
    }

    public byte[] downloadVideo(Long id) throws IOException {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        String filePath = video.getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    public VideoDto getVideo(Long id) throws IOException {
        return videoMapper.toDto(videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found")));
    }

    public String getMediaType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (fileExtension) {
            case "mp4" -> "video/mp4";
            case "avi" -> "video/x-msvideo";
            case "mov" -> "video/quicktime";
            case "wmv" -> "video/x-ms-wmv";
            case "mkv" -> "video/x-matroska"; // Matroska format
            default -> "application/octet-stream"; // Fallback
        };
    }

}
