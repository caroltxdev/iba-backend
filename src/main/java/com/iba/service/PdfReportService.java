package com.iba.service;

import com.iba.domain.entity.Occurrence;
import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.projection.TypeCountProjection;
import com.iba.repository.OccurrenceRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfReportService {

    private final OccurrenceRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Transactional(readOnly = true)
    public byte[] generatePdfReport(LocalDate start, LocalDate end, OccurrenceType type) {
        log.info("Generating PDF report: start={}, end={}, type={}", start, end, type);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            addTitle(document);
            addPeriod(document, start, end);
            addSummaryByType(document, start, end);
            document.add(new Paragraph("\n"));
            addOccurrencesTable(document, start, end, type);

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            log.info("PDF report generated successfully: {} bytes", pdfBytes.length);
            return pdfBytes;

        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException("Error generating PDF report", e);
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Relatório Ĩbá - Ocorrências Ambientais", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addPeriod(Document document, LocalDate start, LocalDate end) throws DocumentException {
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        String periodText = String.format("Período: %s a %s",
                start.format(DATE_FORMATTER),
                end.format(DATE_FORMATTER));
        Paragraph period = new Paragraph(periodText, normalFont);
        period.setAlignment(Element.ALIGN_CENTER);
        period.setSpacingAfter(15);
        document.add(period);
    }

    private void addSummaryByType(Document document, LocalDate start, LocalDate end) throws DocumentException {
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Paragraph summaryHeader = new Paragraph("Resumo por Tipo", headerFont);
        summaryHeader.setSpacingAfter(10);
        document.add(summaryHeader);

        List<TypeCountProjection> stats = repository.countByType(start, end);

        Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
        for (TypeCountProjection stat : stats) {
            String line = String.format("  • %s: %d ocorrência(s)",
                    stat.getType(), stat.getCount());
            document.add(new Paragraph(line, normalFont));
        }
    }

    private void addOccurrencesTable(Document document, LocalDate start, LocalDate end,
                                     OccurrenceType type) throws DocumentException {
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Paragraph tableHeader = new Paragraph("Detalhamento das Ocorrências", headerFont);
        tableHeader.setSpacingAfter(10);
        document.add(tableHeader);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 1.5f, 3f, 1.2f, 1.2f, 2f});

        Font cellHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        addTableHeader(table, cellHeaderFont);

        // Converte o enum para String (ou null se type for null)
        String typeStr = type != null ? type.name() : null;

        List<Occurrence> occurrences = repository.findFiltered(typeStr, start, end);
        Font cellFont = new Font(Font.HELVETICA, 9, Font.NORMAL);

        for (Occurrence occ : occurrences) {
            addTableRow(table, occ, cellFont);
        }

        document.add(table);
    }

    private void addTableHeader(PdfPTable table, Font font) {
        String[] headers = {"Data", "Tipo", "Descrição", "Latitude", "Longitude", "Foto"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, Occurrence occ, Font font) {
        table.addCell(new Phrase(occ.getDate().format(DATE_FORMATTER), font));
        table.addCell(new Phrase(occ.getType().name(), font));

        String desc = occ.getDescription();
        if (desc.length() > 50) {
            desc = desc.substring(0, 47) + "...";
        }
        table.addCell(new Phrase(desc, font));

        table.addCell(new Phrase(occ.getLatitude().toString(), font));
        table.addCell(new Phrase(occ.getLongitude().toString(), font));

        String photoUrl = occ.getPhotoUrl() != null ? occ.getPhotoUrl() : "-";}}