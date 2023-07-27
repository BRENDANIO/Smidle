import java.text.SimpleDateFormat;

public class God {

    private final int releaseOrder;
    private final String name;
    private final String title;
    private final Class position;
    private final Pantheon pantheon;
    private final Type type;
    private final BasicAttack attackType;
    private final SimpleDateFormat releaseDate;

    public God(int order, String godName, String godTitle, Class godClass, Pantheon myth, Type godType, BasicAttack godAttack, SimpleDateFormat releasedOn) {
        releaseOrder = order;
        name = godName;
        title = godTitle;
        position = godClass;
        pantheon = myth;
        type = godType;
        attackType = godAttack;
        releaseDate = releasedOn;
    }

    public int getReleaseOrder() {
        return releaseOrder;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Pantheon getPantheon() {
        return pantheon;
    }

    public Type getType() {
        return type;
    }

    public BasicAttack getAttackType() {
        return attackType;
    }

    public Class getPosition() { return position; }

    public SimpleDateFormat getReleaseDate() { return releaseDate; }
}
