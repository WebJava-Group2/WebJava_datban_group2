package org.datban.webjava.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.datban.webjava.models.Reservation;
import org.datban.webjava.repositories.ReservationRepository;
import org.datban.webjava.helpers.DatabaseConnector;

public class ReservationService {
    private ReservationRepository reservationRepository;

    public ReservationService() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            this.reservationRepository = new ReservationRepository(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return reservationRepository.getAll();
    }

    public boolean createReservation(Reservation reservation) throws SQLException {
        return reservationRepository.insertReservatation(reservation)>0;
    }
}
