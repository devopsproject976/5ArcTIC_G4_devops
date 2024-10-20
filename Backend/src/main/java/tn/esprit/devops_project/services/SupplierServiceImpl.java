package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

    SupplierRepository supplierRepository;

    @Override
    public List<Supplier> retrieveAllSuppliers() {
        log.info("Retrieving all suppliers");
        return supplierRepository.findAll();
    }

    @Override
    @Transactional
    public Supplier addSupplier(Supplier supplier) {
        log.info("Adding new supplier with code: {}", supplier.getCode());
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        log.info("Updating supplier with id: {}", supplier.getIdSupplier());
        if (!supplierRepository.existsById(supplier.getIdSupplier())) {
            throw new IllegalArgumentException("Supplier with ID " + supplier.getIdSupplier() + " does not exist.");
        }
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long supplierId) {
        log.info("Deleting supplier with id: {}", supplierId);
        if (!supplierRepository.existsById(supplierId)) {
            throw new IllegalArgumentException("Supplier with ID " + supplierId + " does not exist.");
        }
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public Supplier retrieveSupplier(Long supplierId) {
        log.info("Retrieving supplier with id: {}", supplierId);
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + supplierId + " not found."));
    }
}
