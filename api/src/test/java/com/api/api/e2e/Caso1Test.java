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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.model.Veterinario;
import com.api.api.repository.ClienteRepository;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.VeterinarioRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class Caso1Test {

    private final String BASE_URL = "http://localhost:4200";
    private WebDriver driver;
    private WebDriverWait wait;

    private final String usuarioVetValido = "juanPerez";
    private final String vetConstraseniaValida = "123";
    private final String vetContraseniaInvalida = "contraseñaIncorrecta";
    
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private MascotaRepository mascotaRepository;

    @BeforeEach
    public void init(){
        verificarDatosDeTest();
        
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-extensions");

        this.driver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private void pausa(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

      private void verificarDatosDeTest(){
        System.out.println("Verificando datos de TEST en BD...");
        
        List<Veterinario> vets = veterinarioRepository.findAll();
        Assertions.assertThat(vets).isNotEmpty();
        
        Veterinario vet1 = veterinarioRepository.findByNombreUsuarioAndContrasenia(usuarioVetValido, vetConstraseniaValida);
        Assertions.assertThat(vet1).isNotNull();
        Assertions.assertThat(vet1.getContrasenia()).isEqualTo(vetConstraseniaValida);
        Assertions.assertThat(vet1.getActivo()).isEqualTo(1);
        
        List<Cliente> clientes = clienteRepository.findAll();
        Assertions.assertThat(clientes).hasSizeGreaterThanOrEqualTo(5);
        
        System.out.println("Veterinarios: " + vets.size());
        System.out.println("Clientes: " + clientes.size());
        System.out.println("Mascotas: " + mascotaRepository.count());
        System.out.println("Usuario vet: " + vet1.getNombreUsuario() + " | Activo: Sí");
        System.out.println("Datos verificados\n");
    }

    @AfterEach
    void tearDown(){
        if(driver != null){
            driver.quit();
            System.out.println("WebDriver cerrado");}
    }

    @Test
    public void casoCompletoRegistroClienteYMascota(){
        
        //Login del veterinario
        loginVeterinarioConError();
        
        //Registro del cliente
        registrarNuevoClienteConError();
        
        //Registro de mascota
        registrarMascotaExitosa();
        
        //Logout del veterinario
        logoutVeterinario();
        
        //Login del cliente y verificación de datos
        loginClienteYVerificarDatos();
        
    }

    /*Test para el login con credenciales correctas e incorrectas del veterinario*/
    public void loginVeterinarioConError(){

        //Cargar la URL y verificar que el formulario de login esté presente
        driver.get(BASE_URL + "/login-veterinario");
        System.out.println("URL actual: " + driver.getCurrentUrl());
    
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("usuario")));
        System.out.println("Formulario de login cargado\n");
        
        /*Caso de credenciales incorrectas*/
        WebElement inputUsuario = driver.findElement(By.id("usuario"));
        WebElement inputContrasena = driver.findElement(By.id("contrasena"));
        
        inputUsuario.sendKeys(usuarioVetValido);
        pausa(1000);
        inputContrasena.sendKeys(vetContraseniaInvalida);
        pausa(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.boton-enviar[type='submit']")));


        //Click
        WebElement btnAcceder = driver.findElement(By.cssSelector("button.boton-enviar[type='submit']"));
        btnAcceder.click();
        pausa(1000);
        
        //Revisión de mensajde de error
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));
        
        WebElement mensajeError = driver.findElement(By.cssSelector(".alert-danger"));
        String textoError = mensajeError.getText();
        
        //Assert dekl mensaje de error
        Assertions.assertThat(mensajeError.isDisplayed()).as("El mensaje de error debe ser visible").isTrue();
        Assertions.assertThat(textoError).as("El mensaje debe contener la palabra 'Error'").contains("Error");
        Assertions.assertThat(textoError).as("El mensaje debe indicar credenciales inválidas").containsIgnoringCase("Credenciales inválidas");

        /*Caso de credenciales correctas*/
        inputUsuario = driver.findElement(By.id("usuario"));
        inputContrasena = driver.findElement(By.id("contrasena"));
        
        inputUsuario.clear();
        inputContrasena.clear();
        
        //Escribir y mandar datos
        inputUsuario.sendKeys(usuarioVetValido);
                  pausa(2000);
        inputContrasena.sendKeys(vetConstraseniaValida);
                  pausa(2000);
        
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.boton-enviar[type='submit']")));
        btnAcceder = driver.findElement(By.cssSelector("button.boton-enviar[type='submit']"));
        btnAcceder.click();
        
        //Verificación del login
        wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("/mascotas/ver-mascotas")));
        
        String urlMascotas = driver.getCurrentUrl();
        
        //Verificar el login
        Assertions.assertThat(urlMascotas).as("Después del login exitoso, no debemos estar en /login-vet").doesNotContain("/login-vet");
        
        //Verificar el header del veterinario
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-header-vet")));
        WebElement headerVet = driver.findElement(By.tagName("app-header-vet"));
        
        Assertions.assertThat(headerVet.isDisplayed()).as("El header del veterinario debe estar visible").isTrue();
    }

    private void registrarNuevoClienteConError(){
        
        //Sección de clientes nuevos
        driver.get(BASE_URL + "/clientes/nuevo");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        System.out.println("Formulario de cliente cargado");
        
        //Datos del nuevo cliente
        WebElement inputCedula = driver.findElement(By.cssSelector("input[formControlName='cedula']"));
        WebElement inputNombre = driver.findElement(By.cssSelector("input[formControlName='nombre']"));
        WebElement inputCorreo = driver.findElement(By.cssSelector("input[formControlName='correo']"));
        WebElement inputCelular = driver.findElement(By.cssSelector("input[formControlName='celular']"));

        String cedulaNuevoCliente = "9998887776";
        String nombreNuevoCliente = "Natalia Jiménez";
        String celularNuevoCliente = "3123456789";
        
        //Envio de datos con error en el correo
        inputCedula.sendKeys(cedulaNuevoCliente);
                  pausa(2000);
        inputNombre.sendKeys(nombreNuevoCliente);
                  pausa(2000);
        inputCorreo.sendKeys("correo-invalido"); 
                  pausa(2000);
        inputCelular.sendKeys(celularNuevoCliente);
        
        WebElement btnRegistrar = driver.findElement(By.cssSelector("button.btn-primary[type='submit']"));
        btnRegistrar.click();
        System.out.println("Primer intento con correo inválido");
        
        //Se debe mostrar el mensaje de error y y deshabilitar el botón de registrar
        wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".error-message")),
            ExpectedConditions.attributeToBe(By.cssSelector("button.btn-primary[type='submit']"), "disabled", "true")
        ));

        //Envio de datos correcto
        inputCorreo.clear();

        String correoCorrecto = "na@gmail.com";
        inputCorreo.sendKeys(correoCorrecto);
                  pausa(1000);
        
        //Esperar a validez del formulario
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-primary[type='submit']")));
        btnRegistrar = driver.findElement(By.cssSelector("button.btn-primary[type='submit']"));
        btnRegistrar.click();
        System.out.println("Segundo intento con datos correctos");
        
        //Verificar redirección a la lista de mascotas
        wait.until(ExpectedConditions.urlContains("/mascotas/agregar"));
        System.out.println("Cliente registrado exitosamente");
        
        //Assert de la bd 
        Cliente clienteGuardado = clienteRepository.findByCedula(cedulaNuevoCliente);
        Assertions.assertThat(clienteGuardado).isNotNull();
        Assertions.assertThat(clienteGuardado.getNombre()).isEqualTo(nombreNuevoCliente);
        System.out.println("Cliente verificado en base de datos\n");
    }

     private void registrarMascotaExitosa(){
        
        //Ir a registro de nueva mascota
        driver.get(BASE_URL + "/mascotas/agregar");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        System.out.println("Formulario de registro de mascota cargado");
        
        //Llenar los datos
        WebElement inputFotoURL = driver.findElement(By.cssSelector("input[formcontrolname='fotoURL']"));
        WebElement inputNombre = driver.findElement(By.cssSelector("input[formcontrolname='nombre']"));
        WebElement inputEdad = driver.findElement(By.cssSelector("input[formcontrolname='edad']"));
        WebElement selectTipo = driver.findElement(By.cssSelector("select[formcontrolname='tipo']"));
        WebElement inputRaza = driver.findElement(By.cssSelector("input[formcontrolname='raza']"));
        WebElement inputPeso = driver.findElement(By.cssSelector("input[formcontrolname='peso']"));
        WebElement inputEnfermedad = driver.findElement(By.cssSelector("input[formcontrolname='enfermedad']"));
        

        String fotoUrlDefault = "/assets/images/defaultPerro.jpg";
        String nombreMascota = "Firulais2";
        String edadMascota = "4";
        String tipoMascota = "Perro";
        String razaMascota = "Labrador";
        String pesoMascota = "12.5";
        String cedulaNuevoCliente = "9998887776";
        String nombreNuevoCliente = "Natalia Jiménez";
        
        inputFotoURL.sendKeys(fotoUrlDefault);
                  pausa(1000);
        inputNombre.sendKeys(nombreMascota);
                  pausa(1000);
        inputEdad.sendKeys(edadMascota);
                  pausa(1000);
        
        Select tipoSelect = new Select(selectTipo);
                  pausa(1000);
        tipoSelect.selectByValue(tipoMascota);
        
        inputRaza.sendKeys(razaMascota);
                  pausa(1000);
        inputPeso.sendKeys(pesoMascota);
                  pausa(1000);
        inputEnfermedad.sendKeys("Ninguna");
        
        System.out.println("Datos de mascota ingresados");
        
        //Se busca al cliente 
        WebElement selectCliente = driver.findElement(By.id("clienteSelect"));
        Select clienteSelect = new Select(selectCliente);
        
        //Se espera a que carguen las opciones
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#clienteSelect option"), 1));
        
        List<WebElement> opciones = clienteSelect.getOptions();
        boolean clienteEncontrado = false;

        for (WebElement opcion : opciones) {
            String textoOpcion = opcion.getText();
            System.out.println("Opción disponible: " + textoOpcion);
            if (textoOpcion.contains("Natalia Jiménez") || textoOpcion.contains(nombreNuevoCliente)) {
                opcion.click();
                clienteEncontrado = true;
                System.out.println("Cliente seleccionado: " + textoOpcion);
                break;
            }
        }
        
        Assertions.assertThat(clienteEncontrado).as("Debe existir el cliente 'Natalia Jiménez' o '" + nombreNuevoCliente + "' en el select").isTrue();
        System.out.println("Cliente asociado a la mascota");
        
        //Esperar a la info
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientInfo")));
        System.out.println("Información del cliente mostrada");
        
        //Enviar formulario
        WebElement btnRegistrar = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-primary[type='submit']")));

        //Hacer scroll
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", btnRegistrar);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100);");

        try {
            btnRegistrar.click();
        } catch (Exception e) {
            System.out.println("Click normal falló, intentando con JS...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnRegistrar);
        }

        System.out.println("Formulario enviado");
        
        //Verificar redirección
        wait.until(ExpectedConditions.urlContains("/mascotas/ver-mascotas"));
        System.out.println("Mascota registrada exitosamente");
        
        //Assert en la BD de los datos registrados
        Cliente cliente = clienteRepository.findByCedula(cedulaNuevoCliente);
        if (cliente == null) {
            List<Cliente> todosClientes = clienteRepository.findAll();
            cliente = todosClientes.stream()
                .filter(c -> c.getNombre().contains("Natalia Jiménez"))
                .findFirst().orElse(null);
        }

        long idCliente = cliente.getId();
        
        Assertions.assertThat(cliente).as("Debe existir el cliente en la BD").isNotNull();
        
        List<Mascota> mascotas = mascotaRepository.findByClienteId(idCliente);
        Assertions.assertThat(mascotas).isNotEmpty();
        
        Mascota mascotaGuardada = mascotas.stream()
            .filter(m -> m.getNombre().equals(nombreMascota))
            .findFirst()
            .orElse(null);
            
        Assertions.assertThat(mascotaGuardada).isNotNull();
        Assertions.assertThat(mascotaGuardada.getRaza()).isEqualTo(razaMascota);
        System.out.println("Mascota verificada en base de datos\n");
    }

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

    private void loginClienteYVerificarDatos(){

    
       //Esperar a que el enlace esté presente
        WebElement enlaceCliente = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.nav-link.pastilla[href='/clientes/login-cliente']")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", enlaceCliente);
        pausa(500);

        //Esperar que realmente sea clickeable
        wait.until(ExpectedConditions.elementToBeClickable(enlaceCliente));

        try {
            enlaceCliente.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", enlaceCliente);
        }

        wait.until(ExpectedConditions.urlContains("/clientes/login-cliente"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correo")));
        System.out.println("Portal de clientes cargado correctamente");
        pausa(1000);
        
        //Se obtiene el correo por cédula
        String cedulaNuevoCliente = "9998887776";

        Cliente cliente = clienteRepository.findByCedula(cedulaNuevoCliente);
        if (cliente == null) {
            List<Cliente> todosClientes = clienteRepository.findAll();
            cliente = todosClientes.stream()
                .filter(c -> c.getNombre().contains("Natalia Jiménez"))
                .findFirst()
                .orElse(null);
        }
        
        Assertions.assertThat(cliente).as("El cliente debe existir").isNotNull();
        String correoCliente = cliente.getCorreo();
        String claveCliente = cliente.getCedula(); 
        
        //Se ingresa con los datos
        WebElement inputCorreo = driver.findElement(By.id("correo"));
        WebElement inputClave = driver.findElement(By.id("clave"));
        
        inputCorreo.sendKeys(correoCliente);
        pausa(500);
        inputClave.sendKeys(claveCliente);
        pausa(500);
        
        WebElement btnIngresar = driver.findElement(By.cssSelector("button[type='submit']"));
        btnIngresar.click();
        System.out.println("Cliente ingresando con correo: " + correoCliente);
        pausa(1000);
        
        //Se espera a la carga
        wait.until(ExpectedConditions.urlContains("/clientes/mis-mascotas"));
        System.out.println("Páginacargada");
        pausa(500);
        
        //Se verifica que esté la info de la mascota
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".pet-card")));
        System.out.println("Tarjeta de mascota visible");
        
        //Verificar información de la mascota en la tarjeta
        WebElement petCard = driver.findElement(By.cssSelector(".pet-card"));
        String cardHtml = petCard.getText();
        

        String nombreMascota = "Firulais2";
        String edadMascota = "4";
        String razaMascota = "Labrador";
        String pesoMascota = "12.5";

        Assertions.assertThat(cardHtml).contains(nombreMascota);
        Assertions.assertThat(cardHtml).contains("Perro");
        Assertions.assertThat(cardHtml).contains(edadMascota + " años");
        Assertions.assertThat(cardHtml).contains(razaMascota);
        Assertions.assertThat(cardHtml).contains(pesoMascota + " kg");
        Assertions.assertThat(cardHtml).contains("Ninguna");
        
        System.out.println("Información de la mascota verificada en la tarjeta:");
        
        //Click para ver el detalle de la mascota
        petCard.click();
        System.out.println("✓ Click en la tarjeta de la mascota");
        pausa(500);
        
        //Verificar redirección
        wait.until(ExpectedConditions.urlMatches(".*/clientes/mascotas/\\d+"));
        String urlDetalle = driver.getCurrentUrl();
        System.out.println("✓ Redirigido a: " + urlDetalle);
        pausa(1);
        
        // Verificar información detallada de la mascota
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".pet-info")));
        
        List<WebElement> petInfoElements = driver.findElements(By.cssSelector(".pet-info"));
        String detalleHtml = driver.getPageSource();
        
        //Verificar al menos el peso
        boolean pesoEncontrado = false;
        for (WebElement element : petInfoElements) {
            String texto = element.getText();
            if (texto.contains("Peso:") && texto.contains(pesoMascota + " kg")) {
                pesoEncontrado = true;
                System.out.println("✓ Peso verificado: " + pesoMascota + " kg");
                break;
            }
        }
        Assertions.assertThat(pesoEncontrado).as("Debe mostrar el peso correcto").isTrue();
        pausa(500);
        
        //Verificar la info del dueño
        Assertions.assertThat(detalleHtml).contains("Tu información");
        Assertions.assertThat(detalleHtml).contains("Natalia Jiménez");
        
        boolean correoEncontrado = false;
        boolean telefonoEncontrado = false;
        
        for (WebElement element : petInfoElements) {
            String texto = element.getText();
            if (texto.contains("Correo:")) {
                correoEncontrado = true;
                System.out.println("Correo del dueño encontrado: " + texto);
            }
            if (texto.contains("Teléfono:")) {
                telefonoEncontrado = true;
                System.out.println("Teléfono del dueño encontrado: " + texto);
            }
        }
        
        Assertions.assertThat(correoEncontrado).as("Debe mostrar el correo del dueño").isTrue();
        Assertions.assertThat(telefonoEncontrado).as("Debe mostrar el teléfono del dueño").isTrue();
        
        System.out.println("Información del dueño verificada:");
        System.out.println("Nombre: Natalia Jiménez");
        System.out.println("Datos de contacto presentes");
    }    
}
