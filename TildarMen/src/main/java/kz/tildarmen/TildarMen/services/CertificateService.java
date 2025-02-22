package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.CertificateDto;
import kz.tildarmen.TildarMen.mapper.CertificateMapper;
import kz.tildarmen.TildarMen.model.Certificate;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final TranslatorService translatorService;
    private final CertificateMapper certificateMapper;

    public CertificateDto addCertificate(Long translatorId, CertificateDto request) {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Certificate certificate = new Certificate();
        certificate.setTranslator(translator);
        certificate.setTitle(request.getTitle());
        certificate.setYear(request.getYear());
        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    public CertificateDto updateCertificate(Long translatorId, Long certificateId, CertificateDto request) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        if(!certificate.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Certificate does not belong to translator");
        }
        certificate.setTitle(request.getTitle());
        certificate.setYear(request.getYear());
        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    public void deleteCertificate(Long translatorId, Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        if(!certificate.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Certificate does not belong to translator");
        }
        certificateRepository.delete(certificate);

    }

}
