package backend

import (
	"fmt"
	"net/http"
)

func AreaSetting(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")
		ressex := r.FormValue("sex_text")

		selectquery := "SELECT phone_num FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		queryphonenum := SelectQuery(db, selectquery, "phone_num")

		if resphonenum == "" || resarea == "" || restime == "" || ressex == "" {
			fmt.Fprintf(w, "필수 입력사항입니다.")
		} else {
			if queryphonenum == resphonenum {
				fmt.Fprintf(w, "update")
				query = "UPDATE noticeboard_setting SET area = " + "'" + resarea + "'" + ", time_t = " + "'" + restime + "'" + ", sex = " + "'" + ressex + "'" + "WHERE phone_num=" + "'" + resphonenum + "'" + ";"
				UpdateQuery(db, query)

			} else {
				fmt.Fprintf(w, "insert")
				query = "INSERT INTO noticeboard_setting VALUES (" + "'" + resphonenum + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + ressex + "'" + "," + "'" + "'" + ");"
				InsertQuery(db, query)
			}
		}

	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))
	}
}

func AreaInfo(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		query := "SELECT area.title, area.content, area.image, AVG(evaluation.preference) FROM area, evaluation WHERE area.title = evaluation.area_detail GROUP BY area.title"
		//query := "SELECT title, content, image FROM area"

		result := AreaQuery(db, query)
		fmt.Fprintf(w, string(result))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
	}
}
