package com.sahachko.consoledbproject.service.implementations;

import java.util.List;

import com.sahachko.consoledbproject.model.Region;
import com.sahachko.consoledbproject.repository.RegionRepository;
import com.sahachko.consoledbproject.service.RegionService;

public class RegionServiceImplementation implements RegionService {
	private RegionRepository repository;
	
	public RegionServiceImplementation(RegionRepository repository) {
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
