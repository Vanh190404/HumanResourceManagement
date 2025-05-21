package DAO;

import Model.Position;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDAO {
    private Connection connection;

    public PositionDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Lấy tất cả vị trí từ CSDL
    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM Position";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String title = resultSet.getString("Title");
                positions.add(new Position(id, title));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }
}