package com.tuthien.backend.schedule;

import com.tuthien.backend.constant.ProjectStatus;
import com.tuthien.backend.dao.DonateDAO;
import com.tuthien.backend.dao.ProjectDAO;
import com.tuthien.backend.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectSchedule {

    private final ProjectDAO projectDAO;
    private final DonateDAO donateDAO;

    @Scheduled(cron = "${spring.schedule.cron}")
    public void updateEndedProject() {
        List<Project> projects = this.projectDAO.findByEndDate(new Date());
        projects.forEach(project -> {
            BigDecimal totalMoney = this.donateDAO.sumTotalByProjectId(project.getId());
            if (totalMoney.compareTo(project.getMoney()) == -1) {
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
