package com.bnwzy.smartclassesspringbootweb.service.impl;

import com.bnwzy.smartclassesspringbootweb.exception.DepartmentNotFoundException;
import com.bnwzy.smartclassesspringbootweb.exception.TeacherNotFoundException;
import com.bnwzy.smartclassesspringbootweb.exception.UserAlreadyExistException;
import com.bnwzy.smartclassesspringbootweb.exception.UserNotFoundException;
import com.bnwzy.smartclassesspringbootweb.pojo.Classes;
import com.bnwzy.smartclassesspringbootweb.pojo.Department;
import com.bnwzy.smartclassesspringbootweb.pojo.Teacher;
import com.bnwzy.smartclassesspringbootweb.pojo.dto.TeacherCreateDTO;
import com.bnwzy.smartclassesspringbootweb.pojo.dto.TeacherUpdateDTO;
import com.bnwzy.smartclassesspringbootweb.repository.ClassesRepository;
import com.bnwzy.smartclassesspringbootweb.repository.DepartmentRepository;
import com.bnwzy.smartclassesspringbootweb.repository.TeacherRepository;
import com.bnwzy.smartclassesspringbootweb.repository.UserRepository;
import com.bnwzy.smartclassesspringbootweb.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService implements ITeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassesRepository classesRepository;

    @Override
    public Teacher addTeacher(TeacherCreateDTO teacherCreateDTO) {
        if(userRepository.findByUsername(teacherCreateDTO.getUsername()).isPresent()){
            Teacher teacher = new Teacher();
            teacher.setUsername(teacherCreateDTO.getUsername());
            teacher.setName(teacherCreateDTO.getName());
            teacher.setGender(teacherCreateDTO.getGender());
            if(departmentRepository.findById(teacherCreateDTO.getDepartmentId()).isPresent()){
                teacher.setDepartment(departmentRepository.findById(teacherCreateDTO.getDepartmentId()).get());
            }else{
                throw new DepartmentNotFoundException("Department not found");
            }
            if (teacherRepository.findByUsername(teacherCreateDTO.getUsername()).isPresent()) {
                throw new UserAlreadyExistException("Teacher already exist");
            } else {
                teacher.setUsername(teacherCreateDTO.getUsername());
            }
            return teacherRepository.save(teacher);
        }else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public Teacher updateTeacher(TeacherUpdateDTO teacherUpdateDTO) {
        if(teacherRepository.existsById(teacherUpdateDTO.getId())){
            if (teacherRepository.findById(teacherUpdateDTO.getId()).isEmpty()) {
                throw new TeacherNotFoundException("Teacher not found");
            }
            Teacher teacher=teacherRepository.findById(teacherUpdateDTO.getId()).get();
            if (teacherUpdateDTO.getName()!=null) {
                teacher.setName(teacherUpdateDTO.getName());
            }
            if (teacherUpdateDTO.getGender()!=null) {
                teacher.setGender(teacherUpdateDTO.getGender());
            }
            if (teacherUpdateDTO.getDepartmentId()!=null) {
                if (departmentRepository.findById(teacherUpdateDTO.getDepartmentId()).isPresent()) {
                    teacher.setDepartment(departmentRepository.findById(teacherUpdateDTO.getDepartmentId()).get());
                } else {
                    throw new DepartmentNotFoundException("Department not found");
                }
            }
            return teacherRepository.save(teacher);
        }else{
            throw new TeacherNotFoundException("Teacher not found");
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if(teacherRepository.findById(id).isPresent()){
            Teacher teacher=teacherRepository.findById(id).get();
            List<Classes> classes = classesRepository.findByTeacher(teacher);
            if (!classes.isEmpty()) {
                for (Classes aClass : classes) {
                    aClass.setTeacher(null);
                }
            }
            teacherRepository.delete(teacher);
            return true;
        }else{
            throw new TeacherNotFoundException("Teacher not found");
        }
    }

    @Override
    public Teacher getTeacherById(Long id) {
        if (teacherRepository.findById(id).isPresent()) {
            return teacherRepository.findById(id).get();
        }else{
            throw new TeacherNotFoundException("Teacher not fount");
        }
    }

    @Override
    public Teacher getTeacherByUsername(String username) {
        if(teacherRepository.findByUsername(username).isPresent()){
            return teacherRepository.findByUsername(username).get();
        }else{
            throw new TeacherNotFoundException("Teacher not found");
        }
    }

    @Override
    public List<Teacher> getAllTeacher() {
        return new ArrayList<>(teacherRepository.findAll());
    }

    @Override
    public Long getTeacherCount() {
        return teacherRepository.count();
    }

    @Override
    public List<Teacher> getTeachersOfDept(Long deptId) {
        if (departmentRepository.findById(deptId).isPresent()) {
            return teacherRepository.findByDepartment(departmentRepository.findById(deptId).get());
        } else {
            throw new DepartmentNotFoundException("Department not found");
        }
    }
}
