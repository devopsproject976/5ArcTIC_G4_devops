package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.SupplierRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SupplierServiceImplTestJU {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierServiceImpl supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setCode("SUP123");
        supplier.setLabel("Test Supplier");
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);

        supplierRepository.save(supplier); // Save supplier for tests
    }

    @Test
    void testRetrieveAllSuppliers() {
        List<Supplier> suppliers = supplierService.retrieveAllSuppliers();

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty());
        assertEquals(1, suppliers.size());
        assertEquals("Test Supplier", suppliers.get(0).getLabel());
    }

    @Test
    void testAddSupplier() {
        Supplier newSupplier = new Supplier();
        newSupplier.setCode("SUP456");
        newSupplier.setLabel("New Supplier");
        newSupplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        Supplier savedSupplier = supplierService.addSupplier(newSupplier);

        assertNotNull(savedSupplier);
        assertEquals("New Supplier", savedSupplier.getLabel());
        assertEquals("SUP456", savedSupplier.getCode());
    }

    @Test
    void testUpdateSupplier() {
        supplier.setLabel("Updated Supplier");
        Supplier updatedSupplier = supplierService.updateSupplier(supplier);

        assertNotNull(updatedSupplier);
        assertEquals("Updated Supplier", updatedSupplier.getLabel());
    }

    @Test
    void testDeleteSupplier() {
        supplierService.deleteSupplier(supplier.getIdSupplier());

        assertFalse(supplierRepository.findById(supplier.getIdSupplier()).isPresent());
    }

    @Test
    void testRetrieveSupplier() {
        Supplier foundSupplier = supplierService.retrieveSupplier(supplier.getIdSupplier());

        assertNotNull(foundSupplier);
        assertEquals(supplier.getLabel(), foundSupplier.getLabel());
    }

    @Test
    void testRetrieveSupplierNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            supplierService.retrieveSupplier(999L); // Non-existing ID
        });
    }
}
