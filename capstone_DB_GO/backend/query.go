package backend

import (
	"database/sql"
	"fmt"
	"log"
)

var db = dbInfo{"root", "1463", "localhost:3306", "mysql", "makers"}

type dbInfo struct {
	user     string
	pwd      string
	url      string
	engine   string
	database string
}

func SelectQuery(db dbInfo, query string) string {
	var area string
	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&area)
		if err != nil {
			log.Fatal(err)
		}
	}
	return area
}

func FindQuery(db dbInfo, query string) (string, string, string, string, string, string, string, string, string) {
	var no string
	var phone_num string
	var name string
	var email string
	var sex string
	var title string
	var content string
	var area string
	var time_t string

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&no, &phone_num, &name, &email, &sex, &title, &content, &area, &time_t)
		if err != nil {
			log.Fatal(err)
		}
	}
	return no, phone_num, name, email, sex, title, content, area, time_t
}

func InsertQuery(db dbInfo, query string) {
	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	result, err := conn.Exec(query)

	if err != nil {
		log.Fatal(err)
	}
	nRow, err := result.RowsAffected()
	fmt.Println("insert count : ", nRow)
	conn.Close()
}
