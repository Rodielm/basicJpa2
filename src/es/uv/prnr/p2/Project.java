package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

//TODO JPQL de Ejercicio3 employeeInProject 
@NamedQuery(name = "Project.findEmployee", query = "select e.id from Employee e "
		+ " inner join e.assignedTo aT "
		+ " where e.firstName like :name and e.last_name like :last_name and aT.id = :project_id")

// TODO JPQL de Ejercicio3 getTopHoursMonth
@NamedQuery(name = "Project.getTopMonths", query = "select ph.month, max(ph.month) as top from ProjectHours ph "
+ "group by ph.month")

// TODO Consulta SQL para getMonthly Budget. Se recomienda encarecidamente
// testearla con Workbench
// antes de incluirla aqu�
// @NamedNativeQuery(name = "Project.getMonthlyBudget", query = "",
// resultSetMapping = "MonthBudgetMapping")

// TODO Mapeo del ResultSet para la consulta anterior
/*
 * @SqlResultSetMapping( name="MonthBudgetMapping", classes = {
 * 
 * @ConstructorResult( targetClass=, columns= { } ) } )
 */

@Entity
@Table(name = "project")
public class Project {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name", unique = true)
	private String name;

	// Relacion * a 1 con Department
	@ManyToOne
	@JoinColumn(name = "fk_department")
	private Department department;

	@Column(name = "budget")
	private BigDecimal budget;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "area")
	private String area;

	// Relacion * a 1 con Project
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_manager")
	private Manager manager;

	// TODO relacion * a * utilizando una tabla intermedia
	@ManyToMany
	@JoinTable(name = "project_team", joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "emp_no"))
	private Set<Employee> team = new HashSet<Employee>(0);

	// TODO Relacion 1 a * con la clase ProjectHours
	// @OneToMany(fetch = FetchType.LAZY)
	// @JoinColumn(name = "fk_employee")
	// lazy persist
	// @OneToMany(mappedBy="project")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_project")
	private List<ProjectHours> hours = new ArrayList<ProjectHours>();

	public Project() {
	}

	public Project(String name, Department department, Manager manager, BigDecimal budget, LocalDate startDate,
			LocalDate endDate, String area) {
		this.name = name;
		this.department = department;
		this.manager = manager;
		this.budget = budget;
		this.startDate = startDate;
		this.endDate = endDate;
		this.area = area;
	}

	/**
	 * Relaciona el proyecto con el empleado e
	 * 
	 * @param e
	 */
	public void addEmployee(Employee e) {
		// Codigo para relacionar el empleado con el proyecto
		this.team.add(e);
	}

	/**
	 * Añade un numero de horas al empleado e para un mes-año concreto
	 * 
	 * @param e
	 * @param month
	 * @param year
	 * @param hours
	 */
	public void addHours(Employee e, int month, int year, int hours) {
		// Codigo añadir las horas del empleado
		this.hours.add(new ProjectHours(month, year, hours, e, this));
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department Department) {
		this.department = Department;
	}

	public BigDecimal getBudget() {
		return this.budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Manager getManager() {
		return manager;
	}

	public Set<Employee> getEmployees() {
		return this.team;
	}

	public List<ProjectHours> getHours() {
		return this.hours;
	}

	public void print() {
		System.out.println("Project " + this.name + " from department " + this.department.getDeptName());
		System.out.print("Managed by ");
		this.manager.print();
		System.out.println("Project Team");
	}

}