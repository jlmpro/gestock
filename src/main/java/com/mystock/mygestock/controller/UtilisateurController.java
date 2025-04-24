package com.mystock.mygestock.controller;

import com.mystock.mygestock.controller.api.UtilisateurApi;
import com.mystock.mygestock.dto.UtilisateurDto;
import com.mystock.mygestock.service.UtilisateurService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/utilisateur")
public class UtilisateurController implements UtilisateurApi {

    protected int rownum;

    private final UtilisateurService utilisateurService;
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        return utilisateurService.save(dto);
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurService.findAll();
    }

    @Override
    public UtilisateurDto findById(Long id) {
        return utilisateurService.findById(id);
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurService.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        utilisateurService.delete(id);
    }

    @Override
    public ResponseEntity<Void> exportToExcel(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=utilisateurs.xlsx");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Utilisateurs");

            XSSFCellStyle headerStyle = feuilleStyle(workbook);

            createHeader(workbook, sheet, headerStyle);

            List<UtilisateurDto> utilisateurs = utilisateurService.findAll();
            for (UtilisateurDto utilisateur : utilisateurs) {
                Row row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(utilisateur.getId());
                row.createCell(1).setCellValue(utilisateur.getLastname());
                row.createCell(2).setCellValue(utilisateur.getFirstname());
                row.createCell(3).setCellValue(utilisateur.getEmail());
            }

            // Ajustement automatique des colonnes
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            response.flushBuffer();
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



    protected void createHeader(XSSFWorkbook wb, XSSFSheet sheet, CellStyle simpleStyle) {
        XSSFRow headerRow = sheet.createRow(rownum);
        String[] headers = {
                "ID","Nom", "Prénoms", "Email"
        };

        int colIndex = 0;
        for (String header : headers) {
            XSSFCell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(header);
            cell.setCellStyle(simpleStyle);
        }
        rownum++;
    }

    public XSSFCellStyle feuilleStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
//        font.setColor(IndexedColors.WHITE.getIndex()); // Texte blanc

        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);

        return style;
    }

}
