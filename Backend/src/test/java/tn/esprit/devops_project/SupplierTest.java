package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;

import java.util.HashSet;

public class SupplierTest {

    private Supplier supplier;

    @BeforeEach
    public void setUp() {
        // Initialisation d'un fournisseur avant chaque test
        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setCode("SUP123");
        supplier.setLabel("Test Supplier");
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE); // Ou toute autre catégorie
        supplier.setInvoices(new HashSet<>());
    }

    @Test
    public void testSupplierCreation() {
        // Vérification que le fournisseur est correctement créé
        assertNotNull(supplier);
        assertEquals(1L, supplier.getIdSupplier());
        assertEquals("SUP123", supplier.getCode());
        assertEquals("Test Supplier", supplier.getLabel());
        assertEquals(SupplierCategory.ORDINAIRE, supplier.getSupplierCategory());
        assertTrue(supplier.getInvoices().isEmpty());
    }

    @Test
    public void testAddInvoice() {
        // Ajout d'une facture au fournisseur
        Invoice invoice = new Invoice();
        invoice.setIdInvoice(1L);
        invoice.setAmountInvoice(150f);
        invoice.setArchived(false);
        supplier.getInvoices().add(invoice);

        // Vérification que la facture a été ajoutée
        assertEquals(1, supplier.getInvoices().size());
        assertTrue(supplier.getInvoices().contains(invoice));
    }

    @Test
    public void testSupplierCategory() {
        // Vérification des catégories de fournisseur
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        assertEquals(SupplierCategory.CONVENTIONNE, supplier.getSupplierCategory());
    }

    // Ajoutez d'autres tests selon les besoins...
}
