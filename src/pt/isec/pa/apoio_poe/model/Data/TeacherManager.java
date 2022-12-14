package pt.isec.pa.apoio_poe.model.Data;

import pt.isec.pa.apoio_poe.data.Data;
import pt.isec.pa.apoio_poe.model.Data.Teacher.Teacher;
import pt.isec.pa.apoio_poe.model.Data.Teacher.TeacherOrder;
import pt.isec.pa.apoio_poe.model.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TeacherManager extends Manager<Teacher> {
    public TeacherManager(Data data) {
        super(data);
    }

    @Override
    public boolean readFile(String filePath) {
        List<Teacher> items = new ArrayList<>();
        try (Scanner input = new Scanner(new File(filePath))) {
            input.useDelimiter(",\\s*|\r\n|\n");
            input.useLocale(Locale.US);
            while (input.hasNext()) {
                String name = input.next();
                String email = input.next();
                items.add(new Teacher(email, name));
            }
        } catch (FileNotFoundException e) {
            Log.getInstance().addMessage("The file does not exist");
            return false;
        } catch (NoSuchElementException e) {
            Log.getInstance().addMessage("Error reading");
        }
        items.forEach(this::insert);
        return true;
    }

    @Override
    public boolean insert(Teacher item) {
        if (!item.getEmail().contains("@isec.pt")) {
            Log.getInstance().addMessage(item.getEmail() + " is not a valid email because is not from isec");
            return false;
        }
        return super.insert(item);
    }

    public float average() {
        float count = 0;
        for (Teacher teacher : list) {
            count += teacher.getAmount();
        }
        return count / list.size();
    }

    public int highest() {
        if (!list.isEmpty()) {
            Teacher teacher = Collections.max(list, new TeacherOrder());
            return teacher.getAmount();
        }
        return 0;
    }

    public int lowest() {
        if (!list.isEmpty()) {
            Teacher teacher = Collections.min(list, new TeacherOrder());
            return teacher.getAmount();
        }
        return 0;
    }

    public String getTeacherList() {
        StringBuilder builder = new StringBuilder();
        for (Teacher teacher : list) {
            builder.append(teacher.getEmail()).append("\n");
        }
        return builder.toString();
    }

    public List<Teacher> top5() {
        List<Teacher> aux = new ArrayList<>(list);
        Collections.sort(aux,new TeacherOrder());
        return aux;
    }

    public List<Teacher> getTeachers() {
        return new ArrayList<>(list);
    }
}
