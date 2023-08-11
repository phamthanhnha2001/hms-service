package com.hms.demo.repository;

import com.hms.demo.model.AppointmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentSchedule, Integer> {
    // Tìm các cuộc hẹn dựa trên staffId (ID của bác sĩ)
    List<AppointmentSchedule> findByStaff_Id(Integer staffId);
    // Tìm các cuộc hẹn dựa trên tên đăng nhập của bệnh nhân
    List<AppointmentSchedule> findByPatient_Account_Username(String patientUsername);
    // Kiểm tra xem có cuộc hn nào đã có thơi gian và khung thời gian đã cho hay không
    Boolean existsByTimeAndTimeFrame(Date time, String timeFrame);
    // Kiểm tra xem có cuộc hẹn nào của bệnh nhân có ID và thời gian đã cho hay không
    Boolean existsByPatient_IdAndTime(Integer patientId, Date time);
}
