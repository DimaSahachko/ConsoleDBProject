package com.sahachko.consoledbproject.service.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.sahachko.consoledbproject.model.Region;
import com.sahachko.consoledbproject.repository.RegionRepository;
import com.sahachko.consoledbproject.service.implementations.RegionServiceImplementation;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplementationTest {
	
	@InjectMocks
	RegionServiceImplementation regionService;
	
	@Mock
	RegionRepository regionRepository;
	
	@Test
	public void testSaveRegion_shouldCallRepositorySaveMethod() {
		Region region = new Region("Ukraine");
		regionService.saveRegion(region);
		verify(regionRepository).save(region);
	}
	
	@Test
	public void testSaveRegion_shouldReturnTheSameObject() {
		Region region = new Region("Ukraine");
		when(regionRepository.save(region)).thenReturn(region);
		assertEquals(region, regionService.saveRegion(region));
	}
	
	@Test
	@Disabled
	public void testSaveRegion_shouldFail() {
		when(regionRepository.save(null)).thenThrow(NullPointerException.class);
		assertEquals(null, regionService.saveRegion(null));
	}
	
	@Test
	public void testUpdateRegion_shouldCallRepositoryUpdateMethod() {
		Region region = new Region("Ukraine");
		regionService.updateRegion(region);
		verify(regionRepository).update(region);
	}
	
	@Test
	public void testUpdateRegion_shouldReturnTheSameObject() {
		Region region = new Region("Ukraine");
		when(regionRepository.update(region)).thenReturn(region);
		assertEquals(region, regionService.updateRegion(region));
	}
	
	@Test
	@Disabled
	public void testUpdateRegion_shouldFail() {
		when(regionRepository.update(null)).thenThrow(NullPointerException.class);
		assertEquals(null, regionService.updateRegion(null));
	}
	
	@Test
	public void testGetAllRegions_shouldCallRepositoryGetAllMethod() {
		regionService.getAllRegions();
		verify(regionRepository).getAll();
	}
	
	@Test
	public void testGetAllRegions_shouldReturnListOfAllRegions() {
		List<Region> allRegions = Arrays.asList(new Region("Ukraine"), new Region("USA"), new Region("The Netherlands"));
		when(regionRepository.getAll()).thenReturn(allRegions);
		assertEquals(allRegions, regionService.getAllRegions());
	}
	
	@Test
	@Disabled
	public void testGetAllRegions_shouldFail() {
		List<Region> allRegions = Arrays.asList(new Region("Ukraine"), new Region("USA"), new Region("The Netherlands"));
		assertEquals(allRegions, regionService.getAllRegions());
	}
	
	@Test
	public void testGetRegionById_shouldCallRepositoryGetByIdMethod() {
		regionService.getRegionById(1l);
		verify(regionRepository).getById(1l);
	}
	
	@Test
	public void testGetRegionById_shouldReturnRegionById() {
		Region ukraine = new Region("Ukraine");
		Region belgium = new Region("Belgium");
		Map<Integer, Region> regions = new HashMap<>();
		regions.put(1, ukraine);
		regions.put(2, belgium);
		when(regionRepository.getById(1l)).thenReturn(regions.get(1));
		assertEquals(ukraine, regionService.getRegionById(1l));
	}
	
	@Test
	@Disabled
	public void testGetRegionById_shouldFail() {
		Region ukraine = new Region("Ukraine");
		Region belgium = new Region("Belgium");
		Map<Integer, Region> regions = new HashMap<>();
		regions.put(1, ukraine);
		regions.put(2, belgium);
		when(regionRepository.getById(gt(2l))).thenReturn(null);
		assertThrows(NullPointerException.class, () -> regionService.getRegionById(3));
	}
	
	@Test
	public void testDeleteRegionById_shouldCallRepositoryDeleteByIdMethod() {
		regionService.deleteRegionById(3l);
		verify(regionRepository).deleteById(3l);
	}
	
	@Test
	@Disabled
	public void testDeleteRegionById_shouldFail() {
		regionService.deleteRegionById(1l);
		verify(regionRepository, never()).deleteById(1l);
	}
}
