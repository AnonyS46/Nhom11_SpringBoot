package com.haui.main.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.haui.main.Entity.Employee;
import com.haui.main.Model.EmployeeReport;

public interface EmployeeDao extends JpaRepository<Employee, Integer> {
	@Query("SELECT new EmployeeReport(e.user, e.position, e.id, e.startDay) FROM Employee e WHERE e.user.fullname LIKE ?1")
	Page<EmployeeReport> fillToTable(String fullname, Pageable pageable);
}
