package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceSummary;

import java.util.Date;
import java.util.List;

public interface IInvoiceService {
	List<Invoice> retrieveAllInvoices();

	List<Invoice> getInvoicesBySupplier(Long idSupplier);

	void cancelInvoice(Long id);

	Invoice retrieveInvoice(Long id);
	
	void assignOperatorToInvoice(Long idOperator, Long idInvoice);

	float getTotalAmountInvoiceBetweenDates(Date startDate, Date endDate);

    InvoiceSummary generateDetailedInvoiceSummary(Long invoiceId);
}
