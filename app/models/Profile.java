package models;

/**
 * Created by sxh on 17/3/26.
 */
public class Profile extends Model{
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @ManyToOne
    public Title title;

    public String research;

    public String firstName;

    public String lastName;

    public String position;

    public String affiliation;

    public String email;

    public String phone;

    public String fax;

    public String address;

    public String city;

    public String country;

    public String region;

    public Long zipcode;

    public String comment;

}
