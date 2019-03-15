package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase principal para probar la ejecucion de ProjectService Se recomienda
 * descomentar el codigo de los ejercicios conforme se vayan realizando
 * 
 * @author Paco
 * 
 *         // ?servertimezone = utc gmt+1
 *
 */
public class P2Main {

	public static void main(String[] args) {

		ProjectService service = new ProjectService();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("acmeEmployees");
		EntityManager em = emf.createEntityManager();

		// em.getTransaction().begin();

		/* Comprobar funcionamiento */
		// Employee e = em.find(Employee.class, 222222);
		// e.print();

		// Employee newEmployee = new Employee(1, "Edgar", "Cood", LocalDate.of(1923, 8,
		// 19), LocalDate.now(),
		// Employee.Gender.M);
		// em.persist(newEmployee);
		// e = em.find(Employee.class, 1);
		// e.print();
		// em.remove(e);
		// em.getTransaction().commit();

		/* Ejercicio 2 */

		Department proyDepartment = service.getDepartmentById("d005");
		Manager projectManager = service.promoteToManager(10001, 1000L);

		Project acmeProject = service.createBigDataProject("Persistence Layer", proyDepartment, projectManager,
				new BigDecimal(1500000.99));
		
		service.assignTeam(acmeProject, 10001, 10005);

		int totalHours = service.assignInitialHours(acmeProject.getId());
		
		System.out.println("Total project hours: " + totalHours);
		
		/*
		 * Ejercicio 3. Prueba de consultas
		 */

		if (service.employeeInProject(acmeProject.getId(), "Parto", "Bamford"))
			System.out.println("Parto Bamford assigned to project");
		if (!service.employeeInProject(acmeProject.getId(), "Luke", "Johnson"))
			System.out.println("Luke Johnson is not assigned to project");

		List<Object[]> results = service.getTopHourMonths(acmeProject.getId(), 2019, 3);
		for (Object[] result : results) {
			System.out.println("Month " + result[0] + " Hours " + result[1]);
		}

		List<MonthlyBudget> monthBudgets = service.getMonthlyBudget(acmeProject.getId());
		for (MonthlyBudget budget : monthBudgets) {
			System.out.println(budget.getMonth() + "-" + budget.getYear() + " :  " + budget.getAmount() + " ");
		}

		// Eliminamos la información creada *
		// Manager projectManager = service.em.find(Manager.class, 10001);
		// Project acmeProject = em.find(Project.class, 67);
		// em.getTransaction().begin();
		// em.merge(acmeProject);
		// em.merge(projectManager);
		// em.remove(acmeProject);
		// em.createNativeQuery("Delete from manager where emp_no = " + projectManager.getId()).executeUpdate();
		// em.getTransaction().commit();	

		return;
	}

}
