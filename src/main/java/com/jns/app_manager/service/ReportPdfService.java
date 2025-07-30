package com.jns.app_manager.service;

import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ReportRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportPdfService {

    private final ReportRepository reportRepository;
    private final TemplateEngine templateEngine;

    public byte[] generatePdf(UUID id) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Report not found"));

        Context context = new Context();
        context.setVariable("report", Map.of(
                "councilRegistrationNumber", report.getCouncilRegistrationNumber(),
                "date", report.getDate(),
                "clientName", report.getClient().getName(),
                "userName", report.getUser().getName(),
                "text", report.getText()
        ));

        String html = templateEngine.process("report-pdf", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, "");
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e.getCause());
        }
    }
}