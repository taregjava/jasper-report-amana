package com.jamilxt.java_springboot_japserreport.controller;

import com.jamilxt.java_springboot_japserreport.domain.report.ExportType;
import com.jamilxt.java_springboot_japserreport.service.SrpService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/srp-transactions")
public class SrpTransactionController {

    private final SrpService transactionService;

    @GetMapping("/srp")
    public void download(

            @RequestParam ExportType exportType,
            HttpServletResponse response

    ) throws Exception {

        transactionService.exportTransactionReportSrp(

                exportType,
                response

        );

    }

}
