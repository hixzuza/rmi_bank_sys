

# Bank System - JavaFX RMI Application

## Description

This is a distributed banking system built with JavaFX for the client interface and Java RMI (Remote Method Invocation) for server communication. The application provides a complete banking platform with role-based access control for administrators and regular clients. Administrators can manage user accounts, create and delete bank accounts, and view all client accounts. Clients can perform standard banking operations including checking balances, depositing funds, withdrawing money, transferring between accounts, and viewing transaction history. The system uses a MySQL database for persistent storage and implements secure authentication with password hashing.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Dependencies](#dependencies)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Contact or Support](#contact-or-support)
- [Acknowledgments](#acknowledgments)

## Installation

Prerequisites:
- Java Development Kit (JDK) 11 or higher
- MySQL Server 8.0 or higher
- MySQL Workbench (optional, for database management)

Step-by-step installation:

1. Clone the repository:
   ```
   git clone https://github.com/hixzuza/rmi_bank_sys
   cd rmi_bank_sys
   ```

2. Set up the MySQL database:
    - Open MySQL Workbench or command line client
    - Execute the SQL schema creation script located at `DB.sql`
    - The expected database name is `BANK`

3. Configure database credentials:
    - Navigate to `project/bank/server/db/DatabaseConnection.java`
    - Update the URL, USER, and PASSWORD constants if necessary

4. Start the RMI server:
   ```
   cd rmi_bank_sys
   javac project/bank/server/ServerLauncher.java
   java project.bank.server.ServerLauncher
   ```

5. Launch the client application:
   ```
   java project.bank.MAIN.Launcher
   ```

## Usage

Default test accounts (created by running the test database insertion):

Administrator access:
- Username: admin
- Password: admin

Client access:
- Username: client1
- Password: client1
- Username: client2
- Password: client2

Client operations:
- View Balance: Displays all accounts belonging to the logged-in client
- Deposit Money: Add funds to a specified account number
- Withdraw Money: Remove funds from a specified account (requires sufficient balance)
- Transfer Money: Move funds between accounts (source and destination must be different)
- View Transactions: Shows complete transaction history for all user accounts

Administrator operations:
- List All Users: View all client accounts with balances and creation dates
- Create Account: Generate a new account for an existing user with initial balance
- Delete Account: Soft-delete an account by marking it inactive

The application uses a dashboard interface where users select operations from a sidebar. Each operation loads in the main content area. The switch button logs out the current user and returns to the login screen.

## Configuration

Database Configuration (`DatabaseConnection.java`):
```
private static final String URL = "jdbc:mysql://localhost:3306/BANK";
private static final String USER = "root";
private static final String PASSWORD = "admin";
```

RMI Configuration (`ServiceLocator.java`):
- Registry host: localhost
- Registry port: 1099
- Service name: "BanqueService"

To modify RMI settings, update the `connect()` method in `ServiceLocator.java`:
```
Registry registry = LocateRegistry.getRegistry("[hostname]", [port]);
service = (IBanqueService) registry.lookup("BanqueService");
```

CSS stylesheets are located in the `css/` directory relative to FXML files. Modify these files to change the application appearance.

## Dependencies

Software requirements:
- Java Runtime Environment (JRE) 11 or higher
- MySQL Server 8.0 or higher
- JavaFX SDK (requires separate download for JDK 11 and above)

Java libraries:
- java.rmi - Remote Method Invocation for client-server communication
- javafx - GUI framework for desktop application
- java.sql - Database connectivity
- java.security - SHA-256 password hashing

External JAR files required:
- mysql-connector-java-[version].jar (MySQL JDBC Driver)

The project uses no additional third-party libraries beyond the Java standard library and MySQL Connector.

## Testing

To run the test database insertion and verify database connectivity:

1. Uncomment the `main` method in `DbManager.java`
2. Uncomment the `test_the_db()` method call in the same file
3. Run `DbManager.java` as a standalone Java application:
   ```
   java project.bank.server.db.DbManager
   ```

This will populate the database with test users (admin, client1, client2) and sample accounts with initial balances.

Manual testing workflow:
1. Start the RMI server
2. Launch the client application
3. Test authentication with default credentials
4. Verify each operation (deposit, withdraw, transfer, history)
5. Test administrator functions with admin credentials
6. Verify session management by switching between users

The system logs operations to the console for debugging purposes. Check terminal output for error messages and operation confirmations.

## Contributing

Guidelines for contributing to this project:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Make your changes following existing code conventions (Java naming standards, package structure)
4. Write clear commit messages
5. Test your changes with both client and administrator roles
6. Push to your fork and submit a pull request

Code style requirements:
- Use camelCase for variables and methods
- Use PascalCase for class names
- Include JavaDoc comments for public methods
- Maintain existing package structure (client.model, client.viewmodel, server.impl, commun)

Reporting issues:
- Describe the expected behavior and actual behavior
- Include steps to reproduce
- Attach relevant console output or error messages
- Specify Java version and operating system

## License

This project is provided under the MIT License.

MIT License

Copyright (c) 2026 hixzuza

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files, to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

## Contact or Support

For questions, issues, or support requests:

- GitHub Issues: https://github.com/hixzuza/rmi_bank_sys/issues

Reporting guidelines:
- Check existing issues before creating a new one
- Use descriptive titles
- Include environment details (OS, Java version, MySQL version)
- Attach screenshots for UI issues when applicable

## Acknowledgments

- JavaFX documentation and community for GUI development references
- Oracle RMI tutorials for distributed system architecture
- MySQL Connector/J team for database connectivity driver
- Icons by Icons8 (https://icons8.com) used in the dashboard sidebar
- CSS styling inspiration from modern banking application interfaces
- Contributors who tested the application and provided feedback

---
