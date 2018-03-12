package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/19.
 */

public class GridRecordBean implements Serializable {


    private String id;
    private String task_id;
    private String patrol_object_id;
    private String problem_desc;
    private double x;
    private double y;
    private String address;
    private String is_have_problem;
    private String create_time;
    private String GridArea;
    private String SupervisePerson;
    private String task_name;
    private String patrol_object_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getPatrol_object_id() {
        return patrol_object_id;
    }

    public void setPatrol_object_id(String patrol_object_id) {
        this.patrol_object_id = patrol_object_id;
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public void setProblem_desc(String problem_desc) {
        this.problem_desc = problem_desc;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_have_problem() {
        return is_have_problem;
    }

    public void setIs_have_problem(String is_have_problem) {
        this.is_have_problem = is_have_problem;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGridArea() {
        return GridArea;
    }

    public void setGridArea(String GridArea) {
        this.GridArea = GridArea;
    }

    public String getSupervisePerson() {
        return SupervisePerson;
    }

    public void setSupervisePerson(String SupervisePerson) {
        this.SupervisePerson = SupervisePerson;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getPatrol_object_name() {
        return patrol_object_name;
    }

    public void setPatrol_object_name(String patrol_object_name) {
        this.patrol_object_name = patrol_object_name;
    }
}
