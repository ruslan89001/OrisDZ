import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MainRepository {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345678";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/testdb_3";

    public static void main(String[] args) throws Exception {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        UserRepository userRepository = new UsersRepositoryJdbcImpl(connection);

        List<User> usersToAdd = List.of(
                new User(null, "Alice", "Smith", 30, "alice@example.com", "+1234567890", "A12345678"),
                new User(null, "Bob", "Brown", 25, "bob@example.com", "+9876543210", "B87654321"),
                new User(null, "Charlie", "Johnson", 35, "charlie@example.com", "+1122334455", "C11223344")
        );
        userRepository.saveAll(usersToAdd);
        System.out.println("Добавлено несколько пользователей.");

        System.out.println("Список всех пользователей:");
        List<User> allUsers = userRepository.findAll();
        allUsers.forEach(user -> System.out.println(
                user.getFirstName() + " " + user.getLastName() + ", email: " + user.getEmail()
        ));

        System.out.println("Пользователи с email = 'alice@example.com':");
        List<User> usersByEmail = userRepository.findAllByEmail("alice@example.com");
        usersByEmail.forEach(user -> System.out.println(user.getFirstName() + " " + user.getLastName()));

        System.out.println("Пользователи с phone_number = '+1234567890':");
        List<User> usersByPhone = userRepository.findAllByPhoneNumber("+1122334455");
        usersByPhone.forEach(user -> System.out.println(user.getFirstName() + " " + user.getLastName()));

        System.out.println("Пользователи с license_number = 'A12345678':");
        List<User> usersByLicense = userRepository.findAllByLicenseNumber("A12345678");
        usersByLicense.forEach(user -> System.out.println(user.getFirstName() + " " + user.getLastName()));
    }
}
