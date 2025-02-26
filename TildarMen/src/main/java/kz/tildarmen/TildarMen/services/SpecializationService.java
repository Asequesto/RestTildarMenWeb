package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Specialization;
import kz.tildarmen.TildarMen.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public Specialization getSpecializationByName(String name) {
        return specializationRepository.findByName(name);
    }

    public List<Specialization> getAllByName(List<String> specializations) {
        return specializationRepository.findByNameIn(specializations);
    }
}
