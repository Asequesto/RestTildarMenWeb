package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.CertificateDto;
import kz.tildarmen.TildarMen.mapper.CertificateMapper;
import kz.tildarmen.TildarMen.model.Certificate;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.CertificateRepository;
import kz.tildarmen.TildarMen.requests.UploadCertificateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final TranslatorService translatorService;
    private final CertificateMapper certificateMapper;
    private final ImageService imageService;

    public CertificateDto addCertificate(Long translatorId, MultipartFile file,
                                         UploadCertificateRequest request) throws IOException {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Certificate certificate = new Certificate();
        certificate.setTranslator(translator);
        certificate.setTitle(request.getTitle());
        certificate.setYear(request.getYear());
        String url = imageService.uploadFile(translatorId, file, null);
        certificate.setCertificateUrl(url);
        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    public CertificateDto updateCertificate(Long translatorId, Long certificateId,
                                            MultipartFile file, UploadCertificateRequest request) throws IOException {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        if(!certificate.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Certificate does not belong to translator");
        }
        imageService.deleteImage(certificate.getCertificateUrl());
        certificate.setTitle(request.getTitle());
        certificate.setYear(request.getYear());
        String url = imageService.uploadFile(translatorId, file, null);
        certificate.setCertificateUrl(url);
        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    public void deleteCertificate(Long translatorId, Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        if(!certificate.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Certificate does not belong to translator");
        }
        imageService.deleteImage(certificate.getCertificateUrl());
        certificateRepository.delete(certificate);

    }

}
