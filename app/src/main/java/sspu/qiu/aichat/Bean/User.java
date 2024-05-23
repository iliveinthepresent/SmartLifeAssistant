package sspu.qiu.aichat.Bean;

public class User {
    private int id;
    private String name;
    private String password;
    private String phone;
    public User(String name, String password, String phone) {
        super();
        this.name = name;
        this.password = password;
        this.phone = phone;
    }
    public User(String name) {
        super();
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public User() {
        super();
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}