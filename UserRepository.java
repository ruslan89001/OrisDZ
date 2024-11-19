import java.util.List;

public interface UserRepository extends CrudRepository<User>{
    List<User> findAllByAge(Integer age);
    void saveAll(List<User> users);

    List<User> findAllByEmail(String email);

    List<User> findAllByPhoneNumber(String phoneNumber);

    List<User> findAllByLicenseNumber(String licenseNumber);
}
