package backend

import (
	"database/sql"
	"fmt"
	"log"
	"encoding/json"
)

var db = dbInfo{"root", "1463", "localhost:3306", "mysql", "makers"}

type dbInfo struct {
	user     string
	pwd      string
	url      string
	engine   string
	database string
}

type Noticeboard struct {
	No int			`json:"no"`
	Phone_num int	`json:"phone_num"`
	Name string		`json:"name"`
	Email string	`json:"email"`
	Sex string		`json:"sex"`
	Title string	`json:"title"`
	Content string	`json:"content"`
	Area string		`json:"area"`
	Time_t string	`json:"time_t"`
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

func FindsQuery(db dbInfo, query string) []byte {
	var no int
	var phone_num int
	var name string
	var email string
	var sex string
	var title string
	var content string
	var area string
	var time_t string

	var returnjsondata []byte

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
		} else {
			structdata := Noticeboard {no, phone_num, name, email, sex, title, content, area, time_t}

			returnjsondata, _ = json.MarshalIndent(structdata, "", "	")

			fmt.Println(string(returnjsondata))
		}
	}
	return returnjsondata
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
