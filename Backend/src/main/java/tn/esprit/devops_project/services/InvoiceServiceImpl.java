package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.Iservices.IInvoiceService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Slf4j
@AllArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);



	final InvoiceRepository invoiceRepository;
	final OperatorRepository operatorRepository;
	final InvoiceDetailRepository invoiceDetailRepository;
	final SupplierRepository supplierRepository;
	private final StockRepository stockRepository;



	@Override
	public List<Invoice> retrieveAllInvoices() {
		return invoiceRepository.findAll();
	}
	@Override
	public void cancelInvoice(Long invoiceId) {
		// method 01
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException("Invoice not found"));
		invoice.setArchived(true);
		invoiceRepository.save(invoice);
		//method 02 (Avec JPQL)
		invoiceRepository.updateInvoice(invoiceId);
	}

	@Override
	public Invoice retrieveInvoice(Long invoiceId) {

		return invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException("Invoice not found"));
	}

	@Override
	public List<Invoice> getInvoicesBySupplier(Long idSupplier) {
		Supplier supplier = supplierRepository.findById(idSupplier).orElseThrow(() -> new NullPointerException("Supplier not found"));
		return (List<Invoice>) supplier.getInvoices();
	}

	@Override
	public void assignOperatorToInvoice(Long idOperator, Long idInvoice) {
		Invoice invoice = invoiceRepository.findById(idInvoice).orElseThrow(() -> new NullPointerException("Invoice not found"));
		Operator operator = operatorRepository.findById(idOperator).orElseThrow(() -> new NullPointerException("Operator not found"));
		operator.getInvoices().add(invoice);
		operatorRepository.save(operator);
	}

	@Override
	public float getTotalAmountInvoiceBetweenDates(Date startDate, Date endDate) {
		return invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate);
	}

	@Override
	public InvoiceSummary generateDetailedInvoiceSummary(Long invoiceId) {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new IllegalArgumentException("Invoice not found for ID: " + invoiceId));

		Supplier supplier = invoice.getSupplier();
		Operator operator = Optional.ofNullable(invoice.getOperator())
				.orElseThrow(() -> new IllegalArgumentException("No operator associated with Invoice ID: " + invoiceId));

		float totalAmount = 0.0f;
		float totalDiscount = 0.0f;
		float totalTax = 0.0f;

		// Step 3: Calculate amounts based on invoice details
		for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
			Product product = detail.getProduct();
			float linePrice = detail.getQuantity() * product.getPrice();
			float discount = calculateDiscount(product, supplier);
			linePrice *= (1 - discount); // Apply discount to line price

			// Calculate total discount separately
			totalDiscount += detail.getQuantity() * product.getPrice() * discount;

			// Calculate tax based on effective line price after discount
			float effectivePriceAfterDiscount = linePrice; // This is already discounted
			float taxRate = calculateTax(product);
			float taxAmount = effectivePriceAfterDiscount * taxRate; // Tax based on discounted price
			totalTax += taxAmount; // Accumulate total tax
			totalAmount += effectivePriceAfterDiscount + taxAmount; // Total amount includes effective price and tax

			// Debug output for each product
			logger.debug("Product: {}, Quantity: {}, Line Price Before Discount: {}, Discount: {}, " +
							"Line Price After Discount: {}, Tax Rate: {}, Tax Amount: {}",
					product.getTitle(), detail.getQuantity(), detail.getQuantity() * product.getPrice(),
					discount, effectivePriceAfterDiscount, taxRate, taxAmount);
		}

		if (isPenaltyApplicable(invoice)) {
			totalAmount *= 1.5;
			logger.debug("Penalty applied. Total amount after penalty: {}", totalAmount);
		}

		logger.debug("Total Amount: {}, Total Discount: {}, Total Tax: {}", totalAmount, totalDiscount, totalTax);

		return new InvoiceSummary(totalAmount, totalDiscount, totalTax);
	}

	private float calculateDiscount(Product product, Supplier supplier) {
		float discount = 0.0f;
		if (supplier.getSupplierCategory() == SupplierCategory.CONVENTIONNE) {
			discount += 0.05f; // 5% supplier discount
		}
		if (product.getCategory() == ProductCategory.ELECTRONICS) {
			discount += 0.1f; // Additional 10% discount for electronics
		}
		return discount;
	}

	private float calculateTax(Product product) {
		if (product.getCategory() == ProductCategory.ELECTRONICS) {
			return 0.15f; // 15% tax for electronics
		} else {
			return 0.10f; // Default tax rate for other products
		}
	}

	private boolean isPenaltyApplicable(Invoice invoice) {
		LocalDate creationDate = invoice.getDateCreationInvoice().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = LocalDate.now();
		return ChronoUnit.DAYS.between(creationDate, currentDate) > 10;
	}


}