package com.example.cab_booking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cab_booking.model.VehicleCategory;
import com.example.cab_booking.repository.VehicleCategoryRepository;

@Service
public class VehicleCategoryService {

    @Autowired
    private VehicleCategoryRepository repository;

    public VehicleCategory saveCategory(VehicleCategory category) {
        return repository.save(category);
    }

    public List<VehicleCategory> getAllCategories() {
        return repository.findAll();
    }

    public Optional<VehicleCategory> getCategoryById(Long id) {
        return repository.findById(id);
    }

    public Optional<VehicleCategory> getCategoryByName(String name) {
        return repository.findByName(name);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
