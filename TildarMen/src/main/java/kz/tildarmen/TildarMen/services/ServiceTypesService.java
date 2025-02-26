package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.ServiceTypes;
import kz.tildarmen.TildarMen.repository.ServiceTypesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ServiceTypesService {

    private final ServiceTypesRepository serviceTypesRepository;

    public ServiceTypes getServiceTypesByName(String name) {
        return serviceTypesRepository.findByName(name);
    }

    public List<ServiceTypes> getAllByName(List<String> serviceTypes) {
        return serviceTypesRepository.findByNameIn(serviceTypes);
    }
}
