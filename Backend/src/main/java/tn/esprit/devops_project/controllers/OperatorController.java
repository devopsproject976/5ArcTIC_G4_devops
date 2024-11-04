package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.OperatorDTO;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.services.Iservices.IOperatorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class OperatorController {

	IOperatorService operatorService;

	@GetMapping("/operator")
	public List<OperatorDTO> getOperators() {
		return operatorService.retrieveAllOperators().stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/operator/{operatorId}")
	public OperatorDTO retrieveOperator(@PathVariable Long operatorId) {
		Operator operator = operatorService.retrieveOperator(operatorId);
		return convertToDTO(operator);
	}

	@PostMapping("/operator")
	public OperatorDTO addOperator(@RequestBody OperatorDTO operatorDTO) {
		Operator operator = convertToEntity(operatorDTO);
		Operator savedOperator = operatorService.addOperator(operator);
		return convertToDTO(savedOperator);
	}

	@DeleteMapping("/operator/{operatorId}")
	public void removeOperator(@PathVariable Long operatorId) {
		operatorService.deleteOperator(operatorId);
	}

	@PutMapping("/operator")
	public OperatorDTO modifyOperator(@RequestBody OperatorDTO operatorDTO) {
		Operator operator = convertToEntity(operatorDTO);
		Operator updatedOperator = operatorService.updateOperator(operator);
		return convertToDTO(updatedOperator);
	}

	private OperatorDTO convertToDTO(Operator operator) {
		OperatorDTO dto = new OperatorDTO();
		dto.setIdOperateur(operator.getIdOperateur());
		dto.setFname(operator.getFname());
		// Set other necessary fields
		return dto;
	}

	private Operator convertToEntity(OperatorDTO operatorDTO) {
		Operator operator = new Operator();
		operator.setIdOperateur(operatorDTO.getIdOperateur());
		operator.setFname(operatorDTO.getFname());
		// Set other necessary fields
		return operator;
	}
}
