package com.api.api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.api.api.repository.AdministradorRepository;
import com.api.api.repository.ClienteRepository;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.repository.TratamientoRepository;
import com.api.api.repository.VeterinarioRepository;

import jakarta.transaction.Transactional;
@Component
@Transactional
public class DatabaseInit implements ApplicationRunner {

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
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {

        Administrador admin1 = new Administrador("Administrador Principal", "admin@animalheart.com", "admin123");
        administradorRepository.save(admin1);

        Veterinario vet1 = new Veterinario("Dr. Juan Pérez", "Cirugía", "juanPerez", "123", "/assets/images/drJuan.jpeg", 1, 100);
        Veterinario vet2 = new Veterinario("Dra. Laura Gómez", "Dermatología", "lauraGomez", "123", "/assets/images/draLaura.jpeg", 1, 85);
        Veterinario vet3 = new Veterinario("Dr. Carlos Rodríguez", "Cardiología", "carlosRod", "123", "/assets/images/drCarlos.jpeg", 1, 120);
        
        veterinarioRepository.saveAll(Arrays.asList(vet1, vet2, vet3));

        admin1.agregarVeterinario(vet1);
        admin1.agregarVeterinario(vet2);
        admin1.agregarVeterinario(vet3);
        administradorRepository.save(admin1);

        Cliente cliente1 = new Cliente("123", "Carlos Ruiz", "carlos@gmail.com", "3214567890");
        Cliente cliente2 = new Cliente("456", "Lucía Gómez", "lucia@gmail.com", "3101234567");
        Cliente cliente3 = new Cliente("789", "Mateo Torres", "mateo@gmail.com", "3149876543");
        Cliente cliente4 = new Cliente("101", "Ana López", "ana@gmail.com", "3151122334");
        Cliente cliente5 = new Cliente("202", "Diego Pérez", "diego@gmail.com", "3002233445");

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3, cliente4, cliente5));

        Mascota mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascota1.setCliente(cliente1);
        
        Mascota mascota2 = new Mascota("Roberto", "Persa", 2, "Gato", "Ninguna", 4.8, "/assets/images/roberto.jpeg", "Sano", true);
        mascota2.setCliente(cliente2);
        
        Mascota mascota3 = new Mascota("Rocky", "Criollo", 5, "Perro", "Patita torcida", 30.2, "/assets/images/rocky.jpeg", "Enfermo", false);
        mascota3.setCliente(cliente3);
        
        Mascota mascota4 = new Mascota("Luna", "Siamés", 4, "Gato", "Problemas digestivos", 3.5, "/assets/images/luna.jpeg", "Enfermo", true);
        mascota4.setCliente(cliente4);
        
        Mascota mascota5 = new Mascota("Max", "Labrador", 2, "Perro", "Alergias", 28.0, "/assets/images/max.jpeg", "Sano", false);
        mascota5.setCliente(cliente5);
        
        mascotaRepository.saveAll(Arrays.asList(mascota1, mascota2, mascota3, mascota4, mascota5));

        Medicamento med1 = new Medicamento("Amoxicilina", 2.0f, 4.5f, 100, 10);
        Medicamento med2 = new Medicamento("Ivermectina", 1.5f, 3.0f, 80, 5);
        Medicamento med3 = new Medicamento("Ketoprofeno", 2.5f, 5.0f, 60, 12);
        Medicamento med4 = new Medicamento("Metronidazol", 1.8f, 3.5f, 90, 8);
        Medicamento med5 = new Medicamento("Carprofeno", 2.2f, 4.0f, 75, 6);
        Medicamento med6 = new Medicamento("Meloxicam", 2.3f, 4.8f, 70, 7);
        Medicamento med7 = new Medicamento("Doxiciclina", 1.9f, 3.6f, 65, 9);
        Medicamento med8 = new Medicamento("Enrofloxacina", 2.7f, 5.2f, 85, 6);
        Medicamento med9 = new Medicamento("Prednisolona", 1.6f, 3.2f, 50, 11);
        Medicamento med10 = new Medicamento("Cefalexina", 2.4f, 4.9f, 95, 8);

        medicamentosRepository.saveAll(Arrays.asList(med1, med2, med3, med4, med5));
        medicamentosRepository.saveAll(Arrays.asList(med6, med7, med8, med9, med10));

        Tratamiento tratamiento1 = new Tratamiento(LocalDate.now(), 2);
        tratamiento1.setMascota(mascota1);
        tratamiento1.setMedicamento(med1);
        tratamiento1.setVeterinario(vet1);
        
        Tratamiento tratamiento2 = new Tratamiento(LocalDate.now().minusDays(1), 1);
        tratamiento2.setMascota(mascota2);
        tratamiento2.setMedicamento(med2);
        tratamiento2.setVeterinario(vet2);
        
        Tratamiento tratamiento3 = new Tratamiento(LocalDate.now().minusDays(3), 3);
        tratamiento3.setMascota(mascota3);
        tratamiento3.setMedicamento(med3);
        tratamiento3.setVeterinario(vet1);
        
        Tratamiento tratamiento4 = new Tratamiento(LocalDate.now().minusWeeks(1), 1);
        tratamiento4.setMascota(mascota4);
        tratamiento4.setMedicamento(med4);
        tratamiento4.setVeterinario(vet3);
        
        Tratamiento tratamiento5 = new Tratamiento(LocalDate.now().minusDays(2), 2);
        tratamiento5.setMascota(mascota5);
        tratamiento5.setMedicamento(med5);
        tratamiento5.setVeterinario(vet2);
        
        Tratamiento tratamiento6 = new Tratamiento(LocalDate.now().minusDays(5), 1);
        tratamiento6.setMascota(mascota1);
        tratamiento6.setMedicamento(med2);
        tratamiento6.setVeterinario(vet3);

        Tratamiento tratamiento7 = new Tratamiento(LocalDate.now().minusDays(7), 2);
        tratamiento7.setMascota(mascota2);
        tratamiento7.setMedicamento(med6);
        tratamiento7.setVeterinario(vet1);

        Tratamiento tratamiento8 = new Tratamiento(LocalDate.now().minusDays(10), 1);
        tratamiento8.setMascota(mascota3);
        tratamiento8.setMedicamento(med7);
        tratamiento8.setVeterinario(vet2);

        Tratamiento tratamiento9 = new Tratamiento(LocalDate.now().minusWeeks(2), 3);
        tratamiento9.setMascota(mascota4);
        tratamiento9.setMedicamento(med8);
        tratamiento9.setVeterinario(vet3);

        Tratamiento tratamiento10 = new Tratamiento(LocalDate.now().minusDays(15), 1);
        tratamiento10.setMascota(mascota5);
        tratamiento10.setMedicamento(med9);
        tratamiento10.setVeterinario(vet1);

        Tratamiento tratamiento11 = new Tratamiento(LocalDate.now().minusDays(20), 2);
        tratamiento11.setMascota(mascota1);
        tratamiento11.setMedicamento(med10);
        tratamiento11.setVeterinario(vet2);
        
        tratamientosRepository.save(tratamiento1);
        tratamientosRepository.save(tratamiento2);
        tratamientosRepository.save(tratamiento3);
        tratamientosRepository.save(tratamiento4);
        tratamientosRepository.save(tratamiento5);
        tratamientosRepository.save(tratamiento6);
        tratamientosRepository.save(tratamiento7);
        tratamientosRepository.save(tratamiento8);
        tratamientosRepository.save(tratamiento9);
        tratamientosRepository.save(tratamiento10);
        tratamientosRepository.save(tratamiento11);

        List<String> nombresClientes = Arrays.asList(
            "Carlos", "Lucía", "Mateo", "Ana", "Diego", "Laura", "Andrés", "Paula", "Felipe", "Valentina",
            "Sebastián", "Camila", "Juan", "Daniela", "Santiago", "Isabella", "Tomás", "Gabriela",
            "Martín", "Mariana", "Samuel", "Sara", "David", "Juana", "Nicolás", "Antonia",
            "Emilio", "Adriana", "Cristian", "Manuela", "Pablo", "Juliana", "Ángel", "Sofía",
            "Ricardo", "Elena", "Hugo", "Natalia", "Mauricio", "Clara", "Esteban", "Rosa",
            "Alejandro", "Inés", "Fernando", "Victoria", "Eduardo", "Lorena"
        );

        List<String> apellidos = Arrays.asList(
            "Ruiz", "Gómez", "Torres", "López", "Pérez", "Martínez", "Castro", "Ramírez", "Hernández", "Morales",
            "Vargas", "Jiménez", "Ortega", "Castaño", "García", "Fernández", "Muñoz", "Álvarez", "Rojas", "Mendoza",
            "Silva", "Córdoba", "Mejía", "Valencia", "Quintero", "Serrano", "Ibarra", "Salazar", "Reyes", "Acosta"
        );

        List<String> nombresMascotas = Arrays.asList(
            "Firulais", "Luna", "Max", "Rocky", "Bella", "Simba", "Nala", "Coco", "Tommy", "Kira",
            "Toby", "Bruno", "Milo", "Daisy", "Lucky", "Bobby", "Sasha", "Rex", "Chispa", "Zeus",
            "Pelusa", "Misha", "Galleta", "Canela", "Oreo", "Bigotes", "Sol", "Estrella", "Princesa", "Chiqui",
            "Doki", "Rocco", "Apolo", "Thor", "Athena", "Chocolate", "Copito", "Manchas", "Duque", "Titán",
            "Bolt", "Fiona", "Kiara", "Chiquita", "Maya", "Loki", "Hércules", "Juno", "Sombra", "Trueno",
            "Perla", "Nube", "Tigresa", "Bandido", "Negrito", "Colita", "Luz", "Rayito", "Pecas", "Tiza",
            "Bambi", "Canuto", "Gaspar", "Mota", "Canelita", "Miel", "Copérnico", "Pluto", "Lola", "Greta",
            "Golfo", "Tana", "Chanel", "India", "Mapache", "Balto", "Arwen", "Sam", "Rambo", "Lila",
            "Nina", "Brisa", "Sultán", "Greñas", "Cookie", "Peluchín", "Bandera", "Pirata", "Yaco", "Floki",
            "Leia", "Nieve", "Rubí", "Osa", "Tango", "Cira", "Roco", "Beto", "Cielito", "Zafiro"
        );

        List<String> razasPerros = Arrays.asList(
            "Labrador", "Criollo", "Pastor Alemán", "Beagle", "Pug",
            "Bulldog", "Golden Retriever", "Rottweiler", "Husky Siberiano", "Dálmata",
            "Chihuahua", "Doberman", "Shih Tzu", "Boxer", "Cocker Spaniel"
        );

        List<String> razasGatos = Arrays.asList(
            "Siamés", "Persa", "Angora", "Criollo", "Bengalí",
            "Maine Coon", "Esfinge", "Ragdoll", "Azul Ruso", "British Shorthair",
            "Scottish Fold", "Savannah", "Himalayo", "Bombay", "Chartreux"
        );

        List<String> enfermedades = Arrays.asList(
            "Ninguna", "Otitis", "Parásitos", "Dermatitis", "Gripe Felina",
            "Moquillo", "Artritis", "Obesidad", "Conjuntivitis", "Gastritis"
        );

        Random random = new Random();

        List<Cliente> clientes = new ArrayList<>();
        for (int i = 1; i<=45; i++) {
            String nombreCompleto = nombresClientes.get(random.nextInt(nombresClientes.size())) + " " +
                                    apellidos.get(random.nextInt(apellidos.size()));
            Cliente c = new Cliente(
                String.valueOf(1000 + i),  
                nombreCompleto,
                "cliente" + i + "@gmail.com",
                "3" + (100000000 + random.nextInt(899999999)) 
            );
            clientes.add(c);
        }
        clienteRepository.saveAll(clientes);

        List<Mascota> mascotas = new ArrayList<>();

        for (int i=1; i<=95; i++) {
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
            String estado = random.nextBoolean() ? "Sano" : "Enfermo";
            Boolean activo = random.nextBoolean();

            Mascota m = new Mascota(
                null, 
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

            m.setCliente(clientes.get(random.nextInt(clientes.size())));
            mascotas.add(m);
        }

        mascotaRepository.saveAll(mascotas);
    
}}