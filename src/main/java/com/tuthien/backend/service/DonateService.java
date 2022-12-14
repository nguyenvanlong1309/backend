package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.constant.DonateType;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.dao.RegionDAO;
import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.DonateStatistic;
import com.tuthien.backend.model.ResponseModel;
import com.tuthien.backend.model.StatisticModel;
import com.tuthien.backend.utils.DataUtils;
import com.tuthien.backend.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DonateService {

    private final DonateDAO donateDAO;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ProjectDAO projectDAO;
    private final IOService ioService;
    private final RegionDAO regionDAO;

    public List<DonateModel> findAll() {
        return this.donateDAO.findAllDonate();
    }

    public List<DonateModel> getMyDonate() {
        User sessionUser = this.userService.getSessionUser();
        return this.findDonateByUsername(sessionUser.getUsername());
    }

    public List<DonateModel> findDonateByUsername(String username) {
        return this.donateDAO.finDonateByUsername(username);
    }

    public List<DonateModel> findDonateByProject(String projectId) {
        return this.donateDAO.findDonateByProject(projectId);
    }

    @Transactional
    public ResponseModel<Donate> donatePersonal(DonateModel donateModel) {

        this.projectDAO.findById(donateModel.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Kh??ng t??m th???y d??? ??n"));

        User sessionUser = this.userService.getSessionUser();
        Donate donate = this.objectMapper.convertValue(donateModel, Donate.class);
        donate.setType(DonateType.PERSONAL.getType());
        donate.setCreatedDate(new Date());

        if (Objects.nonNull(sessionUser)) {
            donate.setCreatedBy(sessionUser.getUsername());
            donate.setFullName(sessionUser.getFullName());
            donate.setPhone(sessionUser.getPhone());
            donate.setEmail(sessionUser.getEmail());
        }
        donate = this.donateDAO.save(donate);
        return new ResponseModel<>(HttpStatus.OK, donate, "Th??nh c??ng");
    }

    @Transactional
    public ResponseModel<Donate> donateBusiness(DonateModel donateModel, MultipartFile avatarFile) {
        if (Objects.nonNull(avatarFile)) {
            String avatar = this.ioService.saveImageToStore(avatarFile)
                    .orElseThrow(() -> new IllegalArgumentException("Upload ???nh th???t b???i"));
            donateModel.setImage(avatar);
        }
        donateModel.setType(DonateType.BUSINESS.getType());
        donateModel.setMode(0);
        Donate donate = this.objectMapper.convertValue(donateModel, Donate.class);
        donate.setCreatedDate(new Date());
        donate = this.donateDAO.save(donate);
        return new ResponseModel<>(HttpStatus.OK, donate, "Th??nh c??ng");
    }

    public List<Map<String, Object>> getTopDonate() {
        return this.donateDAO.findListDonate();
    }

    public List<Map<String, Object>> getTopDonate2(Integer type, Integer limit, String projectId) {
        Pageable pageable = null;
        if (Objects.nonNull(limit)) {
            pageable = PageRequest.of(0, limit);
        }
        return this.donateDAO.findTopPersonalDonate2(type, projectId, pageable);
    }

    public List<DonateStatistic> statisticDonate(StatisticModel statisticModel) {
        return this.regionDAO.findAll().stream()
                .map(region -> new DonateStatistic(region.getId(), region.getName(), null))
                .peek(region -> {
                    region.setData(this.donateDAO.statisticDonate(statisticModel.getYear(), region.getRegionId()));
                }).collect(Collectors.toList());
    }

    public ByteArrayInputStream exportExcelFile() throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        sheet.setColumnWidth(0, 7500);
        sheet.setColumnWidth(1, 7500);
        sheet.setColumnWidth(2, 7500);

        // create header for excel file
        CellStyle headerStyle = ExcelUtils.getStyleHeader(wb);
        Row rowHeader = sheet.createRow(0);
        Cell publicNameCell = rowHeader.createCell(0);
        publicNameCell.setCellStyle(headerStyle);
        publicNameCell.setCellValue("T??n ng?????i t??i tr???");

        Cell titleCell = rowHeader.createCell(1);
        titleCell.setCellStyle(headerStyle);
        titleCell.setCellValue("T??n D??? ??n");

        Cell createdDateCell = rowHeader.createCell(2);
        createdDateCell.setCellStyle(headerStyle);
        createdDateCell.setCellValue("Ng??y t??i tr???");

        Cell totalMoneyCell = rowHeader.createCell(3);
        totalMoneyCell.setCellStyle(headerStyle);
        totalMoneyCell.setCellValue("S??? ti???n");

        Cell timesCell = rowHeader.createCell(4);
        timesCell.setCellStyle(ExcelUtils.getStyleHeader(wb));
        timesCell.setCellValue("S??? l???n");

        List<Map<String, Object>> topDonate = this.getTopDonate();

        IntStream.range(0, topDonate.size())
                .forEach(index -> {
                    Row row = sheet.createRow(index + 1);
                    Map<String, Object> data = topDonate.get(index);

                    CellStyle cellStyle = ExcelUtils.getBorder(wb);
                    int bgColor = index % 2 != 0 ? IndexedColors.GREY_25_PERCENT.index : IndexedColors.WHITE.index;
                    ;
                    cellStyle.setFillForegroundColor((short) bgColor);
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                    Cell nameCell = row.createCell(0);
                    nameCell.setCellStyle(cellStyle);
                    nameCell.setCellValue(data.get("publicName").toString());

                    Cell titleNameCell = row.createCell(1);
                    titleNameCell.setCellStyle(cellStyle);
                    titleNameCell.setCellValue(data.get("title").toString());

                    Cell dateCell = row.createCell(2);
                    dateCell.setCellValue(data.get("createdDate").toString());
                    dateCell.setCellStyle(cellStyle);

                    Cell totalCell = row.createCell(3);
                    totalCell.setCellValue(DataUtils.safeToDouble(data.get("total")));
                    totalCell.setCellStyle(cellStyle);

                    Cell countCell = row.createCell(4);
                    countCell.setCellValue(DataUtils.safeToInt(data.get("count")));
                    countCell.setCellStyle(cellStyle);
                });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        wb.close();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public DonateModel getTotalProjectAndDonate() {
        DonateModel donateModel = new DonateModel();
        donateModel.setCountProject(this.projectDAO.countApprovedProject());
        donateModel.setTotalDonate(this.donateDAO.sumAllDonate());
        donateModel.setCountProvince(this.projectDAO.findByGroupByCityId().size());
        return donateModel;
    }
}