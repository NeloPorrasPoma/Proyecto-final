/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import modelo.Producto;
import conexion.conexion;
import DAO.PedidoDAO;

public class ProcesarPagoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        String usuario = (String) session.getAttribute("usuario");

        PedidoDAO pedidoDAO = new PedidoDAO();
        int clienteId = pedidoDAO.getClientIdByUsuario(usuario);

        if (carrito != null && !carrito.isEmpty() && clienteId > 0) {
            try (Connection conn = conexion.conectar()) {
                // Inserta un nuevo pedido
                String sqlPedido = "INSERT INTO pedidos (CLIENTE_ID, FECHA, TOTAL, ESTADO) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psPedido = conn.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psPedido.setInt(1, clienteId);
                    psPedido.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    psPedido.setDouble(3, (double) session.getAttribute("total"));
                    psPedido.setString(4, "Pendiente");
                    psPedido.executeUpdate();

                    // Obtén el ID del pedido recién insertado
                    int pedidoId;
                    try (var rs = psPedido.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedidoId = rs.getInt(1);
                        } else {
                            throw new SQLException("Error al obtener el ID del pedido.");
                        }
                    }

                    // Inserta los detalles del pedido
                    String sqlDetallePedido = "INSERT INTO detalle_pedido (PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement psDetallePedido = conn.prepareStatement(sqlDetallePedido)) {
                        for (Producto producto : carrito) {
                            psDetallePedido.setInt(1, pedidoId);
                            psDetallePedido.setInt(2, producto.getId());
                            psDetallePedido.setInt(3, producto.getStock());
                            psDetallePedido.setDouble(4, producto.getPrecio());
                            psDetallePedido.addBatch();
                        }
                        psDetallePedido.executeBatch();
                    }
                }

                // Limpiar carrito y redirigir a index.jsp con mensaje de éxito
                session.removeAttribute("carrito");
                session.removeAttribute("total");
                request.getSession().setAttribute("mensaje", "Pago procesado correctamente");
                response.sendRedirect("index.jsp");

            } catch (SQLException e) {
                throw new ServletException("Error al procesar el pago", e);
            }
        } else {
            response.sendRedirect("carrito.jsp?error=empty");
        }
    }
}
