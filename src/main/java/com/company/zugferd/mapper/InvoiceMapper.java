package com.company.zugferd.mapper;

import com.company.zugferd.dto.InvoiceRequest;
import org.mustangproject.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Maps the Endur/OpenJVS structured payload into the Mustang Invoice model
 * that is used to generate the CII XML (EN 16931 profile).
 */
@Component
public class InvoiceMapper {

    public Invoice toMustangInvoice(InvoiceRequest request) {
        InvoiceRequest.InvoiceData data = request.getInvoiceData();

        Invoice invoice = new Invoice()
                .setNumber(request.getInvoiceNumber())
                .setIssueDate(toDate(data.getIssueDate()))
                .setCurrency(data.getCurrency() != null ? data.getCurrency() : "EUR");

        if (data.getDueDate() != null) {
            invoice.setDueDate(toDate(data.getDueDate()));
        }
        if (data.getDeliveryDate() != null) {
            invoice.setDeliveryDate(toDate(data.getDeliveryDate()));
        }
        if (data.getBuyerReference() != null && !data.getBuyerReference().isBlank()) {
            invoice.setReferenceNumber(data.getBuyerReference());
        }

        // Seller (Sender)
        InvoiceRequest.Party sellerDto = data.getSeller();
        TradeParty seller = new TradeParty(
                sellerDto.getName(),
                nullToEmpty(sellerDto.getStreet()),
                nullToEmpty(sellerDto.getZip()),
                nullToEmpty(sellerDto.getCity()),
                nullToEmpty(sellerDto.getCountry())
        );
        if (sellerDto.getVatId() != null) seller.addVATID(sellerDto.getVatId());
        if (sellerDto.getTaxId() != null) seller.addTaxID(sellerDto.getTaxId());
        if (sellerDto.getIban() != null) {
            seller.addBankDetails(new BankDetails(sellerDto.getIban(), nullToEmpty(sellerDto.getBic())));
        }
        if (sellerDto.getContact() != null) {
            InvoiceRequest.Contact c = sellerDto.getContact();
            seller.setContact(new Contact(
                    nullToEmpty(c.getName()),
                    nullToEmpty(c.getPhone()),
                    nullToEmpty(c.getEmail())
            ));
        }
        invoice.setSender(seller);

        // Buyer (Recipient)
        InvoiceRequest.Party buyerDto = data.getBuyer();
        TradeParty buyer = new TradeParty(
                buyerDto.getName(),
                nullToEmpty(buyerDto.getStreet()),
                nullToEmpty(buyerDto.getZip()),
                nullToEmpty(buyerDto.getCity()),
                nullToEmpty(buyerDto.getCountry())
        );
        if (buyerDto.getVatId() != null) buyer.addVATID(buyerDto.getVatId());
        if (buyerDto.getTaxId() != null) buyer.addTaxID(buyerDto.getTaxId());
        invoice.setRecipient(buyer);

        // Line items
        for (InvoiceRequest.LineItem line : data.getLines()) {
            Product product = new Product(
                    line.getName(),
                    nullToEmpty(line.getDescription()),
                    nullToEmpty(line.getUnitCode()),
                    line.getVatRate()
            );
            Item item = new Item(product, line.getNetPrice(), line.getQuantity());
            invoice.addItem(item);
        }

        return invoice;
    }

    private static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
