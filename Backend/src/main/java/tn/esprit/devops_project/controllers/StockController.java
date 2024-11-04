package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.StockDTO;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.services.Iservices.IStockService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class StockController {

    IStockService stockService;

    @PostMapping("/stock/add")
    public StockDTO addStock(@RequestBody StockDTO stockDTO) {
        return convertToDTO(stockService.addStock(convertToEntity(stockDTO)));
    }

    @GetMapping("/stock/{id}")
    public StockDTO retrieveStock(@PathVariable Long id) {
        return convertToDTO(stockService.retrieveStock(id));
    }

    @GetMapping("/stock")
    public List<StockDTO> retrieveAllStock() {
        return stockService.retrieveAllStock()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversion methods
    private StockDTO convertToDTO(Stock stock) {
        StockDTO dto = new StockDTO();
        dto.setIdStock(stock.getIdStock());
        dto.setTitle(stock.getTitle());
        return dto;
    }

    private Stock convertToEntity(StockDTO dto) {
        return Stock.builder()
                .idStock(dto.getIdStock())
                .title(dto.getTitle())
                .build();
    }
}
