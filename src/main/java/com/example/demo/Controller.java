package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping("/getAllEmployee")
	public List<Employee> getAllEmployee(){
		List<Employee> allEmployee = employeeRepository.findAll();
		return allEmployee;	
	}
	
	//Using REST DTO
	@RequestMapping( value = "/saveEmployee", method = RequestMethod.POST, produces = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<List<EmployeeDto>> save(@RequestBody List<Employee> e) {
	    
		 List<EmployeeDto> el = new ArrayList<EmployeeDto>();
	
		  for (int i=0;i<e.size();i++) 
		  {
			  Employee employee = e.get(i);
			
			  boolean existsById = employeeRepository.existsById(employee.getId());
			  if(!existsById) 
			  {
			      Employee findByFirstNameAndLastName = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
			      
			      if(findByFirstNameAndLastName==null) 
			      {
			    	   employeeRepository.save(employee);
			           EmployeeDto employeeDto = modelMapper.map(employee, com.example.demo.EmployeeDto.class);
			           el.add(employeeDto);  
			      }
			  }
		  }
		  
		  return new ResponseEntity<List<EmployeeDto>>(el, HttpStatus.OK);         
	}
	
	//Using Wrapper class
	@RequestMapping( value = "/save", method = RequestMethod.POST, produces = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EmployeeWrapper> saveEmployee(@RequestBody EmployeeWrapper ew)
	{
		EmployeeWrapper ewObj = new EmployeeWrapper();
		List<Employee> el = new ArrayList<Employee>();
		
		System.out.println("Above employee list");
		List<Employee> employeeList = ew.getEmployeeList();
		System.out.println("Below employee list");
		
		for(int i=0; i<employeeList.size();i++) 
		{
			System.out.println("INSIDE FOR LOOP");
			Employee employee = employeeList.get(i);
			Long id = employee.getId();
			String firstName = employee.getFirstName();
			String lastName = employee.getLastName();
			
			boolean existsById = employeeRepository.existsById(id);
			
			if(!existsById) 
			{
				Employee findByFirstNameAndLastName = employeeRepository.findByFirstNameAndLastName(firstName, lastName);
				
				if(findByFirstNameAndLastName == null) 
				{
				    Employee save = employeeRepository.save(employee);
				    el.add(save);	
				}
				ewObj.setEmployeeList(el);
			}
		}
		return new ResponseEntity<EmployeeWrapper>(ewObj, HttpStatus.CREATED);
		
	}

	
}
