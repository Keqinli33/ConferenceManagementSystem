package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import com.avaje.ebean.Model;

/**
 * Created by shuang on 3/28/17.
 */
public class Paper extends Model {
    private static final long serialVersionUID = 1L;
    @Id
    public long id;

    public String title;
    public String contactemail;
    public String firstname1;
    public String lastname1;
    public String email1;
    public String affilation1;
    public String firstname2;
    public String lastname2;
    public String email2;
    public String affilation2;
    public String firstname3;
    public String lastname3;
    public String email3;
    public String affilation3;
    public String firstname4;
    public String lastname4;
    public String email4;
    public String affilation4;
    public String firstname5;
    public String lastname5;
    public String email5;
    public String affilation5;
    public String firstname6;
    public String lastname6;
    public String email6;
    public String affilation6;
    public String firstname7;
    public String lastname7;
    public String email7;
    public String affilation7;
    public String otherauthor;
    public String candidate;
    public String volunteer;
    public String paperabstract;
    public String ifsubmit;
    public String format;
    public String papersize;
    public String conference;
    public String topic;
    public String status;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date date;
}
