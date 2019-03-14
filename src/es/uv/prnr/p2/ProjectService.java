package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;

import javax.persistence.*;

public class ProjectService {
	EntityManagerFactory emf;
	EntityManager em;

	public ProjectService() {
		this.emf = Persistence.createEntityManagerFactory("acmeEmployees");
		this.em = emf.createEntityManager();
	}

	/**
	 * Busca un departamento
	 * 
	 * @param id identificador del departamento
	 * @return entidad con el deparamenteo encontrado
	 */
	public Department getDepartmentById(String id) {
		return em.find(Department.class, id);
	}

	/**
	 * Asciende a un empleado a manager. Utilizar una estrategía de herencia
	 * adecuada en employee. Tened en cuenta que NO puede haber dos entidades con el
	 * mismo id por lo que habrá que eliminar el empleado original en algun momento.
	 * 
	 * @param employeeId
	 * @param bonus
	 * @return
	 */

	public Manager promoteToManager(int employeeId, long bonus) {
		Employee emp = em.find(Employee.class, employeeId);
		Manager manager = new Manager(emp, bonus);
		em.getTransaction().begin();
		em.remove(emp);
		em.persist(manager);
		em.getTransaction().commit();

		return manager;
	}

	/**
	 * Crea un nuevo proyecto en el area de Big Data que comienza en la fecha actual
	 * y que finaliza en 3 años.
	 * 
	 * @param name
	 * @param d      departamento asignado al proyecto
	 * @param m      manager que asignado al proyecto
	 * @param budget
	 * @return el proyecto creado
	 */
	public Project createBigDataProject(String name, Department d, Manager m, BigDecimal budget) {
		em.getTransaction().begin();
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.of(2022, Month.JANUARY, 1);
		Project pro = new Project(name, d, m, budget, startDate, endDate, "Area1");
		em.persist(pro);
		em.getTransaction().commit();
		return pro;
	}

	/**
	 * Crea un equipo de proyecto. Se debera implementa el metodo addEmployee de
	 * Project para incluir los empleados
	 * 
	 * @param p       proyecto al cual asignar el equipo
	 * @param startId identificador a partir del cual se asignan empleado
	 * @param endId   identificador final de empleados. Se asume que start id <
	 *                endId
	 */
	public void assignTeam(Project p, int startId, int endId) {
		em.getTransaction().begin();
		for (int i = startId; i <= endId; i++) {
			Employee e = new Employee();
			e = em.find(Employee.class, i);
			p.addEmployee(e);
		}
		em.merge(p);
		em.getTransaction().commit();
	}

	/**
	 * Genera un conjunto de horas inicial para cada empleado. El metodo asigna para
	 * cada mes de duracion del proyecto, un numero entre 10-165 de horas a cada
	 * empleado.
	 * 
	 * @param projectId
	 * @return total de horas generadas para el proyecto
	 */
	public int assignInitialHours(int projectId) {
		em.getTransaction().begin();
		int totalHours = 0;
		// Buscar proyecto
		Project p = em.find(Project.class, projectId);
		LocalDate start = p.getStartDate();
		while (start.isBefore(p.getEndDate())) {
			for (Employee e : p.getEmployees()) {
				int hours = new Random().nextInt(165) + 10;
				totalHours += hours;
				// Agregar las horas del empleado al proyecto
				p.addHours(e, start.getMonthValue(), start.getYear(), hours);
			}
			start = start.plusMonths(1);
		}
		// guardar resultados
		em.persist(p);
		em.getTransaction().commit();
		return totalHours;
	}

	/**
	 * Busca si un empleado se encuentra asignado en el proyecto utilizando la
	 * namedQuery Project.findEmployee
	 * 
	 * @param projectId
	 * @param firstName
	 * @param lastName
	 * @return cierto si se encuentra asignado al proyecto
	 */
	public boolean employeeInProject(int projectId, String firstName, String lastName) {
		Query q = em.createNamedQuery("Project.findEmployee", Integer.class).setParameter("name", firstName)
				.setParameter("last_name", lastName).setParameter("project_id", projectId);
		List<Employee> emp = q.getResultList();
		return !emp.isEmpty();
	}

	/**
	 * Devuelve los meses con mayor número de horas de un año determinado
	 * utilizando la namedQuery Project.getTopMonths
	 * 
	 * @param projectId
	 * @param year      año a seleccionar
	 * @param rank      nñmero de meses a mostrar, se asume que rank <= 12
	 * @return una lista de objetos mes,hora ordenados de mayor a menor
	 */
	public List getTopHourMonths(int projectId, int year, int rank) {
		return em.createNamedQuery("Project.getTopMonths").setParameter("idProject", projectId)
				.setParameter("year", year).setMaxResults(rank).getResultList();
	}

	/**
	 * Devuelve para cada par mes-año el presupuesto teniendo en cuenta el
	 * coste/hora de los empleados asociado utilizando la namedQuery
	 * Project.getMonthlyBudget que realiza una consulta nativa
	 * 
	 * @param projectId
	 * @return una coleccion de objetos MonthlyBudget
	 */
	public List<MonthlyBudget> getMonthlyBudget(int projectId) {
		return em.createNamedQuery("Project.getMonthlyBudget", MonthlyBudget.class).setParameter("projectId", projectId)
				.getResultList();
	}

}
