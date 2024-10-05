package projects2.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import projects2.entity.Project;
import projects2.exception.DbException2;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase {
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "projects2";
	private static final String PROJECT_CATEGORY_TABLE = "projects2_category";
	private static final String STEP_TABLE = "step";

	public Project insertProject(Project project) {
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
		setParameter(stmt, 1, project.getProjectName(), String.class);
		setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
		setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
		setParameter(stmt, 4, project.getDifficulty(), Integer.class);
		setParameter(stmt, 5, project.getNotes(), String.class);
		
		stmt.executeUpdate();
		
		Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
		commitTransaction(conn);
		
		project.setProjectId(projectId);;
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
}}
