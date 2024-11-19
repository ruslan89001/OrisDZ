import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UserRepository {

    private Connection connection;

    public UsersRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() {
        String SQL_SELECT_ALL = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("license_number")
                );
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch all users", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("license_number")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(User entity) {
        String query = "INSERT INTO driver (first_name, last_name, age, email, phone_number, license_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getAge());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getPhoneNumber());
            preparedStatement.setString(6, entity.getLicenseNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE driver SET first_name = ?, last_name = ?, age = ?, email = ?, phone_number = ?, license_number = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getAge());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getPhoneNumber());
            preparedStatement.setString(6, entity.getLicenseNumber());
            preparedStatement.setLong(7, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void remove(User entity) {
        removeById(entity.getId());
    }

    @Override
    public void removeById(Long id) {
        String query = "DELETE FROM driver WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<User> findAllByAge(Integer age) {
        String query = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver WHERE age = ?";
        return findByField(query, age.toString());
    }

    @Override
    public void saveAll(List<User> users) {
        String query = "INSERT INTO driver (first_name, last_name, age, email, phone_number, license_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (User user : users) {
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setInt(3, user.getAge());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, user.getPhoneNumber());
                preparedStatement.setString(6, user.getLicenseNumber());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<User> findAllByEmail(String email) {
        String query = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver WHERE email = ?";
        return findByField(query, email);
    }

    @Override
    public List<User> findAllByPhoneNumber(String phoneNumber) {
        String query = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver WHERE phone_number = ?";
        return findByField(query, phoneNumber);
    }

    @Override
    public List<User> findAllByLicenseNumber(String licenseNumber) {
        String query = "SELECT id, first_name, last_name, age, email, phone_number, license_number FROM driver WHERE license_number = ?";
        return findByField(query, licenseNumber);
    }

    private List<User> findByField(String query, String fieldValue) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fieldValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("license_number")
                ));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return users;
    }
}
