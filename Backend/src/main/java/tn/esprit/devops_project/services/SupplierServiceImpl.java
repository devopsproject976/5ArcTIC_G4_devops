package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.exceptions.SupplierNotFoundException;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.Date;
import java.util.List;

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
		return  supplierRepository.save(supplier);
	}

	@Override
	public void deleteSupplier(Long SupplierId) {
		supplierRepository.deleteById(SupplierId);

	}

	@Override
	public Supplier retrieveSupplier(Long supplierId) {

		return supplierRepository.findById(supplierId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + supplierId));
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

}