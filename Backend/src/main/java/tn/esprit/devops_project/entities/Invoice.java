package tn.esprit.devops_project.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long idInvoice;
	float amountDiscount;
	float amountInvoice;
	@Temporal(TemporalType.DATE)
	Date dateCreationInvoice;
	@Temporal(TemporalType.DATE)
	Date dateLastModificationInvoice;
	Boolean archived;
	@OneToMany(mappedBy = "invoice")
	Set<InvoiceDetail> invoiceDetails;
	@ManyToOne
	@JsonIgnore
	Supplier supplier;

	// getters and setters
	@Setter
	@Getter
	@ManyToOne // Assuming one operator can be associated with multiple invoices
	@JoinColumn(name = "operator_id") // The foreign key column name in the invoice table
	private Operator operator;



}
