package com.dfs.switchsimulator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dfs.switchsimulator.common.BasePdu;
import com.dfs.switchsimulator.config.SwitchMockServer;
import com.dfs.switchsimulator.transactionProcessor.SwitchMessageProcessor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
Author Name: abdul.fatah

Project Name: switch-simulator

Package Name: com.dfs.switchsimulator.controller

Class Name: Incoming

Date and Time:3/27/2023 2:07 PM

Version:1.0
*/
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IncomingTransactionController {
    private Logger logger = LoggerFactory.getLogger(IncomingTransactionController.class);

    @Autowired
    SwitchMessageProcessor switchMessageProcessor;


    @RequestMapping(value = "/title-fetch", method = RequestMethod.POST)
    public BasePdu titleFetchIncomingRequest( @RequestBody BasePdu incomingTitleFetchRequest ) {
        long startTime = new Date().getTime();
        BasePdu ibftResponse = new BasePdu();
        try {
            int currentYear = LocalDate.now().getYear();
            currentYear = currentYear % 10;
            // Get total days in the year to date
            int totalDaysToDate = LocalDate.now().getDayOfYear();
            String formattedTotalDays = String.format("%03d", totalDaysToDate);
            // Get current hour
            int currentHour = LocalDateTime.now().getHour();
            String formattedTotalHrs = String.format("%02d", currentHour);

            String rrn = currentYear+formattedTotalDays+formattedTotalHrs+incomingTitleFetchRequest.getStan();

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the desired date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmmss");
            DateTimeFormatter formatterMMDD = DateTimeFormatter.ofPattern("MMdd");
            DateTimeFormatter formatterHHmm = DateTimeFormatter.ofPattern("HHmmss");

            // Format the current date
            String formattedDate = currentDateTime.format(formatter);
            String formattedDateMMDD = currentDateTime.format(formatterMMDD);
            String formattedDateHHmm = currentDateTime.format(formatterHHmm);

            // Print the formatted date
            incomingTitleFetchRequest.setTransactionDate(formattedDate);
            incomingTitleFetchRequest.setTransactionLocalDate(formattedDateMMDD);
            incomingTitleFetchRequest.setTransactionLocalTime(formattedDateHHmm);
            incomingTitleFetchRequest.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
            ibftResponse = switchMessageProcessor.incomingRequest(incomingTitleFetchRequest);
            logger.info("******* Processing title Fetch Incoming Request *********");
        } catch (Exception e) {
            logger.info("Internal Error@ Sign On", e);
        }
        long endTime = new Date().getTime();
        long difference = endTime - startTime;
        logger.info("**** REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        logger.info("Processed title Fetch Incoming Request : " + ibftResponse);
        return ibftResponse;
    }

    @RequestMapping(value = "/ibft-advice", method = RequestMethod.POST)
    public BasePdu ibftAdviceIncomingRequest( @RequestBody BasePdu ibftAdviceIncomingRequest ) {
        long startTime = new Date().getTime();
        BasePdu ibftResponse = new BasePdu();
        try {
            int currentYear = LocalDate.now().getYear();
            currentYear = currentYear % 10;
            // Get total days in the year to date
            int totalDaysToDate = LocalDate.now().getDayOfYear();
            String formattedTotalDays = String.format("%03d", totalDaysToDate);
            // Get current hour
            int currentHour = LocalDateTime.now().getHour();
            String formattedTotalHrs = String.format("%02d", currentHour);

            String rrn = currentYear+formattedTotalDays+formattedTotalHrs+ibftAdviceIncomingRequest.getStan();

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the desired date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmmss");
            DateTimeFormatter formatterMMDD = DateTimeFormatter.ofPattern("MMdd");
            DateTimeFormatter formatterHHmm = DateTimeFormatter.ofPattern("HHmmss");

            // Format the current date
            String formattedDate = currentDateTime.format(formatter);
            String formattedDateMMDD = currentDateTime.format(formatterMMDD);
            String formattedDateHHmm = currentDateTime.format(formatterHHmm);

            // Print the formatted date
//            ibftAdviceIncomingRequest.setRrn(rrn);
//            ibftAdviceIncomingRequest.setTransactionDate(formattedDate);
            ibftAdviceIncomingRequest.setTransactionLocalDate(formattedDateMMDD);
            ibftAdviceIncomingRequest.setTransactionLocalTime(formattedDateHHmm);
            ibftAdviceIncomingRequest.setTransactionDateTime(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));

            ibftResponse = switchMessageProcessor.incomingIbftAdviceRequest(ibftAdviceIncomingRequest);
            logger.info("******* Processing Ibft Advice Incoming Request *********");
        } catch (Exception e) {
            logger.info("Internal Error@ Sign On", e);
        }
        long endTime = new Date().getTime();
        long difference = endTime - startTime;
        logger.info("**** REQUEST PROCESSED IN ****: " + difference + " milliseconds");
        logger.info("Processed title Fetch Incoming Request - RRN: " + ibftResponse.getRrn());
        return ibftResponse;
    }

    
    @GetMapping("/disconnect")
    public void disconnect()
    {
        switchMessageProcessor.closeallsession();
    }
}
