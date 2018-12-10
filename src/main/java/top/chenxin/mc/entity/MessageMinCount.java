package top.chenxin.mc.entity;

public class MessageMinCount extends BaseEntity {

    public static final int TypePush = 1; // push
    public static final int TypePop = 2; // push

    // 统计维度:1=push,2=pop
    private Integer type;

    // 统计数值
    private Long num;

    // 创建时间
    private Integer createDate;


    public static int getTypePush() {
        return TypePush;
    }

    public static int getTypePop() {
        return TypePop;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }
}
