/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.CategoriaDAO;
import modelo.Categoria;
import modelo.Producto;
import DAO.ProductoDAO;

public class ProductosServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    String buscarNombre = request.getParameter("buscarNombre");
    String buscarCategoria = request.getParameter("buscarCategoria");
    List<Producto> productos;

    if ("buscarProductos".equals(action)) {
        if ((buscarNombre != null && !buscarNombre.isEmpty()) || (buscarCategoria != null && !buscarCategoria.isEmpty())) {
            productos = productoDAO.buscarProductos(buscarNombre, buscarCategoria);
        } else {
            productos = productoDAO.obtenerTodosProductos();
        }

        List<Categoria> categorias = categoriaDAO.obtenerCategorias();
        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("index.jsp").forward(request, response);  // Redirigir a index.jsp
    } else if ("buscarAlmacen".equals(action)) {
        if ((buscarNombre != null && !buscarNombre.isEmpty()) || (buscarCategoria != null && !buscarCategoria.isEmpty())) {
            productos = productoDAO.buscarProductos(buscarNombre, buscarCategoria);
        } else {
            productos = productoDAO.obtenerTodosProductos();
        }

        List<Categoria> categorias = categoriaDAO.obtenerCategorias();
        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("almacen.jsp").forward(request, response);  // Redirigir a almacen.jsp
    }
}

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            // Guardar nuevo producto
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            int stock = Integer.parseInt(request.getParameter("stock"));
            String categoriaStr = request.getParameter("categoria");
            String imagen = request.getParameter("imagen");
            double precio = Double.parseDouble(request.getParameter("precio"));

            // Convertir la categoría a entero (ID)
            int categoriaId;
            try {
                categoriaId = Integer.parseInt(categoriaStr);
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "Error: La categoría no es un número válido.");
                request.getRequestDispatcher("almacen.jsp").forward(request, response);
                return;
            }

            Producto nuevoProducto = new Producto(nombre, descripcion, stock, categoriaId, imagen, precio);

            boolean guardado = productoDAO.guardarProducto(nuevoProducto);
            request.setAttribute("mensaje", guardado ? "Producto guardado exitosamente" : "Error al guardar el producto");

        } else if ("editar".equals(action)) {
            // Editar producto existente
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            int stock = Integer.parseInt(request.getParameter("stock"));
            String categoriaStr = request.getParameter("categoria");
            String imagen = request.getParameter("imagen");
            double precio = Double.parseDouble(request.getParameter("precio"));

            // Convertir la categoría a entero (ID)
            int categoriaId;
            try {
                categoriaId = Integer.parseInt(categoriaStr);
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "Error: La categoría no es un número válido.");
                request.getRequestDispatcher("almacen.jsp").forward(request, response);
                return;
            }

            Producto productoEditado = new Producto(id, nombre, descripcion, stock, categoriaId, imagen, precio);

            boolean actualizado = productoDAO.actualizarProducto(productoEditado);
            request.setAttribute("mensaje", actualizado ? "Producto actualizado exitosamente" : "Error al actualizar el producto");

        } else if ("eliminar".equals(action)) {
            // Eliminar producto
            int id = Integer.parseInt(request.getParameter("id"));
            boolean eliminado = productoDAO.eliminarProducto(id);
            request.setAttribute("mensaje", eliminado ? "Producto eliminado exitosamente" : "Error al eliminar el producto");
        } else if ("cargar".equals(action)) {
            // Cargar datos de producto para edición
            int id = Integer.parseInt(request.getParameter("id"));
            Producto producto = productoDAO.buscarProductoPorId(id);

            // Establecer los datos del producto en la solicitud
            request.setAttribute("producto", producto);
            List<Categoria> categorias = categoriaDAO.obtenerCategorias();
            request.setAttribute("categorias", categorias);
        }

        // Redirigir a almacen.jsp después de cada acción
        List<Producto> productos = productoDAO.obtenerTodosProductos();
    request.setAttribute("productos", productos);
    List<Categoria> categorias = categoriaDAO.obtenerCategorias();
    request.setAttribute("categorias", categorias);
    request.getRequestDispatcher("almacen.jsp").forward(request, response);
    }
}
