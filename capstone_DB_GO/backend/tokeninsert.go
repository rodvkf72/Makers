package backend

import "net/http"

func TokenInsert(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		restoken := r.FormValue("Token")
		query := "INSERT INTO users(Token) VALUES(" + "'" + restoken + "'" + ") ON DUPLICATE KEY UPDATE Token=" + "'" + restoken + "'" + ";"

		InsertQuery(db, query)
	}
}
