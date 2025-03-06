package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.VideoDto;
import kz.tildarmen.TildarMen.model.Image;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.ImageService;
import kz.tildarmen.TildarMen.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final ImageService imageService;
    private final VideoService videoService;


    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadTranslatorFile(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);

    }

    @GetMapping("/video/download/{videoId}")
    public ResponseEntity<?> downloadTranslatorVideo(@PathVariable Long videoId) throws IOException {
        try {
            byte[] videoData = videoService.downloadVideo(videoId);
            VideoDto video = videoService.getVideo(videoId);
            String mediaType = videoService.getMediaType(video.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(mediaType))
                    .body(videoData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

}
