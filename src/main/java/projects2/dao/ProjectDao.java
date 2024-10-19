package projects2.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import projects2.entity.Category;
import projects2.entity.Material;
import projects2.entity.Projects2;
import projects2.entity.Step;
import projects2.exception.DbException2;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase {
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "projects2";
	private static final String PROJECT_CATEGORY_TABLE = "projects2_category";
	private static final String STEP_TABLE = "step";

	public Projects2 insertProject(Projects2 project) {
	//@formatter:off
	String sql = ""
			+ "INSERT INTO " + PROJECT_TABLE + " "
			+ "(projects2_name, estimated_hours, actual_hours, difficulty, notes) "
			+ "VALUES "
			+ "(?, ?, ?, ?, ?)";
	//@formatter:off
	
	try(Connection conn = DbConnection2.getConnection()) {
		startTransaction(conn);
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
		setParameter(stmt, 1, project.getProjects2Name(), String.class);
		setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
		setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
		setParameter(stmt, 4, project.getDifficulty(), Integer.class);
		setParameter(stmt, 5, project.getNotes(), String.class);
		
		stmt.executeUpdate();
		
		Integer projects2Id = getLastInsertId(conn, PROJECT_TABLE);
		commitTransaction(conn);
		
		project.setProjects2Id(projects2Id);
		return project;
		}
		catch(Exception e) {
			rollbackTransaction(conn);
			throw new DbException2(e);
		}
	}
	catch(SQLException e) {
		throw new DbException2(e);
	}
}

	public List<Projects2> fetchAllProjects() {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY projects2_name;";
		
		try(Connection conn = DbConnection2.getConnection()) {
			startTransaction(conn);
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			try(ResultSet rs = stmt.executeQuery()) {
				List <Projects2> projects = new LinkedList<> ();
		
		while(rs.next()) {
		projects.add(extract(rs, Projects2.class));
		}
		return projects;
		}
		}
			
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException2(e);
			}
		}
		catch(SQLException e) {
			throw new DbException2(e);
		
	}
	}

	public Optional<Projects2> fetchProjectById(Integer projects2Id) {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE projects2_id = ?";
		
		try(Connection conn = DbConnection2.getConnection()) {
			startTransaction(conn);
			
			try {
			Projects2 project = null;
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projects2Id, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					project = extract(rs, Projects2.class);
					}
				}
			}
			if(Objects.nonNull(project)) {
				project.getMaterials().addAll(fetchMaterialsForProject(conn, projects2Id));
				project.getSteps().addAll(fetchStepsForProject(conn, projects2Id));
				project.getCategories().addAll(fetchCategoriesForProject(conn, projects2Id));
			}
			
			commitTransaction(conn);
				return Optional.ofNullable(project);
			}
						
					catch(Exception e) {
						rollbackTransaction(conn);
						throw new DbException2(e);
					}
				}		
	
			catch(SQLException e) {
				throw new DbException2(e);
			}
		}
	
	private List<Category> fetchCategoriesForProject(Connection conn, 
			Integer projectId) {
		String sql = ""
				+ "SELECT c.* FROM " + CATEGORY_TABLE + " c "
				+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
				+ "WHERE projects2_id = ?";
						 
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Category> categories = new LinkedList<>();
				
				while(rs.next()) {
					categories.add(extract(rs, Category.class));
				}
				
				return categories;
			}
		}
		catch(SQLException e) {
			throw new DbException2 (e);
		}		 		
}
	
	private List<Step> fetchStepsForProject(Connection conn, 
			Integer projects2Id) throws SQLException {
		String sql = "SELECT * FROM " + STEP_TABLE + " WHERE projects2_id = ?";
						 
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projects2Id, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Step> steps = new LinkedList<>();
				
				while(rs.next()) {
					steps.add(extract(rs, Step.class));
				}
				
				return steps;
			}
		}
				 		
}
	
	private List<Material> fetchMaterialsForProject(Connection conn, 
			Integer projects2Id) throws SQLException {
		String sql = "SELECT * FROM " + MATERIAL_TABLE + " WHERE projects2_id = ?";
						 
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projects2Id, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Material> materials = new LinkedList<>();
				
				while(rs.next()) {
					materials.add(extract(rs, Material.class));
				}
				
				return materials;
			}
		}
				 		
}

	public boolean modifyProjectDetails(Projects2 project) {
		String sql = "" 
	+ "UPDATE " + PROJECT_TABLE
	+ " SET " 
	+ "projects2_name = ?, "
	+ "estimated_hours = ?, "
	+ "actual_hours = ?, "
	+ "difficulty = ?, "
	+ "notes = ? "
	+ "WHERE projects2_id = ?";
		//System.out.println(sql);
		try(Connection conn = DbConnection2.getConnection()) {
			startTransaction(conn);
			
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, project.getProjects2Name(), String.class);
			setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
			setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
			setParameter(stmt, 4, project.getDifficulty(), Integer.class);
			setParameter(stmt, 5, project.getNotes(), String.class);
			setParameter(stmt, 6, project.getProjects2Id(), Integer.class);
			
			boolean modified = stmt.executeUpdate() == 1;
				commitTransaction(conn);
				
				return modified;
		}
			
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException2(e);
			}
		}
		catch(SQLException e) {
			throw new DbException2 (e);
	}}

	public boolean deleteProject(Integer projects2Id) {
		String sql = "DELETE FROM " + PROJECT_TABLE + " WHERE projects2_id = ?";
		
		try(Connection conn = DbConnection2.getConnection()) {
			startTransaction(conn);
			
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter (stmt, 1, projects2Id, Integer.class);
			
			boolean deleted = stmt.executeUpdate() == 1;
				
			commitTransaction(conn);
			return deleted;
	}
		catch(Exception e) {
			rollbackTransaction(conn);
			throw new DbException2(e);
		}
		}
		
		catch(SQLException e) {
			throw new DbException2 (e);
		}
		}
	}
