
package com.mapuni.mobileenvironment.model;


        import android.os.Parcel;
        import android.os.Parcelable;

        import com.mapuni.mobileenvironment.R;
        import com.mapuni.mobileenvironment.utils.SqliteUtil;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

/**
 * 树节点
 * @author wsc
 */
public class  Node implements Serializable{
    private Node parent;//父节点
    private List<Node> children = new ArrayList<Node>();//子节点
    private String text;//节点显示的文字
    private String value;//节点的值
    private int icon = -1;//是否显示小图标,-1表示隐藏图标
    private boolean isChecked = false;//是否处于选中状态
    private boolean isExpanded = true;//是否处于展开状态
    private boolean hasCheckBox = true;//是否拥有复选框

    /**
     * Node构造函数
     * @param text 节点显示的文字
     * @param value 节点的值
     */
    public Node(String text,String value){
        this.text = text;
        this.value = value;
    }

    /**
     * 设置父节点
     * @param node
     */
    public void setParent(Node node){
        this.parent = node;
    }
    /**
     * 获得父节点
     * @return
     */
    public Node getParent(){
        return this.parent;
    }
    /**
     * 设置节点文本
     * @param text
     */
    public void setText(String text){
        this.text = text;
    }
    /**
     * 获得节点文本
     * @return
     */
    public String getText(){
        return this.text;
    }
    /**
     * 设置节点值
     * @param value
     */
    public void setValue(String value){
        this.value = value;
    }
    /**
     * 获得节点值
     * @return
     */
    public String getValue(){
        return this.value;
    }
    /**
     * 设置节点图标文件
     * @param icon
     */
    public void setIcon(int icon){
        this.icon = icon;
    }
    /**
     * 获得图标文件
     * @return
     */
    public int getIcon(){
        return icon;
    }
    /**
     * 是否根节点
     * @return
     */
    public boolean isRoot(){
        return parent==null?true:false;
    }
    /**
     * 获得子节点
     * @return
     */
    public List<Node> getChildren(){
        return this.children;
    }
    /**
     * 添加子节点
     * @param node
     */
    public void add(Node node){
        if(!children.contains(node)){
            children.add(node);
        }
    }
    /**
     * 清除所有子节点
     */
    public void clear(){
        children.clear();
    }
    /**
     * 删除一个子节点
     * @param node
     */
    public void remove(Node node){
        if(!children.contains(node)){
            children.remove(node);
        }
    }
    /**
     * 删除指定位置的子节点
     * @param location
     */
    public void remove(int location){
        children.remove(location);
    }
    /**
     * 获得节点的级数,根节点为0
     * @return
     */
    public int getLevel(){
        return parent==null?0:parent.getLevel()+1;
    }
    /**
     * 设置节点选中状态
     * @param isChecked
     */
    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
    /**
     * 获得节点选中状态
     * @return
     */
    public boolean isChecked(){
        return isChecked;
    }
    /**
     * 设置是否拥有复选框
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
        this.hasCheckBox = hasCheckBox;
    }
    /**
     * 是否拥有复选框
     * @return
     */
    public boolean hasCheckBox(){
        return hasCheckBox;
    }

    /**
     * 是否叶节点,即没有子节点的节点
     * @return
     */
    public boolean isLeaf(){
        return children.size()<1?true:false;
    }
    /**
     * 当前节点是否处于展开状态
     * @return
     */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * 设置节点展开状态
     * @return
     */
    public void setExpanded(boolean isExpanded){
        this.isExpanded =  isExpanded;
    }
    /**
     * 递归判断父节点是否处于折叠状态,有一个父节点折叠则认为是折叠状态
     * @return
     */
    public boolean isParentCollapsed(){
        if(parent==null)return !isExpanded;
        if(!parent.isExpanded())return true;
        return parent.isParentCollapsed();
    }
    /**
     * 递归判断所给的节点是否当前节点的父节点
     * @param node 所给节点
     * @return
     */
    public boolean isParent(Node node){
        if(parent==null)return false;
        if(node.equals(parent))return true;
        return parent.isParent(node);
    }

