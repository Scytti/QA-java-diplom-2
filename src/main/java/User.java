import com.github.javafaker.Faker;

public class User {
    static Faker faker = new Faker();

    public String email;
    public String password;
    public String name;

    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;

    }

    public User(){
    }

    public static User getRandom() {

        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6,15);
        final String name = faker.name().firstName();
        return new User(email, password, name);

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAnotherEmail() {
        return faker.internet().emailAddress();
    }

    public String getAnotherPassword() {
        return faker.internet().password(6,15);
    }

    public String getAnotherName() {
        return faker.name().firstName();
    }
}
