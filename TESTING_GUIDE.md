# OC Verification Report - Testing Guide

## Overview

This guide explains how to test the OCVerificationReportService with the new **Boundary Compliance** and **Building Components** pages.

---

## 1. Test via REST API (Postman/cURL)

### Static Test (Sample Data)

```bash
# GET Static PDF with sample data
curl -X GET "http://localhost:8080/reports/oc-verification/pdf" \
  -H "Accept: application/pdf" \
  -o oc_verification_sample.pdf
```

**Response:** PDF file containing all pages with sample data populated

---

## 2. Code-Level Testing (Unit/Integration Test)

### Example: Spring Boot Test Class

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class OCVerificationReportServiceTest {

    @Autowired
    private OCVerificationReportService reportService;

    /**
     * Test generating static PDF with sample boundary compliance & building components data
     */
    @Test
    public void testGenerateOCVerificationPdfStatic() throws Exception {
        byte[] pdfBytes = reportService.generateOCVerificationPdfStatic();
        
        assertNotNull("PDF should not be null", pdfBytes);
        assertThat(pdfBytes.length, greaterThan(0));
        
        // Save to temp file for manual review
        Files.write(
            Paths.get("/tmp/oc_verification_static.pdf"),
            pdfBytes
        );
        System.out.println("✅ Static PDF generated: " + pdfBytes.length + " bytes");
    }

    /**
     * Test generating PDF with dynamic DTO data
     */
    @Test
    public void testGenerateOCVerificationPdfWithDto() throws Exception {
        // Create sample project start DTO
        ProjectStartReportDto dto = new ProjectStartReportDto();
        
        // Set owner info
        OwnerInfo ownerInfo = new OwnerInfo();
        ownerInfo.setOwnerName("أحمد محمد علي");
        ownerInfo.setOwnerIdNumber("1234567890");
        ownerInfo.setOwnerMobile("0512345678");
        ownerInfo.setDeedNumber("D-2026-500");
        ownerInfo.setProjectNameAndAddress("مشروع سكني - حي النور - الرياض");
        ownerInfo.setReportDate("1447/02/15");
        dto.setOwnerInfo(ownerInfo);
        
        // Set building info
        BuildingInfo buildingInfo = new BuildingInfo();
        buildingInfo.setBuildingType("سكني");
        buildingInfo.setBuildingDescription("عمارة سكنية بـ 4 طوابق");
        buildingInfo.setLicenseNumber("LIC-2026-001");
        buildingInfo.setLicenseDate("1446/12/01");
        buildingInfo.setPlanNumber("PLN-2026-001");
        buildingInfo.setLandNumber("500");
        buildingInfo.setBlockNumber("25");
        buildingInfo.setDistrict("حي النور");
        buildingInfo.setStreet("شارع الأمير محمد بن عبدالعزيز");
        dto.setBuildingInfo(buildingInfo);
        
        // Set photos
        ProjectPhotosDTO photos = new ProjectPhotosDTO();
        photos.setOfficeName("المكتب الهندسي المتميز");
        dto.setProjectPhotos(photos);
        
        // Set inspection result
        dto.setStageResult("مقبول");
        dto.setStageInspectionAccepted(Boolean.TRUE);
        
        // Generate PDF
        byte[] pdfBytes = reportService.generatePdf(dto);
        
        assertNotNull("PDF should not be null", pdfBytes);
        assertThat(pdfBytes.length, greaterThan(0));
        
        // Save to temp file
        Files.write(
            Paths.get("/tmp/oc_verification_dto.pdf"),
            pdfBytes
        );
        System.out.println("✅ DTO-based PDF generated: " + pdfBytes.length + " bytes");
    }
}
```

---

## 3. Controller Endpoints

### Available Endpoints

#### **GET Static PDF** (Sample Data)
```http
GET /reports/oc-verification/pdf
Accept: application/pdf

Response: 200 OK
Content-Type: application/pdf
Body: [Binary PDF Content]
```

#### **POST Dynamic PDF** (Custom Data)
```http
POST /reports/oc-verification/pdf
Content-Type: application/json

Body:
{
  "ownerInfo": {
    "ownerName": "محمد أحمد",
    "ownerIdNumber": "1234567890",
    "ownerMobile": "0500000000",
    "deedNumber": "D-2026-100",
    "projectNameAndAddress": "مشروع سكني - الرياض",
    "reportDate": "1447/03/01"
  },
  "buildingInfo": {
    "buildingType": "سكني",
    "buildingDescription": "عمارة سكنية",
    "licenseNumber": "LIC-001",
    "planNumber": "PLN-001",
    "landNumber": "100",
    "blockNumber": "20",
    "district": "حي النور",
    "street": "شارع الأمير"
  },
  "projectPhotos": {
    "officeName": "المكتب الهندسي"
  },
  "stageResult": "مقبول",
  "stageInspectionAccepted": true
}

