package moe.chionlab.wechatmomentstat.Model;

/**
 * Created by chenjunfan on 2017/3/14.
 */

public class GroupBean {
    private int id;
    String group;
    boolean auto_upload;

    public GroupBean(int id, String group, boolean auto_upload) {
        this.id = id;
        this.group = group;
        this.auto_upload = auto_upload;
    }

    public GroupBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isAuto_upload() {
        return auto_upload;
    }

    public void setAuto_upload(boolean auto_upload) {
        this.auto_upload = auto_upload;
    }
}
