package org.inbloom.model.student;

import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: paullawler
 * Date: 11/5/13
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class StudentFactory {

    private static ObjectMapper mapper = new ObjectMapper();

    public static List<Student> createStudents(String json) {
        List<Student> students = Lists.newArrayList();
        try {
            JsonNode results = mapper.readTree(json);
            Iterator<JsonNode> iterator = results.getElements();
            while (iterator.hasNext()) {
                students.add(createStudent(iterator.next()));
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return students;
    }

    public static Student createStudent(String json) {
        Student student = null;
        try {
            JsonNode result = mapper.readTree(json);
            student = createStudent(result);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return student;
    }

    private static Student createStudent(JsonNode jsonNode) throws IOException {
        return mapper.readValue(jsonNode, Student.class);
    }

}
