module project.bank {
    // ============ EXPORTS ============
    // For JavaFX to launch Application
    exports project.bank.MAIN to javafx.graphics;

    // For RMI communication
    exports project.bank.commun;
    exports project.bank.client.model;

    // ============ OPENS (Reflection) ============
    // JavaFX FXML needs reflection access
    opens project.bank.client.viewmodel to javafx.fxml;
    opens project.bank.MAIN to javafx.fxml, javafx.graphics;

    // RMI needs reflection access
    opens project.bank.commun to java.rmi;
    opens project.bank.server to java.rmi;
    opens project.bank.server.impl to java.rmi;

    // ============ REQUIRES ============
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.rmi;
    requires java.sql;
    requires mysql.connector.j;
}