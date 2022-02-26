public class UserCredentials {

    public String email;
    public String password;

    public UserCredentials(String email, String password){
        this.email = email;
        this.password = password;
    }

    public UserCredentials(){
    }

    public static UserCredentials from (User user){
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }
}
