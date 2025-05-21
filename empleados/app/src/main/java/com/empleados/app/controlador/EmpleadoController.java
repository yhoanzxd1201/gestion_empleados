package com.empleados.app.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empleados.app.entidades.Empleado;
import com.empleados.app.servicio.EmpleadoService;
import com.empleados.app.util.paginacion.PageRender;
import com.empleados.app.util.reportes.EmpleadoExporterExcel;
import com.empleados.app.util.reportes.EmpleadoExporterPDF;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/ver/{id}")
    public String verDetallesDelEmpleado(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
            RedirectAttributes flash) {
        Empleado empleado = empleadoService.findOne(id);
        if (empleado == null) {
            flash.addFlashAttribute("error", "el empleado no existe en la base de datos");
            return "redirect:/listar";
        }
        modelo.put("empleado", empleado);
        modelo.put("titulo", "Detalles del emplead@: " + empleado.getNombre());
        return "ver";
    }

    @GetMapping({ "/", "/listar", "" })
    public String listarEmpleados(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 6);
        Page<Empleado> empleados = empleadoService.findAll(pageRequest);
        PageRender<Empleado> pageRender = new PageRender<>("/listar", empleados);
        modelo.addAttribute("titulo", "Listado de empleados");
        modelo.addAttribute("empleados", empleados);
        modelo.addAttribute("page", pageRender);

        return "listar";
    }

    @GetMapping("/form")
    public String mostrarFormularioDeRegistrarEmpleado(Map<String, Object> modelo) {
        Empleado empleado = new Empleado();
        modelo.put("empleado", empleado);
        modelo.put("titulo", "Registro de Empleados");
        return "/form";
    }

    @PostMapping("/form")
    public String guardarEmpleado(@Valid Empleado empleado, BindingResult result, Model modelo,
            RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de empleado");
            return "form";
        }
        String mensaje = (empleado.getId() != null) ? "El empleado ha sido editado con éxito"
                : "Empleado registrado con éxito";
        empleadoService.save(empleado);
        status.setComplete();
        flash.addFlashAttribute("success", mensaje);
        return "redirect:/listar";
    }

    @GetMapping("/form/{id}")
    public String editarEmpleado(@PathVariable(value = "id") Long id, Map<String, Object> modelo,
            RedirectAttributes flash) {
        Empleado empleado = null;
        if (id > 0) {
            empleado = empleadoService.findOne(id);
            if (empleado == null) {
                flash.addFlashAttribute("error", "El id del empleado no existe en la base de datos");
                return "redirect :/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El id del empleado no puede ser 0");
            return "redirect :/listar";
        }
        modelo.put("empleado", empleado);
        modelo.put("titulo", "Edicion de empleado");
        return "form";
    }

    @GetMapping("/eliminar/{id}")
    public String getMethodName(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            empleadoService.delete(id);
            flash.addFlashAttribute("success", "Empleado eliminado con éxito");
        }

        return "redirect:/listar";
    }

    @GetMapping("/exportarPDF")
    public String exportarListaDeEmpleadosEnPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; Filename=Empleados_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);
        List<Empleado> empleados = empleadoService.findAll();
        EmpleadoExporterPDF exporter = new EmpleadoExporterPDF(empleados);
        exporter.exportar(response);
        return null;

    }

    @GetMapping("/exportarExcel")
    public String exportarListaDeEmpleadosEnExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; Filename=Empleados_" + fechaActual + ".xlsx";

        response.setHeader(cabecera, valor);

        List<Empleado> empleados = empleadoService.findAll();

        EmpleadoExporterExcel exporter = new EmpleadoExporterExcel(empleados);
        exporter.exportar(response);

        return null;

    }

}
