package com.tuthien.backend.schedule;

import com.tuthien.backend.constant.ProjectStatus;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectSchedule {

    private final ProjectDAO projectDAO;
    private final DonateDAO donateDAO;

    @Scheduled(cron = "${spring.schedule.cron}")
    public void updateEndedProject() {
        log.info("start schedule update ended project");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        List<Project> projects = this.projectDAO.findByEndDate(calendar.getTime());
        projects.forEach(project -> {
            BigDecimal totalMoney = this.donateDAO.sumTotalByProjectId(project.getId());
            if (Objects.isNull(totalMoney) || totalMoney.compareTo(project.getMoney()) == -1) {
                project.setStatus(ProjectStatus.CANCEL.getStatus());
            } else {
                project.setStatus(ProjectStatus.DONE.getStatus());
            }
        });
        this.projectDAO.saveAll(projects);
    }

    @Scheduled(cron = "${spring.schedule.cron}")
    public void updateNotificationProject() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        List<Project> projects = this.projectDAO.findByEndDate(calendar.getTime());
        projects.forEach(project -> {
            project.setStatus(ProjectStatus.EXPIRED.getStatus());
        });
        this.projectDAO.saveAll(projects);
    }
}
