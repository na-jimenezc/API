package com.api.api.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.api.api.model.Administrador;
import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.AdministradorRepository;
import com.api.api.repository.ClienteRepository;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.repository.TratamientoRepository;
import com.api.api.repository.VeterinarioRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
@Profile("test")
public class TestDatabaseInit implements ApplicationRunner {

    @Autowired
    VeterinarioRepository veterinarioRepository;

    @Autowired
    AdministradorRepository administradorRepository;

    @Autowired
    MascotaRepository mascotaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    MedicamentoRepository medicamentosRepository;

    @Autowired
    TratamientoRepository tratamientosRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
       System.out.println(">>> DatabaseInitTest: Iniciando carga de datos de prueba...");

        System.out.println("========================================");
        System.out.println("Inicializando base de datos TEST");
        System.out.println("========================================");

        Administrador admin1 = new Administrador("Administrador Principal", "admin@animalheart.com", "admin123");
        administradorRepository.save(admin1);
        System.out.println("Administrador creado");

        Veterinario vet1 = Veterinario.builder()
            .nombre("Dr. Juan Pérez")
            .especialidad("Cirugía")
            .nombreUsuario("juanPerez")
            .contrasenia("123")
            .imagen("/assets/images/drJuan.jpeg")
            .activo(1)
            .consultas(100)
            .build();
        veterinarioRepository.save(vet1);
        
        admin1.agregarVeterinario(vet1);
        administradorRepository.save(admin1);
        
        System.out.println("Veterinario creado: juanPerez | Contraseña: 123 | Activo: Sí");

