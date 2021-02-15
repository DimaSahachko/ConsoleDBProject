package com.sahachko.consoledbproject.service;

import java.util.List;

import com.sahachko.consoledbproject.model.Region;
import com.sahachko.consoledbproject.repository.RegionRepository;

public class RegionService {
	private RegionRepository repository;
	
	public RegionService(RegionRepository repository) {
		this.repository = repository;
	}
	
	public Region saveRegion(Region region) {
		return repository.save(region);
	}
	
	public Region updateRegion(Region region) {
		return repository.update(region);
	}

	public List<Region> getAllRegions() {
		return repository.getAll();
	}
	
	public Region getRegionById(long id) {
		return repository.getById(id);
	}
	
	public void deleteRegionById(long id) {
		repository.deleteById(id);
	}
}
