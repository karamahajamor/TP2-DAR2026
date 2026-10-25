package tp2.activite22;

import java.io.Serializable;

public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double operande1;
    private final String operateur;
    private final double operande2;

    public Operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() {
        return operande1;
    }

    public String getOperateur() {
        return operateur;
    }

    public double getOperande2() {
        return operande2;
    }
}