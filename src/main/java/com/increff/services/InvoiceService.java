package com.increff.services;

import com.increff.models.OrderData;
import com.increff.models.OrderItem;
import org.apache.fop.apps.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class InvoiceService {
    @Transactional
    public String generateInvoicePdf(OrderData orderData) {
        try {
            Long orderId = orderData.getId();

            File xmlFile = new File("src/main/resources/invoice.xml");
            File xslFile = new File("src/main/resources/stylesheet.xsl");
            generateXml(orderData, xmlFile);

            File pdfFile = new File("src/main/pdf/output.pdf");
            transformToPdf(xmlFile, xslFile, pdfFile);

            return encodePdfToBase64(pdfFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private void generateXml(OrderData orderData, File xmlFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile))) {
            writer.write("<invoice>");
            writer.write("<orderId>" + orderData.getId() + "</orderId>");
            writer.write("<customerName>" + orderData.getCustomerName() + "</customerName>");
            writer.write("<customerPhone>" + orderData.getCustomerPhone() + "</customerPhone>");
            writer.write("<orderDate>" + orderData.getOrderDate() + "</orderDate>");
            writer.write("<totalAmount>" + orderData.getTotalAmount() + "</totalAmount>");
            writer.write("<items>");
            for (OrderItem item : orderData.getItems()) {
                writer.write("<item>");
                writer.write("<barcode>" + item.getBarcode() + "</barcode>");
                writer.write("<prodName>" + item.getProdName() + "</prodName>");
                writer.write("<price>" + item.getPrice() + "</price>");
                writer.write("<quantity>" + item.getQuantity() + "</quantity>");
                writer.write("<sellingPrice>"+ item.getSellingPrice() + "</sellingPrice>");
                writer.write("</item>");
            }
            writer.write("</items>");
            writer.write("</invoice>");
        }
    }

    private void transformToPdf(File xmlFile, File xslFile, File pdfFile) throws Exception {
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try (OutputStream out = Files.newOutputStream(pdfFile.toPath())) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
            Source src = new StreamSource(xmlFile);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        }
    }

    private String encodePdfToBase64(File pdfFile) throws IOException {
        byte[] fileContent = java.nio.file.Files.readAllBytes(pdfFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
