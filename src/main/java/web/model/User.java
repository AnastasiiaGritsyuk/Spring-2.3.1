package web.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",  nullable = false)
    private String name;

    @Column(name = "age")
    private Byte age;

    @Column(name = "email", unique = true)
    private String email;

    public User() {}

    public User(String firstName, Byte age, String email) {
        this.name = firstName;
        this.age = age;
        this.email = email;
    }

    public User(User other) {
        this.id = other.id;
        this.name = other.name;
        this.age = other.age;
        this.email = other.email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge (Byte age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
