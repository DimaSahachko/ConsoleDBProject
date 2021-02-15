package com.sahachko.consoledbproject.repository;

import java.util.*;

import com.sahachko.consoledbproject.model.Region;

import java.sql.*;

public class JavaIORegionRepository implements RegionRepository {
	
	@Override
	public List<Region> getAll() {
		List<Region> allRegions = new ArrayList<>();
		try (Statement statement = ConnectionUtils.getStatement()) {
			ResultSet allRegionsSet = statement.executeQuery("SELECT * FROM regions;");
			while(allRegionsSet.next()) {
				long id = allRegionsSet.getLong("id");
				String name = allRegionsSet.getString("name");
				Region region = new Region(name);
				region.setId(id);
				allRegions.add(region);
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return allRegions;
	}

	@Override
	public Region getById(Long id) {
		Region regionById = null;
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT name FROM regions WHERE id = ?;")) {
			prepStatement.setLong(1, id);
			ResultSet regionByIdSet = prepStatement.executeQuery();
			if(regionByIdSet.next()) {
				String name = regionByIdSet.getString("name");
				regionById = new Region(name);
				regionById.setId(id);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return regionById;
	}

	@Override
	public Region save(Region region) {
		long nextEmptyId = 0;
		try(Statement statement = ConnectionUtils.getStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			ResultSet regionsId = statement.executeQuery("SELECT id FROM regions;");
			if(regionsId.last()) {
				nextEmptyId = regionsId.getLong(1) + 1;
			} else {
				nextEmptyId = 1;
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		region.setId(nextEmptyId);
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("INSERT INTO regions VALUES(?, ?);")) {
			prepStatement.setLong(1, region.getId());
			prepStatement.setString(2, region.getName());
			prepStatement.executeUpdate();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return region;
	}

	@Override
	public Region update(Region region) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM regions WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
		prepStatement.setLong(1, region.getId());
			ResultSet updateRegionSet = prepStatement.executeQuery();
			if(updateRegionSet.next()) {
				updateRegionSet.updateString("name", region.getName());
				updateRegionSet.updateRow();
			} else {
				System.out.println("There is no region with such id");
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return region;
	}

	@Override
	public void deleteById(Long id) {
		try(PreparedStatement prepStatement = ConnectionUtils.getPreparedStatement("SELECT * FROM regions WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			prepStatement.setLong(1, id);
			ResultSet deleteRegionSet = prepStatement.executeQuery();
			if(deleteRegionSet.next()) {
				deleteRegionSet.deleteRow();
			} else {
				System.out.println("There is no region with such id");
			}
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
}
