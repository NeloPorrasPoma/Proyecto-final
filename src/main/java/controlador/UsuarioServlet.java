/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;
 
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.UsuarioAdminDAO;
import DAO.DatosPersonalesDAO;
import modelo.Usuario;
import modelo.DatosPersonales;
import modelo.Rol;
import DAO.RolDAO;

public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioAdminDAO usuarioDAO = new UsuarioAdminDAO();
    private DatosPersonalesDAO datosPersonalesDAO = new DatosPersonalesDAO();
    private RolDAO rolDAO = new RolDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "buscarUsuario":
                    buscarUsuario(request, response);
                    break;
                                    case "cargar":
                    cargarUsuario(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "guardar":
                    guardarUsuario(request, response);
                    break;
                case "editar":
                    editarUsuario(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                case "cargar":
                    cargarUsuario(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void buscarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String nombre = request.getParameter("buscarUsuarioNombre");
        List<Usuario> listaUsuarios = usuarioDAO.buscarUsuariosPorNombre(nombre);
        request.setAttribute("usuarios", listaUsuarios);
                List<Rol> roles = rolDAO.listarRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
        request.setAttribute("usuarios", listaUsuarios);
                List<Rol> roles = rolDAO.listarRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void guardarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String docIdentidad = request.getParameter("docIdentidad");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String usuarioNombre = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        int rolId = Integer.parseInt(request.getParameter("rol"));

        Usuario usuario = new Usuario();
        usuario.setUsuario(usuarioNombre);
        usuario.setContrasena(contrasena);
        usuario.setRol(String.valueOf(rolId));

        DatosPersonales datosPersonales = new DatosPersonales();
        datosPersonales.setNombre(nombre);
        datosPersonales.setApellidos(apellidos);
        datosPersonales.setDocIdentidad(docIdentidad);
        datosPersonales.setDireccion(direccion);
        datosPersonales.setTelefono(telefono);
        datosPersonales.setCorreo(correo);

        int usuarioId = usuarioDAO.registrarUsuario(usuario);
        datosPersonales.setUsuarioId(usuarioId);

        datosPersonalesDAO.registrarDatosPersonales(datosPersonales);

        response.sendRedirect("UsuarioServlet");
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String docIdentidad = request.getParameter("docIdentidad");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String usuarioNombre = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        int rolId = Integer.parseInt(request.getParameter("rol"));

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsuario(usuarioNombre);
        usuario.setContrasena(contrasena);
        usuario.setRol(String.valueOf(rolId));

        usuarioDAO.actualizarUsuario(usuario);

        DatosPersonales datosPersonales = new DatosPersonales();
        datosPersonales.setNombre(nombre);
        datosPersonales.setApellidos(apellidos);
        datosPersonales.setDocIdentidad(docIdentidad);
        datosPersonales.setDireccion(direccion);
        datosPersonales.setTelefono(telefono);
        datosPersonales.setCorreo(correo);
        datosPersonales.setUsuarioId(id);

        datosPersonalesDAO.actualizarDatosPersonales(datosPersonales);

        response.sendRedirect("UsuarioServlet");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        usuarioDAO.eliminarUsuario(id);
        datosPersonalesDAO.eliminarDatosPersonales(id);

        response.sendRedirect("UsuarioServlet");
    }

    private void cargarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
        DatosPersonales datosPersonales = datosPersonalesDAO.obtenerDatosPersonalesPorUsuarioId(id);

        request.setAttribute("usuario", usuario);
        request.setAttribute("datosPersonales", datosPersonales);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }
}