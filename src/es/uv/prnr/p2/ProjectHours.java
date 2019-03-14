package es.uv.prnr.p2;

import javax.persistence.*;

@Entity
@Table(name = "monthly_hours")
public class ProjectHours {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "month", columnDefinition = "UNSIGNED INT(2)")
	int month;

	@Column(name = "year", columnDefinition = "UNSIGNED INT(4)")
	int year;

	@Column(name = "hours", columnDefinition = "UNSIGNED INT(3)")
	int hours;

	// Relacion * a 1 con Employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_employee")
	Employee employee;

	// Relacion * a 1 con Project
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_project")
	Project project;

	public ProjectHours() {

	}

	public ProjectHours(int month, int year, int hours, Employee employee, Project project) {
		this.month = month;
		this.year = year;
		this.hours = hours;
		this.employee = employee;
		this.project = project;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public Employee getEmployee() {
		return employee;
	}

	public Project getProject() {
		return project;
	}

}
