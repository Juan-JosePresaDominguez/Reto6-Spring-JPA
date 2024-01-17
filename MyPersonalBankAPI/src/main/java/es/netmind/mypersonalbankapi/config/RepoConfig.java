package es.netmind.mypersonalbankapi.config;

import es.netmind.mypersonalbankapi.persistencia.ClientesDBRepo;
import es.netmind.mypersonalbankapi.persistencia.ClientesInMemoryRepo;
import es.netmind.mypersonalbankapi.persistencia.IClientesRepo;
import es.netmind.mypersonalbankapi.properties.PropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

import static es.netmind.mypersonalbankapi.persistencia.ClientesInMemoryRepo.getInstance;

@Configuration
public class RepoConfig {

//    @Bean
//    public String getUrlConn() throws IOException {
//        PropertyValues props = new PropertyValues();
//        String connectUrl = props.getPropValues().getProperty("db_url");
//        return connectUrl;
//    }

    //@Autowired
    @Value("${db_url}") //Inyecta un valor de propiedad
    String connectUrl;

    @Bean
    @Profile("default")
    public IClientesRepo getClientesDBRepo() throws Exception {
        System.out.println("Creando RepoConfig... JDBC");
        ClientesDBRepo repo = new ClientesDBRepo();
        repo.setDb_url(connectUrl);
        return repo;
    }

    @Bean
    @Profile("testing")
    public IClientesRepo getClientesInMemoryRepo() throws Exception {
        System.out.println("Creando RepoConfig... InMemory");
        ClientesInMemoryRepo repo = new ClientesInMemoryRepo();
        return repo;
    }

}
