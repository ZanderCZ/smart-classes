package com.bnwzy.smartclassesspringbootweb.controller;

import com.bnwzy.smartclassesspringbootweb.pojo.ClassMissionResource;
import com.bnwzy.smartclassesspringbootweb.pojo.ResponseMessage;
import com.bnwzy.smartclassesspringbootweb.pojo.dto.ClassMissionCreateDTO;
import com.bnwzy.smartclassesspringbootweb.pojo.dto.ClassMissionResourceCreateDTO;
import com.bnwzy.smartclassesspringbootweb.service.IClassMissionResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/classMissionResource")
public class ClassMissionResourceController {
    @Autowired
    private IClassMissionResourceService classMissionResourceService;

    @PostMapping("/create")
    public ResponseMessage createClassMissionResource(@Validated @RequestBody ClassMissionResourceCreateDTO classMissionResourceCreateDTO){
        return ResponseMessage.success("<Create class mission resource>", classMissionResourceService.createClassMissionResource(classMissionResourceCreateDTO));
    }

    @GetMapping("/getAllClassMissionResourceByClassMissionId/{id}")
    public ResponseMessage getAllClassMissionResourcesByClassMissionId(@PathVariable("id") Long id) {
        return ResponseMessage.success("<Get all classMissionResource>", classMissionResourceService.getAllClassMissionResourcesByClassMissionId(id));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseMessage deleteClassMissionResourceById(@PathVariable("id") Long id) {
        return ResponseMessage.success("<Delete classMissionResource>", classMissionResourceService.deleteClassMissionResourceById(id));
    }
}
