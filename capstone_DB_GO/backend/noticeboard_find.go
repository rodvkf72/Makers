package backend

import (
	"encoding/json"
	"fmt"
	"net/http"
)

func NoticeboardFind(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_num")
		ressex := r.FormValue("sex_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")

		if restime == "" || ressex == "" {
			query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
		} else {
			query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
		}

		json.MarshalIndent(query, "", "\t")
		jsondata := FindsQuery(db, query)
		fmt.Fprintf(w, string(jsondata))
		fmt.Println(jsondata)
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))
	/*
		body, _ := ioutil.ReadAll(r.Body)
		var prettyJSON bytes.Buffer
		error := json.Indent(&prettyJSON, body, "", "\t")

		if error != nil {
			log.Println("JSON parse error: ", error)
			return
		}
		log.Println("results", string(prettyJSON.Bytes()))

	}*/
	}
}
