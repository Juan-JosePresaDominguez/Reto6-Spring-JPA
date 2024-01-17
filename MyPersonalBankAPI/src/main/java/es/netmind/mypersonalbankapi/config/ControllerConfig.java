package es.netmind.mypersonalbankapi.config;

import es.netmind.mypersonalbankapi.controladores.ClientesController;
import es.netmind.mypersonalbankapi.persistencia.IClientesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ControllerConfig {
    /*@Autowired
    IClientesRepo clientesRepo;

    @Bean
    @Lazy
    public ClientesController getClientesController() {
        System.out.println("clienteRepo: " + clientesRepo);
        ClientesController clientes = new ClientesController();
        clientes.setClientesRepo(clientesRepo);
        return clientes;
    }*/
}

