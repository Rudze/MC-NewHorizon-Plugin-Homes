package fr.rudy.homes.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class HomesManager {

    private final Connection database;

    public HomesManager(Connection database) {
        this.database = database;
        createTable();
    }

    private void createTable() {
        try (PreparedStatement statement = database.prepareStatement(
                "CREATE TABLE IF NOT EXISTS homes (" +
                        "uuid TEXT PRIMARY KEY, " +
                        "home_world TEXT, " +
                        "home_x DOUBLE, " +
                        "home_y DOUBLE, " +
                        "home_z DOUBLE, " +
                        "home_yaw FLOAT, " +
                        "home_pitch FLOAT)"
        )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Location getHome(UUID player) {
        try (PreparedStatement statement = database.prepareStatement(
                "SELECT home_world, home_x, home_y, home_z, home_yaw, home_pitch " +
                        "FROM homes WHERE uuid = ?"
        )) {
            statement.setString(1, player.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return null;

                return new Location(
                        Bukkit.getWorld(resultSet.getString("home_world")),
                        resultSet.getDouble("home_x"),
                        resultSet.getDouble("home_y"),
                        resultSet.getDouble("home_z"),
                        resultSet.getFloat("home_yaw"),
                        resultSet.getFloat("home_pitch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean setHome(UUID player, Location home) {
        try (PreparedStatement statement = database.prepareStatement(
                "INSERT INTO homes (uuid, home_world, home_x, home_y, home_z, home_yaw, home_pitch) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET " +
                        "home_world = excluded.home_world, " +
                        "home_x = excluded.home_x, " +
                        "home_y = excluded.home_y, " +
                        "home_z = excluded.home_z, " +
                        "home_yaw = excluded.home_yaw, " +
                        "home_pitch = excluded.home_pitch"
        )) {
            statement.setString(1, player.toString());
            statement.setString(2, Objects.requireNonNull(home.getWorld()).getName());
            statement.setDouble(3, home.getX());
            statement.setDouble(4, home.getY());
            statement.setDouble(5, home.getZ());
            statement.setFloat(6, home.getYaw());
            statement.setFloat(7, home.getPitch());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
