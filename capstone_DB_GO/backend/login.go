package backend

import (
	"fmt"
	"net/http"
)

func Login(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resid := r.FormValue("u_id")
		respw := r.FormValue("u_pass")

		query := "SELECT IF(STRCMP(pass," + "'" + respw + "'" + "),0,1) FROM info WHERE phone_num = " + "'" + resid + "'" + ";"

		result := SelectQuery(db, query, "login")
		if result == "1" {
			fmt.Fprintf(w, "true")
		} else {
			fmt.Fprintf(w, "false")
		}
	} else {
		fmt.Fprintf(w, "잘못된 접근입니다.")
	}
}
