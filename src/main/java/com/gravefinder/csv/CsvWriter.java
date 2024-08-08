package com.gravefinder.csv;

import com.opencsv.CSVWriter;
import com.gravefinder.model.Memorial;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvWriter {
    private String csvPath;
    private String csvName;

    // Constructor
    public CsvWriter(String csvPath, String csvName) {
        this.csvPath = csvPath;
        this.csvName = csvName;
    }

    // Getters and setters
    public String getCsvPath() {
        return csvPath;
    }

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    // Write to CSV
    public void writeMemorialsToCsv(ArrayList<Memorial> memorials) {
        // try {
        // // Write the header
        // CSVWriter csvWriter = new CSVWriter(new FileWriter(csvPath + csvName));
        // String[] header = { "Memorial ID", "Cemetery ID", "First Name", "Last Name",
        // "Birth Date", "Death Date",
        // "Default Link To Share", "Default Photo To Share" };
        // csvWriter.writeNext(header);

        // // Write the data
        // for (Memorial memorial : memorials) {
        // String[] data = { String.valueOf(memorial.getMemorialId()),
        // String.valueOf(memorial.getCemeteryId()),
        // memorial.getFirstName(), memorial.getLastName(), memorial.getBirthDate(),
        // memorial.getDeathDate(),
        // memorial.getDefaultLinkToShare(), memorial.getDefaultPhotoToShare() };
        // csvWriter.writeNext(data);
        // }
        // csvWriter.close();

        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        try {
            // Log the file path
            String filePath = csvPath + csvName;
            System.out.println("Writing to file: " + filePath);

            // Write the header
            CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
            String[] header = { "Memorial ID", "Cemetery ID", "First Name", "Last Name", "Birth Date", "Death Date",
                    "Default Link To Share", "Default Photo To Share" };
            csvWriter.writeNext(header);

            // Write the data
            for (Memorial memorial : memorials) {
                String[] data = { String.valueOf(memorial.getMemorialId()), String.valueOf(memorial.getCemeteryId()),
                        memorial.getFirstName(), memorial.getLastName(), memorial.getBirthDate(),
                        memorial.getDeathDate(),
                        memorial.getDefaultLinkToShare(), memorial.getDefaultPhotoToShare() };
                csvWriter.writeNext(data);
            }
            csvWriter.close();
            System.out.println("Writing completed successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file.");
            e.printStackTrace();
        }
    }

}