        Cliente cliente1 = new Cliente("123", "Carlos Ruiz", "carlos@gmail.com", "3214567890");
        Cliente cliente2 = new Cliente("456", "Lucía Gómez", "lucia@gmail.com", "3101234567");
        Cliente cliente3 = new Cliente("789", "Mateo Torres", "mateo@gmail.com", "3149876543");
        Cliente cliente4 = new Cliente("101", "Ana López", "ana@gmail.com", "3151122334");
        Cliente cliente5 = new Cliente("202", "Diego Pérez", "diego@gmail.com", "3002233445");

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3, cliente4, cliente5));
        System.out.println("✓ Clientes base creados: 5");

        Mascota mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascota1.setCliente(cliente1);
        mascotaRepository.save(mascota1);
        System.out.println("Mascota base creada: Firulais (de Carlos Ruiz)");

        Medicamento med1 = new Medicamento("Amoxicilina", 2.0f, 4.5f, 100, 10);
        Medicamento med2 = new Medicamento("Ivermectina", 1.5f, 3.0f, 80, 5);
        Medicamento med3 = new Medicamento("Ketoprofeno", 2.5f, 5.0f, 60, 12);
        Medicamento med4 = new Medicamento("Metronidazol", 1.8f, 3.5f, 90, 8);
        Medicamento med5 = new Medicamento("Cefalexina", 2.2f, 4.0f, 70, 6);
        Medicamento med6 = new Medicamento("Doxiciclina", 3.0f, 6.0f, 50, 4);
        Medicamento med7 = new Medicamento("Prednisona", 1.0f, 2.5f, 120, 15);
        Medicamento med8 = new Medicamento("Meloxicam", 2.8f, 5.5f, 55, 7);
        Medicamento med9 = new Medicamento("Enrofloxacina", 3.5f, 7.0f, 40, 3);
        Medicamento med10 = new Medicamento("Omeprazol", 1.2f, 2.8f, 110, 9);

        medicamentosRepository.saveAll(Arrays.asList(med1, med2, med3, med4, med5));
        medicamentosRepository.saveAll(Arrays.asList(med6, med7, med8, med9, med10));
        System.out.println("Medicamentos creados: 10");

        Tratamiento tratamiento1 = new Tratamiento(LocalDate.now(), 2);
        tratamiento1.setMascota(mascota1);
        tratamiento1.setMedicamento(med1);
        tratamiento1.setVeterinario(vet1);
        
        Tratamiento tratamiento11 = new Tratamiento(LocalDate.now().minusDays(20), 2);
        tratamiento11.setMascota(mascota1);
        tratamiento11.setMedicamento(med4);
        tratamiento11.setVeterinario(vet1);
    
        tratamientosRepository.save(tratamiento1);
        tratamientosRepository.save(tratamiento11);
        System.out.println("Tratamientos creados: 2");

        List<String> nombresClientes = Arrays.asList(
            "Carlos", "Lucía", "Mateo", "Ana", "Diego", "Laura", "Andrés", "Paula", "Felipe", "Valentina",
            "Sebastián", "Camila", "Juan", "Daniela", "Santiago", "Isabella", "Tomás", "Gabriela",
            "Martín", "Mariana", "Samuel", "Sara", "David", "Juana", "Nicolás", "Antonia"
        );

        List<String> apellidos = Arrays.asList(
            "Ruiz", "Gómez", "Torres", "López", "Pérez", "Martínez", "Castro", "Ramírez", "Hernández", "Morales",
            "Vargas", "Jiménez", "Ortega", "Castaño", "García", "Fernández", "Muñoz", "Álvarez", "Rojas", "Mendoza"
        );

        List<String> nombresMascotas = Arrays.asList(
            "Max", "Luna", "Rocky", "Bella", "Simba", "Nala", "Coco", "Tommy", "Kira",
            "Toby", "Bruno", "Milo", "Daisy", "Lucky", "Bobby", "Sasha", "Rex", "Chispa", "Zeus"
        );

        List<String> razasPerros = Arrays.asList(
            "Labrador", "Criollo", "Pastor Alemán", "Beagle", "Pug",
            "Bulldog", "Golden Retriever", "Rottweiler", "Husky Siberiano", "Dálmata"
        );

        List<String> razasGatos = Arrays.asList(
            "Siamés", "Persa", "Angora", "Criollo", "Bengalí",
            "Maine Coon", "Esfinge", "Ragdoll", "Azul Ruso", "British Shorthair"
        );

        List<String> enfermedades = Arrays.asList(
            "Ninguna", "Otitis", "Parásitos", "Dermatitis", "Gripe Felina",
            "Moquillo", "Artritis", "Obesidad", "Conjuntivitis", "Gastritis"
        );

        Random random = new Random();

        List<Cliente> clientesAdicionales = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String nombreCompleto = nombresClientes.get(random.nextInt(nombresClientes.size())) + " " +
                                    apellidos.get(random.nextInt(apellidos.size()));
            Cliente c = new Cliente(
                String.valueOf(1000 + i),  
                nombreCompleto,
                "cliente" + (i + 5) + "@gmail.com",
                "3" + String.format("%09d", 100000000 + random.nextInt(899999999))
            );
            clientesAdicionales.add(c);
        }
        clienteRepository.saveAll(clientesAdicionales);
        System.out.println("✓ Clientes adicionales creados: 5");

        List<Cliente> todosLosClientes = new ArrayList<>();
        todosLosClientes.add(cliente1);
        todosLosClientes.add(cliente2);
        todosLosClientes.add(cliente3);
        todosLosClientes.add(cliente4);
        todosLosClientes.add(cliente5);
        todosLosClientes.addAll(clientesAdicionales);

        List<Mascota> mascotasAdicionales = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String nombreMascota = nombresMascotas.get(random.nextInt(nombresMascotas.size())) + i; 
            boolean esPerro = random.nextBoolean();
            String tipo = esPerro ? "Perro" : "Gato";
            String raza = esPerro 
                ? razasPerros.get(random.nextInt(razasPerros.size())) 
                : razasGatos.get(random.nextInt(razasGatos.size()));

            String fotoUrl = esPerro 
                ? "/assets/images/defaultPerro.jpg"
                : "/assets/images/defaultGato.png";

            String enfermedad = enfermedades.get(random.nextInt(enfermedades.size()));
            Double peso = 2.0 + (30.0 * random.nextDouble());
            peso = Math.round(peso * 10.0) / 10.0;
            String estado = enfermedad.equals("Ninguna") ? "Sano" : "Enfermo";
            Boolean activo = true; 

            Mascota m = new Mascota(
                nombreMascota,
                raza,
                1 + random.nextInt(15),
                tipo,
                enfermedad,
                peso,
                fotoUrl,
                estado,
                activo
            );
            m.setCliente(todosLosClientes.get(random.nextInt(todosLosClientes.size())));
            mascotasAdicionales.add(m);
        }
        mascotaRepository.saveAll(mascotasAdicionales);
        System.out.println("Mascotas adicionales creadas: 5");

        System.out.println("========================================");
        System.out.println("Base de datos TEST inicializada");
        System.out.println("Total Veterinarios: " + veterinarioRepository.count());
        System.out.println("Total Clientes: " + clienteRepository.count());
        System.out.println("Total Mascotas: " + mascotaRepository.count());
        System.out.println("Total Medicamentos: " + medicamentosRepository.count());
        System.out.println("Total Tratamientos: " + tratamientosRepository.count());
        System.out.println("========================================");
    }
}