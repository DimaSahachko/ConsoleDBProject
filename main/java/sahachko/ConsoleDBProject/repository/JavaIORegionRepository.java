package sahachko.ConsoleDBProject.repository;

import java.util.*;
import java.sql.*;
import sahachko.ConsoleDBProject.model.Region;

public class JavaIORegionRepository implements RegionRepository {
	Connection connection = null;
	

	public List<Region> getAll() {
		List<Region> allRegions = new ArrayList<>();
		if(connection == null) {
			connectToDB();
		}
		try (Statement st = connection.createStatement()) {
			ResultSet allRegionsSet = st.executeQuery("SELECT * FROM regions;");
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

	public Region getById(Long id) {
		Region regionById = null;
		if(connection == null) {
			connectToDB();
		}
		try(PreparedStatement getRegionById = connection.prepareStatement("SELECT name FROM regions WHERE id = ?;")) {
			getRegionById.setLong(1, id);
			ResultSet regionByIdSet = getRegionById.executeQuery();
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

	public Region save(Region region) {
		long nextEmptyId = 0;
		if(connection == null) {
			connectToDB();
		}
		try{
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet regionsId = st.executeQuery("SELECT id FROM regions;");
			if(regionsId.last()) {
				nextEmptyId = regionsId.getLong(1) + 1;
			} else {
				nextEmptyId = 1;
			}
			regionsId.close();
			st.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		region.setId(nextEmptyId);
		try {
			PreparedStatement saveRegionToDB = connection.prepareStatement("INSERT INTO regions VALUES(?, ?);");
			saveRegionToDB.setLong(1, region.getId());
			saveRegionToDB.setString(2, region.getName());
			saveRegionToDB.executeUpdate();
			saveRegionToDB.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return region;
	}

	public Region update(Region region) {
		if(connection == null) {
			connectToDB();
		}
		try(PreparedStatement updateRegionById = connection.prepareStatement("SELECT * FROM regions WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
		updateRegionById.setLong(1, region.getId());
			ResultSet updateRegionSet = updateRegionById.executeQuery();
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

	public void deleteById(Long id) {
		if(connection == null) {
			connectToDB();
		}
		try(PreparedStatement deleteRegionById = connection.prepareStatement("SELECT * FROM regions WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
				deleteRegionById.setLong(1, id);
				ResultSet deleteRegionSet = deleteRegionById.executeQuery();
				if(deleteRegionSet.next()) {
					deleteRegionSet.deleteRow();
				} else {
					System.out.println("There is no region with such id");
				}
			} catch(SQLException exc) {
				exc.printStackTrace();
			}
	}
	
	private void connectToDB() {
		Scanner sc = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306";
		String name = "root";
		System.out.println("Type your password");
		String password = sc.nextLine();
		sc.close();
		try{
			connection = DriverManager.getConnection(url, name, password);
			Statement st = connection.createStatement();
			String createDB = "CREATE DATABASE IF NOT EXISTS consoleProject;";
			st.execute(createDB);
			String useDB = "USE consoleProject;";
			st.execute(useDB);
			String createRegionTable = "CREATE TABLE IF NOT EXISTS regions (id BIGINT, name VARCHAR(100), PRIMARY KEY(id));";
			st.execute(createRegionTable);
			st.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		
	}
}
