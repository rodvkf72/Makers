package backend

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

func NoticeboardFind(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//구조체로 바꿀 것
	var no string
	var phone_num string
	var name string
	var email string
	var sex string
	var title string
	var content string
	var area string
	var time_t string

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
		no, phone_num, name, email, sex, title, content, area, time_t = FindQuery(db, query)
		fmt.Fprintf(w, no, phone_num, name, email, sex, title, content, area, time_t)
		fmt.Println(no, phone_num, name, email, sex, title, content, area, time_t)
	} else {
		body, _ := ioutil.ReadAll(r.Body)
		var prettyJSON bytes.Buffer
		error := json.Indent(&prettyJSON, body, "", "\t")

		if error != nil {
			log.Println("JSON parse error: ", error)
			return
		}
		log.Println("results", string(prettyJSON.Bytes()))

	}

}
