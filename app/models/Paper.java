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
    public String authorsinfo;
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
