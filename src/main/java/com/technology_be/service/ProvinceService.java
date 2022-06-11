package com.technology_be.service;

import com.technology_be.model.Province;
import com.technology_be.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

//    public List<Province> findAll() {
//        return this.provinceRepository.findAll();
//    }

    public Province getProvinceById(Long id) {
        return this.provinceRepository.findById(id).orElse(null);
    }
}
