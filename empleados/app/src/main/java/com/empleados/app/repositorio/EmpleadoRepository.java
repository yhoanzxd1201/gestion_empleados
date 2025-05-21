package com.empleados.app.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empleados.app.entidades.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{

   

}