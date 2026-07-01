
package com.nit.noticeboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.noticeboard.model.Department;
import com.nit.noticeboard.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByDepartment(Department department);
    
    long countByDepartment(Department department);

}
