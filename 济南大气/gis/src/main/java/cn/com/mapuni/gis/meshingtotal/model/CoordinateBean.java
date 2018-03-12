package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/19.
 */

public class CoordinateBean implements Serializable {



    private String problem_desc;
    private double x;
    private double y;
    private String GridArea;
    private String SupervisePerson;
    private String task_name;
    private String patrol_object_name;

    public CoordinateBean(String problem_desc, double x, double y, String gridArea, String supervisePerson, String task_name, String patrol_object_name) {
        this.problem_desc = problem_desc;
        this.x = x;
        this.y = y;
        GridArea = gridArea;
        SupervisePerson = supervisePerson;
        this.task_name = task_name;
        this.patrol_object_name = patrol_object_name;
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
