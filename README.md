[![Java CI with Maven](https://github.com/jamilxt/java_spring-boot_japser-report/actions/workflows/maven.yml/badge.svg)](https://github.com/jamilxt/java_spring-boot_japser-report/actions/workflows/maven.yml)
# Jasper Report using Spring Boot (Jave 11, Maven)

## Project-start report architecture

This project also contains a multi-page **project start inspection report** under:

- `src/main/resources/report/projectStart/project_start_report_master.jrxml`

This file is the **main orchestrator** for the project-start PDF. It does not draw every table and image itself. Instead, it acts like a **controller/layout shell** that:

1. declares all report parameters,
2. receives compiled subreports from Java,
3. forwards data to the correct child template,
4. controls the page order of the full report.

If you want to understand the project-start report, start with `project_start_report_master.jrxml` first.

### What `project_start_report_master.jrxml` is responsible for

`project_start_report_master.jrxml` is responsible for the **overall report flow**, not the fine visual design of every section.

Its job is to assemble these pages/sections:

1. **First page / main introduction page**
   - header
   - owner information
   - building information
2. **Checklist table page**
3. **Main images page**
4. **Changes page**
5. **Photos/details page**
6. **Inspection responsibility page** (conditionally shown)

So the master file is best understood as the **report router**.

---

## Deep explanation of `project_start_report_master.jrxml`

### Line-number quick map (`project_start_report_master.jrxml`)

Use these ranges while reading:

- File header and page setup: lines `1-18`
- Parameter declarations (all inputs): lines `20-103`
- Group-order comment (important behavior note): lines `105-113`
- `InspectionResponsibilityGroup` (prints photos/details subreport): lines `115-156`
- `ChangesGroup` (prints changes page subreport): lines `158-179`
- `MainImagesGroup` (prints main images page): lines `181-202`
- `TableSectionGroup` (prints checklist page first among groups): lines `204-220`
- `title` band: lines `222-224`
- `detail` band (first page composition): lines `226-295`
- `summary` band (final conditional page): lines `297-310`

Inside the `detail` band:

- Header subreport: lines `231-244`
- Owner subreport wiring (includes `contractorName`): lines `247-266`
- Building subreport wiring: lines `269-292`

Note: these line numbers are accurate for the current version and may shift after future edits.

### 1) High-level structure

The file has four important areas:

1. **Parameters section**
2. **Groups section**
3. **`detail` band**
4. **`summary` band**

Each part has a different purpose.

### 2) Parameters section

The top of the file declares many `<parameter>` values. These parameters are the contract between Java code and JasperReports.

They fall into these categories:

#### A. Compiled subreport parameters
Examples:

- `headerSubreport`
- `ownerSubreport`
- `buildingSubreport`
- `tableSubreport`
- `photosSubreport`
- `mainImagesSubreport`
- `changesPageSubreport`
- `changesRowsSubreport`
- `inspectionResponsibilitySubreport`

These are not strings or file paths. They are compiled `JasperReport` objects prepared in Java, mainly in:

- `src/main/java/com/jamilxt/java_springboot_japserreport/service/report/ProjectStartReportService.java`

That Java service compiles each `.jrxml` file, then injects it into the master report using `params.put(...)`.

#### B. Datasource parameters
Examples:

- `tableData`
- `photosTopData`
- `photosBottomData`
- `changesData`
- `extraItemsData`
- `inspectionResponsibilityData`

These are used by subreports that repeat rows, render tables, or print collections of items.

#### C. Plain value parameters
Examples:

- `ownerName`
- `contractorName`
- `projectNameAndAddress`
- `reportDate`
- `officeName`
- `digitalStampPath`

These are the text/image values that are forwarded into child templates.

### 3) Why there are so many parameters

Because the master report is a **hub**.

It does not know how each child report is drawn internally, but it must pass the correct inputs to each child subreport. In practice, the master file is the place where all page-level dependencies are gathered.

---

## How page flow works in the master report

This is the most important part to understand.

### 4) The `detail` band = the first visible page

Inside `<detail>`, the master report places three subreports at fixed positions:

1. header subreport
2. owner subreport
3. building subreport

This means the **first page** is built directly inside the master file.

In simple terms:

- `headerSubreport` at the top
- `ownerSubreport` below it
- `buildingSubreport` below owner

So if you want to change the first page layout, the first place to inspect is:

- the `detail` band of `project_start_report_master.jrxml`

### 5) The groups = extra pages after the first page

After the first page, the master report uses **group footers** to emit more full-page subreports.

Declared groups:

- `InspectionResponsibilityGroup`
- `ChangesGroup`
- `MainImagesGroup`
- `TableSectionGroup`

Each group has:

- `<groupExpression><![CDATA[1]]></groupExpression>`

That means each group is effectively a constant one-time group, used as a layout trick to print one footer band.

### 6) Important JasperReports behavior: group footers print from inner to outer

Even though the XML declares groups in this order:

1. `InspectionResponsibilityGroup`
2. `ChangesGroup`
3. `MainImagesGroup`
4. `TableSectionGroup`

their footers are rendered in **reverse nesting order**.

So the actual page order is:

1. `TableSectionGroup` footer → checklist table page
2. `MainImagesGroup` footer → main images page
3. `ChangesGroup` footer → changes page
4. `InspectionResponsibilityGroup` footer → photos/details page

Then after that, the report may also render the `summary` band.

This is a very important point because many people read the XML top-to-bottom and expect the same print order. Jasper group nesting does not work that way.

---

## What each main section prints

### A. First page (`detail` band)

Printed directly by the master report:

- `headerSubreport`
- `ownerSubreport`
- `buildingSubreport`

This page contains the project identity + owner/building information.

### B. `TableSectionGroup`

Prints:

- `tableSubreport`

Uses datasource:

- `tableData`

Purpose:

- render the checklist/task table page.

### C. `MainImagesGroup`

Prints:

- `mainImagesSubreport`

Uses image parameters such as:

- `spatialPortalPhoto`
- `implementationPhoto`
- `aerialPhoto`

Purpose:

- render the main site images page.

### D. `ChangesGroup`

Prints:

- `changesPageSubreport`

Forwards:

- `changesRowsSubreport`
- `changesData`
- `extraItemsData`

Purpose:

- render the page that contains:
  - approved-plan changes
  - extra items not mentioned in the report

Important note:

- `changesPageSubreport` is the page shell.
- `changesRowsSubreport` is the repeating row template used inside the changes page.

### E. `InspectionResponsibilityGroup`

Despite the group name, this footer currently prints:

- `photosSubreport`

It also forwards:

- `topGridSubreport`
- `bottomGridSubreport`
- `photosTopData`
- `photosBottomData`

Purpose:

- render the detailed photos page.

This naming can be confusing: the group is named `InspectionResponsibilityGroup`, but its footer prints the photos/details page, not the final responsibility page.

### F. `summary` band

The final `summary` band prints:

- `inspectionResponsibilitySubreport`

Uses:

- `inspectionResponsibilityData`

Condition:

- it only prints when `hasInspectionResponsibilityData` is `true` (or null by current expression logic).

So the **actual final inspection responsibility page** comes from the `summary` band, not from the similarly named group footer.

---

## Relationship with `ProjectStartReportService.java`

The main Java assembly logic lives in:

- `src/main/java/com/jamilxt/java_springboot_japserreport/service/report/ProjectStartReportService.java`

That service does the following:

1. loads each JRXML file,
2. compiles each one into a `JasperReport`,
3. builds data sources,
4. puts everything into `params`,
5. fills `project_start_report_master.jrxml`.

So the data flow is:

`ProjectStartReportService` → parameters map → `project_start_report_master.jrxml` → child subreports

This is why parameter naming must stay consistent.

If you rename a parameter in the master file but do not update the Java service, the child report may stop receiving data.

### Parameter source map (where parameters come from)

This map answers: for each important parameter in `project_start_report_master.jrxml`, which file creates it, and which file consumes it.

| Master parameter | Comes from (source file) | DTO/Method source | Consumed by (JRXML file) |
|---|---|---|---|
| `headerSubreport` | `src/main/java/com/jamilxt/java_springboot_japserreport/service/report/ProjectStartReportService.java` (`JasperCompileManager.compileReport` + `params.put`) | Compiled from `src/main/resources/report/projectStart/project_start_report_header.jrxml` | `project_start_report_master.jrxml` forwards it to `project_start_main_images.jrxml`, `project_start_changes_page.jrxml`, `project_start_report_photos.jrxml`, `project_start_inspection_responsibility.jrxml`; also used in master `detail` |
| `ownerSubreport` | `ProjectStartReportService.java` (`params.put("ownerSubreport", ...)`) | Compiled from `src/main/resources/report/projectStart/project_start_report_owner.jrxml` | Used in `project_start_report_master.jrxml` `detail` owner block |
| `tableData` | `ProjectStartReportService.java` (`params.put("tableData", new JRBeanCollectionDataSource(...))`) | Built by `buildTableRows(...)` from `ProjectStartReportDto.getTasks()` / `TaskRow` | Forwarded by `project_start_report_master.jrxml` to `project_start_report_table.jrxml` |
| `changesData` | `ProjectStartReportService.java` (`params.put("changesData", ...)`) | Built by `buildTextRowsDataSource(dto.getChanges())` from `ProjectStartReportDto.getChanges()` | Forwarded by `project_start_report_master.jrxml` to `project_start_changes_page.jrxml` |
| `extraItemsData` | `ProjectStartReportService.java` (`params.put("extraItemsData", ...)`) | Built by `buildTextRowsDataSource(dto.getExtraItems())` from `ProjectStartReportDto.getExtraItems()` | Forwarded by `project_start_report_master.jrxml` to `project_start_changes_page.jrxml` |
| `changesRowsSubreport` | `ProjectStartReportService.java` (`params.put("changesRowsSubreport", ...)`) | Compiled from `src/main/resources/report/projectStart/project_start_changes_rows.jrxml` | Forwarded to `project_start_changes_page.jrxml`, which uses it for row rendering |
| `photosTopData` | `ProjectStartReportService.java` (`params.put("photosTopData", buildTopPhotoRows(params))`) | Built by `buildTopPhotoRows(...)` from image params | Forwarded by `project_start_report_master.jrxml` to `project_start_report_photos.jrxml` |
| `photosBottomData` | `ProjectStartReportService.java` (`params.put("photosBottomData", buildBottomPhotoRows(params))`) | Built by `buildBottomPhotoRows(...)` from image params + descriptions | Forwarded by `project_start_report_master.jrxml` to `project_start_report_photos.jrxml` |
| `inspectionResponsibilityData` | `ProjectStartReportService.java` (`params.put("inspectionResponsibilityData", ...)`) | Built by `buildInspectionResponsibilityDataSource(dto.getInspectionResponsibility())` from `InspectionResponsibilityDTO` | Used by summary subreport in `project_start_report_master.jrxml` -> `project_start_inspection_responsibility.jrxml` |
| `reportLogo` | `ProjectStartReportService.java` (`params.put("reportLogo", logoBytes)`) | Loaded from classpath resource image (best-effort) | Forwarded to header consumers (`project_start_report_header.jrxml` through parent pages) |
| `projectNameAndAddress` | `ProjectStartReportService.java` (`params.put("projectNameAndAddress", ...)`) | Mainly from `OwnerInfo` in `ProjectStartReportDto` | Forwarded by master to header-bearing pages/subreports |
| `officeName` | `ProjectStartReportService.java` (`params.put("officeName", ...)`) | Mainly from `ProjectPhotosDTO.getOfficeName()` | Used in pages with footer office label/stamp (for example `project_start_changes_page.jrxml`, `project_start_main_images.jrxml`, `project_start_report_photos.jrxml`) |
| `contractorName` | `ProjectStartReportService.java` (`params.put("contractorName", ...)`) | Mainly from `OwnerInfo.getContractorName()` | Forwarded in master owner block to `project_start_report_owner.jrxml` |

Important:

- The master file `project_start_report_master.jrxml` is the wiring hub.
- Most value/data parameters originate in `ProjectStartReportService.java`.
- DTO origins are primarily: `ProjectStartReportDto`, `OwnerInfo`, `BuildingInfo`, `ProjectPhotosDTO`, `TaskRow`, `InspectionResponsibilityDTO` under `src/main/java/com/jamilxt/java_springboot_japserreport/dto/`.
- If a parameter appears blank in PDF, check both the Java `params.put(...)` source and the target child JRXML `subreportParameter`/`$P{...}` usage.

---

## Why this file matters so much

If something is wrong with:

- page order,
- missing page,
- wrong datasource going to a page,
- wrong subreport printed,
- image/text not appearing,

then `project_start_report_master.jrxml` is usually the first file to inspect.

It is the central place where the full report is wired together.

---

## Maintenance notes and common pitfalls

### 1) Group order is not print order

This is the most common source of confusion.

The order in XML is not the same as the final printed page order because nested group footers render from inner to outer.

### 2) Fixed full-page bands

Most group footers use:

- `height="545"`
- `splitType="Stretch"`

This means each footer behaves like a full-page container.

If a child subreport is designed larger than the available page content area, layout issues can happen.

### 3) Subreport height does not automatically make content safe

Example:

- `changesPageSubreport` may contain internal fixed-position elements.

Even if the master report gives it a full-page box, overlap can still happen inside the child report if that child mixes fixed `y` coordinates with dynamic content.

### 4) `bodySubreport` is declared but not used here

The master file declares:

- `bodySubreport`

and Java also prepares it, but in the current `project_start_report_master.jrxml` structure, that parameter is not actually used inside the visible layout.

So if you are debugging body content and nothing changes, check whether the subreport is currently wired into the layout at all.

### 5) Naming can be misleading

`InspectionResponsibilityGroup` sounds like it should print the responsibility page, but the group footer currently prints the photos/details page, while the true responsibility section prints from `summary`.

### 6) Changes page is intentionally constrained

The current Java service caps the row datasource for the changes page to a fixed count so the fixed visual grid does not overflow its page layout.

This is important if you expect unlimited rows: the present design favors **stable page layout** over unlimited expansion.

---

## Quick mental model for this file

If you want a short way to remember it:

> `project_start_report_master.jrxml` is the book binder.
> Each subreport is one chapter/page.
> The master decides which chapter comes first, what data each chapter gets, and whether the final chapter is printed.

---

## When to edit which file

Use this rule of thumb:

- Edit `project_start_report_master.jrxml` when you want to change:
  - page order
  - which subreport appears
  - parameter wiring
  - whole-page composition

- Edit child JRXML files when you want to change:
  - table cell design
  - fonts/colors
  - exact labels
  - internal page layout
  - row rendering

Examples of child files:

- `project_start_report_header.jrxml`
- `project_start_report_owner.jrxml`
- `project_start_report_building.jrxml`
- `project_start_report_table.jrxml`
- `project_start_main_images.jrxml`
- `project_start_changes_page.jrxml`
- `project_start_changes_rows.jrxml`
- `project_start_report_photos.jrxml`
- `project_start_inspection_responsibility.jrxml`

---

## Simple sequence of rendering

In practical terms, the report renders like this:

1. first page from the master `detail` band,
2. checklist page from `TableSectionGroup`,
3. main images page from `MainImagesGroup`,
4. changes page from `ChangesGroup`,
5. photos/details page from `InspectionResponsibilityGroup`,
6. final responsibility page from `summary` if enabled.

That is the easiest way to think about `project_start_report_master.jrxml`.

## Export (PDF, EXCEL, CSV, DOCX)
### Reference Documentation
Run the application.
Then, Export:
* PDF: [http://localhost:8080/reports/transactions/download?exportType=PDF](http://localhost:8080/reports/transactions/download?exportType=PDF) <br><br>
![image](https://user-images.githubusercontent.com/18072164/106577485-9bcf7280-6568-11eb-925e-938f188ca2b1.png) <br><br>
* EXCEL: [http://localhost:8080/reports/transactions/download?exportType=EXCEL](http://localhost:8080/reports/transactions/download?exportType=EXCEL) <br><br>
![image](https://user-images.githubusercontent.com/18072164/106577645-d0432e80-6568-11eb-9063-5fd02df2bc0c.png) <br><br>
* CSV: [http://localhost:8080/reports/transactions/download?exportType=CSV](http://localhost:8080/reports/transactions/download?exportType=CSV) <br><br>
![image](https://user-images.githubusercontent.com/18072164/106577739-ee109380-6568-11eb-87ef-8ae5d7f7d50e.png) <br><br>
* DOCX: [http://localhost:8080/reports/transactions/download?exportType=DOCX](http://localhost:8080/reports/transactions/download?exportType=DOCX) <br><br>
![image](https://user-images.githubusercontent.com/18072164/106577866-14ceca00-6569-11eb-85d8-08bd5d80a115.png) <br><br>

### Download [Jaspersoft Studio](https://community.jaspersoft.com/project/jaspersoft-studio) to modify the template (file with .jrxml extension) as you like.
#### Templates are: 
* PDF: transaction_report_pdf.jrxml
* EXCEL: transaction_report_excel.jrxml
* CSV: transaction_report_csv.jrxml
* DOCX: transaction_report_docx.jrxml

Open these templates using Jasper Studio & modify as your own. It takes sometime to be familiar with the syntax to design the template. It's similar to HTML but need some practice to make a better design. 
Enjoy!

### Credits:
* [iabur](https://github.com/iabur)
* https://github.com/javahowtos/jasper-export-demo
* https://github.com/javahowtos/jasper-excel-export-demo
* StackOverflow

I didn't find any repo which provides (PDF, EXCEL, CSV, DOCX) all of them at once. Thanks to these^ repo I got the basic idea how to export the report. Thank you very much [iabur](https://github.com/iabur) vai to provide me different template design from your old projects as well as assist me in every way.  

[![Stargazers repo roster for @jamilxt/java_spring-boot_japser-report](http://reporoster.com/stars/jamilxt/java_spring-boot_japser-report)](http://github.com/jamilxt/java_spring-boot_japser-report/stargazers)
[![Forkers repo roster for @jamilxt/java_spring-boot_japser-report](http://reporoster.com/forks/jamilxt/java_spring-boot_japser-report)](http://github.com/jamilxt/java_spring-boot_japser-report/network/members)
