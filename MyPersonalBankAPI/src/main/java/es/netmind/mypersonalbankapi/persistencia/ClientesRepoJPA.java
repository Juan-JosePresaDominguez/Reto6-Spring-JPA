package es.netmind.mypersonalbankapi.persistencia;

import es.netmind.mypersonalbankapi.exceptions.ClienteException;
import es.netmind.mypersonalbankapi.exceptions.ClienteNotFoundException;
import es.netmind.mypersonalbankapi.exceptions.ErrorCode;
import es.netmind.mypersonalbankapi.modelos.clientes.Cliente;
import es.netmind.mypersonalbankapi.modelos.clientes.Empresa;
import es.netmind.mypersonalbankapi.modelos.clientes.Personal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Repository
public class ClientesRepoJPA implements IClientesRepo {

    @PersistenceContext // Accede al emf; emf.createEntityManager();
    EntityManager em;

    private static ClientesRepoJPA instance;

    private static String db_url = null;
    //private String db_url = null;

    private Connection conn = null;

    //La URL se debe definir en RepoConfig.java, sino se produce el error "java.sql.SQLException: The url cannot be null"
//    public ClientesDBRepo() throws Exception {
//        PropertyValues props = new PropertyValues();
//        db_url = props.getPropValues().getProperty("db_url");
//    }

    public static ClientesRepoJPA getInstance() {
        if (instance == null) {
            try {
                instance = new ClientesRepoJPA();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    @Override
    public List<Cliente> getAll() throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente u WHERE 1";

        try (
                //Connection conn = DriverManager.getConnection(db_url);
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("dtype").equals("Empresa")) {
                    clientes.add(new Empresa(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("direccion"),
                            rs.getDate("alta").toLocalDate(),
                            rs.getBoolean("activo"),
                            rs.getBoolean("moroso"),
                            rs.getString("cif"),
                            new String[]{rs.getString("unidades_de_negocio")}));
                } else {
                    clientes.add(new Personal(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("direccion"),
                            rs.getDate("alta").toLocalDate(),
                            rs.getBoolean("activo"),
                            rs.getBoolean("moroso"),
                            rs.getString("dni")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return clientes;
    }

    @Override       //Devuelve el cliente indicado por parámetro
    public Cliente getClientById(Integer id) throws Exception {
        return em.find(Cliente.class, id);
    }

    @Override       //INSERT
    @Transactional
    public Cliente addClient(Cliente cliente) throws Exception {
        em.persist(cliente);
        return cliente;
    }

    @Override
    @Transactional
    public boolean deleteClient(Cliente cliente) throws Exception {
        String sql = "DELETE FROM cliente WHERE id=?";

        try (
                //Connection conn = DriverManager.getConnection(db_url);
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, cliente.getId());

            int rows = stmt.executeUpdate();
            System.out.println(rows);

            if (rows <= 0) {
                throw new ClienteNotFoundException();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @Override
    @Transactional
    public Cliente updateClient(Cliente cliente) throws Exception {
        String sql = "UPDATE cliente SET dtype=?, nombre=?, email=?, direccion=?, alta=?, activo=?, moroso=?, cif=?, unidades_de_negocio=?, dni=? WHERE id=?";

//        try (
//                Connection conn = DriverManager.getConnection(db_url);
//                PreparedStatement stmt = conn.prepareStatement(sql);
//        ) {
        try {
            System.out.println("Traza 1");
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (cliente instanceof Personal) {
                System.out.println("Traza 2");
                stmt.setString(1, "Personal");
                stmt.setString(8, null);
                stmt.setString(9, null);
                stmt.setString(10, ((Personal) cliente).getDni());
            } else {
                System.out.println("Traza 3");
                stmt.setString(1, "Empresa");
                stmt.setString(8, ((Empresa) cliente).getCif());
                stmt.setString(9, Arrays.toString(((Empresa) cliente).getUnidadesNegocio()));
                stmt.setString(10, null);
            }

            System.out.println("Traza 4");
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getAlta().toString());
            stmt.setBoolean(6, cliente.isActivo());
            stmt.setBoolean(7, cliente.isMoroso());
            stmt.setInt(11, cliente.getId());

            System.out.println("Traza 5");
            int rows = stmt.executeUpdate();

            System.out.println(rows);

            if (rows <= 0) {
                throw new ClienteNotFoundException();     // Si queremos que devuelva excepción al no encontrar cliente
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return cliente;
    }

    public void setDb_url(String connectUrl) {
        this.db_url = connectUrl;
    }

    public void connectClientRepo() throws Exception {
        try {
            conn = DriverManager.getConnection(db_url);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public void commitClientRepo() throws Exception {
        try //(                Connection conn = DriverManager.getConnection(db_url);)
        {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public void rollbackClientRepo() throws Exception {
        try //(                Connection conn = DriverManager.getConnection(db_url);)
        {
            conn.rollback();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
