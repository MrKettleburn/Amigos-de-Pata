package ReportesPDF

import Class_DB.ContratoDB.getContratosVeterinariosFilter
import Models.ContratoVeterinario
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun createPdfContratosVeterinarios(destination: String, contratos: List<ContratoVeterinario>, fechaYhora: LocalDateTime) {
    val writer = PdfWriter(destination)
    val pdf = PdfDocument(writer)
    val document = Document(pdf)

    // Encabezado del refugio
    val title = Paragraph("Refugio Amigos de Pata")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(20f)
        .setBold()
    document.add(title)

    // Nombre del reporte
    val reportTitle = Paragraph("Reporte de Contratos Veterinarios")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(16f)
        .setMarginTop(10f)
    document.add(reportTitle)

    val fechaYhoraParagraph = Paragraph()
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(0f)
        .setMarginTop(0f)
        .setFontSize(12f)
    fechaYhoraParagraph.add(Paragraph("Fecha: ").setBold().setMargin(0f))
    fechaYhoraParagraph.add(Paragraph(fechaYhora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
    fechaYhoraParagraph.add(Paragraph("    "))
    fechaYhoraParagraph.add(Paragraph("Hora: ").setBold().setMargin(0f))
    fechaYhoraParagraph.add(Paragraph(fechaYhora.format(DateTimeFormatter.ofPattern("HH:mm"))).setMargin(0f))
    document.add(fechaYhoraParagraph)


    // Espacio entre el título y la tabla
    document.add(Paragraph("\n"))

    contratos.forEach { contrato ->
        document.add(Paragraph("Codigo: ${contrato.codigo}")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setMarginTop(0f)
            .setMarginBottom(0f))

        val veterinarioParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        veterinarioParagraph.add(Paragraph("Veterinario: ").setBold().setMargin(0f))
        veterinarioParagraph.add(Paragraph(contrato.nombreVet).setMargin(0f))
        document.add(veterinarioParagraph)

        val clinicaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        clinicaParagraph.add(Paragraph("Clínica: ").setBold().setMargin(0f))
        clinicaParagraph.add(Paragraph(contrato.clinicaVet).setMargin(0f))
        document.add(clinicaParagraph)

        val provinciaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        provinciaParagraph.add(Paragraph("Provincia: ").setBold().setMargin(0f))
        provinciaParagraph.add(Paragraph(contrato.provinciaVet).setMargin(0f))
        document.add(provinciaParagraph)

        val direccionParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        direccionParagraph.add(Paragraph("Dirección: ").setBold().setMargin(0f))
        direccionParagraph.add(Paragraph(contrato.direccVet).setMargin(0f))
        document.add(direccionParagraph)

        val especialidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        especialidadParagraph.add(Paragraph("Especialidad: ").setBold().setMargin(0f))
        especialidadParagraph.add(Paragraph(contrato.especialidad).setMargin(0f))
        document.add(especialidadParagraph)

        val modalidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        modalidadParagraph.add(Paragraph("Modalidad del Servicio: ").setBold().setMargin(0f))
        modalidadParagraph.add(Paragraph(contrato.modalidadServVet).setMargin(0f))
        document.add(modalidadParagraph)

        val fechaInicio = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fechaInicio.add(Paragraph("Fecha de Inicio: ").setBold().setMargin(0f))
        fechaInicio.add(Paragraph(contrato.fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fechaInicio)

        val fechaFin = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fechaFin.add(Paragraph("Fecha de Fin: ").setBold().setMargin(0f))
        fechaFin.add(Paragraph(contrato.fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fechaFin)

        val fechaConcil = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fechaConcil.add(Paragraph("Fecha de Conciliación: ").setBold().setMargin(0f))
        fechaConcil.add(Paragraph(contrato.fechaConcil.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fechaConcil)

        val descripParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        descripParagraph.add(Paragraph("Descripción: ").setBold().setMargin(0f))
        descripParagraph.add(Paragraph(contrato.descripcion).setMargin(0f))
        document.add(descripParagraph)

        document.add(Paragraph("\n"))
    }

    // Cerrar el documento
    document.close()
}

suspend fun generarReporteContratosVeterinarios(destination: String, fechaYhora: LocalDateTime) {
    // Obtener los contratos desde la base de datos
    val contratos = getContratosVeterinariosFilter(
        codigo = null,
        precioLI = null,
        precioLS = null,
        nombreVet = null,
        clinicaVet = null,
        provinciaVet = null,
        especialidad = null,
        modalidadServVet = null,
        fechaInicioLI = null,
        fechaInicioLS = null,
        fechaFinLI = null,
        fechaFinLS = null,
        fechaConcilLI = null,
        fechaConcilLS = null
    )

    // Crear el PDF con los contratos obtenidos
    createPdfContratosVeterinarios(destination, contratos, fechaYhora)
}