    public static Node getAllPeople() {
        Node root, chidNode, secNode, thrNode, forNode;
        // 查找出来所有的支队和总队
        root = new Node("请选择所属网格", "0000");
        root.setExpanded(false);
        String serchCondition = "130300000000";
        String sqlDepart = "select t.grid_areacode,t.grid_areaname from T_Base_GridInfo t  where t.grid_parentgid='" + serchCondition + "'";
        ArrayList<HashMap<String, Object>> arrayListNodeDepart = SqliteUtil
                .getInstance().queryBySqlReturnArrayListHashMap(sqlDepart);

        if (arrayListNodeDepart.size() > 0) {
            for (HashMap<String, Object> hashMapNodDepart : arrayListNodeDepart) {
                chidNode = new Node(hashMapNodDepart.get("grid_areaname").toString(),
                        hashMapNodDepart.get("grid_areacode").toString());
                chidNode.setExpanded(false);
                chidNode.setParent(root);
//                chidNode.setIcon(R.mipmap.ic_launcher);// 设置图标
                root.add(chidNode);

                // 查找支队或者总队所属科室
                String sqlChildDepart = "select grid_areacode,grid_areaname from T_Base_GridInfo where grid_parentgid ='"
                        + hashMapNodDepart.get("grid_areacode").toString() + "'";
                ArrayList<HashMap<String, Object>> arrayListNodeChildDepat = SqliteUtil
                        .getInstance().queryBySqlReturnArrayListHashMap(
                                sqlChildDepart);
                for (HashMap<String, Object> hashMapNodChildDepart : arrayListNodeChildDepat) {
                    secNode = new Node(hashMapNodChildDepart.get("grid_areaname")
                            .toString(), hashMapNodChildDepart.get("grid_areacode")
                            .toString());
                    secNode.setExpanded(false);
                    secNode.setParent(chidNode);
//                    secNode.setIcon(R.mipmap.ic_launcher);// 设置图标
                    chidNode.add(secNode);
                    // 查找科室所含人员
                    String sqlChildPeople = "select grid_areaname,grid_areacode from T_Base_GridInfo where grid_parentgid ='"
                            + hashMapNodChildDepart.get("grid_areacode").toString()
                            + "'";
                    // String
                    // sqlChildPeople="select t.UserID,t.U_RealName from PC_Users t  where t.depid='"+
                    // hashMapNodChildDepart.get("depid").toString() +"'";
                    ArrayList<HashMap<String, Object>> arrayListNodeChildPeople = SqliteUtil
                            .getInstance().queryBySqlReturnArrayListHashMap(sqlChildPeople);
                    for (HashMap<String, Object> hashMapNodChildPeople : arrayListNodeChildPeople) {

                        // 查找科室所含人员
                        String sqlChild2People = "select grid_areaname,grid_areacode from T_Base_GridInfo where grid_parentgid ='"
                                + hashMapNodChildPeople.get("grid_areacode").toString()
                                + "'";
                        // String
                        // sqlChildPeople="select t.UserID,t.U_RealName from PC_Users t  where t.depid='"+
                        // hashMapNodChildDepart.get("depid").toString() +"'";
                        ArrayList<HashMap<String, Object>> arrayListNodeChildPeople1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlChild2People);
                        if (arrayListNodeChildPeople1.size() > 0) {
                            thrNode = new Node(hashMapNodChildPeople
                                    .get("grid_areaname").toString(),
                                    hashMapNodChildPeople.get("grid_areacode")
                                            .toString());
                            thrNode.setExpanded(false);
//                            thrNode.setIcon(R.mipmap.ic_launcher);// 设置图标

                        } else {
                            thrNode = new Node(hashMapNodChildPeople
                                    .get("grid_areaname").toString(),
                                    hashMapNodChildPeople.get("grid_areacode")
                                            .toString());
                            thrNode.setExpanded(false);
//                            thrNode.setIcon(R.mipmap.ic_launcher);// 设置图标

                        }
                        thrNode.setParent(secNode);
                        secNode.add(thrNode);

                        for (HashMap<String, Object> hashMapNodChildPeople1 : arrayListNodeChildPeople1) {
                            forNode = new Node(hashMapNodChildPeople1.get(
                                    "grid_areaname").toString(), hashMapNodChildPeople1
                                    .get("grid_areacode").toString());
                            forNode.setExpanded(false);
                            forNode.setParent(thrNode);
                            thrNode.add(forNode);
//                            forNode.setIcon(R.mipmap.ic_launcher);// 设置图标
                        }
                    }
                }
            }
        }
        return  root;
    }



}
