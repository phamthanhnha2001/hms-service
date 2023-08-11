package com.hms.demo.service.impl;

import com.hms.demo.model.AppointmentSchedule;
import com.hms.demo.model.Patient;
import com.hms.demo.model.Staff;
import com.hms.demo.repository.AppointmentScheduleRepository;
import com.hms.demo.repository.PatientRepository;
import com.hms.demo.repository.StaffRepository;
import com.hms.demo.service.AppointmentScheduleService;
import com.hms.demo.service.utils.MappingHelper;
import com.hms.demo.web.dto.AppointmentScheduleDto;
import com.hms.demo.web.dto.PatientDto;
import com.hms.demo.web.dto.StaffDto;
import com.hms.demo.web.dto.request.AppointmentScheduleReq;
import com.hms.demo.web.exception.EntityNotFoundException;
import com.hms.demo.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentScheduleServiceImpl implements AppointmentScheduleService {
    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final MappingHelper mappingHelper;

    @Override
    public void createAppointmentSchedule(AppointmentScheduleReq appointmentScheduleReq) {
        // Tìm thông tin bệnh nhân dựa trên patientId
        var patient = patientRepository.findById(appointmentScheduleReq.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException(Patient.class.getName()
                        , appointmentScheduleReq.getPatientId().toString()));
        // Tìm thông tin nhân viên dựa trên staffId
        var staff = staffRepository.findById(appointmentScheduleReq.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException(Staff.class.getName()
                        , appointmentScheduleReq.getStaffId().toString()));
        // Kiểm tra khung thời gian này đã được đặt hay chưa
        if (appointmentScheduleRepository
                .existsByTimeAndTimeFrame(appointmentScheduleReq.getTime(), appointmentScheduleReq.getTimeFrame()))
            throw new ServiceException("Khung thời gian này đã được đặt"
                    , "err.api.appointment-schedule-is-existed");
        // Kiểm tra xem bệnh nhân đã đăng ký lịch hẹn trong ngày đó chưa
        if (appointmentScheduleRepository
                .existsByPatient_IdAndTime(appointmentScheduleReq.getPatientId(), appointmentScheduleReq.getTime()))
            throw new ServiceException("Một bệnh nhân chỉ có thể đăng ký được 1 lịch hẹn trong 1 ngày"
                    , "err.api.appointment-schedule-is-existed");
        // Tạo đối tượng AppoinmentSchedule từ AppoinmentScheduleReq và lưu vào cơ sở dữ liệu
        var appointmentSchedule = mappingHelper.map(appointmentScheduleReq, AppointmentSchedule.class);
        appointmentSchedule.setPatient(patient);
        appointmentSchedule.setStaff(staff);
        appointmentScheduleRepository.save(appointmentSchedule);
    }

    @Override
    public List<AppointmentScheduleDto> getSchedules() {
        // Truy vấn danh sách tất cả các lịch hẹn từ cơ sở dữ liệu
        return appointmentScheduleRepository.findAll()
                .stream().map(e -> {
                    // Ánh xạ các đối tượng thực thể thành DTO
                    var res = mappingHelper.map(e, AppointmentScheduleDto.class);
                    res.setPatientDto(mappingHelper.map(e.getPatient(), PatientDto.class));
                    res.setStaffDto(mappingHelper.map(e.getStaff(), StaffDto.class));
                    return res;
                }).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentScheduleDto> getScheduleOfDoctor(Integer doctorId) {
        //Truy van danh sách lịch hẹn của 1 bác sĩ dựa trên doctorID
        return appointmentScheduleRepository.findByStaff_Id(doctorId)
                .stream().map(e -> {
                    // Ánh xạ các đối tượng thực thể thành DTO
                    var res = mappingHelper.map(e, AppointmentScheduleDto.class);
                    res.setPatientDto(mappingHelper.map(e.getPatient(), PatientDto.class));
                    res.setStaffDto(mappingHelper.map(e.getStaff(), StaffDto.class));
                    return res;
                }).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentScheduleDto> getScheduleOfPatient(String patientUsername) {
        // Truy vấn danh sách lịch hẹn của mot bệnh nhân dựa trên patientUsername
        return appointmentScheduleRepository.findByPatient_Account_Username(patientUsername)
                .stream().map(e -> {
                    // Ánh xạ các đối tượng thực thể thành DTO
                    var res = mappingHelper.map(e, AppointmentScheduleDto.class);
                    res.setPatientDto(mappingHelper.map(e.getPatient(), PatientDto.class));
                    res.setStaffDto(mappingHelper.map(e.getStaff(), StaffDto.class));
                    return res;
                }).collect(Collectors.toList());
    }

    @Override
    public AppointmentScheduleDto getScheduleById(Integer scheduleId) {
        // Truy vấn lịch hẹn dựa trên scheduleId
        var schedule = appointmentScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(AppointmentSchedule.class.getName()
                        , scheduleId.toString()));
        // Ánh x đối tuong thực thể thành DTO
        var res = mappingHelper.map(schedule, AppointmentScheduleDto.class);
        res.setPatientDto(mappingHelper.map(schedule.getPatient(), PatientDto.class));
        res.setStaffDto(mappingHelper.map(schedule.getStaff(), StaffDto.class));
        return res;
    }

    @Override
    public void updateSchedule(Integer scheduleId, AppointmentScheduleReq appointmentScheduleReq) {
        // Truy vấn lịch hẹn dựa trên scheduleId
        var schedule = appointmentScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(AppointmentSchedule.class.getName()
                        , scheduleId.toString()));

        // Nếu có staffId được cung cấp, truy vấn thông tin nhân viên
        if (appointmentScheduleReq.getStaffId() != null) {
            var staff = staffRepository.findById(appointmentScheduleReq.getStaffId())
                    .orElseThrow(() -> new EntityNotFoundException(Staff.class.getName()
                            , appointmentScheduleReq.getStaffId().toString()));
            schedule.setStaff(staff);
        }
        // Nếu có patientId được cung cấp, truy vấn thông tin bệnh nhân
        if (appointmentScheduleReq.getPatientId() != null) {
            var patient = patientRepository.findById(appointmentScheduleReq.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException(Patient.class.getName()
                            , appointmentScheduleReq.getPatientId().toString()));
            schedule.setPatient(patient);
        }
        // Ánh xạ và cp nhật thông tin lịch hẹn từ appoinmentScheduleReq
        mappingHelper.mapIfSourceNotNullAndStringNotBlank(appointmentScheduleReq, schedule);
        // Lưu thông tin đã cập nhật vào cơ sở dữ liệu
        appointmentScheduleRepository.save(schedule);
    }

    @Override
    // Xóa lịch hẹn dựa trên scheduleId
    public void deleteSchedule(Integer scheduleId) {
        appointmentScheduleRepository.deleteById(scheduleId);
    }
}
