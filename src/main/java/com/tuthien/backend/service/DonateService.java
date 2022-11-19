package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.constant.DonateType;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DonateService {

    private final DonateDAO donateDAO;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ProjectDAO projectDAO;
    private final IOService ioService;

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
    @Transactional
    public ResponseModel<Donate> donatePersonal(DonateModel donateModel) {

        this.projectDAO.findById(donateModel.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dự án"));

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
        return new ResponseModel<>(HttpStatus.OK, donate, "Thành công");
    }

    @Transactional
    public ResponseModel<Donate> donateBusiness(DonateModel donateModel, MultipartFile avatarFile) {
        if (Objects.nonNull(avatarFile)) {
            String avatar = this.ioService.saveImageToStore(avatarFile)
                    .orElseThrow(() -> new IllegalArgumentException("Upload ảnh thất bại"));
            donateModel.setImage(avatar);
        }
        donateModel.setType(DonateType.BUSINESS.getType());
        Donate donate = this.objectMapper.convertValue(donateModel, Donate.class);
        donate.setCreatedDate(new Date());
        donate = this.donateDAO.save(donate);
        return new ResponseModel<>(HttpStatus.OK, donate, "Thành công");
    }

    public List<Map<String, Object>> getTopDonate(Integer type, Integer limit) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (Objects.isNull(limit)) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(0, limit);
        return this.donateDAO.findTopPersonalDonate(type, pageable);
    }

}