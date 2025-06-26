#include <iostream>
#include <sqlite3.h>

static int callback(void *NotUsed, int argc, char **argv, char **azColName) {
    (void)NotUsed; // 告诉编译器这个参数有意未使用
    for (int i = 0; i < argc; i++) {
        std::cout << azColName[i] << " = " << (argv[i]? argv[i] : "NULL") << "\t";
    }
    std::cout << std::endl;
    return 0;
}

int main() {
    sqlite3 *db;
    char *zErrMsg = 0;
    int rc;
    std::string sql;

    // 打开数据库
    rc = sqlite3_open("/sdcard/test.db", &db);
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        return(0);
    } else {
        std::cout << "Opened database successfully" << std::endl;
    }

    // 创建表
    sql = "CREATE TABLE IF NOT EXISTS users ("
          "id INTEGER PRIMARY KEY AUTOINCREMENT,"
          "name TEXT,"
          "age INTEGER);";
    rc = sqlite3_exec(db, sql.c_str(), callback, 0, &zErrMsg);
    if (rc != SQLITE_OK) {
        std::cerr << "SQL error: " << zErrMsg << std::endl;
        sqlite3_free(zErrMsg);
    } else {
        std::cout << "Table created successfully" << std::endl;
    }

    // 插入数据
    sql = "INSERT INTO users (name, age) VALUES ('Tom', 25);";
    rc = sqlite3_exec(db, sql.c_str(), callback, 0, &zErrMsg);
    if (rc != SQLITE_OK) {
        std::cerr << "SQL error: " << zErrMsg << std::endl;
        sqlite3_free(zErrMsg);
    } else {
        std::cout << "Data inserted successfully" << std::endl;
    }

    // 查询数据
    sql = "SELECT * FROM users;";
    rc = sqlite3_exec(db, sql.c_str(), callback, 0, &zErrMsg);
    if (rc != SQLITE_OK) {
        std::cerr << "SQL error: " << zErrMsg << std::endl;
        sqlite3_free(zErrMsg);
    } else {
        std::cout << "Query executed successfully" << std::endl;
    }

    // 关闭数据库
    sqlite3_close(db);
    return 0;
}
