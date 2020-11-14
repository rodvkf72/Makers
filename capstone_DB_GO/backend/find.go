package backend

import (
	"fmt"
	"net/http"
)

func FindPW(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	resname := r.FormValue("u_name")
	resnum := r.FormValue("u_num")

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		query := "SELECT pass FROM info WHERE name = " + "'" + resname + "'" + " AND phone_num = " + "'" + resnum + "'" + ";"
		//query := "SELECT title, content, image FROM area"

		result := SelectQuery(db, query, "findpw")
		fmt.Fprintf(w, string(result))
	} else {

	}
}
