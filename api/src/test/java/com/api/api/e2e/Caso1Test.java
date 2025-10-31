package com.api.api.e2e;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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

import com.api.api.model.Cliente;
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

    /*Test para el login con credenciales correctas e incorrectas del veterinario*/
    @Test
    public void loginVeterinario(){

        //Cargar la URL y verificar que el formulario de login esté presente
        driver.get(BASE_URL + "/login-veterinario");
        System.out.println("URL actual: " + driver.getCurrentUrl());
    
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("usuario")));
        System.out.println("Formulario de login cargado\n");
        
        /*Caso de credenciales incorrectas*/
        WebElement inputUsuario = driver.findElement(By.id("usuario"));
        WebElement inputContrasena = driver.findElement(By.id("contrasena"));
        
        inputUsuario.sendKeys(usuarioVetValido);
        inputContrasena.sendKeys(vetContraseniaInvalida);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.boton-enviar[type='submit']")));
        
        //Click
        WebElement btnAcceder = driver.findElement(By.cssSelector("button.boton-enviar[type='submit']"));
        btnAcceder.click();
        
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
        inputContrasena.sendKeys(vetConstraseniaValida);
        
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

    
}
