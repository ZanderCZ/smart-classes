package com.bnwzy.smartclassesspringbootweb.pojo.dto;

import com.bnwzy.smartclassesspringbootweb.pojo.StudentData;

public class StudentCreateDTO {
    private String name;
    private String username;
    private String gender;
    private Long deptId;
    private Double gpa;
    private Long studentDataId;

    public Long getStudentDataId() {
        return studentDataId;
    }

    public void setStudentDataId(Long studentDataId) {
        this.studentDataId = studentDataId;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
