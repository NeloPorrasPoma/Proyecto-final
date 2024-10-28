/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;
import DAO.PedidoDAO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReporteServlet extends HttpServlet {
    private PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("mostrar".equals(accion)) {
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            List<Pedido> pedidos;

            if (fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                // Si no se selecciona rango de fechas, mostrar total de pedidos
                double totalVentas = pedidoDAO.obtenerTotalVentas();
                int cantidadProductosVendidos = pedidoDAO.obtenerCantidadProductosVendidos();
                
                // Guardar el reporte en la base de datos
                int empleadoId = 1;  
                pedidoDAO.guardarReporte(totalVentas, cantidadProductosVendidos, empleadoId);

                request.setAttribute("totalVentas", totalVentas);
                request.setAttribute("cantidadProductosVendidos", cantidadProductosVendidos);
            } else {
                // Si se selecciona un rango de fechas, mostrar pedidos en ese rango
                pedidos = pedidoDAO.buscarPedidosPorFecha(fechaInicio, fechaFin);
                request.setAttribute("pedidos", pedidos);
            }
            
            request.getRequestDispatcher("reporte.jsp").forward(request, response);
        } else if ("exportar".equals(accion)) {
            
        }
    }
}
