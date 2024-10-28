/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import DAO.UsuarioDAO;
import DAO.DatosPersonalesDAO;
import modelo.Usuario;
import modelo.DatosPersonales;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String docIdentidad = request.getParameter("docIdentidad");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String usuarioNombre = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        Usuario usuario = new Usuario();
        usuario.setUsuario(usuarioNombre);
        usuario.setContrasena(contrasena);
        usuario.setRol("Cliente"); // Asignar rol cliente
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        DatosPersonales datosPersonales = new DatosPersonales(nombre, apellidos, docIdentidad, direccion, telefono, correo);
        
        try {
            int usuarioId = usuarioDAO.registrarUsuario(usuario); // Almacena el usuario y obtiene su ID
            if (usuarioId > 0) {
                datosPersonales.setUsuarioId(usuarioId);
                DatosPersonalesDAO datosDAO = new DatosPersonalesDAO();
                datosDAO.registrarDatosPersonales(datosPersonales); // Guarda datos personales
            }
            response.sendRedirect("login.jsp?registroExitoso=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("registro.jsp?error=true");
        }
    }
}
