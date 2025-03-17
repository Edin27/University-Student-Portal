package model;

public class Course {
	private String courseCode;
	private String name;
	private String description;
	private boolean requiresComputers;
	private String courseOrganiserName;
	private String couresOrganiserEmail;
	private String courseSecretaryName;
	private String courseSecretaryEmail;
	private int requiredTutorials;
	private int requiredLabs;

	public Course(String code, String name, String description,
				  boolean requiresComputers, String COName, String COEmail,
				  String CSName, String CSEmail, int reqTutorials, int reqLabs){
		this.courseCode = code;
		this.name = name;
		this.description = description;
		this.requiresComputers = requiresComputers;
		this.courseOrganiserName = COName;
		this.couresOrganiserEmail = COEmail;
		this.courseSecretaryName = CSName;
		this.courseSecretaryEmail = CSEmail;
		this.requiredTutorials = reqTutorials;
		this.requiredLabs = reqLabs;

	}

	public boolean hasCode(String code){
		if (code.equals(courseCode)){
			return true;
		}
		else{
			return false;
		}
	}

}
