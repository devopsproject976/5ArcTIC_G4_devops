package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.exceptions.SupplierNotFoundException;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

	SupplierRepository supplierRepository;

	@Override
	public List<Supplier> retrieveAllSuppliers() {
		return supplierRepository.findAll();
	}

	@Override
	public Supplier addSupplier(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier updateSupplier(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public void deleteSupplier(Long supplierId) {
		supplierRepository.deleteById(supplierId);
	}

	private static final String SUPPLIER_NOT_FOUND = "Supplier not found: ";

	@Override
	public Supplier retrieveSupplier(Long supplierId) {
		return supplierRepository.findById(supplierId)
				.orElseThrow(() -> new SupplierNotFoundException(SUPPLIER_NOT_FOUND + supplierId));
	}


	public Supplier partialUpdateSupplier(Long supplierId, Supplier updatedSupplier) {
		// Récupérer le fournisseur existant
		Supplier existingSupplier = supplierRepository.findById(supplierId)
				.orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + supplierId));

		// Mettre à jour les champs seulement si des nouvelles valeurs sont fournies
		if (StringUtils.hasText(updatedSupplier.getCode())) {
			log.info("Updating supplier code from {} to {}", existingSupplier.getCode(), updatedSupplier.getCode());
			existingSupplier.setCode(updatedSupplier.getCode());
		}

		if (StringUtils.hasText(updatedSupplier.getLabel())) {
			log.info("Updating supplier label from {} to {}", existingSupplier.getLabel(), updatedSupplier.getLabel());
			existingSupplier.setLabel(updatedSupplier.getLabel());
		}

		if (updatedSupplier.getSupplierCategory() != null) {
			log.info("Updating supplier category from {} to {}", existingSupplier.getSupplierCategory(), updatedSupplier.getSupplierCategory());
			existingSupplier.setSupplierCategory(updatedSupplier.getSupplierCategory());
		}

		// Sauvegarder les modifications partielles dans la base de données
		log.info("Saving partial updates for supplier with ID: {}", supplierId);
		return supplierRepository.save(existingSupplier);
	}

	// Nouvelle méthode pour obtenir des statistiques complexes sur un fournisseur
	public Map<String, Object> getSupplierStatistics(Long supplierId) {
		// Récupérer le fournisseur existant
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + supplierId));

		// Vérifier si le fournisseur a des factures
		if (supplier.getInvoices() == null || supplier.getInvoices().isEmpty()) {
			throw new IllegalArgumentException("No invoices found for supplier with ID: " + supplierId);
		}

		// Initialisation des variables pour les statistiques
		float totalInvoiceAmount = 0f;
		long totalProductsSupplied = 0;
		Date lastModificationDate = null;

		// Parcourir toutes les factures pour calculer les statistiques
		for (Invoice invoice : supplier.getInvoices()) {
			// Exclure les factures archivées
			if (Boolean.FALSE.equals(invoice.getArchived())){
				totalInvoiceAmount += invoice.getAmountInvoice(); // Additionner le montant des factures

				// Compter les produits fournis
				for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
					totalProductsSupplied += detail.getQuantity(); // Additionner la quantité
				}

				// Mettre à jour la date de modification si nécessaire
				Date modificationDate = invoice.getDateLastModificationInvoice();
				if (lastModificationDate == null || (modificationDate != null && modificationDate.after(lastModificationDate))) {
					lastModificationDate = modificationDate;
				}
			}
		}

		// Créer un Map pour stocker les statistiques
		Map<String, Object> supplierStats = Map.of(
				"totalInvoiceAmount", totalInvoiceAmount,
				"totalProductsSupplied", totalProductsSupplied,
				"lastInvoiceModificationDate", lastModificationDate
		);

		// Log des statistiques générées
		log.info("Statistics for supplier with ID {}: {}", supplierId, supplierStats);

		return supplierStats;
	}

}
