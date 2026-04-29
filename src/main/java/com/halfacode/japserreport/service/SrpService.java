package com.halfacode.japserreport.service;

import com.halfacode.japserreport.config.JasperReportConfig;
import com.halfacode.japserreport.domain.report.ExportType;
import com.halfacode.japserreport.dto.ImageDto;
import com.halfacode.japserreport.dto.InspectionImageDto;
import com.halfacode.japserreport.dto.InspectionItemDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SrpService {
    private final JasperReportConfig jasperReportConfig;


    public void exportTransactionReportSrp(
            ExportType exportType,
            HttpServletResponse response
    ) throws Exception {

        List<InspectionItemDto> list = getInspectionList();

        JRBeanCollectionDataSource tableData =
                new JRBeanCollectionDataSource(list);

        JRBeanCollectionDataSource imageData =
                new JRBeanCollectionDataSource(getImages());

        Map<String, Object> params = new HashMap<>();

        // ✅ MATCH MASTER REPORT PARAM NAMES
        params.put("tableData", tableData);
        params.put("imageData", imageData);
        params.put("facilityName", "مطعم النور");
        params.put("inspectionDate", "2026-02-14");
        params.put("inspectorName", "محمد أحمد");
        params.put("reportLogo", getClass().getResourceAsStream("/report/logo.png"));

        // Compile subreports at runtime
        JasperReport inspectionTableReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/inspection_table.jrxml")
        );
        JasperReport inspectionImagesReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/report/inspection_images.jrxml")
        );

        params.put("SUBREPORT_DIR", getClass().getResource("/report/").toString());
        params.put("inspectionTableSubreport", inspectionTableReport);
        params.put("inspectionImagesSubreport", inspectionImagesReport);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReportConfig.getInspectionReport(),
                        params,
                        new JREmptyDataSource()
                );

        export(exportType, jasperPrint, response);
    }
    private List<InspectionImageDto> getImages() throws Exception {

        return List.of(

                new InspectionImageDto(
                        getClass().getResource("/report/img1.png")
                ),

                new InspectionImageDto(
                        getClass().getResource("/report/img2.png")
                )

        );

    }
    private List<InspectionItemDto> getInspectionList() {

        return List.of(

                new InspectionItemDto(
                        1,
                        "وجود مواقف سيارات",
                        "MATCHED",
                        "سكني",
                        1
                ),

                new InspectionItemDto(
                        2,
                        "عدم وجود مخالفات",
                        "NOT_MATCHED",
                        "سكني",
                        1
                ),

                new InspectionItemDto(
                        3,
                        "توفر طفايات حريق",
                        "MATCHED",
                        "تجاري",
                        2
                )
        );

    }

    private List<Map<String, Object>> getImageList() {

        Map<String, Object> image1 = new HashMap<>();

        image1.put(
                "imagePath",
                getClass().getResource("/report/images/img1.jpg")
        );


        Map<String, Object> image2 = new HashMap<>();

        image2.put(
                "imagePath",
                getClass().getResource("/report/images/img2.jpg")
        );


        return List.of(image1, image2);

    }
    private void export(

            ExportType exportType,
            JasperPrint jasperPrint,
            HttpServletResponse response

    ) throws Exception {

        if (exportType == ExportType.PDF) {

            response.setContentType("application/pdf");

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=inspection.pdf"
            );

            JasperExportManager.exportReportToPdfStream(

                    jasperPrint,
                    response.getOutputStream()

            );

        }

    }

}
