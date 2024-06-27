package me.iipho3nix.antiairplace;

public class BiObject {
    private final Object a;
    private final Object b;

    public BiObject(Object a, Object b) {
        this.a = a;
        this.b = b;
    }

    public Object getA() {
        return a;
    }

    public Object getB() {
        return b;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BiObject obj) {
            return getA().equals(obj.getA()) && getB().equals(obj.getB());
        }
        return false;
    }
}
