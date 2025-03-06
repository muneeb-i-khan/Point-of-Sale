package com.increff.services;

import com.increff.db.dao.InvoiceDao;
import com.increff.db.pojo.InvoicePojo;
import com.increff.models.OrderData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.models.OrderItem;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceDao invoiceDao;

    private static final String ORDER_API_URL = "http://localhost:9000/pos/api/order/";

    public OrderData fetchOrderDetails(Long orderId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(ORDER_API_URL + orderId, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, OrderData.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch order details", e);
        }
    }

    @Transactional
    public String generateInvoicePdf(OrderData orderData) {
        try {
            Long orderId = orderData.getId();

            InvoicePojo existingInvoice = invoiceDao.getByOrderId(orderId);
            if (existingInvoice != null) {
                File existingPdf = new File(existingInvoice.getPath());
                if (existingPdf.exists()) {
                    return encodePdfToBase64(existingPdf);
                }
            }

            File xmlFile = new File("src/main/resources/invoice.xml");
            File xslFile = new File("src/main/resources/stylesheet.xsl");
            generateXml(orderData, xmlFile);

            File pdfFile = new File("src/main/pdf/output" + orderId + ".pdf");
            transformToPdf(xmlFile, xslFile, pdfFile);

            InvoicePojo invoicePojo = new InvoicePojo();
            invoicePojo.setOrderId(orderId);
            invoicePojo.setPath(pdfFile.getAbsolutePath());
            invoiceDao.add(invoicePojo);

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
