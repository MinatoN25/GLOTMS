package com.glotms.searchservice.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glotms.searchservice.model.Ticket;

public class ExcelHelper {
	static String[] columns = { "Ticket ID", "Summary", "Description", "Issue Type", "Project Code", "Ticket Status",
			"Priority", "Assignee", "Reporter", "Complexity", "Labels", "Resolution", "SLA", "Created Date",
			"Update Date", "Resolved Date", "Created By", "Updated By" };

	public static ByteArrayInputStream ticketsToExcel(List<Ticket> tickets) throws IOException {
		try (Workbook workbook = new XSSFWorkbook();) {

			CreationHelper createHelper = workbook.getCreationHelper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Sheet sheet = workbook.createSheet("Tickets");
			try (FileOutputStream fileOut = new FileOutputStream("tickets.xlsx");) {
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);
				headerFont.setFontHeightInPoints((short) 12);

				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);

				Row headerRow = sheet.createRow(0);

				for (int i = 0; i < columns.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(columns[i]);
					cell.setCellStyle(headerCellStyle);

				}

				CellStyle dateCellStyle = workbook.createCellStyle();
				dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

				int rowNum = 1;
				for (Ticket ticket : tickets) {
					Row row = sheet.createRow(rowNum++);

					row.createCell(0).setCellValue(ticket.getTicketId() != null ? ticket.getTicketId() : "");

					row.createCell(1).setCellValue(ticket.getTicketSummary() != null ? ticket.getTicketSummary() : "");
					row.createCell(2).setCellValue(ticket.getDescription() != null ? ticket.getDescription() : "");
					row.createCell(3)
							.setCellValue(ticket.getIssueType() != null ? ticket.getIssueType().toString() : "");

					row.createCell(4).setCellValue(ticket.getProjectCode() != null ? ticket.getProjectCode() : "");
					row.createCell(5)
							.setCellValue(ticket.getTicketStatus() != null ? ticket.getTicketStatus().toString() : "");
					row.createCell(6).setCellValue(ticket.getPriority() != null ? ticket.getPriority().toString() : "");
					row.createCell(7).setCellValue(ticket.getAssignee() != null ? ticket.getAssignee() : "");
					row.createCell(8).setCellValue(ticket.getReporter() != null ? ticket.getReporter() : "");
					row.createCell(9)
							.setCellValue(ticket.getComplexity() != null ? ticket.getComplexity().toString() : "");

					row.createCell(10)
							.setCellValue(ticket.getLabel() != null ? Arrays.toString(ticket.getLabel()) : "");
					row.createCell(11).setCellValue(ticket.getResolution() != null ? ticket.getResolution() : "");
					row.createCell(12).setCellValue(ticket.getSla() != null ? ticket.getSla().toString() : "");
					row.createCell(13)
							.setCellValue(ticket.getCreatedDate() != null ? ticket.getCreatedDate().toString() : "");
					row.createCell(14)
							.setCellValue(ticket.getUpdatedDate() != null ? ticket.getUpdatedDate().toString() : "");
					row.createCell(15)
							.setCellValue(ticket.getResolvedDate() != null ? ticket.getResolvedDate().toString() : "");
					row.createCell(16).setCellValue(ticket.getCreatedBy() != null ? ticket.getCreatedBy() : "");
					row.createCell(17).setCellValue(ticket.getUpdatedBy() != null ? ticket.getUpdatedBy() : "");
				}

				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}

				workbook.write(out);

				workbook.write(fileOut);

				return new ByteArrayInputStream(out.toByteArray());
			}
		}
	}

}