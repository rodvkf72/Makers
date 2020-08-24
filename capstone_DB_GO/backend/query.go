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

type Noticeboards struct {
	Result []Noticeboard `json:"results"`
}

type Noticeboard_check struct {
	Phone_num string `json:"phone_num"`
	Area      string `json:"area"`
	Time_t    string `json:"time_t"`
	Sex       string `json:"sex"`
	Authority string `json:"authority"`
}

type Noticeboard_checks struct {
	Result []Noticeboard_check `json:"results"`
}

func SelectQuery(db dbInfo, query string, choose string) string {
	var area, phone_num, login, result string

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		switch choose {
		case "area":
			err := rows.Scan(&area)
			if err != nil {
				log.Fatal(err)
			}
			result = area
		case "phone_num":
			err := rows.Scan(&phone_num)
			if err != nil {
				log.Fatal(err)
			}
			result = phone_num
		case "login":
			err := rows.Scan(&login)
			if err != nil {
				log.Fatal(err)
			}
			result = login
		}
	}
	return result
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

func UpdateQuery(db dbInfo, query string) {
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
	fmt.Println("update count : ", nRow)
	conn.Close()
}

func FindsQuery(db dbInfo, query string) []byte {
	var no, phone_num, name, email, sex, title, content, area, time_t string

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

	//Noticeboards로 구조체 묶기
	result := Noticeboards{n}
	/*
		result 변수는 Noticeboard 구조체 이므로 []byte 형식의 전달이 불가함
		따라서 마샬링을 통해 json으로 변환하고 []byte 타입으로 리턴
	*/
	returnresult, _ := json.Marshal(result)

	//마샬링 된 변수값 리턴
	return returnresult
}

func CheckQuery(db dbInfo, query string) []byte {
	var phone_num, area, time_t, sex, authority string

	//Noticeboard_check 구조체 배열 선언
	var n []Noticeboard_check

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&phone_num, &area, &time_t, &sex, &authority)
		if err != nil {
			log.Fatal(err)
		} else {
			structdata := Noticeboard_check{phone_num, area, time_t, sex, authority}

			n = append(n, structdata)
		}
	}

	//Noticeboard_checks로 구조체 묶기
	result := Noticeboard_checks{n}
	/*
		result 변수는 Noticeboard_check 구조체 이므로 []byte 형식의 전달이 불가함
		따라서 마샬링을 통해 json으로 변환하고 []byte 타입으로 리턴
	*/
	returnresult, _ := json.Marshal(result)

	//마샬링 된 변수값 리턴
	return returnresult
}