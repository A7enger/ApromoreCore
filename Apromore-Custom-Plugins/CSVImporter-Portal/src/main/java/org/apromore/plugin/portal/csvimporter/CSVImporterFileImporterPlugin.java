/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2021 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package org.apromore.plugin.portal.csvimporter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apromore.dao.model.Log;
import org.apromore.dao.model.Usermetadata;
import org.apromore.exception.UserNotFoundException;
import org.apromore.plugin.portal.FileImporterPlugin;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.plugin.portal.csvimporter.listener.CsvImportListener;
import org.apromore.service.UserMetadataService;
import org.apromore.service.csvimporter.services.ParquetFactoryProvider;
import org.apromore.service.csvimporter.services.legacy.LogImporterProvider;
import org.apromore.util.UserMetadataTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.opencsv.CSVReader;

public class CSVImporterFileImporterPlugin implements FileImporterPlugin {

    private static Logger LOGGER = LoggerFactory.getLogger(CSVImporterFileImporterPlugin.class);

    private ParquetFactoryProvider parquetFactoryProvider;
    private LogImporterProvider logImporterProvider;
    private UserMetadataService userMetadataService;

    public ParquetFactoryProvider getParquetFactoryProvider() {
        return parquetFactoryProvider;
    }

    public void setParquetFactoryProvider(ParquetFactoryProvider parquetFactoryProvider) {
        this.parquetFactoryProvider = parquetFactoryProvider;
    }

    public LogImporterProvider getLogImporterProvider() {
        return logImporterProvider;
    }

    public void setLogImporterProvider(LogImporterProvider logImporterProvider) {
        this.logImporterProvider = logImporterProvider;
    }

    public void setUserMetadataService(UserMetadataService newUserMetadataService) {
        LOGGER.info("Injected CSV importer logic {}", newUserMetadataService);
        this.userMetadataService = newUserMetadataService;
    }

    // Implementation of FileImporterPlugin

    @Override
    public Set<String> getFileExtensions() {
        return new HashSet<>(Arrays.asList("csv", "parquet", "xlsx"));
    }

    @Override
    public void importFile(Media media, boolean isLogPublic) {

        // Configure the arguments to pass to the CSV importer view
        Map arg = new HashMap<>();
        arg.put("parquetFactoryProvider", parquetFactoryProvider);
        arg.put("logImporterProvider", logImporterProvider);
        arg.put("media", media);
        Sessions.getCurrent().setAttribute(CSVImporterController.SESSION_ATTRIBUTE_KEY, arg);
        PortalContext portalContext = (PortalContext) Sessions.getCurrent().getAttribute("portalContext");

        // Only works for CSV
        try {
            if ("csv".equals(getMediaFormat(media))) {
                String username = portalContext.getCurrentUser().getUsername();
                // Get header from imported CSV
                List<String> header = new ArrayList<>();
                String fileEncoding = "UTF-8";
                CSVFileReader csvFileReader = new CSVFileReader();

		try (CSVReader csvReader = csvFileReader.newCSVReader(media, fileEncoding);) {
                    header = Arrays.asList(csvReader.readNext());
                } catch (IOException e) {
                    LOGGER.error("Unable to read CSV", e);
                }

                // Get saved schema mapping from DB
                List<Usermetadata> mappingJSONList;
                Set<Usermetadata> usermetadataSet = null;

                try {
                    usermetadataSet = userMetadataService.getUserMetadataWithoutLog(UserMetadataTypeEnum.CSV_IMPORTER,
                            username);
                } catch (UserNotFoundException e) {
                    LOGGER.error("Unable to find user " + username, e);
                }

                if (usermetadataSet != null) {
                    mappingJSONList = new ArrayList<>(usermetadataSet);
                } else mappingJSONList = new ArrayList<>();

                // Sort by Usermetadata object Id, since a new csv schema mapping is created each time.
                Collections.sort(mappingJSONList, Comparator.comparing(Usermetadata::getId));

                if (mappingJSONList.size() != 0) {

                    // Matching from the latest record
                    for (int i = mappingJSONList.size() - 1; i >= 0; i--) {
                        System.out.println(mappingJSONList.get(i));

                        Usermetadata usermetadata = mappingJSONList.get(i);

                        JSONObject jsonObject = (JSONObject) JSONValue.parse(usermetadata.getContent());

                        System.out.println(JSONValue.parse(jsonObject.get("header").toString()));

                        List<String> sampleHeader = (List<String>) jsonObject.get("header");

                        // Attempt 1: try to create a popup window on top of csvImporter window here.
                        if (sampleHeader != null && sampleHeader.equals(header)) try {
                            Window matchedMappingPopUp =
                                    (Window) portalContext.getUI().createComponent(getClass().getClassLoader(), "zul" +
                                            "/matchedMapping.zul", null, null);
                            matchedMappingPopUp.doModal();

                            Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(usermetadata.getCreatedTime());
                            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                            String formattedTime = new SimpleDateFormat("HH:mm:ss").format(date);

                            Label fileNameLabel = (Label) matchedMappingPopUp.getFellow("fileNameLabel");
                            Set<Log> logs = usermetadata.getLogs();

                            for (Log log : logs) {
                                fileNameLabel.setValue("  This mapping was extracted from file \"" + log.getName() +
                                        "\", uploaded at " + formattedTime + " on " + formattedDate);
                            }

                            Button uploadWithMatchedMappingBtn = (Button) matchedMappingPopUp.getFellow(
                                    "uploadWithMatchedMapping");
                            Button uploadAsNewBtn = (Button) matchedMappingPopUp.getFellow(
                                    "uploadAsNew");
			    uploadWithMatchedMappingBtn.addEventListener("onClick",
				    new CsvImportListener(arg,
					    (String) Sessions.getCurrent().getAttribute("fileimportertarget"),
					    matchedMappingPopUp, jsonObject));

			    uploadAsNewBtn.addEventListener("onClick",
				    new CsvImportListener(arg,
					    (String) Sessions.getCurrent().getAttribute("fileimportertarget"),
					    matchedMappingPopUp, null));

                        } catch (IOException | ParseException e) {
                            LOGGER.error("Unable to import CSV", e);
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Can't read file format ", e);
        }
        // can't find match in JSONList or no mapping record
	try {
	    new CsvImportListener(arg, (String) Sessions.getCurrent().getAttribute("fileimportertarget"), null, null)
		    .onEvent(null);
	} catch (Exception e) {
	    LOGGER.error("Unable to import CSV", e);
	}
    }

    private static String getMediaFormat(Media media) throws Exception {
        if (media.getName().lastIndexOf('.') < 0)
            throw new Exception("Can't read file format");
        return media.getName().substring(media.getName().lastIndexOf('.') + 1);
    }
}
