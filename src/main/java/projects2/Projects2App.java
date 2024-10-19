package projects2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects2.entity.Projects2;
import projects2.exception.DbException2;
import projects2.service.ProjectService;

//This class is a menu-driven application 
//that allows users to make a choice and perform an operation on a table.

public class Projects2App {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Projects2 curProject;
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
);
	// @formatter:on

	// Java application entry point
	public static void main(String[] args) {
		new Projects2App().processUserSelections();
	}
	
	//This prints the operations, prompts for a user menu selection
	//and does what the user inputs. It keeps going until the user quits.
	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;
					
				case 2:
					listProjects2();
					break;
					
				case 3:
					selectProjects2();
					break;
					
				case 4:
					updateProjects2Details();
					break;
					
				case 5:
					deleteProjects2();
					break;

				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			} 
			catch (Exception e) {
				System.out.println("\nError: " + e + "Try again");
			}
		}
	}
	private void deleteProjects2() {
		listProjects2();
		Integer projects2Id = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProjects2(projects2Id);
		System.out.println("Project " + projects2Id + " was deleted successfully.");
		
		if(Objects.nonNull(curProject) && curProject.getProjects2Id().equals(projects2Id)) {
		      curProject = null;
		    }
	}


	private void updateProjects2Details() {
		if (Objects.isNull(curProject)) {
			System.out.println( "\nPlease select a project.");
			return;}
		String projects2Name = getStringInput("Enter the project name [" + curProject.getProjects2Name() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
	
		Projects2 project = new Projects2();
		
		project.setProjects2Id(curProject.getProjects2Id());
		project.setProjects2Name(Objects.isNull(projects2Name) ? curProject.getProjects2Name() : projects2Name);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		curProject = projectService.fetchProjectById(curProject.getProjects2Id());
	}
	

	// This allows user to select a current project
	private void selectProjects2() {
		listProjects2();
		Integer projects2Id = getIntInput("Enter a project ID to select a project");
		
		//Unselects the current project if an exception is thrown
		curProject = null;
		
		//This throws an exception if the project ID is invalid
		curProject = projectService.fetchProjectById(projects2Id);
		
	}
	
	//This allows project service to get a list of projects from the project table 
	private void listProjects2() {
		List<Projects2> projects2 = projectService.fetchAllProjects();
		System.out.println("\nProjects:");
		projects2.forEach(project -> System.out.println("   " + project.getProjects2Id() + ":  " + project.getProjects2Name()));
	}

	//Get more user input here
	private void createProject() {
	String projects2Name = getStringInput("Enter the project name");
	BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
	BigDecimal actualHours = getDecimalInput("Enter the actual hours");
	Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
	String notes = getStringInput("Enter the project notes");
	
	Projects2 project = new Projects2();
	
	project.setProjects2Name(projects2Name);
	project.setEstimatedHours(estimatedHours);
	project.setActualHours(actualHours);
	project.setDifficulty(difficulty);
	project.setNotes(notes);
	
	Projects2 dbProjects2 = projectService.addProject(project);
	System.out.println("You have successfully created project: " + dbProjects2);
	
	}
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
	try {
		return new BigDecimal(input).setScale(2);
	}
	catch(NumberFormatException e) {
		throw new DbException2(input + " is not a valid decimal number.");
	}
	
	}
	
	//This allows the user to quit and prints an exit message.
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	//Next, the available menu selections print, 
	//takes user input and converts to an int.
	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException2(input + " is not a valid number.");
		}
	}
	
	//Prints a prompt and then takes user's input, if no input it returns null
	private String getStringInput(String prompt) {
		System.out.print(prompt + ":");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

	//Prints menu selections
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		// Lambda expression
		operations.forEach(line -> System.out.println(" " + line));

		if(Objects.isNull(curProject)){
	System.out.println("\nYou are not working with a project.");
		}
	else {
	System.out.println("\nYou are working with project: " + curProject);	
			
	}
}
}
