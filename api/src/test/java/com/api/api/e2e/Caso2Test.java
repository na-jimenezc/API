package com.api.api.e2e;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.api.api.model.Mascota;
import com.api.api.model.Veterinario;
import com.api.api.repository.ClienteRepository;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.VeterinarioRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

// Clase de prueba end-to-end para el Caso 2: Tratamiento y verificacion de administrador
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class Caso2Test {

    // Configuracion de URLs y credenciales
    private final String BASE_URL = "http://localhost:4200";
    private WebDriver driver;
    private WebDriverWait wait;

    private final String usuarioVet = "juanPerez";
    private final String contraseniaVet = "123";
    private final String usuarioAdmin = "admin@animalheart.com";
    private final String contraseniaAdmin = "admin123";
    
    // Repositorios para verificar datos de prueba
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private MascotaRepository mascotaRepository;

    // Metodo de inicializacion antes de cada prueba
    @BeforeEach
    public void init(){
        verificarDatosTest();
        
        // Configurar ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-extensions");

        this.driver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // Metodo auxiliar para pausas entre acciones
    private void pausa(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Verifica que los datos de prueba esten disponibles en la base de datos
    private void verificarDatosTest(){
        System.out.println("Verificando datos de TEST en BD...");
        
        List<Veterinario> vets = veterinarioRepository.findAll();
        Assertions.assertThat(vets).isNotEmpty();
        
        // Verificar que el veterinario de prueba existe y esta activo
        Veterinario vet = veterinarioRepository.findByNombreUsuarioAndContrasenia(usuarioVet, contraseniaVet);
        Assertions.assertThat(vet).isNotNull();
        Assertions.assertThat(vet.getActivo()).isEqualTo(1);
        
        List<Mascota> mascotas = mascotaRepository.findAll();
        Assertions.assertThat(mascotas).isNotEmpty();
        
        System.out.println("Veterinarios: " + vets.size());
        System.out.println("Mascotas: " + mascotas.size());
        System.out.println("Usuario vet: " + vet.getNombreUsuario() + " | Activo: Si");
        System.out.println("Datos verificados\n");
    }

    // Metodo de limpieza despues de cada prueba
    @AfterEach
    void tearDown(){
        if(driver != null){
            driver.quit();
            System.out.println("WebDriver cerrado");}
    }

    // Prueba principal que ejecuta el flujo completo del caso 2
    @Test
    public void casoCompletoTratamientoYVerificacionAdmin(){
        loginVeterinario();
        buscarMascotaYVerDetalles();
        agregarTratamiento();
        llenarYGuardarTratamiento();
        verificarTratamientoGuardado();
        logoutVeterinario();
        loginAdminYVerificarEstadisticas();
    }

    // Metodo para iniciar sesion como veterinario
    public void loginVeterinario(){
        driver.get(BASE_URL + "/login-veterinario");
        System.out.println("URL actual: " + driver.getCurrentUrl());
    
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("usuario")));
        System.out.println("Formulario de login cargado\n");
        
        WebElement inputUsuario = driver.findElement(By.id("usuario"));
        WebElement inputContrasena = driver.findElement(By.id("contrasena"));
        
        inputUsuario.sendKeys(usuarioVet);
        pausa(1000);
        inputContrasena.sendKeys(contraseniaVet);
        pausa(1000);
        
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.boton-enviar[type='submit']")));
        WebElement btnAcceder = driver.findElement(By.cssSelector("button.boton-enviar[type='submit']"));
        btnAcceder.click();
        
        wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("/mascotas/ver-mascotas")));
        
        String urlActual = driver.getCurrentUrl();
        Assertions.assertThat(urlActual).as("Debe redirigir a pagina de mascotas").contains("/mascotas/ver-mascotas");
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-header-vet")));
        WebElement headerVet = driver.findElement(By.tagName("app-header-vet"));
        
        Assertions.assertThat(headerVet.isDisplayed()).as("El header del veterinario debe estar visible").isTrue();
        System.out.println("Login veterinario exitoso\n");
    }

    // Busca una mascota especifica y accede a sus detalles
    private void buscarMascotaYVerDetalles(){
        WebElement barraBusqueda = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input.va-search-input")));
        
        String nombreMascota = "Firulais";
        
        barraBusqueda.clear();
        pausa(500);
        barraBusqueda.sendKeys(nombreMascota);
        pausa(2000);
        
        System.out.println("Buscando mascota: " + nombreMascota);
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".table-row")));
        
        List<WebElement> filasMascotas = driver.findElements(By.cssSelector(".table-row"));
        WebElement mascotaEncontrada = null;
        
        for (WebElement fila : filasMascotas) {
            WebElement celdaNombre = fila.findElement(By.cssSelector(".va-cell-name"));
            String nombreEnTabla = celdaNombre.getText().trim();
            
            if (nombreEnTabla.equalsIgnoreCase(nombreMascota)) {
                mascotaEncontrada = fila;
                System.out.println("Mascota encontrada: " + nombreEnTabla);
                break;
            }
        }
        
        Assertions.assertThat(mascotaEncontrada).as("Debe encontrar la mascota '" + nombreMascota + "'").isNotNull();
        
        WebElement btnVerMas = mascotaEncontrada.findElement(By.cssSelector("a.action-btn.btn-view"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", btnVerMas);
        pausa(1000);
        
        btnVerMas.click();
        System.out.println("Clic en 'Ver mas' para ver detalles");
        
        wait.until(ExpectedConditions.urlContains("/mascotas/detalle/"));
        System.out.println("Pagina de detalle de mascota cargada");
    }

    // Navega a la pagina de agregar tratamiento desde el header
    private void agregarTratamiento(){
        WebElement btnAgregarTratamientoHeader = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[routerLink='/tratamientos/agregar']")));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", btnAgregarTratamientoHeader);
        pausa(1000);
        
        btnAgregarTratamientoHeader.click();
        System.out.println("Clic en 'Agregar Tratamiento' del header");
        
        wait.until(ExpectedConditions.urlContains("/tratamientos/agregar"));
        System.out.println("Pagina de agregar tratamiento cargada");
    }

    // Llena y envia el formulario de tratamiento
    private void llenarYGuardarTratamiento(){
        System.out.println("=== INICIANDO LLENADO DE FORMULARIO ===");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        System.out.println("Formulario cargado");
        
        // Esperar carga de mascotas en el dropdown
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            longWait.until(driver -> {
                WebElement selectMascota = driver.findElement(By.cssSelector("select[formcontrolname='mascotaId']"));
                List<WebElement> opciones = selectMascota.findElements(By.tagName("option"));
                return opciones.size() > 1;
            });
            System.out.println("Dropdown de mascotas cargado con opciones");
        } catch (Exception e) {
            System.out.println("Timeout esperando opciones de mascotas. Recargando pagina...");
            driver.navigate().refresh();
            pausa(3000);
        }
        
        // Diagnosticar mascotas disponibles
        System.out.println("=== DIAGNOSTICO DE MASCOTAS DISPONIBLES ===");
        WebElement selectMascota = driver.findElement(By.cssSelector("select[formcontrolname='mascotaId']"));
        List<WebElement> opcionesMascota = selectMascota.findElements(By.tagName("option"));
        
        System.out.println("Numero total de opciones en mascotas: " + opcionesMascota.size());
        for (int i = 0; i < opcionesMascota.size(); i++) {
            WebElement opcion = opcionesMascota.get(i);
            String texto = opcion.getText();
            String valor = opcion.getAttribute("value");
            System.out.println("Opcion " + i + ": Texto='" + texto + "', Valor='" + valor + "'");
        }
        
        // Buscar y seleccionar mascota Firulais
        String mascotaId = null;
        String mascotaNombre = null;
        
        for (WebElement opcion : opcionesMascota) {
            String texto = opcion.getText().toLowerCase();
            if (texto.contains("firulais")) {
                mascotaId = opcion.getAttribute("value");
                mascotaNombre = opcion.getText();
                System.out.println("Encontrada mascota por nombre: " + mascotaNombre);
                break;
            }
        }
        
        Assertions.assertThat(mascotaId).as("Debe encontrar la mascota Firulais").isNotNull();
        
        System.out.println("Seleccionando mascota: " + mascotaNombre + " (ID: " + mascotaId + ")");
        
        // Seleccionar mascota usando JavaScript
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];", 
            selectMascota, mascotaId
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
            selectMascota
        );
        System.out.println("Seleccion via JavaScript exitosa");
        
        pausa(2000);
        
        String valorSeleccionado = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", selectMascota);
        System.out.println("Valor seleccionado en dropdown: " + valorSeleccionado);
        
        // Establecer fecha del tratamiento
        System.out.println("=== ESTABLECIENDO FECHA ===");
        WebElement inputFecha = driver.findElement(By.cssSelector("input[formcontrolname='fecha']"));
        
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = '2025-11-04';", 
            inputFecha
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", 
            inputFecha
        );
        System.out.println("Fecha establecida via JavaScript: 2025-11-04");
        
        pausa(1000);
        
        String fechaInsertada = inputFecha.getAttribute("value");
        System.out.println("Fecha despues de JavaScript: " + fechaInsertada);
        
        // Seleccionar medicamento
        System.out.println("=== SELECCION DE MEDICAMENTO ===");
        
        WebElement selectMedicamento = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("select[formcontrolname='medicamentoId']")));
        
        System.out.println("Esperando a que carguen los medicamentos...");
        pausa(3000);
        
        List<WebElement> opcionesMedicamento = selectMedicamento.findElements(By.tagName("option"));
        System.out.println("Numero total de opciones en medicamentos: " + opcionesMedicamento.size());
        
        // Buscar primer medicamento disponible
        String medicamentoId = null;
        String medicamentoNombre = null;
        
        for (WebElement opcion : opcionesMedicamento) {
            String valor = opcion.getAttribute("value");
            String texto = opcion.getText();
            
            if (valor != null && !valor.isEmpty() && !valor.equals("null") && 
                !texto.contains("Seleccionar") && !texto.isEmpty()) {
                medicamentoId = valor;
                medicamentoNombre = texto;
                System.out.println("Seleccionando primer medicamento disponible: " + medicamentoNombre);
                break;
            }
        }
        
        Assertions.assertThat(medicamentoId).as("Debe encontrar un medicamento disponible").isNotNull();
        
        System.out.println("Seleccionando medicamento: " + medicamentoNombre + " (ID: " + medicamentoId + ")");
        
        // Seleccionar medicamento usando JavaScript
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];", 
            selectMedicamento, medicamentoId
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
            selectMedicamento
        );
        System.out.println("Seleccion de medicamento via JavaScript exitosa");
        
        pausa(2000);
        
        String medicamentoSeleccionado = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", selectMedicamento);
        System.out.println("Medicamento seleccionado en dropdown: " + medicamentoSeleccionado);
        
        // Completar resto del formulario
        WebElement inputCantidad = driver.findElement(By.cssSelector("input[formcontrolname='cantidadUsada']"));
        inputCantidad.clear();
        pausa(500);
        inputCantidad.sendKeys("1");
        System.out.println("Cantidad establecida: 1");
        pausa(1000);
        
        WebElement selectTipo = driver.findElement(By.cssSelector("select[formcontrolname='tipoTratamiento']"));
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));", 
            selectTipo, "Consulta General"
        );
        System.out.println("Tipo de tratamiento seleccionado");
        pausa(1000);
        
        WebElement textareaObs = driver.findElement(By.cssSelector("textarea[formcontrolname='observaciones']"));
        textareaObs.sendKeys("Tratamiento de prueba para caso 2");
        System.out.println("Observaciones escritas");
        pausa(1000);
        
        // Verificacion final de campos antes de guardar
        System.out.println("=== VERIFICACION FINAL DE CAMPOS ===");
        verificarEstadoCampo("mascotaId");
        verificarEstadoCampo("fecha");
        verificarEstadoCampo("medicamentoId");
        verificarEstadoCampo("cantidadUsada");
        verificarEstadoCampo("tipoTratamiento");
        
        String fechaFinal = inputFecha.getAttribute("value");
        System.out.println("Fecha final en el campo: " + fechaFinal);
        
        WebElement btnGuardar = driver.findElement(By.cssSelector("button[type='submit']"));
        boolean puedeGuardar = btnGuardar.isEnabled();
        System.out.println("Puede guardar? " + puedeGuardar);
        
        Assertions.assertThat(puedeGuardar).as("El formulario debe estar completo para guardar").isTrue();
        
        // Guardar formulario
        System.out.println("=== GUARDANDO FORMULARIO ===");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnGuardar);
        pausa(1000);
        btnGuardar.click();
        System.out.println("Formulario enviado");
        
        pausa(5000);
        
        String urlActual = driver.getCurrentUrl();
        System.out.println("URL despues de enviar: " + urlActual);
        
        boolean exito = urlActual.contains("/mascotas/ver-mascotas");
        Assertions.assertThat(exito).as("Deberia redirigir a mascotas despues de guardar").isTrue();
        System.out.println("Formulario guardado exitosamente\n");
    }

    // Metodo auxiliar para verificar estado de campos del formulario
    private void verificarEstadoCampo(String formControlName) {
        try {
            WebElement campo = driver.findElement(By.cssSelector("[formcontrolname='" + formControlName + "']"));
            String valor = campo.getAttribute("value");
            String clase = campo.getAttribute("class");
            System.out.println("Campo " + formControlName + ": valor='" + valor + "', clase='" + clase + "'");
        } catch (Exception e) {
            System.out.println("No se pudo verificar campo " + formControlName + ": " + e.getMessage());
        }
    }

    // Verifica que el tratamiento fue guardado correctamente
    private void verificarTratamientoGuardado(){
        System.out.println("=== VERIFICANDO TRATAMIENTO GUARDADO ===");
        
        WebElement barraBusqueda = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input.va-search-input")));
        
        barraBusqueda.clear();
        pausa(500);
        barraBusqueda.sendKeys("Firulais");
        pausa(2000);
        
        System.out.println("Buscando mascota para verificar tratamiento");
        
        List<WebElement> filasMascotas = driver.findElements(By.cssSelector(".table-row"));
        WebElement mascotaEncontrada = null;
        
        for (WebElement fila : filasMascotas) {
            WebElement celdaNombre = fila.findElement(By.cssSelector(".va-cell-name"));
            if (celdaNombre.getText().trim().equalsIgnoreCase("Firulais")) {
                mascotaEncontrada = fila;
                break;
            }
        }
        
        Assertions.assertThat(mascotaEncontrada).as("Debe encontrar la mascota 'Firulais'").isNotNull();
        
        WebElement btnVerMas = mascotaEncontrada.findElement(By.cssSelector("a.action-btn.btn-view"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", btnVerMas);
        pausa(1000);
        btnVerMas.click();
        
        wait.until(ExpectedConditions.urlContains("/mascotas/detalle/"));
        System.out.println("Pagina de detalle cargada para verificacion");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(., 'Tratamientos Recibidos')]")));
        pausa(2000);
        
        List<WebElement> filasTratamiento = driver.findElements(By.cssSelector("table.table tbody tr"));
        Assertions.assertThat(filasTratamiento).as("Debe haber al menos un tratamiento registrado").isNotEmpty();
        
        System.out.println("Tratamientos encontrados: " + filasTratamiento.size());
        
        for (int i = 0; i < filasTratamiento.size(); i++) {
            System.out.println("Tratamiento " + i + ": " + filasTratamiento.get(i).getText());
        }
        
        String contenidoPagina = driver.getPageSource();
        boolean tratamientoEncontrado = contenidoPagina.contains("1 unidades") || 
                                        contenidoPagina.contains("1 unidad") ||
                                        contenidoPagina.contains("2025-11-04");
        
        Assertions.assertThat(tratamientoEncontrado).as("Debe encontrar el tratamiento guardado").isTrue();
        
        System.out.println("Tratamiento verificado exitosamente:");
        System.out.println("- Mascota: Firulais");
        System.out.println("- Fecha: 4 de noviembre 2025");
        System.out.println("- Cantidad: 1 unidad");
        System.out.println("- Verificacion exitosa\n");
    }

    // Cierra sesion del veterinario
    private void logoutVeterinario(){
        WebElement btnLogout = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/logout']")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", btnLogout);
        pausa(500);
        
        btnLogout.click();
        
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains(BASE_URL)
        ));
        System.out.println("Veterinario deslogueado\n");
    }

    // Inicia sesion como administrador y verifica las estadisticas
    private void loginAdminYVerificarEstadisticas(){
        System.out.println("Haciendo clic en enlace 'Administrador' del header...");
        
        // Navegar al login de administrador desde el header
        WebElement adminLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), 'Administrador')]")));
        
        adminLink.click();
        System.out.println("Clic en enlace 'Administrador' realizado");
        
        wait.until(ExpectedConditions.urlContains("/admin/login"));
        System.out.println("Pagina de login de administrador cargada");

        // Llenar formulario de login
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        
        // Buscar campos de entrada
        WebElement inputUsuario = driver.findElement(By.cssSelector("input[type='email'], input[placeholder*='email'], input[placeholder*='usuario']"));
        WebElement inputContrasena = driver.findElement(By.cssSelector("input[type='password'], input[placeholder*='contrasena']"));
        
        // Escribir credenciales
        inputUsuario.clear();
        pausa(500);
        inputUsuario.sendKeys(usuarioAdmin);
        pausa(1000);
        
        inputContrasena.clear();
        pausa(500);
        inputContrasena.sendKeys(contraseniaAdmin);
        pausa(1000);
        
        // Enviar formulario
        WebElement btnAcceder = driver.findElement(By.cssSelector("button[type='submit'], button.boton-enviar"));
        btnAcceder.click();
        
        wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
        System.out.println("Login administrador exitoso");
        
        // Verificar estadisticas del dashboard
        System.out.println("=== VERIFICANDO ESTADISTICAS DEL ADMINISTRADOR ===");
        pausa(5000);
        
        // Verificar ganancias totales
        System.out.println("Buscando componente de ganancias totales...");
        WebElement gananciasElement = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//app-ganancias-totales//*[contains(@class, 'stat-count')]")));
        
        String textoGanancias = gananciasElement.getText().replace("$", "").replace(",", "").trim();
        System.out.println("Texto de ganancias extraido: '" + textoGanancias + "'");
        
        double gananciasReportadas = Double.parseDouble(textoGanancias);
        System.out.println("Ganancias reportadas: $" + gananciasReportadas);
        
        Assertions.assertThat(gananciasReportadas)
            .as("Las ganancias deben ser un valor positivo")
            .isGreaterThan(0);
            
        System.out.println("Verificacion de ganancias exitosa");
        
        // Verificar medicamentos suministrados
        System.out.println("Buscando lista de tratamientos por medicamento...");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("app-tratamientos-tipo-medicamento")));
        
        List<WebElement> itemsMedicamento = driver.findElements(
            By.cssSelector("app-tratamientos-tipo-medicamento .list-item"));
        
        System.out.println("Encontrados " + itemsMedicamento.size() + " items en la lista de medicamentos");
        
        int totalMedicamentosReportados = 0;
        boolean hayMedicamentos = itemsMedicamento.size() > 0;
        
        for (WebElement item : itemsMedicamento) {
            WebElement nombreElement = item.findElement(By.cssSelector(".medicamento-name"));
            WebElement cantidadElement = item.findElement(By.cssSelector(".cantidad-badge"));
            
            String nombreMedicamento = nombreElement.getText().trim();
            String cantidadTexto = cantidadElement.getText().replaceAll("[^0-9]", "");
            int cantidad = Integer.parseInt(cantidadTexto);
            
            totalMedicamentosReportados += cantidad;
            System.out.println("Medicamento encontrado: " + nombreMedicamento + " - Cantidad: " + cantidad);
        }
        
        // Verificaciones finales
        Assertions.assertThat(hayMedicamentos)
            .as("Debe haber al menos un medicamento registrado en el dashboard del administrador")
            .isTrue();
        
        Assertions.assertThat(totalMedicamentosReportados)
            .as("Debe haber al menos 1 medicamento reportado (el que acabamos de agregar)")
            .isGreaterThanOrEqualTo(1);
        
        System.out.println("=== RESUMEN DE VERIFICACION ===");
        System.out.println("- Ganancias totales: $" + gananciasReportadas);
        System.out.println("- Medicamentos encontrados: Si");
        System.out.println("- Total de medicamentos reportados: " + totalMedicamentosReportados);
        System.out.println("Estadisticas del administrador verificadas correctamente\n");
    }
}