package client.main;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Connection {
    private static final Gson json = new Gson();
    private static final HttpClient client = HttpClientBuilder.create().build();



    public static void main(String[] args) {
        Student s = updateStudent("1");
        System.out.println(s);
    }

    public static boolean authenticate(String id, String password) {
        HttpPost req = new HttpPost("http://localhost:8080/auth");
        HttpResponse response = null;
        String json1 = null;
        try {
            req.setEntity(new UrlEncodedFormEntity(List.of(
                    new BasicNameValuePair("id", id),
                    new BasicNameValuePair("password", password)
            )));
            response = client.execute(req);
            json1 = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json.fromJson(json1, Boolean.class);
    }

    public static Courses getCoursesFromServer() {
        HttpResponse response;
        String jsonString;
        List<Course> courses = null;
        try{
            response = client.execute(new HttpGet("http://localhost:8080/courses"));
            jsonString = EntityUtils.toString(response.getEntity());
            //courses = json.fromJson(json1, json.fromJson(json1, new TypeToken<ArrayList<Course>>(){}.getType().getClass()));
            //courses = json.fromJson(json1, com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, Course.class));
            //courses = (ArrayList<Course>) Arrays.asList(json.fromJson(json1, Course.class));
            Course[] a = json.fromJson(jsonString, Course[].class);
            courses = Arrays.asList(a);
            //courses = Arrays.asList(json.fromJson(json1, new Class<Course[]>));

        }  catch (IOException e) {
            e.printStackTrace();
        }
        Courses result = new Courses();
        result.setCourses(courses);
        return result;
    }

    public static Course[][] getStudentCourseSchedule(String id) {
        HttpResponse response;
        String jsonString = null;
        Course[][] result = null;
        try{
            response = client.execute(new HttpGet("http://localhost:8080/studentSchedule/" + id));
            jsonString = EntityUtils.toString(response.getEntity());
            result = json.fromJson(jsonString, Course[][].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
        //return new Course[Person.rowNum][Person.columnNum];
    }
    public static Student updateStudent(String id) {
        HttpResponse response;
        String json1 = null;
        try {
            response = client.execute(new HttpGet("http://localhost:8080/student/" + id));
            json1 = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Student s = json.fromJson(json1, Student.class);
        //s.setSchedule2D(new Course[Person.rowNum][Person.columnNum]);
        s.setSchedule2D(Connection.getStudentCourseSchedule(s.getID()));
        return s;
    }
}
