package es.netmind.mypersonalbankapi.config;

import es.netmind.mypersonalbankapi.persistencia.ClientesDBRepo;
import es.netmind.mypersonalbankapi.persistencia.IClientesRepo;
import org.springframework.context.annotation.*;

@Configuration
@Import({RepoConfig.class, ControllerConfig.class})
@ComponentScan(basePackages = {"es.netmind.mypersonalbankapi.persistencia", "es.netmind.mypersonalbankapi.controladores"})
@PropertySource("classpath:config.properties")
public class SpringConfig {
}
