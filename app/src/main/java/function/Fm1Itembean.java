package function;

/**
 * Created by chenjunfan on 2017/2/16.
 */

public class Fm1Itembean {
    private String title;
    private boolean ischeck;

    public boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public Fm1Itembean(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
