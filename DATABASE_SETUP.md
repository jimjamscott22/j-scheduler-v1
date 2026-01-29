# Database Setup Guide

This guide will help you set up MariaDB on your Raspberry Pi and configure the j-scheduler application to use it.

## Step 1: Set Up MariaDB on Raspberry Pi

### Install MariaDB (if not already installed)

```bash
sudo apt update
sudo apt install mariadb-server
```

### Secure MariaDB Installation

```bash
sudo mysql_secure_installation
```

Follow the prompts to set a root password and secure your installation.

## Step 2: Create Database and User

Log into MariaDB as root:

```bash
sudo mysql -u root -p
```

Run the following SQL commands:

```sql
-- Create the database
CREATE DATABASE jscheduler CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create a dedicated user for the application
CREATE USER 'jscheduler_user'@'%' IDENTIFIED BY 'your_secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON jscheduler.* TO 'jscheduler_user'@'%';

-- Apply changes
FLUSH PRIVILEGES;

-- Verify the database was created
SHOW DATABASES;

-- Exit
EXIT;
```

**Important:** Replace `'your_secure_password'` with a strong password of your choice.

## Step 3: Import the Schema

From your Raspberry Pi, import the schema file:

```bash
mysql -u jscheduler_user -p jscheduler < /path/to/schema.sql
```

Or copy the schema.sql file to your Pi and run:

```sql
USE jscheduler;
SOURCE /path/to/schema.sql;
```

Alternatively, you can copy the contents of [schema.sql](schema.sql) and paste them directly into the MySQL prompt.

## Step 4: Configure Remote Access (if accessing from another machine)

### Edit MariaDB Configuration

```bash
sudo nano /etc/mysql/mariadb.conf.d/50-server.cnf
```

Find the line with `bind-address` and change it to:

```
bind-address = 0.0.0.0
```

This allows connections from any IP address. For better security, you can specify your computer's IP address instead.

### Restart MariaDB

```bash
sudo systemctl restart mariadb
```

### Configure Firewall (if enabled)

```bash
sudo ufw allow 3306/tcp
```

## Step 5: Configure the Application

### Update database.properties

Edit the file: `src/main/resources/database.properties`

```properties
# Update these values to match your Raspberry Pi setup
db.host=192.168.1.100          # Replace with your Raspberry Pi's IP address
db.port=3306
db.name=jscheduler
db.username=jscheduler_user
db.password=your_secure_password  # Use the password you set in Step 2

# Connection pool settings
db.pool.minSize=2
db.pool.maxSize=10
```

**To find your Raspberry Pi's IP address:**

```bash
hostname -I
```

## Step 6: Test the Connection

### From Your Raspberry Pi

```bash
mysql -u jscheduler_user -p -h localhost jscheduler
```

### From Your Development Machine

```bash
mysql -u jscheduler_user -p -h <raspberry_pi_ip> jscheduler
```

If you don't have the MySQL client installed on Windows, you can test the connection when you run the application - it will print connection status to the console.

## Step 7: Build and Run

### Reload Gradle Dependencies

Since we added the MariaDB JDBC driver, you need to reload the Gradle project:

**In VS Code:**
- Press `Ctrl+Shift+P`
- Type "Java: Clean Java Language Server Workspace"
- Restart VS Code

Or simply run:

```powershell
.\gradlew.bat build
```

### Run the Application

```powershell
.\gradlew.bat run
```

The application will:
1. Load database configuration from `database.properties`
2. Connect to your MariaDB database on the Raspberry Pi
3. Load existing courses and assignments on startup
4. Save all changes automatically to the database

## Troubleshooting

### Connection Refused

- Verify MariaDB is running: `sudo systemctl status mariadb`
- Check firewall settings
- Verify the IP address is correct
- Ensure `bind-address` is set to `0.0.0.0` in MariaDB config

### Authentication Failed

- Double-check username and password in `database.properties`
- Verify the user has proper privileges:
  ```sql
  SHOW GRANTS FOR 'jscheduler_user'@'%';
  ```

### Tables Don't Exist

- Make sure you imported the schema: `SOURCE schema.sql;`
- Verify tables exist: `SHOW TABLES;`

### Application Logs

Check the console output when running the application. You'll see messages like:
- "Database configuration loaded successfully"
- "Loaded X courses from database"
- "Loaded X assignments from database"

If there are errors, they'll be printed to the console with details.

## Database Features

The application now supports:

✅ **Persistent Storage** - All data saved to MariaDB
✅ **Automatic Loading** - Data loads on startup
✅ **Real-time Sync** - Changes saved immediately
✅ **Foreign Keys** - Assignments linked to courses
✅ **Cascading Deletes** - Deleting a course removes its assignments
✅ **Timestamps** - Created/updated timestamps for all records
✅ **Cross-platform** - Access your data from any machine
✅ **Future-proof** - Easy to migrate or backup

## Backup Your Data

Create a backup of your database:

```bash
mysqldump -u jscheduler_user -p jscheduler > jscheduler_backup_$(date +%Y%m%d).sql
```

Restore from backup:

```bash
mysql -u jscheduler_user -p jscheduler < jscheduler_backup_20260129.sql
```

## Next Steps

Now that your data is persisted in MariaDB:
1. You can access it from multiple computers
2. Future versions of the app will automatically use the same data
3. You can easily backup and restore your assignment data
4. You can query the database directly for custom reports
