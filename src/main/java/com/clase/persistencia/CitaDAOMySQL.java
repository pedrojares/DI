package com.clase.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.time.LocalDate;
import java.time.LocalTime;

import com.clase.modelo.Cita;

public class CitaDAOMySQL {

    public int guardarCita(Cita cita) throws SQLException {
        String sql = "INSERT INTO citas "
                + "(dnipac, iddoc, fechacita, horacita, motivo, diagnostico, tratamiento, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cita.getCitadnipac());
            ps.setInt(2, cita.getCitaiddoc());
            ps.setDate(3, java.sql.Date.valueOf(cita.getFechacita()));
            ps.setString(4, cita.getHoracita().toString());
            ps.setString(5, cita.getMotivo());
            ps.setString(6, cita.getDiagnostico());
            ps.setString(7, cita.getTratamiento());
            ps.setString(8, cita.getEstadocita());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        throw new SQLException("No se recibió el ID de la cita guardada.");
    }

    public void modificarCita(Cita cita) throws SQLException {
        String sql = "UPDATE citas SET dnipac = ?, iddoc = ?, fechacita = ?, horacita = ?, "
                + "motivo = ?, diagnostico = ?, tratamiento = ?, estado = ? "
                + "WHERE idcita = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cita.getCitadnipac());
            ps.setInt(2, cita.getCitaiddoc());
            ps.setDate(3, java.sql.Date.valueOf(cita.getFechacita()));
            ps.setString(4, cita.getHoracita().toString());
            ps.setString(5, cita.getMotivo());
            ps.setString(6, cita.getDiagnostico());
            ps.setString(7, cita.getTratamiento());
            ps.setString(8, cita.getEstadocita());
            ps.setInt(9, cita.getIdcita());
            ps.executeUpdate();
        }
    }

    public Set<LocalTime> cargarHorasOcupadas(String idDoctor, LocalDate fecha,
            Integer idCitaExcluir) throws SQLException {
        Set<LocalTime> horasOcupadas = new TreeSet<>();
        String sql = "SELECT horacita FROM citas "
                + "WHERE iddoc = ? AND fechacita = ? "
                + "AND LOWER(COALESCE(estado, '')) <> 'anulada' "
                + "AND (? IS NULL OR idcita <> ?)";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idDoctor));
            ps.setDate(2, java.sql.Date.valueOf(fecha));
            if (idCitaExcluir == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, idCitaExcluir);
                ps.setInt(4, idCitaExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    horasOcupadas.add(LocalTime.parse(rs.getString("horacita")));
                }
            }
        }

        return horasOcupadas;
    }

    public List<Cita> cargarCitasPaciente(String dniPaciente) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.idcita, c.dnipac, c.iddoc, c.fechacita, c.horacita, "
                + "c.motivo, c.diagnostico, c.tratamiento, c.estado, "
                + "d.apeldoc, d.nomdoc, d.espedoc "
                + "FROM citas c "
                + "JOIN doctores d ON d.iddoc = c.iddoc "
                + "WHERE c.dnipac = ? "
                + "ORDER BY c.fechacita, c.horacita";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dniPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita(
                            rs.getInt("idcita"),
                            rs.getString("dnipac"),
                            rs.getInt("iddoc"),
                            rs.getDate("fechacita").toLocalDate(),
                            java.time.LocalTime.parse(rs.getString("horacita")),
                            rs.getString("motivo"),
                            rs.getString("diagnostico"),
                            rs.getString("tratamiento"),
                            rs.getString("estado"),
                            rs.getString("apeldoc") + ", " + rs.getString("nomdoc"),
                            rs.getString("espedoc"));
                    citas.add(cita);
                }
            }
        }

        return citas;
    }
}
