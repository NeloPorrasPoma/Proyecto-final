/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.conexion;
import java.sql.Statement;  
import modelo.Usuario;

public class UsuarioDAO {
     

    public Usuario validarUsuario(String username, String password) throws SQLException {
        Usuario usuario = null;
        Connection con = conexion.conectar();
        String query = "SELECT u.ID, u.USUARIO, u.ROL_ID, r.TIPO_ROL FROM USUARIOS u JOIN ROL r ON u.ROL_ID = r.ID WHERE u.USUARIO = ? AND u.CONTRASEÑA = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("ID"));
                    usuario.setUsuario(rs.getString("USUARIO"));
                    usuario.setRol(rs.getString("TIPO_ROL"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }
        return usuario;
    }

    public boolean validarContraseña(String usuario, String contraseñaActual) throws SQLException {
        Connection con = conexion.conectar();
        String query = "SELECT 1 FROM USUARIOS WHERE USUARIO = ? AND CONTRASEÑA = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, usuario);
            ps.setString(2, contraseñaActual);
            return ps.executeQuery().next();
        }
    }

    public void actualizarContraseña(String usuario, String nuevaContraseña) throws SQLException {
        Connection con = conexion.conectar();
        String query = "UPDATE USUARIOS SET CONTRASEÑA = ? WHERE USUARIO = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nuevaContraseña);
            ps.setString(2, usuario);
            ps.executeUpdate();
        }
    }
    
    public int registrarUsuario(Usuario usuario) throws SQLException {
    int usuarioId = 0;
    String query = "INSERT INTO usuarios (USUARIO, CONTRASEÑA, ROL_ID) VALUES (?, ?, ?)";
    Connection con = conexion.conectar();
    try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, usuario.getUsuario());
        ps.setString(2, usuario.getContrasena());
        ps.setInt(3, 2);  
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            usuarioId = rs.getInt(1);  
        }
    }
    return usuarioId;
}
}
