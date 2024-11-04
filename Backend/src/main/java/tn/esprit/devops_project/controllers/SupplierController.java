package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.SupplierDTO;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class SupplierController {

	ISupplierService supplierService;

	@GetMapping("/supplier")
	public List<SupplierDTO> getSuppliers() {
		return supplierService.retrieveAllSuppliers()
				.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/supplier/{supplierId}")
	public SupplierDTO retrieveSupplier(@PathVariable Long supplierId) {
		return convertToDTO(supplierService.retrieveSupplier(supplierId));
	}

	@PostMapping("/supplier")
	public SupplierDTO addSupplier(@RequestBody SupplierDTO supplierDTO) {
		return convertToDTO(supplierService.addSupplier(convertToEntity(supplierDTO)));
	}

	@DeleteMapping("/supplier/{supplierId}")
	public void removeSupplier(@PathVariable Long supplierId) {
		supplierService.deleteSupplier(supplierId);
	}

	@PutMapping("/supplier")
	public SupplierDTO modifySupplier(@RequestBody SupplierDTO supplierDTO) {
		return convertToDTO(supplierService.updateSupplier(convertToEntity(supplierDTO)));
	}

	// Conversion methods
	private SupplierDTO convertToDTO(Supplier supplier) {
		SupplierDTO dto = new SupplierDTO();
		dto.setIdSupplier(supplier.getIdSupplier());
		dto.setCode(supplier.getCode());
		dto.setLabel(supplier.getLabel());
		dto.setSupplierCategory(supplier.getSupplierCategory().name());
		return dto;
	}

	private Supplier convertToEntity(SupplierDTO dto) {
		return Supplier.builder()
				.idSupplier(dto.getIdSupplier())
				.code(dto.getCode())
				.label(dto.getLabel())
				.supplierCategory(SupplierCategory.valueOf(dto.getSupplierCategory()))
				.build();
	}
}
