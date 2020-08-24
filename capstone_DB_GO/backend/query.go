package backend

import (
	"database/sql"
	"encoding/json"
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

type Noticeboards struct {
	Result []Noticeboard `json:"results"`
}

type Noticeboard struct {
	No        string `json:"no"`
	Phone_num string `json:"phone_num"`
	Name      string `json:"name"`
	Email     string `json:"email"`
	Sex       string `json:"sex"`
	Title     string `json:"title"`
	Content   string `json:"content"`
	Area      string `json:"area"`
	Time_t    string `json:"time_t"`
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
	var no string
	var phone_num string
	var name string
	var email string
	var sex string
	var title string
	var content string
	var area string
	var time_t string

	//Noticeboard 구조체 배열 선언
	var n []Noticeboard

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
			structdata := Noticeboard{no, phone_num, name, email, sex, title, content, area, time_t}

			n = append(n, structdata)
		}
	}

	//result로 구조체 묶기
	result := Noticeboards{n}
	/*
		result 변수는 Noticeboard 구조체 이므로 []byte 형식의 전달이 불가함
		따라서 마샬링을 통해 json으로 변환하고 []byte 타입으로 리턴
	*/
	returnresult, _ := json.Marshal(result)

	//마샬링 된 변수값 리턴
	return returnresult
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
