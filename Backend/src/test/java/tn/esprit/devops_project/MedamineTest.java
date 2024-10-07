package tn.esprit.devops_project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.exceptions.SupplierNotFoundException;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.SupplierServiceImpl;

import java.util.Optional;

public class MedamineTest {
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier existingSupplier;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        existingSupplier = new Supplier();
        existingSupplier.setIdSupplier(1L);
        existingSupplier.setCode("OLD_CODE");
        existingSupplier.setLabel("Old Supplier");
        existingSupplier.setSupplierCategory(SupplierCategory.ORDINAIRE);
    }

    @Test
    public void testPartialUpdateSupplier_UpdateCodeAndCategory() {
        // Arrange
        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setCode("NEW_CODE");
        updatedSupplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(existingSupplier));
        when(supplierRepository.save(existingSupplier)).thenReturn(existingSupplier);

        // Act
        Supplier result = supplierService.partialUpdateSupplier(1L, updatedSupplier);

        // Assert
        assertEquals("NEW_CODE", result.getCode());
        assertEquals(SupplierCategory.CONVENTIONNE, result.getSupplierCategory());
        assertEquals("Old Supplier", result.getLabel()); // Label should remain unchanged
        verify(supplierRepository).findById(1L);
        verify(supplierRepository).save(existingSupplier);
    }

    @Test
    public void testPartialUpdateSupplier_SupplierNotFound() {
        // Arrange
        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setCode("NEW_CODE");

        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class, () -> {
            supplierService.partialUpdateSupplier(1L, updatedSupplier);
        });

        assertEquals("Supplier not found: 1", exception.getMessage());
        verify(supplierRepository).findById(1L);
        verify(supplierRepository, never()).save(any());
    }

    @Test
    public void testPartialUpdateSupplier_UpdateLabel() {
        // Arrange
        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setLabel("Updated Supplier");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(existingSupplier));
        when(supplierRepository.save(existingSupplier)).thenReturn(existingSupplier);

        // Act
        Supplier result = supplierService.partialUpdateSupplier(1L, updatedSupplier);

        // Assert
        assertEquals("Updated Supplier", result.getLabel());
        assertEquals("OLD_CODE", result.getCode()); // Code should remain unchanged
        assertEquals(SupplierCategory.ORDINAIRE, result.getSupplierCategory()); // Category should remain unchanged
        verify(supplierRepository).findById(1L);
        verify(supplierRepository).save(existingSupplier);
    }
}

