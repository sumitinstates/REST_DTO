package com.example.demo;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class EmployeeWrapper {
	
	@SerializedName("employeeList")
	private List<Employee> employeeList;

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	
	

}
