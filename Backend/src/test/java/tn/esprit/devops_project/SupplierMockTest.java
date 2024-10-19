package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.SupplierServiceImpl;

import java.util.*;

public class SupplierMockTest {

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Mock
    private SupplierRepository supplierRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSupplierStatistics() {
        // Setup
        Long supplierId = 1L;
        Supplier supplier = new Supplier();
        supplier.setIdSupplier(supplierId);

        // Créer des factures et des détails de facture
        Invoice invoice1 = new Invoice();
        invoice1.setAmountInvoice(200f);
        invoice1.setArchived(false);
        Date modificationDate = new Date();
        invoice1.setDateLastModificationInvoice(modificationDate);

        InvoiceDetail detail1 = new InvoiceDetail();
        detail1.setQuantity(3);
        detail1.setPrice(66.67f);
        detail1.setProduct(new Product()); // Ajoute un produit si nécessaire

        invoice1.setInvoiceDetails(new HashSet<>(Collections.singletonList(detail1)));
        supplier.setInvoices(new HashSet<>(Collections.singletonList(invoice1)));

        // Mocking
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        // Execution
        Map<String, Object> statistics = supplierService.getSupplierStatistics(supplierId);

        // Vérification
        assertEquals(200f, statistics.get("totalInvoiceAmount"));
        assertEquals(Long.valueOf(3), statistics.get("totalProductsSupplied")); // Changez Integer en Long
        assertNotNull(statistics.get("lastInvoiceModificationDate"));
        assertEquals(modificationDate, statistics.get("lastInvoiceModificationDate")); // Vérifiez la date
    }


}
