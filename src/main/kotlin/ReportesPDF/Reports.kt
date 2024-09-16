package ReportesPDF

import Class_DB.ContratoDB.getContratosVeterinariosFilter
import Models.*
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

fun createPdfContratosTransporte(destination: String, contratos: List<ContratoTransporte>, fechaYhora: LocalDateTime) {
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
    val reportTitle = Paragraph("Reporte de Contratos de Transporte")
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
        veterinarioParagraph.add(Paragraph("Transportista: ").setBold().setMargin(0f))
        veterinarioParagraph.add(Paragraph(contrato.nombreTrans).setMargin(0f))
        document.add(veterinarioParagraph)

        val provinciaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        provinciaParagraph.add(Paragraph("Provincia: ").setBold().setMargin(0f))
        provinciaParagraph.add(Paragraph(contrato.provinciaTrans).setMargin(0f))
        document.add(provinciaParagraph)

        val direccionParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        direccionParagraph.add(Paragraph("Dirección: ").setBold().setMargin(0f))
        direccionParagraph.add(Paragraph(contrato.direccionTrans).setMargin(0f))
        document.add(direccionParagraph)

        val modalidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        modalidadParagraph.add(Paragraph("Vehículo: ").setBold().setMargin(0f))
        modalidadParagraph.add(Paragraph(contrato.vehiculo).setMargin(0f))
        document.add(modalidadParagraph)

        val precioUnitario = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        precioUnitario.add(Paragraph("Precio Unitario del Servicio(km): ").setBold().setMargin(0f))
        precioUnitario.add(Paragraph("${contrato.precioUnit}").setMargin(0f))
        document.add(precioUnitario)

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

fun createPdfContratosProveedoresAlim(destination: String, contratos: List<ContratoProveedorAlim>, fechaYhora: LocalDateTime) {
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
    val reportTitle = Paragraph("Reporte de Contratos con Proveedores de Alimentos")
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
        veterinarioParagraph.add(Paragraph("Transportista: ").setBold().setMargin(0f))
        veterinarioParagraph.add(Paragraph(contrato.nombreProv).setMargin(0f))
        document.add(veterinarioParagraph)

        val provinciaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        provinciaParagraph.add(Paragraph("Provincia: ").setBold().setMargin(0f))
        provinciaParagraph.add(Paragraph(contrato.provinciaProv).setMargin(0f))
        document.add(provinciaParagraph)

        val direccionParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        direccionParagraph.add(Paragraph("Dirección: ").setBold().setMargin(0f))
        direccionParagraph.add(Paragraph(contrato.direccProv).setMargin(0f))
        document.add(direccionParagraph)

        val modalidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        modalidadParagraph.add(Paragraph("Tipo de Alimento: ").setBold().setMargin(0f))
        modalidadParagraph.add(Paragraph(contrato.tipoAlim).setMargin(0f))
        document.add(modalidadParagraph)

        val precioUnitario = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        precioUnitario.add(Paragraph("Precio Unitario del Servicio(kg): ").setBold().setMargin(0f))
        precioUnitario.add(Paragraph("${contrato.precioUnit}").setMargin(0f))
        document.add(precioUnitario)

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

fun createPdfActividadesDeUnAnimal(destination:String, animal: Animal, actividades: List<ActividadReporte>, mantenimientoSeisMeses: Double,fechaYhora: LocalDateTime)
{
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
    val reportTitle = Paragraph("Reporte de Actividades de un Animal")
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

    document.add(Paragraph("\n"))
    document.add(Paragraph("Datos Animal")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("Codigo: ${animal.codigo}")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("Estimado de Mantenimiento en 6 meses: ${mantenimientoSeisMeses}")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    val nombreParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    nombreParagraph.add(Paragraph("Nombre: ").setBold().setMargin(0f))
    nombreParagraph.add(Paragraph(animal.nombre).setMargin(0f))
    document.add(nombreParagraph)

    val especieParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    especieParagraph.add(Paragraph("Especie: ").setBold().setMargin(0f))
    especieParagraph.add(Paragraph(animal.especie).setMargin(0f))
    document.add(especieParagraph)

    val razaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    razaParagraph.add(Paragraph("Raza: ").setBold().setMargin(0f))
    razaParagraph.add(Paragraph(animal.raza).setMargin(0f))
    document.add(razaParagraph)

    val edadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    edadParagraph.add(Paragraph("Edad: ").setBold().setMargin(0f))
    edadParagraph.add(Paragraph("${animal.edad}").setMargin(0f))
    document.add(edadParagraph)

    val pesoParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    pesoParagraph.add(Paragraph("Peso(kg): ").setBold().setMargin(0f))
    pesoParagraph.add(Paragraph("${animal.peso}").setMargin(0f))
    document.add(pesoParagraph)

    val fechaInicio = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    fechaInicio.add(Paragraph("Fecha de Ingreso: ").setBold().setMargin(0f))
    fechaInicio.add(Paragraph(animal.fecha_ingreso.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
    document.add(fechaInicio)

    val cantDiasParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
    cantDiasParagraph.add(Paragraph("Días en el refugio: ").setBold().setMargin(0f))
    cantDiasParagraph.add(Paragraph("${ChronoUnit.DAYS.between(animal.fecha_ingreso, LocalDate.now()).toInt()}").setMargin(0f))
    document.add(cantDiasParagraph)

    document.add(Paragraph("\n"))

    document.add(Paragraph("Actividades")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    actividades.forEach { act ->

        val fecha = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fecha.add(Paragraph("Fecha: ").setBold().setMargin(0f))
        fecha.add(Paragraph(act.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fecha)

        val hora = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        hora.add(Paragraph("Hora: ").setBold().setMargin(0f))
        hora.add(Paragraph(act.hora.format(DateTimeFormatter.ofPattern("HH:mm"))).setMargin(0f))
        document.add(hora)

        val descrip = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        descrip.add(Paragraph("Descripción: ").setBold().setMargin(0f))
        descrip.add(Paragraph(act.descrip).setMargin(0f))
        document.add(descrip)

        val precio = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        precio.add(Paragraph("Precio: ").setBold().setMargin(0f))
        precio.add(Paragraph("${act.costo}").setMargin(0f))
        document.add(precio)

        val tipo = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        tipo.add(Paragraph("Tipo: ").setBold().setMargin(0f))
        tipo.add(Paragraph(act.tipo).setMargin(0f))
        document.add(tipo)

        if(act.tipo=="Atención Médica") {
            val veterinario = Paragraph().setMarginTop(0f).setMarginBottom(0f)
            veterinario.add(Paragraph("Veterinario: ").setBold().setMargin(0f))
            veterinario.add(Paragraph(act.detalles).setMargin(0f))
            document.add(veterinario)
        }
        else if(act.tipo=="Socialización y Entrenamiento"){
            val transportista = Paragraph().setMarginTop(0f).setMarginBottom(0f)
            transportista.add(Paragraph("Transportista: ").setBold().setMargin(0f))
            transportista.add(Paragraph(act.detalles).setMargin(0f))
            document.add(transportista)
        }
        else if(act.tipo=="Alimentación"){
            val alimento = Paragraph().setMarginTop(0f).setMarginBottom(0f)
            alimento.add(Paragraph("Tipo de Alimento: ").setBold().setMargin(0f))
            alimento.add(Paragraph(act.detalles).setMargin(0f))
            document.add(alimento)
        }

        document.add(Paragraph("\n"))
    }

    document.close()
}

fun createPdfPlanDeIngresos(destination: String,animalesAdoptados: List<AnimalAdoptado>,montoTotalPorAdopciones:Double,donaciones: List<Donacion>,montoTotalPorDonaciones:Double, fechaYhora: LocalDateTime)
{
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
    val reportTitle = Paragraph("Plan de Ingresos por Adopciones y Donaciones")
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

    document.add(Paragraph("\n"))

    document.add(Paragraph("Adopciones")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("Monto Total: ${montoTotalPorAdopciones}")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("\n"))

    val tableAdop = Table(UnitValue.createPercentArray(floatArrayOf(20f, 20f, 20f, 20f, 20f)))
        .useAllAvailableWidth()

    tableAdop.addHeaderCell(Cell().add(Paragraph("Nombre")).setBackgroundColor(ColorConstants.LIGHT_GRAY))
    tableAdop.addHeaderCell(Cell().add(Paragraph("Especie")).setBackgroundColor(ColorConstants.LIGHT_GRAY))
    tableAdop.addHeaderCell(Cell().add(Paragraph("Raza")).setBackgroundColor(ColorConstants.LIGHT_GRAY))
    tableAdop.addHeaderCell(Cell().add(Paragraph("Edad")).setBackgroundColor(ColorConstants.LIGHT_GRAY))
    tableAdop.addHeaderCell(Cell().add(Paragraph("Monto")).setBackgroundColor(ColorConstants.LIGHT_GRAY))

    animalesAdoptados.forEach { animal ->
        tableAdop.addCell(Cell().add(Paragraph(animal.nombre)).setHeight(20f))
        tableAdop.addCell(Cell().add(Paragraph(animal.especie)).setHeight(20f))
        tableAdop.addCell(Cell().add(Paragraph(animal.raza)).setHeight(20f))
        tableAdop.addCell(Cell().add(Paragraph(animal.edad.toString())).setHeight(20f))
        tableAdop.addCell(Cell().add(Paragraph(animal.precioAdop.toString())).setHeight(20f))
    }
    document.add(tableAdop)
    document.add(Paragraph("\n"))

    document.add(Paragraph("Donaciones")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("Monto Total: ${montoTotalPorDonaciones}")
        .setTextAlignment(TextAlignment.CENTER)
        .setBold()
        .setMarginTop(0f)
        .setMarginBottom(0f))

    document.add(Paragraph("\n"))

    val tableDon = Table(UnitValue.createPercentArray(floatArrayOf(70f, 30f)))
        .useAllAvailableWidth()

    tableDon.addHeaderCell(Cell().add(Paragraph("Nombre del donante")).setBackgroundColor(ColorConstants.LIGHT_GRAY))
    tableDon.addHeaderCell(Cell().add(Paragraph("Monto")).setBackgroundColor(ColorConstants.LIGHT_GRAY))

    donaciones.forEach { d ->
        tableDon.addCell(Cell().add(Paragraph(d.nombreAdopt)).setHeight(20f))
        tableDon.addCell(Cell().add(Paragraph(d.monto.toString())).setHeight(20f))
    }

    document.add(tableDon)
    document.close()
}
fun createPdfVeterinariosActivos(destination: String, clinica: String?, provincia:String?, veterinarios: List<ReporteVeterinariosActivosObj>, fechaYhora: LocalDateTime)
{
    var clinicaText = "Todas"
    var provinciaText = "Todas"

    if(clinica!=null)  {clinicaText=clinica}
    if(provincia!=null) {provinciaText=provincia}

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
    val reportTitle = Paragraph("Veterinarios Activos")
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

    val clinicaYprovincia = Paragraph("Clínica: ${clinicaText}     Provincia: ${provinciaText}")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(14f)
        .setMarginTop(10f)
    document.add(clinicaYprovincia)

    document.add(Paragraph("\n"))

    veterinarios.forEach { vet ->
        document.add(Paragraph("Codigo: ${vet.idVet}")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setMarginTop(0f)
            .setMarginBottom(0f))

        val fechaInicParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fechaInicParagraph.add(Paragraph("Fecha Inicio: ").setBold().setMargin(0f))
        fechaInicParagraph.add(Paragraph(vet.fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fechaInicParagraph)

        val fechaFinParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        fechaFinParagraph.add(Paragraph("Fecha Fin: ").setBold().setMargin(0f))
        fechaFinParagraph.add(Paragraph(vet.fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setMargin(0f))
        document.add(fechaFinParagraph)

        val clinicaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        clinicaParagraph.add(Paragraph("Clínica: ").setBold().setMargin(0f))
        clinicaParagraph.add(Paragraph(vet.clinica).setMargin(0f))
        document.add(clinicaParagraph)

        val provinciaParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        provinciaParagraph.add(Paragraph("Provincia: ").setBold().setMargin(0f))
        provinciaParagraph.add(Paragraph(vet.provincia).setMargin(0f))
        document.add(provinciaParagraph)

        val especialidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        especialidadParagraph.add(Paragraph("Especialidad: ").setBold().setMargin(0f))
        especialidadParagraph.add(Paragraph(vet.especialidad).setMargin(0f))
        document.add(especialidadParagraph)

        val telefonoParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        telefonoParagraph.add(Paragraph("Teléfono: ").setBold().setMargin(0f))
        telefonoParagraph.add(Paragraph(vet.telefono).setMargin(0f))
        document.add(telefonoParagraph)

        val emailParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        emailParagraph.add(Paragraph("E-mail: ").setBold().setMargin(0f))
        emailParagraph.add(Paragraph(vet.email).setMargin(0f))
        document.add(emailParagraph)

        val modalidadParagraph = Paragraph().setMarginTop(0f).setMarginBottom(0f)
        modalidadParagraph.add(Paragraph("Modalidad del Servicio: ").setBold().setMargin(0f))
        modalidadParagraph.add(Paragraph(vet.modalidad).setMargin(0f))
        document.add(modalidadParagraph)

        document.add(Paragraph("\n"))
    }

    document.close()
}