package com.clase.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clase.modelo.Cita;

public class CitaDAOMySQL {

    public List<Cita> cargarCitasPaciente(String dniPaciente) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.idcita, c.iddoc, c.fechacita, c.horacita, "
                + "c.estado, d.apeldoc, d.nomdoc, d.espedoc "
                + "FROM citas c "
                + "JOIN doctores d ON d.iddoc = c.iddoc "
                + "WHERE c.dnipac = ? "
                + "ORDER BY c.fechacita, c.horacita";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dniPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(new Cita(
                            rs.getInt("idcita"),
                            rs.getInt("iddoc"),
                            rs.getDate("fechacita").toLocalDate(),
                            java.time.LocalTime.parse(rs.getString("horacita")),
                            rs.getString("estado"),
                            rs.getString("apeldoc") + ", " + rs.getString("nomdoc"),
                            rs.getString("espedoc")));
                }
            }
        }

        return citas;
    }
}
