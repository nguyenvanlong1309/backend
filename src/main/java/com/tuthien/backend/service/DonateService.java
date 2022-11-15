package com.tuthien.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.Donate;
import com.tuthien.backend.entity.User;
import com.tuthien.backend.model.DonateModel;
import com.tuthien.backend.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DonateService {

    private final DonateDAO donateDAO;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ProjectDAO projectDAO;

    public List<DonateModel> getMyDonate() {
        User sessionUser = this.userService.getSessionUser();
        return this.donateDAO.findMyDonate(sessionUser.getUsername());
    }

    @Transactional
    public ResponseModel<Donate> donatePersonal(DonateModel donateModel) {

        this.projectDAO.findById(donateModel.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dự án"));

        User sessionUser = this.userService.getSessionUser();
        Donate donate = this.objectMapper.convertValue(donateModel, Donate.class);
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
}
