package com.test.dailyreport.service;

import com.test.dailyreport.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;

@Service
public class ReportRunService {

    private static final Map<String, String> filedPositions = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ReportRunService.class);

    @Autowired
    ResourceLoader resourceLoader;

    private static final String CLIENT_TYPE = "clientType";
    private static final String CLIENT_NUMBER = "clientNumber";
    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String SUBACCOUNT_NUMBER = "subAccountNumber";
    private static final String EXCHANGE_CODE = "exchangeCode";
    private static final String PRODUCT_GROUP_CODE = "productGrpCode";
    private static final String SYMBOL = "symbol";
    private static final String EXPIRATION_DATE = "expDate";
    private static final String QUANTITY_LONG = "qtyLong";
    private static final String QUANTITY_SHORT = "qtyshort";

    static  {
        filedPositions.put("clientType", "4,7");
        filedPositions.put("clientNumber", "8,11");
        filedPositions.put("accountNumber", "12,15");
        filedPositions.put("subAccountNumber", "16,19");
        filedPositions.put("exchangeCode", "28,31");
        filedPositions.put("productGrpCode", "26,27");
        filedPositions.put("symbol", "32,37");
        filedPositions.put("expDate", "38,45");
        filedPositions.put("qtyLong", "53,62");
        filedPositions.put("qtyshort", "64,73");
    }

    /**
     * This is the main method to read the input test file
     * process all the lines and generates daily summary report
     * as a CSV file.
     *
     */
    public void processFile() {
        List<Client> clients = new ArrayList<>();
        try {
            Resource resource  = resourceLoader.getResource("classpath:Input.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            logger.debug("Input file Loaded successfully!!! ");

            String strLine;

            while ((strLine = br.readLine()) != null) {
                String clientId = getClientInformation(strLine).trim();
                Client client = new Client(clientId);
                if (!clients.contains(client)) {
                    clients.add(client);
                } else {
                    client = clients.get(clients.indexOf(client));
                }
                String productInfo = getProductInformation(strLine);
                int total = getTotalTransactionAmount(strLine);
                if (client.getProductMap() == null) {
                    client.setProductMap(new HashMap<>());
                }
                if (client.getProductMap().containsKey(productInfo)) {
                    int prodTotal = client.getProductMap().get(productInfo);
                    total = prodTotal + total;
                }
                client.getProductMap().put(productInfo, total);
            }
            writeToCsvFile(clients);
            br.close();
        } catch(Exception ex) {
            logger.error("Error occurred while running Daily Summary Report!!!");
            ex.printStackTrace();
            return;
        }
        logger.debug("Daily Summary Report created successfully!!!");
    }

    /**
     * This method generates the daily report summary
     *
     * @param clients
     */
    private void writeToCsvFile(List<Client> clients) throws FileNotFoundException {
        logger.debug("Started writing to Output File");
        PrintWriter pw = new PrintWriter(new File("Output.csv"));
        StringBuilder header = new StringBuilder();
        header.append("Client_Information");
        header.append(',');
        header.append("Product_Information");
        header.append(',');
        header.append("Total_Transaction_Amount");
        header.append('\n');
        pw.write(header.toString());

        for (Client client : clients) {
            Set<String> products = client.getProductMap().keySet();
            for (String product : products) {
                StringBuilder details = new StringBuilder();
                details.append(client.getClientId());
                details.append(',');
                details.append(product);
                details.append(',');
                details.append(client.getProductMap().get(product));
                details.append('\n');
                pw.write(details.toString());
            }
        }
        pw.close();
        logger.debug("Output File created with the report details");
    }

    /**
     * This method returns the client information from a transaction
     *
     * @param transaction
     * @return client information
     */
    private String getClientInformation(String transaction) {
        StringBuilder clientInfo = new StringBuilder();
        clientInfo.append(transaction.substring(getStaringPosition(CLIENT_TYPE)-1,getEndingPosition(CLIENT_TYPE)));
        clientInfo.append(transaction.substring(getStaringPosition(CLIENT_NUMBER)-1,getEndingPosition(CLIENT_NUMBER)));
        clientInfo.append(transaction.substring(getStaringPosition(ACCOUNT_NUMBER)-1,getEndingPosition(ACCOUNT_NUMBER)));
        clientInfo.append(transaction.substring(getStaringPosition(SUBACCOUNT_NUMBER)-1,getEndingPosition(SUBACCOUNT_NUMBER)));
        logger.debug("Extracting Client Information - "+clientInfo.toString());
        return clientInfo.toString();
    }

    /**
     * This method returns the product information from a transaction
     *
     * @param transaction
     * @return product information
     */
    private String getProductInformation(String transaction) {
        StringBuilder productInfo = new StringBuilder();
        productInfo.append(transaction.substring(getStaringPosition(EXCHANGE_CODE)-1,getEndingPosition(EXCHANGE_CODE)));
        productInfo.append(transaction.substring(getStaringPosition(PRODUCT_GROUP_CODE)-1,getEndingPosition(PRODUCT_GROUP_CODE)));
        productInfo.append(transaction.substring(getStaringPosition(SYMBOL)-1,getEndingPosition(SYMBOL)));
        productInfo.append(transaction.substring(getStaringPosition(EXPIRATION_DATE)-1,getEndingPosition(EXPIRATION_DATE)));
        logger.debug("Extracting Product Information - "+productInfo.toString());
        return productInfo.toString();
    }

    /**
     * This method returns the total transaction amount from a transaction
     *
     * @param transaction
     * @return total transaction amount
     */
    private int getTotalTransactionAmount(String transaction) {
        String qtyLong = transaction.substring(getStaringPosition(QUANTITY_LONG)-1,getEndingPosition(QUANTITY_LONG));
        String qtyShort = transaction.substring(getStaringPosition(QUANTITY_SHORT)-1,getEndingPosition(QUANTITY_SHORT));
        return Integer.parseInt(qtyLong) - Integer.parseInt(qtyShort);
    }

    /**
     * This method returns the starting position of a field from input line
     *
     * @param filed
     * @return Start position of the field
     */
    private int getStaringPosition(String filed) {
        String position[] = filedPositions.get(filed).split(",");
        return Integer.parseInt(position[0]);
    }

    /**
     * This method returns the ending position of a field from input line
     *
     * @param filed
     * @return end position of the field
     */
    private int getEndingPosition(String filed) {
        String position[] = filedPositions.get(filed).split(",");
        return Integer.parseInt(position[1]);
    }



}
