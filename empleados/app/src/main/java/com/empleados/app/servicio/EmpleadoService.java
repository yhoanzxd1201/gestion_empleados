package com.empleados.app.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.empleados.app.entidades.Empleado;

public interface EmpleadoService {

    public List<Empleado> findAll();

    public Page<Empleado> findAll(Pageable pageable);

    public void save(Empleado epleado);

    public Empleado findOne(Long id);

    public void delete(Long id);

}
