package com.increff.pos.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Constants {
    public static final String INVOICE_URL = "http://localhost:9001/invoice/api/invoice/";
    public static final String PDF_SAVE_PATH = "src/main/pdf/output";
    public final String USER_ID = "userId";
    public final String ROLE = "role";
    public final String FRONTEND_URL = "http://localhost:4200";
    public final List<String> EXPOSED_HEADERS = Arrays.asList("totalClients", "totalDaySaleReport", "totalInventories", "totalOrders", "totalProducts", "totalReports");
    public final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
    public final String OPERATOR = "OPERATOR";
    public final String SUPERVISOR = "SUPERVISOR";
}
