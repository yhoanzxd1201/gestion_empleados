package com.empleados.app.util.reportes;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.empleados.app.entidades.Empleado;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class EmpleadoExporterPDF {

    private List<Empleado> listaEmpleados;

    public EmpleadoExporterPDF(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(Color.BLUE);
        celda.setPadding(5);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.white);

        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Nombre", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Apellido", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Email", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Fecha", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Telefono", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Sexo", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Salario", fuente));
        tabla.addCell(celda);
    }

    public void escribirDatosDeLaTabla(PdfPTable tabla) {

        for (Empleado empleado : listaEmpleados) {
            tabla.addCell(String.valueOf(empleado.getId()));
            tabla.addCell(empleado.getNombre());
            tabla.addCell(empleado.getApellido());
            tabla.addCell(empleado.getEmail());
            tabla.addCell(empleado.getFecha().toString());
            tabla.addCell(String.valueOf(empleado.getTelefono()));
            tabla.addCell(empleado.getSexo());
            tabla.addCell(String.valueOf(empleado.getSalario()));

        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.BLUE);
        fuente.setSize(18);
        Paragraph titulo = new Paragraph("Lista de Empleados", fuente);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[] { 1f, 2.3f, 2.3f, 6f, 2.9f, 3.5f, 2f, 2.2f });
        tabla.setWidthPercentage(110);

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);

        documento.add(tabla);
        documento.close();
    }

}
