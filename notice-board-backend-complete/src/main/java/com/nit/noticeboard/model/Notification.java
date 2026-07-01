
package com.nit.noticeboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String message;

    private String filePath;
    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private Department department;

    // getters and setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}

    public String getFilePath(){return filePath;}
    public void setFilePath(String filePath){this.filePath=filePath;}

    public String getCreatedBy(){return createdBy;}
    public void setCreatedBy(String createdBy){this.createdBy=createdBy;}

    public LocalDateTime getCreatedAt(){return createdAt;}
    
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
}
