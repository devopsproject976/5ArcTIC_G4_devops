package tn.esprit.devops_project.entities;

public class InvoiceSummary {
    private float totalAmount;
    private float totalDiscount;
    private float totalTax;

    public InvoiceSummary(float totalAmount, float totalDiscount, float totalTax) {
        this.totalAmount = totalAmount;
        this.totalDiscount = totalDiscount;
        this.totalTax = totalTax;
    }

    // Getters
    public float getTotalAmount() { return totalAmount; }
    public float getTotalDiscount() { return totalDiscount; }
    public float getTotalTax() { return totalTax; }
}
