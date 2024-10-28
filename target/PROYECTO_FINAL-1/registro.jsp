<%-- 
    Document   : registro
    Created on : 18 sept. 2024, 17:23:49
    Author     : UsuarioTK
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%
    Usuario usuario = (Usuario) session.getAttribute("user");
 
    if (usuario != null) {
        response.sendRedirect("index.jsp"); 
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Usuario | XCEL_SERVER</title>
        <link rel="stylesheet" href="css/estilosGenerales.css">
        <link rel="stylesheet" href="css/estilosRegistro.css">
    </head>
    <body>
        <header>
            <nav>
                <ul class="nav-center"> 
                    <li><a href="productos.jsp">PRODUCTOS</a></li>
                    <li><a href="nosotros.jsp">NOSOTROS</a></li>
                    <li><a href="carrito.jsp">CARRITO</a></li>
                    <li><a href="contacto.jsp">CONTACTO</a></li>
                </ul>
            </nav>
        </header>
        <main>
            <div id="registro">
                <img src="images/logo.png" alt="LOGO" class="registro-image">
                <h2>Registro de Usuario</h2>
                <form action="RegistroServlet" method="post" class="form-registro">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>

                    <label for="apellidos">Apellidos:</label>
                    <input type="text" id="apellidos" name="apellidos" required>

                    <label for="docIdentidad">Documento de Identidad:</label>
                    <input type="text" id="docIdentidad" name="docIdentidad" required>

                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion">

                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono">

                    <label for="correo">Correo Electrónico:</label>
                    <input type="email" id="correo" name="correo" required>

                    <label for="usuario">Usuario:</label>
                    <input type="text" id="usuario" name="usuario" required>

                    <label for="contrasena">Contraseña:</label>
                    <input type="password" id="contrasena" name="contrasena" required>

                    <button type="submit">Registrar</button>
                </form>

                <% if (request.getAttribute("error") != null) { %>
                    <p class="error-message"><%= request.getAttribute("error") %></p>
                <% } %>
 
                <p class="enlace">¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>
                <p class="enlace"><a href="index.jsp">Cancelar</a></p>
            </div>
        </main>
                <button onclick="window.location.href='contacto.jsp'" class="btn-ayuda">¿Necesitas ayuda?</button>
        <footer>
            <p>&copy; 2024 XCEL_SERVER. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>