Response: 200 OK
Content-Type: application/pdf
Body: [Binary PDF Content]
```

---

## 4. Sample Data Reference

### Boundary Compliance Data

```java
// North Setback
SetbackDirection northSetback = new SetbackDirection(
    "شمال",           // Direction
    "شارع",           // Boundary Description
    5.0,              // License Measure
    5.0,              // Plan Measure
    5.0,              // Executed Measure
    0.0,              // Increase
    true,             // Is Compliant
    "مطابق"           // Notes
);
```

### Building Components Data

```java
// Ground Floor
ComponentVerification groundFloor = new ComponentVerification(
    "الدور الأرضي",   // Component Name
    "مختلط",          // Usage
    4,                // Number of Units
    3.5,              // Component Height
    400.0,            // Plan Area
    398.0,            // Executed Area
    false,            // Is Compliant
    "ناقص بـ 2 م²"    // Notes
);
```

### Parking Data

```java
ParkingVerification parking = new ParkingVerification(
    15,               // Required Number
    14,               // Executed Number
    false,            // Is Compliant
    "ناقص مقف واحد"   // Notes
);
```

---

## 5. Expected PDF Page Order

1. **Page 1**: Header + Owner Info + Building Info (detail band)
2. **Page 2**: Boundary Compliance (Setbacks & Protrusions)
3. **Page 3**: Building Components (Floors, Parking, Utilities)
4. **Page 4**: Checklist Table
5. **Page 5**: Main Images (Building Photos)
6. **Page 6**: Site Photos Page
7. **Page 7**: Photo Grids (Top & Bottom)
8. **Page 8**: Changes & Extra Items
9. **Page 9**: Inspection Responsibility & Approval

---

## 6. Testing Checklist

- [ ] Static PDF generates without errors
- [ ] DTO-based PDF accepts custom data
- [ ] All 9 pages are rendered
- [ ] Arabic RTL text displays correctly
- [ ] Colors match specification (#005B41, #E8F5E9)
- [ ] Tables have correct column headers
- [ ] Sample data appears in boundary compliance page
- [ ] Sample data appears in building components page
- [ ] Compliant/Non-Compliant checkboxes render properly
- [ ] PDF opens without corruption

---

## 7. Troubleshooting

| Issue | Solution |
|-------|----------|
| PDF is blank | Check if subreports are loaded correctly; ensure JRXML files are in classpath |
| Arabic text shows as ??? | Verify Arabic font files (`Amiri-Regular.ttf`) exist in `/fonts/` directory |
| Boundary compliance page missing | Ensure `oc_verification_boundary_compliance.jrxml` is in `OCVerificationReport/` folder |
| Building components page missing | Ensure `oc_verification_building_components.jrxml` is in `OCVerificationReport/` folder |
| Data not populated | Verify `buildStaticBoundaryCompliance()` and `buildStaticBuildingComponents()` methods are called |

---

## 8. Integration with Frontend

### Example: Angular/React HTTP Call

```typescript
// Generate Static PDF
fetchOCVerificationPDF(): void {
  this.http.get('/reports/oc-verification/pdf', {
    responseType: 'blob'
  }).subscribe((response: Blob) => {
    const blob = new Blob([response], { type: 'application/pdf' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'oc_verification_report.pdf';
    link.click();
  });
}

// Generate Dynamic PDF with custom data
generateOCVerificationPDFWithData(data: any): void {
  this.http.post('/reports/oc-verification/pdf', data, {
    responseType: 'blob'
  }).subscribe((response: Blob) => {
    const blob = new Blob([response], { type: 'application/pdf' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `oc_verification_${new Date().getTime()}.pdf`;
    link.click();
  });
}
```

---

## 9. Compile & Run

```bash
# Compile project
mvn -q -DskipTests compile

# Run tests
mvn test -Dtest=OCVerificationReportServiceTest

# Start application
mvn spring-boot:run

# Test endpoint
curl "http://localhost:8080/reports/oc-verification/pdf" -o test_report.pdf
```

---

**Created**: April 20, 2026  
**Service**: `OCVerificationReportService`  
**DTOs**: `BoundaryComplianceDTO`, `BuildingComponentsDTO`

